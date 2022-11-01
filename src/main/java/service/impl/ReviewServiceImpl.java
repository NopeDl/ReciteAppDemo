package service.impl;

import com.sun.org.apache.xpath.internal.operations.Mod;
import dao.ModleDao;
import dao.ReviewDao;
import dao.impl.ModleDaoImpl;
import dao.impl.ReviewDaoImpl;
import enums.ReviewPeriod;
import jakarta.servlet.http.HttpServletRequest;
import pojo.po.db.Modle;
import pojo.po.db.Review;
import pojo.vo.Community;
import pojo.vo.Message;
import service.ModleService;
import service.ReviewService;
import tools.handlers.FileHandler;
import tools.handlers.FileHandlerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ReviewServiceImpl implements ReviewService {
  private final ModleDao modleDao=new ModleDaoImpl();
  private final ReviewDao reviewDao=new ReviewDaoImpl();
    /**
     * 用户将某个模板加入学习计划
     * 需要传进userId和modleId，更改模板的学习状态,存进review表中
     * @param request
     * @return
     */
    @Override
    public Message joinThePlan(HttpServletRequest request) {
        Message message=null;
        String userId = request.getParameter("userId");
        String modleId = request.getParameter("modleId");
        //学习状态
        String studyStatus = request.getParameter("studyStatus");
        Modle modle=new Modle();
        modle.setUserId(Integer.parseInt(userId));
        modle.setModleId(Integer.parseInt(modleId));
        modle.setStudyStatus(studyStatus);

        //先查询要添加进复习的模板是不是用户自己有的
        boolean b = modleDao.ifModleBelongUser(modle);
        if(b){
            //此时得出该模板属于用户，接着查询模板是否已加入模板
            boolean b1 = reviewDao.selectModleIsReview(modle);
            if(!b1){
                //说明计划表里没有该模板，可以加入
                //加入学习计划，即将是学习状态改变为复习中
                int i = modleDao.updateStudyStatus(modle);
                if(i>0){
                    //进入复习计划，将modleId加入review表中
                    Review review=new Review();
                    review.setModleId(modle.getModleId());
                    //数据库表中默认的周期为0
                    int insert = reviewDao.joinIntoPlan(review);
                    if(insert>0){
                        //此时已加入学习计划
                        message=new Message("成功加入复习计划");
                    }
                }else{
                    message=new Message("加入复习计划失败，请重新添加");
                }
            }else{
                //说明复习计划表里已经有了该模板
                message=new Message("该模板已经在复习计划里啦，请不要重复添加");
            }

        }else {
            message=new Message("请先添加模板至记忆库哦");

        }

        return message;
    }

    /**
     * 移除模板计划
     * @param request
     * @return
     */
    @Override
    public Message removeFromPlan(HttpServletRequest request) {
        Message message=null;
        String userId = request.getParameter("userId");
        String modleId = request.getParameter("modleId");
        String studyStatus = request.getParameter("studyStatus");

        //先从计划表中移除
        Review review=new Review();
        review.setModleId(Integer.parseInt(modleId));
        int delete = reviewDao.removeModle(review);
        if(delete>0){
            //移除成功，改学习状态
            Modle modle=new Modle();
            modle.setModleId(review.getModleId());
            modle.setUserId(Integer.parseInt(userId));
            modle.setStudyStatus(studyStatus);
            int update = modleDao.updateStudyStatus(modle);
            if(update>0){
                //更新模板成功
                message=new Message("移除成功");
            }else {
                message=new Message("移除失败，请重新尝试");
            }
        }
        return message;
    }


    /**
     * 根据周期获取模板
     * @param request
     * @return
     */
    @Override
    public Message getModleByPeriod(HttpServletRequest request) {
        Message message=null;
        //存储返回的模板内容
        Map<String ,List<Community>> map=new HashMap<>();
        int userId = Integer.parseInt(request.getParameter("userId"));
        Review review=new Review();
        review.setUserId(userId);

        //学习周期
//        String period = request.getParameter("period");
        //查询所有周期,遍历一遍,一共七个周期
        for (int i = 1; i <= 8; i++) {
            review.setPeriod(i);

            //周期显示所需要的时间
            ReviewPeriod reviewPeriod = ReviewPeriod.getReviewPeriod(i);
            review.setDays(reviewPeriod.getDate());
            List<Community> communities = reviewDao.selectModleByPeriod(review);
            if(communities.size()>0) {

                for (int j = 0; j < communities.size(); j++) {
                    String modlePath = communities.get(j).getModlePath();
                    InputStream input;
                    try {
                        input = new FileInputStream(modlePath);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    //读取文本
                    FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
                    String content = txtFileHandler.parseContent();
                    communities.get(j).setContent(content);
                    communities.get(j).setModlePath("");
                }
            }
//            map.put("周期" + (i), communities);用这个的排序为21436587？
            map.put("第"+i+"周期",communities);
        }

        message=new Message();
        message.addData("ModlesOfPeriod",map);
        return message;
    }


    /**
     * 将复习完的模板更新周期
     * @param request
     * @return
     */
    @Override
    public Message updatePeriod(HttpServletRequest request) {
        Message message=null;
        int userId = Integer.parseInt(request.getParameter("userId"));
        int modleId =Integer.parseInt( request.getParameter("modleId"));
        Review review=new Review();
        review.setModleId(modleId);

        //两种情况，一种是还在周期内复习；一种是完成了八个周期的复习，应该离开复习计划，
        //先查模板的周期
        Review result = reviewDao.selectModlePeriod(review);
        int period = result.getPeriod();
        if(period==8){
            //说明用户此次复习已经是最后一阶段的复习了，应该离开复习计划，并且修改学习状态
            int i = reviewDao.removeModle(review);
            if(i>0){
                //移除成功，nextOne修改学习状态
                Modle modle=new Modle();
                modle.setStudyStatus("已学习");
                modle.setUserId(userId);
                modle.setModleId(modleId);
                int update = modleDao.updateStudyStatus(modle);
                if(update>0){
                    message=new Message("恭喜你，已经牢牢掌握了这个模板内容啦!");
                }
            }
        }else{
            //继续周期性学习，周期+1，日期重置
//            Date date=new Date();
//            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            System.out.println(formatter.format(date))
            review.setReTime(LocalDate.now());
            System.out.println(LocalDate.now());
            int i = reviewDao.updatePeriodAndDate(review);
            if(i>0){
                //更新周期和时间成功
                message=new Message("恭喜你完成这个周期的复习啦，下个周期见吧");
            }
        }
        return message;
    }


    /**
     *获取用户所有的复习列表
     * @param request
     * @return
     */
    @Override
    public Message getReviewPlan(HttpServletRequest request) {
        Message message=null;
        int userId = Integer.parseInt(request.getParameter("userId"));
        Modle modle=new Modle();
        modle.setUserId(userId);
        //在计划中的模板状态均为复习中，所以只需要查询用户模板中处于“复习中”状态的就能查询到相对应的复习计划
        modle.setStudyStatus("复习中");
        List<Community> communities = reviewDao.selectReviewPlan(modle);
        if(communities.size()>0){
            //非空说明有正在复习的模板
            //处理下模板的内容和路径问题
            //以下部分出现重复率三次或者以上，考虑封装
            for (int j = 0; j < communities.size(); j++) {
                String modlePath = communities.get(j).getModlePath();
                InputStream input;
                try {
                    input = new FileInputStream(modlePath);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //读取文本
                FileHandler txtFileHandler = FileHandlerFactory.getHandler("txt", input);
                String content = txtFileHandler.parseContent();
                communities.get(j).setContent(content);
                communities.get(j).setModlePath("");
            }

            message=new Message("获取复习列表成功");
            message.addData("reviewPlan",communities);
        }else{
            message=new Message("您的复习列表里还是空的呢，赶快选择一个模板进行学习吧");
        }
        return message;
    }

}
