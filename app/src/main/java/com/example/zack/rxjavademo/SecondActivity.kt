package com.example.zack.rxjavademo

import android.app.Activity
import android.os.Bundle
import com.example.zack.rxjavademo.delayhanlde.OnCheckTextListener
import kotlinx.android.synthetic.main.activity_second.*

/**
 * test delay handle text of edittext
 * Created by zack on 2018/1/30.
 */
class SecondActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        etDelaySecond.cancelCountTimer()
    }

    private fun initViews() {
        etDelaySecond.setCheckTextListener(object : OnCheckTextListener(){
            override fun checkContent(content: String) {
                if (content.contains("error")) {
                    etCountainerSecond.apply {
                        bottomMessage.text = "error input"
                        bottomMessage.contentDescription = "error error input"
                        bottomMessage.announceForAccessibility(bottomMessage.contentDescription)
                        showBottomMessage = true
                    }
                } else {
                    etCountainerSecond.showBottomMessage = false
                }
                tv_content_second.text = content
            }

            override fun beforeCheckContent(s: CharSequence?, start: Int, count: Int, after: Int) {
                etCountainerSecond.showBottomMessage = false
            }
        })

        btnResetCountTime.setOnClickListener({
            etDelaySecond.resetCountTime(10000)
        })
    }

}