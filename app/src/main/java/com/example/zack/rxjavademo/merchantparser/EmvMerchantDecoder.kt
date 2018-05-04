package com.example.zack.rxjavademo.merchantparser

import android.util.Log
import android.widget.Toast
import com.example.zack.rxjavademo.MainApplication
import com.example.zack.rxjavademo.qrscanner.EMVConstants
import io.reactivex.Observable
import org.apache.http.conn.ConnectTimeoutException

/**
 * Created by zack zeng on 2018/3/16.
 */
class EmvMerchantDecoder {

    companion object {
        val instance = EmvMerchantDecoder()
    }

    fun decode(content: String): Observable<MutableList<EmvMerchantTag>> {
        // verify merchant by crc16_ccitt_false
        return Observable.create({ e ->
            run {
                verifyEmvMerchant(content).subscribe({
                    if (it) {
                        e.onNext(getTagListImpl(content, true))
                    } else {
                        handleNotCorrectFormatString(content)
                    }
                }, {
                    handleNotCorrectFormatString(content)
                })
            }
        })
    }

    fun decode2(content: String?): MutableList<EmvMerchantTag>? {
        if (verifyEmvMerchant2(content)){
            return getTagListImpl(content, true)
        } else {
            throw EmvcoDecodeException("Content Wrong Format")
        }
    }

    fun handleNotCorrectFormatString(content: String){
        Toast.makeText(MainApplication.instance(), "wrong format emv", Toast.LENGTH_SHORT).show()
    }

    private fun getTagListImpl(content: String?, shouldDivideSubTag: Boolean): MutableList<EmvMerchantTag> {
        val tagList = mutableListOf<EmvMerchantTag>()
        var currentIndex = 0
        val normalLength = 2

        if (content == null || content.length < 6){
            throw EmvcoDecodeException("Wrong length of decoding content")
        }

        try {
            while (currentIndex <= content.length - 1) {
                val tagStr = content.substring(currentIndex, currentIndex + normalLength)
                currentIndex += normalLength
                val length = content.substring(currentIndex, currentIndex + normalLength).toInt()
                currentIndex += normalLength
                val value = content.substring(currentIndex, currentIndex + length)
                currentIndex += length

                var tag: EmvMerchantTag

                if (shouldDivideSubTag) {
                    when (tagStr.toInt()) {
                    // merchant information
                        in 2..25, in 26..51, EMVConstants.ADDITIONAL_TEMPLATE, EMVConstants.LANGUAGE_TEMPLATE, in 80..99 -> {
                            Log.d("emv merchant sub tag", "tag; $tagStr length: $length value: $value")
                            val subTagList = getTagListImpl(value, false)
                            tag = EmvMerchantTag(tagStr, length, subTagList)
                        }
                        else -> {
                            Log.d("emv merchant tag", "tag; $tagStr length: $length value: $value")
                            tag = EmvMerchantTag(tagStr, length, value)
                        }
                    }
                } else {
                    Log.d("emv merchant tag", "tag; $tagStr length: $length value: $value")
                    tag = EmvMerchantTag(tagStr, length, value)
                }
                tagList.add(tag)
            }
            return tagList
        } catch (e: Exception){
            e.printStackTrace()
        }
        throw EmvcoDecodeException("Wrong format")
    }

    private fun verifyEmvMerchant(content: String?): Observable<Boolean> {
        return Observable.create({
            if (content == null || content.length < 6) {
                it.onNext(false)
            } else {
                val verifiedStr = content.substring(0, content.length - 4)
                val (crc16Str, computedCrc16Str) = computeCrc16(content, verifiedStr)
                if (crc16Str.toUpperCase() == computedCrc16Str.toUpperCase()) {
                    it.onNext(true)
                } else {
                    it.onNext(false)
                }
            }
        })
    }

    private fun verifyEmvMerchant2(content: String?): Boolean {
        if (content == null || content.length < 6) {
            return false
        } else {
            val verifiedStr = content.substring(0, content.length - 4)
            val (crc16Str, computedCrc16Str) = computeCrc16(content, verifiedStr)
            return crc16Str.toUpperCase() == computedCrc16Str.toUpperCase()
        }
    }

    private fun computeCrc16(content: String, verifiedStr: String): Pair<String, String> {
        val crc16Str = content.substring(content.length - 4, content.length)
        val computedCrc16Str = CRC16.computeCRC16ByNormalString(verifiedStr)
        return Pair(crc16Str, computedCrc16Str)
    }
}