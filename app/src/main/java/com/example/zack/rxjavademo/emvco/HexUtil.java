package com.example.zack.rxjavademo.emvco;

import android.util.Base64;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public class HexUtil {

    private static final char[] CHARS_TABLES = "0123456789ABCDEF".toCharArray();
    static final byte[] BYTES = new byte[128];

    static {
        for (int i = 0; i < 10; i++) {
            BYTES['0' + i] = (byte) i;
            BYTES['A' + i] = (byte) (10 + i);
            BYTES['a' + i] = (byte) (10 + i);
        }
    }

    public static byte[] decodeBase64(String aBase64Str) throws Exception {
        return Base64.decode(aBase64Str, Base64.DEFAULT);
    }

    public static String toHexString(byte[] aBytes) {
        return toHexString(aBytes, 0, aBytes.length);
    }

    public static String toFormattedHexString(byte[] aBytes) {
        return toFormattedHexString(aBytes, 0, aBytes.length);
    }

    public static String toHexString(byte[] aBytes, int aLength) {
        return toHexString(aBytes, 0, aLength);
    }

    public static byte[] parseHex(String aHexString) {
        char[] src = aHexString.replace("\n", "").replace(" ", "").toUpperCase().toCharArray();
        byte[] dst = new byte[src.length / 2];

        for (int si = 0, di = 0; di < dst.length; di++) {
            byte high = BYTES[src[si++] & 0x7f];
            byte low = BYTES[src[si++] & 0x7f];
            dst[di] = (byte) ((high << 4) + low);
        }

        return dst;
    }

    public static String toFormattedHexString(byte[] aBytes, int aOffset, int aLength) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(aLength);
        sb.append("] :");
        for (int si = aOffset, di = 0; si < aOffset + aLength; si++, di++) {
            byte b = aBytes[si];
            if (di % 4 == 0) {
                sb.append("  ");
            } else {
                sb.append(' ');
            }
            sb.append(CHARS_TABLES[(b & 0xf0) >>> 4]);
            sb.append(CHARS_TABLES[(b & 0x0f)]);

        }

        return sb.toString();

    }

    public static String toHexString(byte[] aBytes, int aOffset, int aLength) {
        char[] dst = new char[aLength * 2];

        for (int si = aOffset, di = 0; si < aOffset + aLength; si++) {
            byte b = aBytes[si];
            dst[di++] = CHARS_TABLES[(b & 0xf0) >>> 4];
            dst[di++] = CHARS_TABLES[(b & 0x0f)];
        }

        return new String(dst);
    }

    public static String toFormattedHexString(List<? extends BerTlv> tlvList) {
        if (tlvList == null || tlvList.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tlvList.size(); i++) {
            BerTlv berTlv = tlvList.get(i);
            result.append(getBerTlvString(berTlv, 0));
        }

        return result.toString();
    }

    private static String getBerTlvString(BerTlv berTlv, int index) {
        StringBuilder result = new StringBuilder();
        if (!berTlv.isConstructed()) {
            result.append(berTlv.getTag().toString());
            result.append("\n");
            result.append("   ").append(berTlv.getHexValue());
            String normalStr = hexStrToNormalStr(berTlv.getHexValue());
            Log.d("tlv result", "normal string: " + normalStr);
        } else {
            if (berTlv.theList != null) {
                result.append("\n");
                result.append(berTlv.getTag().toString());
                result.append("\n");
                for (int i = 0; i < berTlv.theList.size(); i++) {
                    BerTlv subTlv = berTlv.theList.get(i);
                    result.append("       ");
                    result.append(getBerTlvString(subTlv, 0));
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

    public static String hexStrToNormalStr(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    private static final String hexStr = "0123456789ABCDEF";

    public static String strTo16(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexStr.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexStr.charAt((bytes[i] & 0x0f) >> 0));
        }

        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return b;
    }

}
