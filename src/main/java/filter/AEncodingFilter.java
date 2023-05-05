package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author ?
 */
public class AEncodingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //统一设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String requestUri = request.getRequestURI();
        if (requestUri.contains(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else {
            response.setContentType("text/html;charset=utf-8");
        }
//        //解决跨域访问
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "*");
//        response.setHeader("Access-Control-Allow-Headers","Content-Type,Authorization,X-Requested-With");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }
}
