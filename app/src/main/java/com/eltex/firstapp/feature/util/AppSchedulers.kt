package com.eltex.firstapp.feature.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


interface AppSchedulers {
    val io: CoroutineDispatcher
    val computation: CoroutineDispatcher
    val main: CoroutineDispatcher

    companion object {
        val DEFAULT = object : AppSchedulers {
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val computation: CoroutineDispatcher = Dispatchers.Default
            override val main: CoroutineDispatcher = Dispatchers.Main
        }
    }
}