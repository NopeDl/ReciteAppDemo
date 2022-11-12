package service.impl;


import com.auth0.jwt.interfaces.DecodedJWT;
import dao.*;
import dao.impl.*;
import enums.MsgInf;
import enums.ReviewPeriod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import pojo.po.db.DailyStudy;
import pojo.po.db.User;
import pojo.vo.Message;
import service.UserService;
import tools.easydao.utils.Resources;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;
import tools.utils.JwtUtil;

import java.io.*;
import java.util.*;

/**
 * @author yeyeye and isDucka
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();
    private final DateDao dateDao = new DateDaoImpl();
    private final AccountDao accountDao = new AccountDaoImpl();
    private final ReviewDao reviewDao=new ReviewDaoImpl();

    /**
     * 退出登录
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message quit(HttpServletRequest request) {
        Message message = new Message("退出成功");
        message.addData("quitSuccess", true);
        return message;
    }

    /**
     * 注册用户
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message createUser(HttpServletRequest request) {
        String number = request.getParameter("phone");
        String password = request.getParameter("password");
        String nickName = request.getParameter("username");
        int ret = userDao.createUserByNumber(number, password, nickName);
        Message message;
        if (ret == 1) {
            User user = userDao.selectUserByNickName(nickName);
            int userId = user.getUserId();
            message = new Message(MsgInf.OK);
            message.addData("isSuccess", true);
            //将id发送给前端
            String token = JwtUtil.getInstance(userId);
            message.addData("userId", token);
        } else {
            message = new Message("用户创建失败");
            message.addData("isSuccess", false);
        }
        return message;
    }

    /**
     * 通过userId来查找用户资料
     */
    @Override
    public Message selectUserMsg(HttpServletRequest request) {

        Message message;
//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        User user = userDao.selectUserById(userId);
        if ("".equals(user.getImage()) || user.getImage() == null) {
            //说明此时头像为默认头像，不需要重新读取
            //将响应的数据封装到message里
            user.setBase64("");
        } else {
            //说明头像已经改变过了，需要重新读取
            //头像的存放路径
            String imagePath = user.getImage();
            InputStream input;
            try {
                input = new FileInputStream(imagePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            //读取文本,这里表现为读取头像的base64路径
            FileHandler imgHandler = FileHandlerFactory.getHandler("img", input);
            if (imgHandler != null) {
                String base64 = imgHandler.parseContent();
                user.setBase64(base64);
            }else {
                user.setBase64("");
            }
            user.setImage(null);
        }
        //将响应的数据封装到message里
        message = new Message(MsgInf.OK);
        message.addData("user", user);
        return message;

    }

    /**
     * 通过用户id来改个人信息
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message ReMsgById(HttpServletRequest request) {
//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        String phone = request.getParameter("phone");
        String nickName = request.getParameter("nickName");
        String newPassword = request.getParameter("password");
        String base64 = request.getParameter("base64");

        Message msg;
        if (newPassword != null) {
            //修改密码
            int i = accountDao.changePasswordByUserId(userId, newPassword);
            if (i > 0) {
                msg = new Message("密码修改成功");
                msg.addData("isSuccess", true);
            } else {
                msg = new Message("密码修改失败");
                msg.addData("isSuccess", false);
            }
        } else if (nickName != null) {
            //修改昵称
            int i = userDao.updateNickNameByUserID(userId, nickName);
            if (i > 0) {
                msg = new Message("昵称修改成功");
                msg.addData("isSuccess", true);
            } else {
                msg = new Message("昵称修改失败");
                msg.addData("isSuccess", false);
            }
        } else if (phone != null) {
            //修改手机号
            int i = userDao.updatePhoneByUserID(userId, phone);
            if (i > 0) {
                msg = new Message("手机号修改成功");
                msg.addData("isSuccess", true);
            } else {
                msg = new Message("手机号修改失败");
                msg.addData("isSuccess", false);
            }
        } else if (base64 != null) {
            //说明返回来一个base64，应该将他存进static目录下的imgPath
            String imagePath = WriteImageAsTxt(base64, userId);
            //修改头像
            int i = userDao.updateImageByUserID(userId, imagePath);
            if (i > 0) {
                msg = new Message("头像修改成功");
                msg.addData("isSuccess", true);
            } else {
                msg = new Message("头像修改失败");
                msg.addData("isSuccess", false);
            }
        } else {
            //错误修改
            msg = new Message("参数错误");
        }
        return msg;
    }

    /**
     * 检查昵称是否可用
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message checkNickNameExists(HttpServletRequest request) {
        String nickName = request.getParameter("username");
        String name = userDao.selectNickName(nickName);
        Message msg;
        if (name == null) {
            //用户名可用
            msg = new Message("用户名可用");
            msg.addData("isOk", true);
        } else {
            //用户名不可用
            msg = new Message("用户名已被使用");
            msg.addData("isOk", false);
        }
        return msg;
    }

    /**
     * 获取积分榜前十
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message rankingList(HttpServletRequest request) {
        List<User> userList = userDao.selectTopTen();
        Message msg;
        if (userList.size() > 0) {
            msg = new Message("获取成功");
            msg.addData("ranking", userList);
        } else {
            msg = new Message("获取失败");
        }
        return msg;
    }

    /**
     * 获取用户排位和信息
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message userRanking(HttpServletRequest request) {
        Message msg;
//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        //查询用户排名
        Integer userRanking = userDao.selectUserRanking(userId);
        //查询用户信息
        User user = userDao.selectUserById(userId);
        //封装数据
        Map<String, Object> res = new HashMap<>(2);
        res.put("userRanking", userRanking);
        user.setImage(null);
        res.put("user", user);
        msg = new Message("获取成功");
        msg.addData("userData", res);
        return msg;
    }

    @Override
    public Message clockIn(HttpServletRequest request) {
//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        int i = dateDao.insertDateByUserId(userId);
        Message msg;
        if (i > 0) {
            msg = new Message("打卡成功");
            msg.addData("isSuccess", true);
        } else {
            msg = new Message("打卡失败");
            msg.addData("isSuccess", false);
        }
        return msg;
    }

    /**
     * 获取打卡记录
     *
     * @param request 1
     * @return 1
     */
    @Override
    public Message getClockInRecord(HttpServletRequest request) {
//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        String month = request.getParameter("month");
        String year = request.getParameter("year");

        List<String> list = dateDao.selectDateByUserId(userId, month, year);
        Message msg;
        if (list != null) {
            Set<String> dateSet = new HashSet<>(list);
            msg = new Message("查找成功");
            msg.addData("searchSuccess", true);
            msg.addData("dateList", dateSet);
        } else {
            msg = new Message("无数据");
            msg.addData("searchSuccess", false);
        }
        return msg;
    }

    /**
     * 将头像的base64形式存static/imgPath下的txt文件,返回base64的存储路径
     *
     * @param base64 头像
     * @param useId  用户ID
     * @return 储存路径
     */
    @Override
    public String WriteImageAsTxt(String base64, int useId) {
        String filePath = Resources.getResource("static/imgPath/" + System.currentTimeMillis() + useId + ".txt");
        System.out.println(filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                boolean res = file.createNewFile();
                System.out.println("创建文件" + res + ": " + filePath);
            }
            FileHandler txtHandler = FileHandlerFactory.getHandler("txt", null);
            if (txtHandler != null){
                return txtHandler.saveFile(filePath, base64);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存头像
     *
     * @param request r
     * @return r
     */
    @Override
    public Message saveImg(HttpServletRequest request) {
        Message msg = new Message();
        try {
            //获取USERID
//            int userId = Integer.parseInt(request.getParameter("userId"));
            int userId = (Integer) request.getAttribute("userId");
            Part image = request.getPart("image");
            FileHandler imgFileHandler = FileHandlerFactory.getHandler("img", image.getInputStream());
            String filePath = Resources.getResource("static/images/") + System.currentTimeMillis() + image.getSubmittedFileName() + ".jpg";
            //获取ReciteMemory/.......
            //拼接图像路径
            int i = 0;
            if (imgFileHandler != null){
                imgFileHandler.saveFile(filePath, null);
                i = userDao.updateImageByUserID(userId, filePath);
            }
            if (i > 0) {
                msg.addData("uploadSuccess", true);
            } else {
                msg.addData("uploadSuccess", false);
            }
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }

    /**
     * 保存日常数据
     *
     * @param request req
     * @return r
     */
    @Override
    public Message saveDailyData(HttpServletRequest request) {
        String st = request.getParameter("studyTime");

//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        //处理null
        int studyTime = (st != null ? Integer.parseInt(st) : 0);
        DailyStudy dailyStudy = userDao.selectDailyStudyDataByUserId(userId);
        int i;
        if (dailyStudy == null) {
            //如果用户当天没有学习记录
            //则创建一个
            i = userDao.insertDailyStudyData(userId, 0, studyTime, 0);
        } else {
            //有则更新数据
            studyTime = Math.max(studyTime,dailyStudy.getStudyTime());
            i = userDao.updateDailyStudyByIdAndTime(userId, dailyStudy.getStudyNums(), studyTime, dailyStudy.getReviewNums());
        }
        Message msg;
        if (i > 0) {
            msg = new Message("保存成功");
            msg.addData("saveSuccess", true);
        } else {
            msg = new Message("保存失败");
            msg.addData("saveSuccess", false);
        }
        return msg;
    }

    /**
     * 获取用户日常学习信息： 学习篇数和学习时长
     *
     * @param request req
     * @return ret
     */
    @Override
    public Message getUserDailyStudyData(HttpServletRequest request) {

//        int userId = Integer.parseInt(request.getParameter("userId"));
        int userId = (Integer) request.getAttribute("userId");
        DailyStudy dailyStudy = userDao.selectDailyStudyDataByUserId(userId);
        Message msg;
        if (dailyStudy != null) {
            int totalReviewNums=0;
            //再获取该用户总的复习篇数
            //循环八次
            for (int i = 1; i <= 8 ; i++) {
                ReviewPeriod reviewPeriod = ReviewPeriod.getReviewPeriod(i);

                //分别传userId，周期，显示需要的天数
                totalReviewNums += reviewDao.getTotalReviewNums(userId, i, reviewPeriod.getDate());
            }
            dailyStudy.setTotalReviewNums(totalReviewNums);
            msg = new Message("获取成功");
            msg.addData("getSuccess", true);
            msg.addData("studyData", dailyStudy);
        } else {
            msg = new Message("获取失败");
            msg.addData("getSuccess", false);
        }
        return msg;
    }
}
