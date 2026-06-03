package com.eltex.firstapp

import com.eltex.firstapp.feature.util.AppSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

object TestSchedulers : AppSchedulers {
    override val io: Scheduler = Schedulers.trampoline()
    override val computation: Scheduler = Schedulers.trampoline()
    override val main: Scheduler = Schedulers.trampoline()
}
