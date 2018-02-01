package com.example.zack.rxjavademo.rxbus

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Cancellable

/**
 * Created by zack on 2018/1/25.
 */
class MyEmitter : ObservableOnSubscribe<String>{

    var target: String

    init {
        target = "target"
    }

    companion object {
        val instance = MyEmitter()
    }

    override fun subscribe(e: ObservableEmitter<String>) {
        e.onNext(target)
    }

    fun emit(obj: String){
        target = obj
    }


}