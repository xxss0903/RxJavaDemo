package com.example.zack.rxjavademo.qrscanner

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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

        return createBitmap(matrix)
    }

    fun createBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                when (matrix[x, y]) {
                    true -> {
                        pixels[offset + x] = 0xFFDC143C.toInt()
                    }
                    else -> {
                        pixels[offset + x] = 0xFFFFFFFF.toInt()
                    }
                }
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)


        val canvas = Canvas(bitmap)
        val paint = Paint()
        val paint2 = Paint()
        val paint3 = Paint()
        paint.color = 0xFFFC121C.toInt()
        paint2.color = 0xFFF888F8.toInt()
        paint3.color = 0xFFDDDF88.toInt()

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                when (matrix[x, y]) {
                    true -> {
                        if (x % 2 == 0) {
                            canvas.drawCircle(x.toFloat(), y.toFloat(), 1.toFloat(), paint)
                        } else {
                            canvas.drawCircle(x.toFloat(), y.toFloat(), 1.toFloat(), paint2)
                        }
                    }
                    else -> {
//                        if (x % 2 == 0) {
                            canvas.drawCircle(x.toFloat(), y.toFloat(), 1.toFloat(), paint3)
//                        }
                    }
                }
            }
        }
//        canvas.drawCircle(100f, 100f, 20f, paint)
        return bitmap
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