package kr.co.trito.pdf.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.co.trito.pdf.security.ApiKeyStore;
import kr.co.trito.pdf.util.HmacUtil;

public class HmacAuthInterceptor implements HandlerInterceptor {

    private static final long EXPIRE_TIME = 5 * 60 * 1000; // 5분

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String accessKey = request.getHeader("X-ACCESS-KEY");
        String timestamp = request.getHeader("X-TIMESTAMP");
        String signature = request.getHeader("X-SIGNATURE");

        if (accessKey == null || timestamp == null || signature == null) {
            response.sendError(401, "Missing auth header");
            return false;
        }

        long reqTime;
        try {
            reqTime = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            response.sendError(401, "Invalid timestamp");
            return false;
        }

        long now = System.currentTimeMillis();
        if (Math.abs(now - reqTime) > EXPIRE_TIME) {
            response.sendError(401, "Request expired");
            return false;
        }

        String secretKey = ApiKeyStore.getSecretKey(accessKey);
        if (secretKey == null) {
            response.sendError(401, "Invalid access key");
            return false;
        }

        String data = request.getMethod() + "\n"
                    + request.getRequestURI() + "\n"
                    + timestamp;

        String serverSignature = HmacUtil.generate(data, secretKey);

        if (!serverSignature.equals(signature)) {
            response.sendError(401, "Invalid signature");
            return false;
        }

        return true;
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
}
