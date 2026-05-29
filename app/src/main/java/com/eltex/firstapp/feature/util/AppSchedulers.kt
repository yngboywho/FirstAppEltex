package com.eltex.firstapp.feature.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface AppSchedulers {
    val io: Scheduler
    val computation: Scheduler
    val main: Scheduler

    companion object {
        val DEFAULT = object : AppSchedulers {
            override val io: Scheduler = Schedulers.io()
            override val computation: Scheduler = Schedulers.computation()
            override val main: Scheduler = AndroidSchedulers.mainThread()
        }
    }
}