package kr.co.trito.pdf.util;

public class HmacTokenValidator {


    /** 토큰 검증 */
    public static boolean validate(String token, String data) {

        try {
            String expected = HmacTokenUtil.generate(data);
            return expected.equals(token);
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean validateWithExpire(
            String token, String userId, long expireTime) {

        if (System.currentTimeMillis() > expireTime) {
            return false;
        }

        String data = "userId=" + userId + "&expire=" + expireTime;
        return HmacTokenValidator.validate(token, data);
    }

}
