package com.example.zack.rxjavademo.qrscanner

import io.github.binaryfoo.Decoder
import io.github.binaryfoo.RootDecoder

/**
 * Created by zack on 2018/3/6.
 */
object EmvcoUtil {

    private val qrString = "00020101021226450012HK.COM.HKICL0103004020700099990307000999952040000580"
    private var poi_method = "01"
    private var targetType = ""

    fun decodeEmvcoQRCode(proxyId: String, amount:String) {
        generateEmvcoString(proxyId, amount)
    }

    fun encodeEmvcoQRCode(qrCodeString: String) {

    }

    /**
     * generate emv standard string with proxyId and amount
     */
    private fun generateEmvcoString(proxyId: String, amount: String): String {
        if (proxyId.length >= 13) {
            targetType = EMVConstants.BOT_ID_MERCHANT_PHONE_NUMBER
        } else {
            EMVConstants.BOT_ID_MERCHANT_TAX_ID
        }

        // if dynamic code or static code
        poi_method = if (amount.isNotEmpty()) {
            EMVConstants.ID_POI_METHOD_DYNAMIC
        } else {
            EMVConstants.ID_POI_METHOD_STATIC
        }

        val emvString = createEmvStandardString(proxyId, amount)

        // calculate crc16
        val crc = CRC16().computeCheckSum(emvString.toByteArray())
        val hexCrc = NumUtil.toHex(crc)
        return (emvString + hexCrc).padStart(4, '0')
    }

    private fun createEmvStandardString(proxyId: String, amount: String): String {
        return EMVConstants.ID_PAYLOAD_FORMAT +
                "${EMVConstants.PAYLOAD_FORMAT_EMV_QRCPS_MERCHANT_PRESENTED_MODE.length}" +
                EMVConstants.PAYLOAD_FORMAT_EMV_QRCPS_MERCHANT_PRESENTED_MODE +
                EMVConstants.ID_POI_METHOD +
                "${EMVConstants.ID_POI_METHOD.length}" +
                "${poi_method.length}" +
                poi_method +
                EMVConstants.ID_MERCHANT_INFORMATION_BOT +
                "${37}" +
                EMVConstants.MERCHANT_INFORMATION_TEMPLATE_ID_GUID +
                "${EMVConstants.GUID_PROMPTPAY}${targetType}" +
                "${proxyId.length}" +
                proxyId +
                EMVConstants.ID_COUNTRY_CODE +
                "${EMVConstants.COUNTRY_CODE_TH.length}" +
                EMVConstants.ID_TRANSACTION_CURRENCY +
                "${EMVConstants.TRANSACTION_CURRENCY_THB.length}" +
                EMVConstants.TRANSACTION_CURRENCY_THB +
                EMVConstants.ID_TRANSACTION_AMOUNT +
                "${amount.length}$amount${EMVConstants.ID_CRC}" +
                "04"
    }
}