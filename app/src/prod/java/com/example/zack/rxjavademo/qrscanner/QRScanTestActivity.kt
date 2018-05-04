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
import android.util.Log
import android.widget.Toast
import com.example.zack.rxjavademo.R
import com.example.zack.rxjavademo.R.id.iv_generated_qr
import com.example.zack.rxjavademo.R.id.tv_tlv_content
import com.example.zack.rxjavademo.emvco.BerTlvParser
import com.example.zack.rxjavademo.emvco.BerTlvs
import com.example.zack.rxjavademo.emvco.HexUtil
import com.example.zack.rxjavademo.merchantparser.EmvMerchant
import com.example.zack.rxjavademo.merchantparser.EmvcoDecodeException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.ONE_D_CODE_TYPES
import com.google.zxing.integration.android.IntentIntegrator.QR_CODE_TYPES
import com.google.zxing.integration.android.IntentResult
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
        parseStringToEmvco("")
        // scanactivity
        val parseResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (parseResult != null) {
            parseStringToPayCode(parseResult)
            Toast.makeText(this, parseResult.contents, Toast.LENGTH_SHORT).show()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // 分解扫描获取的字符串
    private fun parseStringToPayCode(result: IntentResult?) {
        if (result == null) {
            return
        }
        parseStringToPayCode(result.contents)

        parseStringWithMerchantFormat(result.contents)
    }

    private fun parseStringWithMerchantFormat(content: String?) {
        if (content == null || content.isNullOrEmpty()) {
            return
        }
        try {
            val result = EmvMerchant.decode2(content)

            if (result == null) {
                Toast.makeText(this, "decode wrong", Toast.LENGTH_SHORT).show()
            } else {

            }
        } catch (e: EmvcoDecodeException) {
            e.printStackTrace()
        }
    }

    private fun parseStringToPayCode(content: String?) {
        if (content == null) {
            Toast.makeText(this, R.string.msg_error, Toast.LENGTH_SHORT).show()
            return
        }

        val content2 = "00020101021126340012HK.COM.HKICL010300402079999999520400005303344540578.905802NA5902NA6002NA6304C8B2"
        parseStringToEmvco(content2)

        qrBitmap = QRUtil.instance.generateQRBitmap(content2)
        iv_generated_qr.setImageBitmap(qrBitmap)
    }

    fun parseStringToEmvco(content: String?) {
        if (content == null) {
            return
        }
        val EMVCO_HEADER = "hQVDUF"
        if (content.startsWith(EMVCO_HEADER)) {
            val base64 = HexUtil.decodeBase64(content)
            val parser = BerTlvParser()
            val tlvList = parser.parse(base64)
            displayTlvList(tlvList)
        }
    }

    private fun displayTlvList(tlvList: BerTlvs?) {
        if (tlvList == null) {
            return
        }
        val tlvList = tlvList.list
        var result = ""
        result = HexUtil.toFormattedHexString(tlvList)
        tv_tlv_content.text = result
        Log.d("tlvresult", result)
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    fun toBytes(str: String?): ByteArray {
        if (str == null || str.trim { it <= ' ' } == "") {
            return ByteArray(0)
        }

        val bytes = ByteArray(str.length / 2)
        for (i in 0 until str.length / 2) {
            val subStr = str.substring(i * 2, i * 2 + 2)
            bytes[i] = Integer.parseInt(subStr, 16).toByte()
        }

        return bytes
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
        scanIntegrator.captureActivity = ScanActivity::class.java
        scanIntegrator.setPrompt("扫描二维码")

        scanIntegrator.initiateScan()
    }
}