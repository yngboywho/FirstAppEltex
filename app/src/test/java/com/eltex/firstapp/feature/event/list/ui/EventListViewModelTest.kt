package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.TestSchedulers
import com.eltex.firstapp.feature.domain.LoadingState
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Test

class EventListViewModelTest {
    private val event1 = Event(
        id = 1L,
        publishedAt = "2026-06-03T10:00",
        content = "Event 1",
        author = "Author 1",
        likedByMe = false,
        likes = 0,
        participantsByMe = false,
        participants = 0,
    )

    private val event2 = Event(
        id = 2L,
        publishedAt = "2026-06-03T12:00",
        content = "Event 2",
        author = "Author 2",
        likedByMe = true,
        likes = 5,
        participantsByMe = true,
        participants = 3,
    )

    private fun viewModel(repository: EventsRepository) =
        EventListViewModel(repository, TestSchedulers)

    @Test
    fun `loadEvents success - events loaded, status is Idle`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1, event2))
        )

        val vm = viewModel(repository)

        assertEquals(listOf(event1, event2).map { it.toUiModel() }, vm.state.events)
        assertEquals(LoadingState.Idle, vm.state.status)
    }

    @Test
    fun `loadEvents error - status is Error with cause`() {
        val error = RuntimeException("Network error")
        val repository = FakeEventsRepository(
            getEventsResult = Single.error(error)
        )

        val vm = viewModel(repository)

        val status = vm.state.status
        assertTrue(status is LoadingState.Error)
        assertEquals(error, (status as LoadingState.Error).value)
    }

    @Test
    fun `retry on error - reloads events successfully`() {
        val error = RuntimeException("fail")
        val repository = FakeEventsRepository(
            getEventsResult = Single.error(error),
            getEventsAfterRetry = Single.just(listOf(event1)),
        )

        val vm = viewModel(repository)
        assertTrue(vm.state.status is LoadingState.Error)

        vm.accept(EventListMessage.Retry)

        assertEquals(listOf(event1.toUiModel()), vm.state.events)
        assertEquals(LoadingState.Idle, vm.state.status)
    }

    @Test
    fun `retry error - status remains Error`() {
        val error = RuntimeException("fail")
        val repository = FakeEventsRepository(
            getEventsResult = Single.error(error),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Retry)

        assertTrue(vm.state.status is LoadingState.Error)
    }

    @Test
    fun `like success - event replaced in list`() {
        val likedEvent = event1.copy(likedByMe = true, likes = 1)
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1, event2)),
            likeByIdResult = Single.just(likedEvent),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Like(id = event1.id, likedByMe = false))

        assertEquals(likedEvent.toUiModel(), vm.state.events.find { it.id == event1.id })
        assertEquals(2, vm.state.events.size)
    }

    @Test
    fun `like error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1)),
            likeByIdResult = Single.error(RuntimeException("error")),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.Like(id = event1.id, likedByMe = false))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `participate success - event replaced in list`() {
        val participatedEvent = event1.copy(participantsByMe = true, participants = 1)
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1, event2)),
            participateByIdResult = Single.just(participatedEvent),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Participate(id = event1.id, participatedByMe = false))

        assertEquals(participatedEvent.toUiModel(), vm.state.events.find { it.id == event1.id })
        assertEquals(2, vm.state.events.size)
    }

    @Test
    fun `participate error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1)),
            participateByIdResult = Single.error(RuntimeException("error")),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.Participate(id = event1.id, participatedByMe = false))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `saveEdited success - event replaced in list`() {
        val updatedEvent = event1.copy(content = "Updated content")
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1)),
            updateResult = Single.just(updatedEvent),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.SaveEdited(id = event1.id, content = "Updated content"))

        assertEquals(updatedEvent.toUiModel(), vm.state.events.find { it.id == event1.id })
    }

    @Test
    fun `saveEdited error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1)),
            updateResult = Single.error(RuntimeException("error")),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.SaveEdited(id = event1.id, content = "Updated content"))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `addPost success - event prepended to list`() {
        val newEvent = event2
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1)),
            saveResult = Single.just(newEvent),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.AddPost(content = newEvent.content))

        assertEquals(newEvent.toUiModel(), vm.state.events.first())
        assertEquals(2, vm.state.events.size)
    }

    @Test
    fun `addPost error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1)),
            saveResult = Single.error(RuntimeException("error")),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.AddPost(content = "New event"))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `delete success - event removed from list`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1, event2)),
            deleteByIdResult = Completable.complete(),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Delete(id = event1.id))

        assertFalse(vm.state.events.any { it.id == event1.id })
        assertEquals(1, vm.state.events.size)
    }

    @Test
    fun `delete error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1, event2)),
            deleteByIdResult = Completable.error(RuntimeException("error")),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.Delete(id = event1.id))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `findById returns event when found`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1, event2))
        )

        val vm = viewModel(repository)

        assertEquals(event1.toUiModel(), vm.findById(event1.id))
    }

    @Test
    fun `findById returns null when not found`() {
        val repository = FakeEventsRepository(
            getEventsResult = Single.just(listOf(event1))
        )

        val vm = viewModel(repository)

        assertNull(vm.findById(999L))
    }

    private class FakeEventsRepository(
        private val getEventsResult: Single<List<Event>> = Single.never(),
        private val getEventsAfterRetry: Single<List<Event>>? = null,
        private val likeByIdResult: Single<Event> = Single.never(),
        private val participateByIdResult: Single<Event> = Single.never(),
        private val updateResult: Single<Event> = Single.never(),
        private val saveResult: Single<Event> = Single.never(),
        private val deleteByIdResult: Completable = Completable.never(),
    ) : EventsRepository {

        private var getEventsCallCount = 0

        override fun getEvents(): Single<List<Event>> {
            val count = ++getEventsCallCount
            return if (count > 1 && getEventsAfterRetry != null) getEventsAfterRetry
            else getEventsResult
        }

        override fun save(
            content: String, author: String, status: String, visit: String, link: String,
        ): Single<Event> = saveResult

        override fun update(id: Long, content: String): Single<Event> = updateResult

        override fun likeById(id: Long, likedByMe: Boolean): Single<Event> = likeByIdResult

        override fun participateById(id: Long, participatedByMe: Boolean): Single<Event> =
            participateByIdResult

        override fun deleteById(id: Long): Completable = deleteByIdResult
    }
}