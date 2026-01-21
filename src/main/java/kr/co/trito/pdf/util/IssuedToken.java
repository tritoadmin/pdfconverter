package kr.co.trito.pdf.util;

public class IssuedToken {

    private String token;
    private long expireTime;

    public IssuedToken(String token, int expireSec) {
        this.token = token;
        this.expireTime =
                System.currentTimeMillis() + (expireSec * 1000L);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }

    public String getToken() {
        return token;
    }
}

