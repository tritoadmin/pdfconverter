package kr.co.trito.pdf.security;

import kr.co.trito.pdf.util.ApiClientUtil;

public class TokenManager {

    private static String accessToken;
    private static long expireTime; // milliseconds

    /** 토큰 반환 (없거나 만료되면 재발급) */
    public static synchronized String getToken() throws Exception {
        if (accessToken == null || !isValid()) {
            refreshToken();
        }
        return accessToken;
    }

    /** 토큰 유효성 체크 */
    public static boolean isValid() {
        return accessToken != null && System.currentTimeMillis() < expireTime;
    }

    /** 토큰 무효화 */
    public static void invalidate() {
        accessToken = null;
        expireTime = 0;
    }

    /** 토큰 재발급 */
    private static void refreshToken() throws Exception {

        // ⬇ 실제 토큰 발급 API 호출
        String response = ApiClientUtil.requestToken(
                "https://api.example.com/token",
                "clientId",
                "clientSecret"
        );

        // 예시 응답: {"access_token":"abc","expires_in":3600}
        String token = parseToken(response);
        int expiresIn = parseExpire(response);

        accessToken = token;
        expireTime = System.currentTimeMillis() + (expiresIn * 1000L);
    }

    /* ===== JSON 파싱 (라이브러리 없는 환경) ===== */

    private static String parseToken(String json) {
        int idx = json.indexOf("access_token");
        int start = json.indexOf("\"", idx + 13) + 1;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private static int parseExpire(String json) {
        int idx = json.indexOf("expires_in");
        int start = json.indexOf(":", idx) + 1;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Integer.parseInt(json.substring(start, end).trim());
    }
}

