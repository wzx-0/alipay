package cn.seehoo.common.elecheck.util;

import cn.seehoo.spg.commons.core.exception.BusinessException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
    private static String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public Md5Utils() {
    }

    private static String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] b = data.getBytes("UTF8");
        md5.update(b, 0, b.length);
        return byteArrayToHexString(md5.digest());
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < b.length; ++i) {
            sb.append(byteToHexString(b[i]));
        }

        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = 256 + b;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String genMd5(String msg){
        try {
            String messageDigest = md5(msg);
            return messageDigest;
        } catch (Exception var3) {
            throw new BusinessException("Md5加密过程出现异常");
        }
    }
}
