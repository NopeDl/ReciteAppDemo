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

/**
 * @author yeyeye
 * @Date 2022/11/9 16:00
 */
@WebFilter("*")
public class JwtFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取token
        String token = JwtUtil.getToken(request);
        DecodedJWT verify = JwtUtil.verify(token);
        Claim userId = verify.getClaim("userId");
        if (userId != null){
            request.setAttribute("userId",userId.asInt());
        }
//        boolean verifySuccess = false;
//        if (token != null && !"".equals(token)){
//            //验证是否有token
//            DecodedJWT verify = JwtUtil.verify(token);
//            Claim userId = verify.getClaim("userId");
//            if (userId != null){
//                verifySuccess = true;
//                request.setAttribute("userId",userId.asInt());
//            }
//        }
//        String uri = StringUtil.parseUri(request.getRequestURI());
//        if (!verifySuccess && !"homePage.html".equals(uri) && !"login.html".equals(uri) && !"Login".equals(uri) && !"Reg".equals(uri)){
//            //没有token 而且 访问的不是 介绍页和 登录页 并且不是css等静态资源
//            if (uri.contains("html")){
//                response.sendRedirect(request.getContextPath() + "/login.html");
//            }else {
//                Message msg = new Message("需要登录才能访问");
//                msg.addData("uri", "login.html");
//                ResponseUtil.send(response, msg);
//            }
//        }else {
//            chain.doFilter(request,response);
//        }
    }
}
