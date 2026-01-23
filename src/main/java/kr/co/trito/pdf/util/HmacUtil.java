package kr.co.trito.pdf.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacUtil {

    private static final String ALGORITHM = "HmacSHA256";

    public static String generate(String data, String secretKey) {
        try {
            SecretKeySpec key =
                new SecretKeySpec(secretKey.getBytes("UTF-8"), ALGORITHM);

            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);

            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("HMAC 생성 실패", e);
        }
    }
}

