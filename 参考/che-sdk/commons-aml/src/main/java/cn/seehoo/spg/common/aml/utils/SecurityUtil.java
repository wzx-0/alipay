package cn.seehoo.spg.common.aml.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

import javax.crypto.SecretKey;
import java.security.MessageDigest;

/**
 * @author chenzhuo
 */
public class SecurityUtil {

    /**
     * 将二进制转换成16进制 加密
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 获取sm4秘钥
     *
     * @return sm4秘钥
     * **/
    public  static String  getSm4Key(){
        SM4 sm4 = SmUtil.sm4();
        SecretKey  secretKey = sm4.getSecretKey();
        byte[]  encoded = secretKey.getEncoded();
        return HexUtil.encodeHexStr(encoded);
    }

    /**
     * sm4加密
     * @param plainText 明文文本
     * @param sm4key  sm4秘钥
     * @return sm4加密后的密文
     * **/
    public static String encrypt(String plainText, String sm4key) {
        SM4  sm4 = new SM4(HexUtil.decodeHex(sm4key));
        return sm4.encryptHex(plainText);
    }

    /**
     * sm4解密
     * @param encryptText 加密的密文
     * @param sm4key  sm4秘钥
     * @return sm4解密后的明文
     * **/
    public static String decrypt(String encryptText, String sm4key) {
        SM4  sm4 = new SM4(HexUtil.decodeHex(sm4key));
        return sm4.decryptStr(encryptText);
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr == null || hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String md5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 根据系统时间、统一客户分配的渠道号、渠道密码生成签名
     *
     * @param channel 统一客户分配的渠道号
     * @param timestamp 请求HEADER中的timestamp当前系统格林威治毫秒数
     * @param channelSecret 统一客户分配的渠道密码
     *
     * @return 生成请求的签名
     */
    public static String generateSignature(String channel, long timestamp, String channelSecret){
        String str       = String.format("%s%d",channel,timestamp);
        String secretStr = encrypt(str, channelSecret);
        String sign      = md5(secretStr);
        return sign.toUpperCase();
    }

}
