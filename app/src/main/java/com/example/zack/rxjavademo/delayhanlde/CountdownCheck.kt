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
        content = "after$content"
        listener.checkResult(content)
    }

    override fun onTick(millisUntilFinished: Long) {
        println("count down: #$millisUntilFinished")
    }

    fun startCheck(t: String, listener: CountdownCheckListener) {
        content = t
        cancel()
        start()
        this.listener = listener
    }

}

interface CountdownCheckListener{
    fun checkResult(result: String)
}