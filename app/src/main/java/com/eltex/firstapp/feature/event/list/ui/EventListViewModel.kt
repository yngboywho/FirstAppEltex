package com.eltex.firstapp.feature.event.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.firstapp.feature.tea.Store
import kotlinx.coroutines.launch

class EventListViewModel(
    reducer: EventListReducer = EventListReducer(),
    effectHandler: EventListEffectHandler = EventListEffectHandler(),
) : ViewModel() {

    private val store = Store(
        reducer,
        effectHandler,
        EventListState(),
        setOf(EventListMessage.LoadInitial),
    )

    var state = store.state
    val effects = store.effects

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: EventListMessage) {
        viewModelScope.launch {
            store.accept(message)
        }
    }

    fun findById(id: Long): EventUiModel? = state.events.find { it.id == id }
}