package com.example.zack.rxjavademo.scheme

import android.app.Activity
import android.os.Bundle
import com.example.zack.rxjavademo.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * scheme open activity
 * Created by zack on 2018/2/8.
 */
class SchemeActivity:Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheme)

        initScheme()
    }

    private fun initScheme() {
        if (intent != null && intent.scheme != null){
            val uri = intent.data
            println(uri)
            println(uri.host)
            println(uri.scheme)
            println(uri.path)
            println(uri.query)

            tv_content.text = uri.query
        }
    }
}