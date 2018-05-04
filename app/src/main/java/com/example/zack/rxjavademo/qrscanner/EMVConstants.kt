package com.example.zack.rxjavademo.qrscanner

/**
 * emv standard constants
 * Created by zack on 2018/3/6.
 */
object EMVConstants {
    const val ID_NORMAL_LENGTH = "02"

    const val ID_POI = "00"
    const val ID_POI_VALUE = "01"

    const val ID_POI_METHOD = "01"
    const val ID_POI_METHOD_STATIC = "11"
    const val ID_POI_METHOD_DYNAMIC = "12"

    // fps
    const val ID_FPS = "26"
    const val ID_FPS_GLOBAL_UNIQUE_IDENTIFIER = "00"
    const val ID_FPS_GLOBAL_UNIQUE_IDENTIFIER_VALUE = "hk.com.hkicl"
    const val ID_FPS_CLEARING_CODE = "01"
    const val ID_FPS_CLEARING_CODE_VALUE = "003"
    const val ID_FPS_FPS_ID = "02"
    const val ID_FPS_FPS_ID_VALUE = ""
    const val ID_FPS_MOBILE_PHONE = "03"
    const val ID_FPS_MOBILE_PHONE_VALUE = ""
    const val ID_FPS_EMAIL_ADDRESS = "04"
    const val ID_FPS_MERCHANT_TIME_OUT = "05"
    const val ID_FPS_MERCHANT_TIME_OUT_VALUE = "101122111111"

    const val ID_CATEGORY_CODE = "52"
    const val ID_CATEGORY_CODE_VALUE = "0000"

    const val ID_TRANSACTION_CURRENCY = "53"
    const val ID_TRANSACTION_CURRENCY_VALUE = "344"

    const val ID_COUNTRY_CODE = "58"
    const val ID_COUNTRY_CODE_VALUE = "NA"

    const val ID_MERCHANT_NAME = "59"
    const val ID_MERCHANT_NAME_VALUE = "NA"

    const val ID_MERCHANT_CITY = "60"
    const val ID_MERCHANT_CITY_VALUE = "NA"

    const val ID_TRANSACTION_AMOUNT = "54"
    const val ID_TRANSACTION_AMOUNT_VALUE = ""


    const val ID_PAYLOAD_FORMAT = "01"
    const val ID_MERCHANT_INFORMATION_BOT = "29"
    const val ID_CRC = "63"

    const val ID_ADDITIONAL_FIELD = "62"
    const val ID_ADDITIONAL_BILL_NUMBER = "01"
    const val ID_ADDITIONAL_REFERENCE_LABEL = "05"
    const val ID_ADDITIONAL_CONSUMER_DATA = "09"


    const val PAYLOAD_FORMAT_EMV_QRCPS_MERCHANT_PRESENTED_MODE = "01"
    const val MERCHANT_INFORMATION_TEMPLATE_ID_GUID = "00"
    const val MERCHANT_INFORMATION_TEMPLATE_ID_GUID_VALUE = "01"
    const val BOT_ID_MERCHANT_PHONE_NUMBER = "01"
    const val BOT_ID_MERCHANT_TAX_ID = "02"
    const val GUID_PROMPTPAY = "A000000677010111"
    const val TRANSACTION_CURRENCY_THB = "764"
    const val COUNTRY_CODE_TH = "TH"
    const val LANGUAGE_TEMPLATE = 64
    const val ADDITIONAL_TEMPLATE = 62

}