package kr.co.trito.pdf.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.trito.pdf.security.TokenManager;

public class ApiClientUtil {

    /** API 호출 (GET) */
    public static String callApiWithToken(String apiUrl) throws Exception {

        String token = TokenManager.getToken();
        HttpURLConnection conn = createConnection(apiUrl, token);

        int code = conn.getResponseCode();

        // 🔁 토큰 만료 → 재발급 후 재시도
        if (code == 401 || code == 403) {
            TokenManager.invalidate();
            token = TokenManager.getToken();
            conn = createConnection(apiUrl, token);
            code = conn.getResponseCode();
        }

        InputStream in = (code == 200)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        reader.close();
        return sb.toString();
    }

    /** Connection 생성 */
    private static HttpURLConnection createConnection(String apiUrl, String token)
            throws Exception {

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Accept", "application/json");

        return conn;
    }

    /** 토큰 발급 API */
    public static String requestToken(String tokenUrl, String clientId, String secret)
            throws Exception {

        String body = "client_id=" + clientId + "&client_secret=" + secret;

        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded");

        OutputStreamWriter writer =
                new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        writer.write(body);
        writer.flush();
        writer.close();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        return sb.toString();
    }
}

