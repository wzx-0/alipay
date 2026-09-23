package cn.seehoo.spg.common.bhrc.model;

import cn.seehoo.spg.common.bhrc.constant.BhrcConstant;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author caofei
 * @desc 百行征信公安四要素认证响应结果
 * @time 2025/9/24 20:28。
 */
public class BhrcResDTO {
    private ResponseHead head;
    private String response;

    public static class ResponseHead{
        private String requestRefId;
        private String responseRefId;
        private String result;
        private String responseCode;
        private String responseMsg;

        public String getRequestRefId() {
            return requestRefId;
        }

        public void setRequestRefId(String requestRefId) {
            this.requestRefId = requestRefId;
        }

        public String getResponseRefId() {
            return responseRefId;
        }

        public void setResponseRefId(String responseRefId) {
            this.responseRefId = responseRefId;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(String responseCode) {
            this.responseCode = responseCode;
        }

        public String getResponseMsg() {
            return responseMsg;
        }

        public void setResponseMsg(String responseMsg) {
            this.responseMsg = responseMsg;
        }
    }
    public ResponseHead getHead() {
        return head;
    }

    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static String decrypt(String response, String keyType, String key) throws Exception {
        if (keyType.toLowerCase().equals("3des") || keyType.toLowerCase().equals("desede")) {
            DESedeKeySpec dks = new DESedeKeySpec(new BASE64Decoder().decodeBuffer(key));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] b = cipher.doFinal(new BASE64Decoder().decodeBuffer(response));
            return new String(b, StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * 请求是否成功
     * @return true 成功 false 失败
     */
    public boolean authSuccess(){
        if(null == this.head){
            return false;
        }
        return BhrcConstant.SUCCESS.equals(this.head.getResponseCode());
    }
}
