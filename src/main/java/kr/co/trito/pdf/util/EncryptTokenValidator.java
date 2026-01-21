package kr.co.trito.pdf.util;

public class EncryptTokenValidator {

    public static boolean validate(String token) {

        try {
            long expire = EncryptTokenParser.getExpire(token);
            return System.currentTimeMillis() < expire;
        } catch (Exception e) {
            return false;
        }
    }
}
