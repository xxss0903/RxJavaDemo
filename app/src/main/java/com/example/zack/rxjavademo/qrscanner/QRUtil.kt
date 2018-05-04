package com.example.zack.rxjavademo.qrscanner

import android.graphics.Bitmap
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers


/**
 * generate qrcode
 * Created by zack on 2018/3/1.
 */
class QRUtil {

    private var decodeCodeError: String = "Decode QRCode error!"
    private val multiFormatWriter: MultiFormatWriter = MultiFormatWriter()
    // qr image saved directory
    private var qrDir: String = ""
    private var qrWidth: Int = 400
    private var qrHeight: Int = 400

    companion object {
        val instance = QRUtil()
        private var hints: HashMap<EncodeHintType, String> = HashMap()
            set(value) {
                value.put(EncodeHintType.CHARACTER_SET, "UTF-8")
            }
    }

    fun generateQRBitmap(content: String?): Bitmap? {
        if (content.isNullOrEmpty()) {
            return null
        }
        val encodeContent = encodeContent(content)
        return generateQRBitmap(encodeContent)
    }

    private fun encodeContent(content: String?): BitMatrix? {
        if (content.isNullOrEmpty()) {
            return null
        }
        return multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints)
    }

    private fun generateQRBitmap(matrix: BitMatrix?): Bitmap? {
        if (matrix == null) {
            return null
        }
        return BarcodeEncoder().createBitmap(matrix)
    }

    fun scanImageQrCode(qrBitmap: Bitmap?): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String> { e ->
            val content = QRCodeDecoder.syncDecodeQRCode(qrBitmap)
            if (content == null) {
                e.onNext(decodeCodeError)
            } else {
                e.onNext(content)
            }
        }).subscribeOn(Schedulers.io())
    }
}