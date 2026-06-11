package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.domain.LoadingState
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

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
        EventListViewModel(repository)

    @Test
    fun `loadEvents success - events loaded, status is Idle`() = runTest {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1, event2)
        )

        val vm = viewModel(repository)

        assertEquals(listOf(event1, event2).map { it.toUiModel() }, vm.state.events)
        assertEquals(LoadingState.Idle, vm.state.status)
    }

    @Test
    fun `loadEvents error - status is Error with cause`() {
        val error = RuntimeException("Network error")
        val repository = FakeEventsRepository(
            getEventsError = error
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
            getEventsError = error,
            getEventsAfterRetry = listOf(event1),
        )

        val vm = viewModel(repository)
        assertTrue(vm.state.status is LoadingState.Error)

        vm.accept(EventListMessage.Refresh)

        assertEquals(listOf(event1.toUiModel()), vm.state.events)
        assertEquals(LoadingState.Idle, vm.state.status)
    }

    @Test
    fun `retry error - status remains Error`() {
        val error = RuntimeException("fail")
        val repository = FakeEventsRepository(
            getEventsError = error,
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Refresh)

        assertTrue(vm.state.status is LoadingState.Error)
    }

    @Test
    fun `like success - event replaced in list`() {
        val likedEvent = event1.copy(likedByMe = true, likes = 1)
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1, event2),
            likeByIdResult = likedEvent,
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Like(id = event1.id, likedByMe = false))

        assertEquals(likedEvent.toUiModel(), vm.state.events.find { it.id == event1.id })
        assertEquals(2, vm.state.events.size)
    }

    @Test
    fun `like error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1),
            likeByIdError = RuntimeException("error"),
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
            getEventsResult = listOf(event1, event2),
            participateByIdResult = participatedEvent,
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Participate(id = event1.id, participatedByMe = false))

        assertEquals(participatedEvent.toUiModel(), vm.state.events.find { it.id == event1.id })
        assertEquals(2, vm.state.events.size)
    }

    @Test
    fun `participate error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1),
            participateByIdError = RuntimeException("error"),
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
            getEventsResult = listOf(event1),
            updateResult = updatedEvent,
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.SaveEdited(id = event1.id, content = "Updated content"))

        assertEquals(updatedEvent.toUiModel(), vm.state.events.find { it.id == event1.id })
    }

    @Test
    fun `saveEdited error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1),
            updateError = RuntimeException("error"),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.SaveEdited(id = event1.id, content = "Updated content"))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `addPost success - event prepended to list`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1),
            saveResult = event2,
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.AddEvent(content = event2.content))

        assertEquals(event2.toUiModel(), vm.state.events.first())
        assertEquals(2, vm.state.events.size)
    }

    @Test
    fun `addPost error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1),
            deleteByIdError = RuntimeException("error"),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.AddEvent(content = "New event"))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `delete success - event removed from list`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1, event2),
        )

        val vm = viewModel(repository)
        vm.accept(EventListMessage.Delete(id = event1.id))

        assertFalse(vm.state.events.any { it.id == event1.id })
        assertEquals(1, vm.state.events.size)
    }

    @Test
    fun `delete error - state unchanged`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1, event2),
            deleteByIdError = RuntimeException("error"),
        )

        val vm = viewModel(repository)
        val stateBefore = vm.state

        vm.accept(EventListMessage.Delete(id = event1.id))

        assertEquals(stateBefore, vm.state)
    }

    @Test
    fun `findById returns event when found`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1, event2)
        )

        val vm = viewModel(repository)

        assertEquals(event1.toUiModel(), vm.findById(event1.id))
    }

    @Test
    fun `findById returns null when not found`() {
        val repository = FakeEventsRepository(
            getEventsResult = listOf(event1)
        )

        val vm = viewModel(repository)

        assertNull(vm.findById(999L))
    }

    private class FakeEventsRepository(
        private val getEventsResult: List<Event> = emptyList(),
        private val getEventsError: Throwable? = null,
        private val getEventsAfterRetry: List<Event>? = null,
        private val likeByIdResult: Event? = null,
        private val likeByIdError: Throwable? = null,
        private val participateByIdResult: Event? = null,
        private val participateByIdError: Throwable? = null,
        private val updateResult: Event? = null,
        private val updateError: Throwable? = null,
        private val saveResult: Event? = null,
        private val saveError: Throwable? = null,
        private val deleteByIdError: Throwable? = null,
    ) : EventsRepository {

        private var getEventsCallCount = 0

        override suspend fun getEvents(): List<Event> {
            val count = ++getEventsCallCount
            if (count > 1 && getEventsAfterRetry != null) return getEventsAfterRetry
            getEventsError?.let { throw it }
            return getEventsResult
        }

        override suspend fun save(content: String): Event {
            saveError?.let { throw it }
            return saveResult!!
        }

        override suspend fun update(id: Long, content: String): Event {
            updateError?.let { throw it }
            return updateResult!!
        }

        override suspend fun likeById(id: Long, likedByMe: Boolean): Event {
            likeByIdError?.let { throw it }
            return likeByIdResult!!
        }

        override suspend fun participateById(id: Long, participatedByMe: Boolean): Event {
            participateByIdError?.let { throw it }
            return participateByIdResult!!
        }

        override suspend fun deleteById(id: Long) {
            deleteByIdError?.let { throw it }
        }
    }
}