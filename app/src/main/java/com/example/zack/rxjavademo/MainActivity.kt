package com.example.zack.rxjavademo

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.zack.rxjavademo.delayhanlde.CheckContentDelay
import com.example.zack.rxjavademo.delayhanlde.CountdownCheck
import com.example.zack.rxjavademo.rxbus.RxBus
import kotlinx.android.synthetic.main.activity_main.*

/**
 * main activity
 * Created by zack on 2018/1/22.
 */
class MainActivity : AppCompatActivity() {

    val delayMiniSeconds:Long = 3000
    lateinit var timerHandler: Handler
    lateinit var checkContent: Runnable
    lateinit var countdownCheck: CountdownCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        CheckContentDelay.instance.remove()
    }

    private fun initData() {
        initHandler()

        initCountDown()

        initRxBus()

        initListener()
    }

    private fun initRxBus() {
        RxBus.instance.toObservable(String.javaClass)
                .subscribe({
                    println("check content: $it")
                })
    }

    private fun initCountDown() {
        countdownCheck = CountdownCheck(delayMiniSeconds, 1000.toLong())
    }

    private fun initHandler() {
        timerHandler = Handler()
        checkContent = Runnable {
            checkEtContent()
        }
    }

    fun post(t: String) {
        // handler type
        // CheckContentDelay.instance.post(t)

        // count down timer
         countdownCheck.startCheck(t)

        // rxbus
        // RxBus.instance.post(t)
    }

    private fun initListener() {
        btn_click.setOnClickListener({
            dismissEtFocus()
        })

        et_input.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            // change focus
            println("release et's focus")
            checkEtContent()
        }

        et_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // after text changed delay 1s trigger a rx target
                println("after et changed ${s.toString()}")
                post(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("before et changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("text changed")
            }
        })
    }

    private fun checkEtContent() {
        // check et content
        println("et content: " + et_input.text.toString())
    }

    private fun dismissEtFocus() {
        // test edittext loses focus
        // et_input.clearFocus()
        // et_input.isFocusable = false
    }

}