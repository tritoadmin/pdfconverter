package kr.co.trito.pdf.util;

import java.util.HashMap;
import java.util.Map;

public class EncryptTokenParser {

    public static Map<String, String> parse(String token) throws Exception {

        String decrypted = CryptoUtil.decrypt(token);

        Map<String, String> map = new HashMap<String, String>();
        String[] pairs = decrypted.split("\\|");

        for (int i = 0; i < pairs.length; i++) {
            String[] kv = pairs[i].split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }

    public static String getUserId(String token) throws Exception {
        return parse(token).get("userId");
    }

    public static long getExpire(String token) throws Exception {
        return Long.parseLong(parse(token).get("expire"));
    }
}
