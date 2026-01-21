package kr.co.trito.pdf.util;

public class EncryptTokenGenerator {

    /** 토큰 생성 */
    public static String generate(String userId, int expireSeconds)
            throws Exception {

        long expireTime =
                System.currentTimeMillis() + (expireSeconds * 1000L);

        String payload =
                "userId=" + userId + "|expire=" + expireTime;

        return CryptoUtil.encrypt(payload);
    }
}
