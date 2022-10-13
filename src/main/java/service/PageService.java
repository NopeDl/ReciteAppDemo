package service;

import jakarta.servlet.http.HttpServletRequest;
import pojo.vo.Message;

public interface PageService {
    /**
     * 获取页面名字
     * @param request
     * @return 页面
     */
    @Deprecated
    Message getPage(HttpServletRequest request);
}
