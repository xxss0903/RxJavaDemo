package com.example.zack.rxjavademo.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit


/**
 * rxbus depends rrelay
 * Created by zack on 2018/1/23.
 */
class RxBus private constructor(){

    private var bus: PublishSubject<Any> = PublishSubject.create<Any>()

    companion object {
        val instance = RxBus()
    }

    fun post(t: Any){
        bus.onNext(t)

    }

    fun<T> toObservable(tClass: Class<T>): Observable<T> {
        return bus.ofType(tClass)
    }

    fun toObservable():Observable<Any>{
        return bus
    }

    fun hasObservers():Boolean{
        return bus.hasObservers()
    }

}