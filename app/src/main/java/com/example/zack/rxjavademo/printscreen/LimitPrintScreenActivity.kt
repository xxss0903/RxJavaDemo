package com.example.zack.rxjavademo.printscreen

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import com.example.zack.rxjavademo.R
import kotlinx.android.synthetic.main.activity_limit_printscreen.*

/**
 * limit print screen
 * Created by zack on 2018/2/1.
 */
class LimitPrintScreenActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_limit_printscreen)

        initViews()
    }

    private fun initViews() {
        btnAddLimit.setOnClickListener({
            addLimitPrintScreen()
        })
        btnAllowPrintScreen.setOnClickListener({
            allowPrintScreen()
        })
    }

    private fun addLimitPrintScreen(){
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun allowPrintScreen(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

}