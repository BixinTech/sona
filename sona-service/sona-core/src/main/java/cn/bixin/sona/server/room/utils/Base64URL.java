package cn.bixin.sona.server.room.utils;

// 使用旧版本 base64 编解码实现增强兼容性

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64URL {
    public static byte[] base64EncodeUrl(byte[] input) {
        byte[] base64 = new BASE64Encoder().encode(input).getBytes();
        for (int i = 0; i < base64.length; ++i)
            switch (base64[i]) {
                case '+':
                    base64[i] = '*';
                    break;
                case '/':
                    base64[i] = '-';
                    break;
                case '=':
                    base64[i] = '_';
                    break;
                default:
                    break;
            }
        return base64;
    }

    public static byte[] base64DecodeUrl(byte[] input) throws IOException {
        byte[] base64 = input.clone();
        for (int i = 0; i < base64.length; ++i)
            switch (base64[i]) {
                case '*':
                    base64[i] = '+';
                    break;
                case '-':
                    base64[i] = '/';
                    break;
                case '_':
                    base64[i] = '=';
                    break;
                default:
                    break;
            }
        return new BASE64Decoder().decodeBuffer(base64.toString());
    }
}
