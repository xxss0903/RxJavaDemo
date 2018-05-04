package com.example.zack.rxjavademo.merchantparser

/**
 * Created by zack zeng on 2018/3/15.
 */
class EmvMerchantTag constructor(tag: String, length: Int, value: String) {

    var tag: String
    var length: Int = -1
    var value: String
    var subTagList: MutableList<EmvMerchantTag>? = mutableListOf()

    constructor(tag: String, length: Int, subTagList: MutableList<EmvMerchantTag>?) : this(tag, length, "") {
        this.subTagList = subTagList
    }


    constructor(tag: String, length: Int, value: String, subTagList: MutableList<EmvMerchantTag>?) : this(tag, length, value) {
        this.subTagList = subTagList
    }

    init {
        this.tag = tag
        this.length = length
        this.value = value
    }


    override fun toString(): String {
        var tempValue = ""
        if (subTagList != null && subTagList!!.isNotEmpty()) {
            for (subTag in subTagList!!) {
                tempValue += subTag.toString()
            }
        } else {
            tempValue = value
        }

        if (tempValue.length < length) {
            val diffLength = length - value.length
            for (i in 0..diffLength) {
                value = "0" + value
            }
        }

        var lengthStr = ""
        if (length < 10) {
            lengthStr = "0$length"
        }
        return tag + lengthStr + tempValue
    }
}