package dao;

import pojo.po.db.DailyStudy;
import pojo.po.db.User;
import pojo.vo.Community;

import java.util.List;

public interface UserDao {


    /**
     * 根据用户id获取用户的昵称和头像
     * @param community
     * @return
     */
    User selectNameImgById(Community community);

    /**
     * 通过用户id获取该用户
     *
     * @param userId 需要查找的用户id
     * @return 存在则返回该用户pojo，不存在返回null
     */
    User selectUserById(int userId);

    /**
     * 通过用户id获取该用户
     *
     * @param nickName 需要查找的用户昵称
     * @return 存在则返回该用户pojo，不存在返回null
     */
    User selectUserByNickName(String nickName);

    /**
     * 根据手机号创建新用户
     *
     * @param number
     * @param password
     * @param nickName
     * @return
     */
    int createUserByNumber(String number, String password, String nickName);

    /**
     * 修改昵称
     * @param userId
     * @param nickName
     * @return
     */
    int updateNickNameByUserID(int userId,String nickName);

    /**
     * 修改头像
     * @param userId 用户ID
     * @param image 头像
     * @return 记录数目
     */
    int updateImageByUserID(int userId,String image);

    /**
     * 更新星星数据
     * @param userId  用户ID
     * @param totalStars 星星总数
     * @return 记录数目
     */
    int updateStarsByUserId(int userId,int totalStars);

    /**
     * 更新用户积分
     * @param userId 用户积分
     * @param totalPoint 积分总数
     * @return 记录数目
     */
    int updatePointByUserId(int userId,int totalPoint);

    /**
     * 修改电话
     * @param userId
     * @param number
     * @return
     */
    int updatePhoneByUserID(int userId,String number);

    /**
     * 获取用户名
     *
     * @param nickName
     * @return
     */
    String selectNickName(String nickName);

    List<User> selectTopTen();

    /**
     * 获取用户榜单排名
     * @param userId 用户ID
     * @return 榜单上的排名
     */
    Integer selectUserRanking(int userId);


    /**
     * 插入日常学习数据
     * @param userId 用户ID
     * @param studyNums 学习篇数
     * @param studyTimes 学习时长
     * @return 返回插入条数
     */
    int insertDailyStudyData(int userId,int studyNums,int studyTimes,int reviewNums);

    /**
     * 查询用户日常学习数据
     * @param userId 用户ID
     * @return 封装好的学习数据
     */
    DailyStudy selectDailyStudyDataByUserId(int userId);

    /**
     * 根据userId和storeTime插入最新的消息
     * @param userId 用户id
     * @param studyNums 学习篇数
     * @param studyTimes 学习时长
     * @param reviewNums 复习篇数
     * @return 返回int
     */
    int updateDailyStudyByIdAndTime(int userId,int studyNums,int studyTimes,int reviewNums);

}
