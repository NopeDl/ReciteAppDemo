package filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.utils.JwtUtil;

import java.io.IOException;
import java.util.Enumeration;

/**
 * @author yeyeye
 * @Date 2022/11/9 16:00
 */
public class JwtFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取token
        String token = request.getHeader("Authorization");
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("所有头名字:");
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            System.out.println(s);
        }
        System.out.println("当前Token:" + token);
        if (token != null && !"".equals(token)) {
            DecodedJWT verify = JwtUtil.verify(token);
            Claim userId = verify.getClaim("userId");
            System.out.println("当前userId:" + userId);
            if (userId != null) {
                request.setAttribute("userId", userId.asInt());
            }
        }
        chain.doFilter(request, response);
    }
}
