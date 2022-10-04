package controller;



import enums.MsgInf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pojo.po.User;
import pojo.vo.Message;
import service.AccountService;
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.UserServiceImpl;
import utils.ResponseUtil;
import utils.StringUtil;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 执行用户相关操作
 */
@WebServlet("/user.do/*")
public class UserController extends HttpServlet {
    private final AccountService accountService = new AccountServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取URI
        String requestURI = StringUtil.parseURI(request.getRequestURI());
        //根据URI类型执行对应方法
        Message<?> msg = null;
        if ("Login".equals(requestURI)) {
            //登录
            msg = accountService.checkAccount(request, response);
        } else if ("Reg".equals(requestURI)) {
            //注册
            msg = userService.createUser(request);
        } else if ("UserMsg".equals(requestURI)) {
            //用户个人信息获取
            msg = userService.selectUserMsg(request);
        } else if ("ChangePswd".equals(requestURI)) {
            //修改密码
            msg = accountService.changePassword(request);
        } else if("ReMessage".equals(requestURI)){
            //用户修改个人资料
            //获取用户修改后的内容
            String phone = request.getParameter("phone");
            String nickName = request.getParameter("nickName");
            String sex = request.getParameter("sex");
            String time =request.getParameter("birthday");
            //将生日转化为data
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = null;
            try {
                birthday = fmt.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Integer points = Integer.parseInt(request.getParameter("points"));
            String imagePath = request.getParameter("imagePath");
            Integer cityId = Integer.parseInt(request.getParameter("cityId"));
            String schoolId = request.getParameter("schoolId");

            //获取userId
            Integer userId = accountService.getIdByNumber(phone);
            //封装用户修改后的资料
            User user=new User(userId,nickName,sex,birthday,points,imagePath,cityId,schoolId);
            //更改修改后的数据
            msg = userService.ReMsgById(user);
        }else {
            msg = new Message<>(MsgInf.NOT_FOUND);
        }
        //发送响应消息体
        ResponseUtil.send(response, msg);



    }
}
