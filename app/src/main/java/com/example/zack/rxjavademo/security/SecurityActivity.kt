package com.example.zack.rxjavademo.security

import android.app.Activity
import android.os.Bundle
import com.example.zack.rxjavademo.R
import kotlinx.android.synthetic.main.activity_security.*
import javax.crypto.KeyGenerator

/**
 * 安全性保存
 * Created by zack on 2018/2/1.
 */
class SecurityActivity: Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)

        initViews()
    }

    private fun initViews() {
        btn_security.setOnClickListener({
            // 对字符串进行加密
            val originString = et_input.text.toString()
            encryptString(originString)
        })
    }

    private fun encryptString(originString: String):String {
        // 进行加密

        return ""
    }

    // 字符串添加到keystore
    private fun addToKeyStore(input: String){

    }

}