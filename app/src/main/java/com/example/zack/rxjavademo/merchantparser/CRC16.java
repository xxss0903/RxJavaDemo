package com.example.zack.rxjavademo.merchantparser;

import com.example.zack.rxjavademo.emvco.HexUtil;

/**
 * Created by zack zeng on 2018/3/16.
 */

public class CRC16 {

    public static String computeCRC16ByNormalString(String str) {
        String hexStr = HexUtil.strTo16(str);
        return computeCRC16ByHexString(hexStr);
    }

    public static String computeCRC16ByHexString(String hexStr) {
        byte[] bytes = HexUtil.hexStringToByteArray(hexStr);
        return getCRC16StringUpper(bytes);
    }

    public static String getCRC16StringUpper(byte[] bytes) {
        int crcInt = computeCRC16_CCITT_FALSE(bytes);
        String crcStr = Integer.toHexString(crcInt).toUpperCase();
        if (crcStr.length() < 4){
            int diffLength = 4 - crcStr.length();
            for (int i = 0; i < diffLength; i++) {
                crcStr = "0" + crcStr;
            }
        }
        return crcStr;
    }

    private static int computeCRC16_CCITT_FALSE(byte[] bytes) {
        // initial value
        int crc = 0xffff;
        // polynial value
        int polynomial = 0x1021;
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }

}
