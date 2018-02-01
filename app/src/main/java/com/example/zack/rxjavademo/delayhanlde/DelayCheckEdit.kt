package com.example.zack.rxjavademo.delayhanlde

import android.content.Context
import android.icu.util.DateInterval
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText

/**
 * delay check with counttimer
 * Created by zack on 2018/1/26.
 */
class DelayCheckEditText : EditText {

    var countTimeAll: Long = 1500
    var countDownInterval: Long = 500
    private var checkListener: OnCheckTextListener? = null
    private var countTimer: CountDownTimer = initCountTimer(countTimeAll, countDownInterval)
    private var endStr: String = ""
    // delay check text changed watcher
    private val delayWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            startCountDown(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            endStr = ""
            if (checkListener == null) {
                checkListener?.apply {
                    beforeCheckContent(s, start, count, after)
                }
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    /**
     * create new countdowntimer
     */
    private fun initCountTimer(countTime:Long, countDownInterval: Long): CountDownTimer{
        return object: CountDownTimer(countTime, countDownInterval){
            override fun onFinish() {
                checkText(endStr)
            }

            override fun onTick(millisUntilFinished: Long) {
                println("delay :# " + millisUntilFinished)
            }

        }
    }

    /**
     * reset countdowntimer all, and create new countdowntimer
     */
    fun resetCountTime(countTimeAll: Long){
        countTimer = initCountTimer(countTimeAll, countDownInterval)
    }

    private fun startCountDown(s: Editable?) {
        countTimer.apply {
            endStr = s.toString()
            cancel()
            start()
        }
    }

    init {
        // init focus listener
        onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                checkTextImmediately()
            }
        }

        // init text change listener
        addTextChangedListener(delayWatcher)
    }

    constructor(context: Context) : super(context) {
//        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
//        init(context, attrs, defStyle)
    }

    fun setCheckTextListener(listener: OnCheckTextListener) {
        checkListener = listener
    }

    fun checkText(endStr: String) {
        if (checkListener != null) {
            checkListener?.apply {
                checkContent(endStr)
            }
        }
    }

    // cancel timer at destroy activity
    fun cancelCountTimer() {
        countTimer.apply {
            cancel()
        }
    }

    private fun checkTextImmediately() {
        countTimer.cancel()
        checkText(endStr)
    }
}

abstract class OnCheckTextListener {
    abstract fun checkContent(content: String)

    abstract fun beforeCheckContent(s: CharSequence?, start: Int, count: Int, after: Int)
}
