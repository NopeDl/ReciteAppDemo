package filter;

import easydao.utils.Resources;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebFilter("/*")
public class EncodingFilter extends HttpFilter {
    @Override
    public void init() throws ServletException {
        //首次打开服务器后获取段位名称
        Properties properties = new Properties();
        try {
            //加载配置文件
            properties.load(new InputStreamReader(Resources.getResourceAsStream("static/rank.properties"), StandardCharsets.UTF_8));
            Map<int[],String> rankMap = new HashMap<>();
            for (String key : properties.stringPropertyNames()) {
                //key: "0,5"
                String value = properties.getProperty(key);
                //value: "青铜"
                //解析范围
                String[] scopeStr = key.split(",");
                if (scopeStr.length>=2){
                    int[] scope = new int[2];
                    scope[0] = Integer.parseInt(scopeStr[0]);//最小值
                    scope[1] = Integer.parseInt(scopeStr[1]);//最大值
                    rankMap.put(scope,value);//存入范围和对应的段位名称
                }else {
                    throw new RuntimeException("段位配置文件有误");
                }
            }
            //存入context域
            getServletContext().setAttribute("rankMap",rankMap);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取段位名称失败");
        }
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //统一设置编码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String requestURI = request.getRequestURI();
        if (requestURI.contains(".css")){
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
