package com.zack.webbrowser

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val htmlPath = "file:///android_asset/scheme.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWebBrowser()
    }

    private fun initWebBrowser() {
        webBrowser.loadUrl(htmlPath)
    }
}
