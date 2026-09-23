package cn.seehoo.spg.common.bhrc.model;

import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author caofei
 * @desc app 百行征信公安四要素认证请求参数
 * @time 2025/9/24 20:22。
 */
public class BhrcReqDTO {
    private RequestHead head;
    private RequestParam request;
    public static class RequestHead{
        private String requestRefId;
        private String secretId;
        private String signature;

        public RequestHead(String requestRefId, String secretId, String signature) {
            this.requestRefId = requestRefId;
            this.secretId = secretId;
            this.signature = signature;
        }

        public String getRequestRefId() {
            return requestRefId;
        }

        public void setRequestRefId(String requestRefId) {
            this.requestRefId = requestRefId;
        }

        public String getSecretId() {
            return secretId;
        }

        public void setSecretId(String secretId) {
            this.secretId = secretId;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        @Override
        public String toString() {
            return "\"head\":{" +
                    "\"requestRefId\":\"" + requestRefId + "\"" +
                    ", \"secretId\":\"" + secretId + "\"" +
                    ", \"signature\":\"" + signature + "\"" +
                    "}";
        }
    }
    public static class RequestParam{
        private Map<String,String> param;

        public RequestParam(Map<String, String> param) {
            this.param = param;
        }

        public Map<String,String> getParam() {
            return param;
        }

        public void setParam(Map<String,String> param) {
            this.param = param;
        }

        @Override
        public String toString() {
            return "\"request\":" +this.getMapStr();
        }
        private String getMapStr(){
            StringBuffer sb = new StringBuffer("{\"param\":{");
            for (String key : param.keySet()) {
                sb.append("\"").append(key).append("\":\"").append(param.get(key)).append("\",");
            }
            return sb.substring(0, sb.length() - 1) + "}}";
        }

        public String toEncryptString(String keyType,String key) throws Exception {
            String raw = this.getMapStr();
            byte[] b = null;

            if (keyType.toLowerCase().equals("3des") || keyType.toLowerCase().equals("desede")) {

                byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);

                DESedeKeySpec dks = new DESedeKeySpec(keyBytes);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
                SecretKey secretKey = keyFactory.generateSecret(dks);

                Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                b = cipher.doFinal(raw.getBytes("UTF-8"));
            }

            return "\"request\":\"" + new BASE64Encoder().encode(b) + "\"";
        }
    }

    public RequestHead getHead() {
        return head;
    }

    public void setHead(RequestHead head) {
        this.head = head;
    }

    public RequestParam getRequest() {
        return request;
    }

    public void setRequest(RequestParam request) {
        this.request = request;
    }

    public String toEncryptString(String keyType,String key) throws Exception {
        return "{" + head + "," + request.toEncryptString(keyType,key) + "}";
    }
    @Override
    public String toString() {
        return "{" + head + "," + request + "}";
    }
}
