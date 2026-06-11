package com.eltex.firstapp.feature.tea

import kotlinx.coroutines.flow.Flow

fun interface EffectHandler<Message, Effect> {
    fun connect(effects: Flow<Effect>): Flow<Message>
}