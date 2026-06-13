package com.eltex.firstapp.feature.event.list.ui

import arrow.core.left
import arrow.core.right
import com.eltex.firstapp.domain.AppException
import com.eltex.firstapp.feature.event.data.EventsRepositoryImpl
import com.eltex.firstapp.feature.event.domain.EventsRepository
import com.eltex.firstapp.feature.tea.EffectHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge

class EventListEffectHandler(
    private val repository: EventsRepository = EventsRepositoryImpl(),
): EffectHandler<EventListMessage, EventListEffect> {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun connect(effects: Flow<EventListEffect>): Flow<EventListMessage> = listOf(
        effects.filterIsInstance<EventListEffect.LoadInitial>().mapLatest { effect ->
            EventListMessage.LoadInitialResult(
                try {
                    repository.getEventsLatest(effect.size).right()
                } catch (e: AppException) {
                    e.left()
                }
            )
        },
        effects.filterIsInstance<EventListEffect.LoadNextPage>().mapLatest { effect ->
            EventListMessage.LoadNextPageResult(
                try {
                    repository.getEventsBefore(effect.id, effect.size).right()
                } catch (e: AppException) {
                    e.left()
                }
            )
        },
        effects.filterIsInstance<EventListEffect.AddEvent>().mapLatest { effect ->
            EventListMessage.AddEventResult(
                try {
                    repository.save(effect.content).right()
                } catch (e: AppException) {
                    e.left()
                }
            )
        },
        effects.filterIsInstance<EventListEffect.EditEvent>().mapLatest { effect ->
            EventListMessage.SaveEditedResult(
                try {
                    repository.update(effect.id, effect.content).right()
                } catch (e: AppException) {
                    e.left()
                }
            )
        },
        effects.filterIsInstance<EventListEffect.Like>().mapLatest { effect ->
            try {
                EventListMessage.LikeSuccess(
                    if (effect.liked) {
                        repository.unlikeById(effect.data.id)
                    } else {
                        repository.likeById(effect.data.id, likedByMe = true)
                    }
                )
            } catch (e: AppException) {
                EventListMessage.LikeError(
                    effect.data.id,
                    effect.data.originalLikedByMe,
                    effect
                        .data.originalLikes,
                    e,
                )
            }
        },
        effects.filterIsInstance<EventListEffect.Participate>().mapLatest { effect ->
            try {
                EventListMessage.ParticipateSuccess(
                    repository.participateById(effect.data.id, effect.participatedByMe)
                )
            } catch (e: AppException) {
                EventListMessage.ParticipateError(
                    eventId = effect.data.id,
                    originalParticipatedByMe = effect.data.originalParticipatedByMe,
                    originalParticipants = effect.data.originalParticipants,
                    error = e,
                )
            }
        },
        effects.filterIsInstance<EventListEffect.Delete>().mapLatest { effect ->
            try {
                repository.deleteById(effect.original.id)

                null
            } catch (e: AppException) {
                EventListMessage.DeleteError(
                    original = effect.original,
                    error = e,
                )
            }
        }
            .filterNotNull(),
    ).merge()
}