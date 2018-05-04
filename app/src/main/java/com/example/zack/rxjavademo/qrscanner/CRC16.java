package com.example.zack.rxjavademo.qrscanner;

/**
 * Created by zack on 2018/3/6.
 */

public class CRC16 {

    private int polynomial= 0x1021;
    private int initialValue = 0xffff;
    private int[] table = new int[256];

    public CRC16(){
        int temp, a;
        for (int i = 0; i < table.length; i++)
        {
            temp = 0;
            a = (i << 8);
            for (int j = 0; j < 8; j++)
            {
                if (((temp ^ a) & 0x8000) != 0)
                    temp = ((temp << 1) ^ polynomial);
                else
                    temp <<= 1;
                a <<= 1;
            }
            table[i] = temp;
        }
    }

    public int computeCheckSum(byte[] bytes){
        int crc = initialValue;
        for (int i = 0; i < bytes.length; i++) {
            crc = ((crc<<8) ^ table[(crc>>8) ^ (0xff & bytes[i])]);
        }
        return crc;
    }

    public byte[] computeCheckSumBytes(byte[] bytes){
        int crc = computeCheckSum(bytes);
        return new byte[]{(byte)(crc>>8), (byte)(crc&0x00ff)};
    }

}
