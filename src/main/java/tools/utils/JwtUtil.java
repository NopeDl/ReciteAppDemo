package tools.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author yeyeye
 * @Date 2022/11/10 21:47
 */
public class JwtUtil {
    private static final JWTVerifier JWT_VERIFIER = JWT.require(Algorithm.HMAC256("!34ADAS")).build();

    /**
     * 验证token
     *
     * @param token token
     * @return DecodedJWT
     */
    public static DecodedJWT verify(String token) {
        return JWT_VERIFIER.verify(token);
    }

    /**
     * 解析请求中的token
     *
     * @param request 请求
     * @return token
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getParameter("userId");
        if (token == null || "".equals(token)) {
            //可能是ws请求
            //从请求中解析
            StringBuffer url = request.getRequestURL();
            token = getToken(url.toString());
        }
        return token;
    }

    public static String getToken(String url) {
        String token = null;
        int index = url.indexOf("userId");
        if (index != -1) {
            //跳过 =
            int begin = index + "userId".length() + 1;
            int end = url.indexOf("/", begin);
            if (end != -1){
                token = url.substring(begin, end);
            }else {
                token = url.substring(begin);
            }
        }
        return token;
    }
}
