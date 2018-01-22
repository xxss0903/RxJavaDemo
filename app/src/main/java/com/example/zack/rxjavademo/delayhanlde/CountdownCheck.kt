package com.example.zack.rxjavademo.delayhanlde

import android.os.CountDownTimer

/**
 * delay handle by countdowntimer
 * Created by zack on 2018/1/22.
 */
class CountdownCheck(delay:Long, duration: Long): CountDownTimer(delay, duration) {

    lateinit var content: String

    override fun onFinish() {
        println("check content: $content")
    }

    override fun onTick(millisUntilFinished: Long) {
        println("count down: #$millisUntilFinished")
    }

    fun startCheck(t: String) {
        content = t
        cancel()
        start()
    }

}