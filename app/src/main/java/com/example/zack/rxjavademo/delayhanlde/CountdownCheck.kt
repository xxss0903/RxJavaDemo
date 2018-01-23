package com.example.zack.rxjavademo.delayhanlde

import android.os.CountDownTimer

/**
 * delay handle by countdowntimer
 * Created by zack on 2018/1/22.
 */
class CountdownCheck(delay:Long, duration: Long): CountDownTimer(delay, duration) {

    lateinit var content: String
    private lateinit var listener: CountdownCheckListener

    override fun onFinish() {
        listener.checkResult(content)
    }

    override fun onTick(millisUntilFinished: Long) {
    }

    fun startCheck(t: String, listener: CountdownCheckListener) {
        cancel()
        content = t
        this.listener = listener
        start()
    }

}

interface CountdownCheckListener{
    fun checkResult(result: String)
}