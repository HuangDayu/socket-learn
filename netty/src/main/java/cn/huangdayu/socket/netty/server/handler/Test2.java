package cn.huangdayu.socket.netty.server.handler;

import cn.huangdayu.socket.netty.server.DecimalUtil;

public class Test2 {
    public static void main(String[] args) {
        Byte[] bytes = new Byte[]{};

        byte top1 = (byte)0xFF;//帧头（2Byte）
        byte top2 = (byte)0xFF;//帧头（2Byte）
        byte version = (byte)0x01;//协议版本（1Byte）
        byte len = (byte)0x0099;//数据长度（2Byte）
        byte from = (byte)0x01;//数据来源（1Byte）

        byte dom = (byte) 0xAAAA;//帧尾（2Byte）


        byte[] bytes2 = new byte[]{top1,top2,version,len,from,dom};

        System.out.println(DecimalUtil.bytesToHex(bytes2));

    }
}
