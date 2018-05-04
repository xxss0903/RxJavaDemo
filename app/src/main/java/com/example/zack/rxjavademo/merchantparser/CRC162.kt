package com.example.zack.rxjavademo.merchantparser

import kotlin.experimental.and

/**
 * Created by zack zeng on 2018/3/19.
 */
object CRC162 {

    fun computeCrc16Normal(content: String):String {
        val hexStr = strTo16(content)
        return computeCrc16Hex(hexStr)
    }

    fun computeCrc16Hex(hexStr: String):String {
        val bytes = hexStringToByteArray(hexStr)
        return getCRC16StringUpper(bytes)
    }

    fun getCRC16StringUpper(bytes: ByteArray): String {
        val crcInt = computeCRC16_CCITT_FALSE(bytes)
        var crcStr = Integer.toHexString(crcInt).toUpperCase()
        if (crcStr.length < 4) {
            val diffLength = 4 - crcStr.length
            for (i in 0 until diffLength) {
                crcStr = "0" + crcStr
            }
        }
        return crcStr
    }

    private fun computeCRC16_CCITT_FALSE(bytes: ByteArray): Int {
        // initial value
        var crc = 0xffff
        // polynial value
        val polynomial = 0x1021
        for (index in bytes.indices) {
            val b = bytes[index]
            for (i in 0..7) {
                val bit = b.toInt() shr 7 - i and 1 == 1
                val c15 = crc shr 15 and 1 == 1
                crc = crc shl 1
                if (c15 xor bit)
                    crc = crc xor polynomial
            }
        }
        crc = crc and 0xffff
        return crc
    }

    fun strTo16(s: String): String {
        val hexStr = "0123456789ABCDEF"
        val bytes = s.toByteArray()
        val sb = StringBuilder(bytes.size * 2)

        for (i in bytes.indices) {
            val shr4 = bytes[i].toInt().and(0xf0) shr 4
            val shr0 = bytes[i].toInt().and(0x0f) shr 0

            sb.append(hexStr[shr4])
            sb.append(hexStr[shr0])
        }

        return sb.toString()
    }

    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val b = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            b[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character
                    .digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return b
    }

}