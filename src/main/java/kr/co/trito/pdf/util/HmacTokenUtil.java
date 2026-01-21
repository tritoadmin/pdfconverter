package kr.co.trito.pdf.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacTokenUtil {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String SECRET_KEY = "trito-pdf-converter";

    public static String generate(String data) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), HMAC_ALGO);

        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(key);

        byte[] raw = mac.doFinal(data.getBytes("UTF-8"));

        return Base64Util.encode(raw);
    }
}

