package com.example.zack.rxjavademo.rxbus

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

/**
 * rxbus depends rrelay
 * Created by zack on 2018/1/23.
 */
class RxBus{

    private val bus: Relay<Any> = PublishRelay.create<Any>().toSerialized()

    companion object {
        val instance = RxBus()
    }

    fun post(obj: Any){
        bus.accept(obj)
    }

    fun toObservable(aClass: Class<Any>): Observable<Any>{
        return bus.ofType(aClass)
    }

    fun toObservable(): Boolean{
        return bus.hasObservers()
    }

    fun hasObservers(): Boolean{
        return bus.hasObservers()
    }
}