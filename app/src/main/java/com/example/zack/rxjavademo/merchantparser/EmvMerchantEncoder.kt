package com.example.zack.rxjavademo.merchantparser

import android.util.Log
import com.example.zack.rxjavademo.qrscanner.EMVConstants

/**
 * Created by zack zeng on 2018/3/16.
 */
class EmvMerchantEncoder {

    companion object {
        val instance = EmvMerchantEncoder()
    }

    fun encode(paramList: MutableMap<String, String>?): String {
        val poiTags = getPoiTags()
        val fpsTags = getFpsTags()
        val fixTags = getFixTags()
        val transactionTags = getTransactionTags()
        val additionalFieldTags = getAdditionalFieldTags()

        val tags = mutableListOf<EmvMerchantTag>()
        tags.addAll(poiTags)
        tags.addAll(fpsTags)
        tags.addAll(fixTags)
        tags.addAll(transactionTags)
        if (paramList != null && paramList.isNotEmpty()) {
            val varableTags = getVariableTags(paramList)
            tags.addAll(varableTags)
        }
        tags.addAll(additionalFieldTags)

        return addCRC16Tag(tags)
    }

    private fun getVariableTags(paramList: MutableMap<String, String>): MutableList<EmvMerchantTag> {
        val tags = mutableListOf<EmvMerchantTag>()
        for (param in paramList) {
            val tag = EmvMerchantTag(param.key, param.value.length, param.value)
            tags.add(tag)
        }
        return tags
    }

    private fun addCRC16Tag(tags: MutableList<EmvMerchantTag>): String {
        var result = ""
        if (!tags.isEmpty()) {
            for (tag in tags) {
                var tempTagStr = tag.toString()
                result += tempTagStr
            }
            result += EMVConstants.ID_CRC + "04"
            val crc16Str = CRC16.computeCRC16ByNormalString(result)
            result += crc16Str
        }
        Log.d("emv merchant encode", "result: " + result)
        return result
    }

    private fun getAdditionalFieldTags(): MutableList<EmvMerchantTag> {
        val billNumberTag = EmvMerchantTag(EMVConstants.ID_ADDITIONAL_BILL_NUMBER, getBillNumber().length, getBillNumber())
        val referenceLabelTag = EmvMerchantTag(EMVConstants.ID_ADDITIONAL_REFERENCE_LABEL, getReferenceLabel().length, getReferenceLabel())
        return mutableListOf(billNumberTag)
    }

    private fun getReferenceLabel(): String {
        return ""
    }

    private fun getBillNumber(): String {
        return "01-1234567"
    }

    private fun getTransactionTags(): MutableList<EmvMerchantTag> {
        val currencyTag = EmvMerchantTag(EMVConstants.ID_TRANSACTION_CURRENCY, EMVConstants.ID_TRANSACTION_CURRENCY_VALUE.length, EMVConstants.ID_TRANSACTION_CURRENCY_VALUE)
        val amountTag = EmvMerchantTag(EMVConstants.ID_TRANSACTION_AMOUNT, getAmount().length, getAmount())
        return mutableListOf(currencyTag, amountTag)
    }


    private fun getAmount(): String {
        return "22.22"
    }

    private fun getFixTags(): MutableList<EmvMerchantTag> {
        val categoryTag = EmvMerchantTag(EMVConstants.ID_CATEGORY_CODE, EMVConstants.ID_CATEGORY_CODE_VALUE.length, EMVConstants.ID_CATEGORY_CODE_VALUE)
        val countryTag = EmvMerchantTag(EMVConstants.ID_COUNTRY_CODE, EMVConstants.ID_COUNTRY_CODE_VALUE.length, EMVConstants.ID_COUNTRY_CODE_VALUE)
        val merchantNameTag = EmvMerchantTag(EMVConstants.ID_MERCHANT_NAME, EMVConstants.ID_MERCHANT_NAME_VALUE.length, EMVConstants.ID_MERCHANT_NAME_VALUE)
        val cityTag = EmvMerchantTag(EMVConstants.ID_MERCHANT_CITY, EMVConstants.ID_MERCHANT_CITY_VALUE.length, EMVConstants.ID_MERCHANT_CITY_VALUE)
        return mutableListOf(categoryTag, countryTag, merchantNameTag, cityTag)
    }

    fun getPoiTags(): MutableList<EmvMerchantTag> {
        val poiTag = EmvMerchantTag(EMVConstants.ID_POI, 2, EMVConstants.ID_POI_VALUE)
        return mutableListOf(poiTag, getPoiMethodTag())
    }

    private fun getPoiMethodTag(): EmvMerchantTag {
        val poiMethod = EmvMerchantTag(EMVConstants.ID_POI_METHOD, 2, EMVConstants.ID_POI_METHOD_STATIC)
        return poiMethod
    }

    fun getFpsTags(): MutableList<EmvMerchantTag> {

        val guiTag = EmvMerchantTag(EMVConstants.ID_FPS_GLOBAL_UNIQUE_IDENTIFIER, EMVConstants.ID_FPS_GLOBAL_UNIQUE_IDENTIFIER_VALUE.length, EMVConstants.ID_FPS_GLOBAL_UNIQUE_IDENTIFIER_VALUE)
        val clearingCodeTag = EmvMerchantTag(EMVConstants.ID_FPS_CLEARING_CODE, EMVConstants.ID_FPS_CLEARING_CODE_VALUE.length, EMVConstants.ID_FPS_CLEARING_CODE_VALUE)
        val fpsIdTag = EmvMerchantTag(EMVConstants.ID_FPS_FPS_ID, getFPSIdValue().length, getFPSIdValue())
        val mobileTag = EmvMerchantTag(EMVConstants.ID_FPS_MOBILE_PHONE, getMobilePhoneValue().length, getMobilePhoneValue())
        val emailTag = EmvMerchantTag(EMVConstants.ID_FPS_EMAIL_ADDRESS, getEmailAddress().length, getEmailAddress())
        val timeOutTag = EmvMerchantTag(EMVConstants.ID_FPS_MERCHANT_TIME_OUT, getTimeOut().length, getTimeOut())
        val subTags = mutableListOf(guiTag, clearingCodeTag, fpsIdTag, mobileTag, emailTag, timeOutTag)

        val fpsTagValueStr = getFpsTagString(subTags)

        return mutableListOf(EmvMerchantTag(EMVConstants.ID_FPS, fpsTagValueStr.length, fpsTagValueStr, subTags))
    }

    private fun getFpsTagString(subTags: MutableList<EmvMerchantTag>): String {
        var result = ""
        for (tag in subTags) {
            result += tag.toString()
        }
        return result
    }

    private fun getTimeOut(): String {
        return ""
    }

    private fun getEmailAddress(): String {
        return ""
    }

    private fun getFPSIdValue(): String {
        return ""
    }

    private fun getMobilePhoneValue(): String {
        return ""
    }
}