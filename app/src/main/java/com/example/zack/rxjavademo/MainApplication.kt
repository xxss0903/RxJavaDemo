package com.example.zack.rxjavademo

import android.app.Application
import android.content.Context

/**
 * Created by zack zeng on 2018/3/16.
 */
class MainApplication : Application() {

    companion object {
        private var instance: Application? = null
        fun instance() = instance!!
    }

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}