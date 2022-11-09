package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.LikesService;
import service.impl.LikesServiceImpl;
import tools.utils.Cache;

import java.io.IOException;

/**
 * @author ?
 */
@WebFilter("*")
public class AEncodingFilter extends HttpFilter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        //定时将点赞数量存到数据库里
        //服务器开启前应该先查找对应的数据放到总缓存
        LikesService likesService=new LikesServiceImpl();
        try {
            likesService.initCaChe();
            Cache.tiemrTask();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //统一设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String requestUri = request.getRequestURI();
        if (requestUri.contains(".css")){
            response.setContentType("text/css;charset=utf-8");
        }else {
            response.setContentType("text/html;charset=utf-8");
        }



        //解决跨域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);
    }
}
