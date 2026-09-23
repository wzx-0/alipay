package cn.seehoo.common.elecheck.util;

import cn.hutool.core.util.StrUtil;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AesAlgorithmUtil {

    private static final String ENCODE = System.getProperty("file.encoding");

    public static String encrypt(String content, String pwdKey){
        if (!StrUtil.isBlank(content) && !StrUtil.isBlank(pwdKey)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(pwdKey.getBytes(StandardCharsets.UTF_8));
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                byte[] byteContent = content.getBytes(ENCODE);
                cipher.init(1, key);
                byte[] result = cipher.doFinal(byteContent);
                return Base64.encodeBase64URLSafeString(result);
            } catch (Exception var10) {
                throw new BusinessException("AESException:对参数进行AES加解密过程中异常");
            }
        } else {
            throw new BusinessException("参数不合法");
        }
    }
}
