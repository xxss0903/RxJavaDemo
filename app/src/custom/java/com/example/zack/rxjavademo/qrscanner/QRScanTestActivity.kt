package com.example.zack.rxjavademo.qrscanner

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.zack.rxjavademo.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.ONE_D_CODE_TYPES
import com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES
import com.google.zxing.integration.android.IntentResult
import com.shadowdata.hsbczxing.CaptureActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_test_scan.*

/**
 * qrcode scan activity
 * Created by zack on 2018/2/27.
 */
class QRScanTestActivity : Activity() {

    private var qrBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_scan)
        initDatas()
    }

    private fun initDatas() {
        btnGenerateCode.setOnClickListener {
            generateQRCode()
        }
        btnScanCode.setOnClickListener({
            scanQRCode()
        })
        iv_generated_qr.setOnLongClickListener {
            decodeQRBitmap()
            return@setOnLongClickListener true
        }
    }

    private fun decodeQRBitmap() {
        if (qrBitmap != null) {
            QRUtil.instance.scanImageQrCode(qrBitmap)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                    })
        }
    }

    private fun scanQRCode() {
        askFormCameraPermission()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun askFormCameraPermission() {
        val permission = Manifest.permission.CAMERA
        var perms = arrayOf(permission)
        val cameraPermission = ActivityCompat.checkSelfPermission(this, permission)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            enterScanActivity()
        } else {
            requestPermissions(perms, 200)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // captureactivity
        if (resultCode == RESULT_OK && data != null) {
            parseStringToPayCode(data.getStringExtra("result"))
        }

        // scanactivity
//        val parseResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (parseResult != null) {
//            parseStringToPayCode(parseResult)
//            Toast.makeText(this, parseResult.contents, Toast.LENGTH_SHORT).show()
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
    }

    // 分解扫描获取的字符串
    private fun parseStringToPayCode(result: IntentResult?) {
        if (result == null) {
            return
        }
        parseStringToPayCode(result.contents)
    }

    private fun parseStringToPayCode(content: String?) {
        if (content == null){
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show()
            return
        }
        qrBitmap = QRUtil.instance.generateQRBitmap(content)
        iv_generated_qr.setImageBitmap(qrBitmap)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        if (grantResults == null) {
            return
        }
        if (requestCode == 200) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (granted) {
                enterScanActivity()
            } else {
                Toast.makeText(this, "request failed", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun enterScanActivity() {
        initScan()
    }

    private fun generateQRCode() {

    }

    private fun initScan() {
        val scanIntegrator = IntentIntegrator(this)
        scanIntegrator.setBarcodeImageEnabled(false)
        scanIntegrator.setBeepEnabled(true)
        val codeTypeList = mutableListOf<String>()
        codeTypeList.addAll(QR_CODE_TYPES.toList())
        codeTypeList.addAll(ONE_D_CODE_TYPES.toList())
        scanIntegrator.setDesiredBarcodeFormats(codeTypeList)
        scanIntegrator.setOrientationLocked(true)
        scanIntegrator.captureActivity = CaptureActivity::class.java
        scanIntegrator.setPrompt("扫描二维码")

        scanIntegrator.initiateScan()
    }
}