package kr.co.trito.pdf.security;

import java.util.HashMap;
import java.util.Map;

public class ApiKeyStore {

    private static final Map STORE = new HashMap();

    static {
        STORE.put("client-key-001", "secret-abc-123");
        STORE.put("client-key-002", "secret-def-456");
    }

    public static String getSecretKey(String accessKey) {
        return (String) STORE.get(accessKey);
    }
}

