package kr.co.trito.pdf.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    // ⚠ 반드시 16/24/32 byte
    private static final String SECRET_KEY = "my-secret-key-16";

    private static final String ALGORITHM = "AES";

    /** 암호화 */
    public static String encrypt(String plainText) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64Util.encode(encrypted).replaceAll("\\s", "");
    }

    /** 복호화 */
    public static String decrypt(String cipherText) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), ALGORITHM);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decoded = Base64Util.decode(cipherText);
        byte[] decrypted = cipher.doFinal(decoded);

        return new String(decrypted, "UTF-8");
    }
}

