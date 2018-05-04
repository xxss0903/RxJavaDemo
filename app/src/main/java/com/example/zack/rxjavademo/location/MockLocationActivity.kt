package com.example.zack.rxjavademo.location

import android.app.Activity
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.example.zack.rxjavademo.R
import kotlinx.android.synthetic.main.activity_mock_location.*
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Config


/**
 * Created by zack zeng on 2018/3/20.
 */
class MockLocationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock_location)
        initView()
    }

    private fun initView() {
        btn_change_location.setOnClickListener({
            getPermission()
        })
    }

    private fun changeCurrentLocation() {
        val locationPair = getLocationText()
        mockGPSLocation(locationPair)
    }

    private fun mockGPSLocation(locationPair: Pair<String, String>) {
        val providerStr = LocationManager.GPS_PROVIDER

//        val provider = LocationManager().getProvider(providerStr)


    }

    fun isAllowMockLocation(context: Activity): Boolean {
        var isOpen = Settings.Secure.getInt(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0

        if (isOpen && Build.VERSION.SDK_INT > 22) {
            isOpen = false
        }

        if (isOpen) {
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
        }

        return isOpen
    }

    private fun getPermission() {
        if (isAllowMockLocation(this)){
            changeCurrentLocation()
        }
    }

    private fun getLocationText(): Pair<String, String> {
        val longitude = et_longitude.text.toString()
        val latitude = et_latitude.text.toString()
        return Pair(longitude, latitude)
    }
}