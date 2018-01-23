package com.example.zack.rxjavademo.delayhanlde

import android.os.Handler

/**
 * check content by handler
 * Created by zack on 2018/1/22.
 */
class CheckContentDelay private constructor() {

    private val handler: Handler = Handler()
    private val checkRunnable: Runnable
    private val delay: Long = 2000
    private lateinit var content: String
    private lateinit var listener: CountdownCheckListener

    init {
        checkRunnable = Runnable {
            println("check $content")
            listener.checkResult("result#$content")
        }
    }

    companion object {
        val instance = CheckContentDelay()
    }

    fun post(t: String, listener: CountdownCheckListener) {
        remove()
        if (!t.isNotEmpty()) {
            return
        }
        this.listener = listener
        content = t
        handler.postDelayed(checkRunnable, delay)
    }

    fun remove() {
        content = ""
        handler.removeCallbacks(checkRunnable)
    }


}
