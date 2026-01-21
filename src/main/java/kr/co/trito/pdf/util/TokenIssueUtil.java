package kr.co.trito.pdf.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.trito.pdf.security.TokenResponse;

public class TokenIssueUtil {

    /** 토큰 발급 */
    public static TokenResponse issueToken(
            String tokenUrl,
            String clientId,
            String clientSecret) throws Exception {

        String body =
                "grant_type=client_credentials"
              + "&client_id=" + clientId
              + "&client_secret=" + clientSecret;

        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        conn.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded");

        // ▶ 요청
        OutputStreamWriter writer =
                new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        writer.write(body);
        writer.flush();
        writer.close();

        // ▶ 응답
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        return parseTokenResponse(sb.toString());
    }

    /** JSON 파싱 (라이브러리 없는 환경) */
    private static TokenResponse parseTokenResponse(String json) {

        TokenResponse token = new TokenResponse();

        // access_token
        int idx = json.indexOf("access_token");
        int start = json.indexOf("\"", idx + 13) + 1;
        int end = json.indexOf("\"", start);
        token.setAccessToken(json.substring(start, end));

        // expires_in
        idx = json.indexOf("expires_in");
        start = json.indexOf(":", idx) + 1;
        end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }
        token.setExpiresIn(
                Integer.parseInt(json.substring(start, end).trim()));

        return token;
    }
}

