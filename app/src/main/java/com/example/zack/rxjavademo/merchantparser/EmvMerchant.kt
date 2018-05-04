package com.example.zack.rxjavademo.merchantparser

import io.reactivex.Observable


/**
 * Created by zack on 2018/3/15.
 */
object EmvMerchant {

    fun encode(paramList: MutableMap<String, String>?) {
        EmvMerchantEncoder.instance.encode(paramList)
    }


    fun decode(content: String?): Observable<MutableList<EmvMerchantTag>>? {
        if (content == null) {
            return null
        }
        return EmvMerchantDecoder.instance.decode(content)
    }

    fun decode2(content:String?): MutableList<EmvMerchantTag>?{
        return EmvMerchantDecoder.instance.decode2(content)
    }
}