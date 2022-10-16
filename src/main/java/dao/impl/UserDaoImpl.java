package dao.impl;

import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import dao.UserDao;
import pojo.po.Account;
import pojo.po.Count;
import pojo.po.User;
import tools.utils.DaoUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final SqlSessionFactory sqlSessionFactory;

    public UserDaoImpl() {
        this.sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }

    /**
     * 通过用户id获取该用户
     *
     * @param userId 需要查找的用户id
     * @return
     */
    @Override
    public User selectUserById(int userId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setUserId(userId);
        List<Object> objectList = sqlSession.selectList("UserMapper.selectUserById", user);
        sqlSession.close();
        return objectList.size() == 0 ? null : (User) objectList.get(0);

    }

    /**
     * 通过用户id获取该用户
     *
     * @param nickName 需要查找的用户昵称
     * @return (有bug, 如果存在重名用户则返回查到的第一个)
     */
    @Override
    public User selectUserByNickName(String nickName) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setNickName(nickName);
        List<Object> objectList = sqlSession.selectList("UserMapper.selectUserByNickName", user);
        sqlSession.close();
        return objectList.size() == 0 ? null : (User) objectList.get(0);
    }

    /**
     * 创建新用户
     *
     * @param number
     * @param password
     * @param nickName
     * @return
     */
    @Override
    public int createUserByNumber(String number, String password, String nickName) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setNickName(nickName);
        //插入用户
        int insert = sqlSession.insert("UserMapper.insertUserByNickName", user);
        System.out.println(insert);
        if (insert > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
            return insert;
        }
        //插入用户成功
        //获取userId
        user = selectUserByNickName(nickName);
        int userId = user.getUserId();
        //插入用户对应账号
        Account account = new Account();
        account.setNumber(number);
        account.setPassword(password);
        account.setUserId(userId);
        insert = sqlSession.insert("AccountMapper.insertAccount", account);
        if (insert > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return insert;
    }


    /**
     * 获取用户名
     *
     * @param nickName
     * @return
     */
    @Override
    public String selectNickName(String nickName) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setNickName(nickName);
        List<Object> userList = sqlSession.selectList("UserMapper.selectNickName", user);
        sqlSession.close();
        if (userList.size() > 0) {
            return ((User) userList.get(0)).getNickName();
        } else {
            return null;
        }
    }

    /**
     * 获取前十信息
     * @return 返回前十的昵称和星星数量
     */
    @Override
    public List<User> selectTopTen() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> objects = sqlSession.selectList("UserMapper.selectTopTen", "");
        sqlSession.close();
        if (objects != null){
            List<User> userList = new ArrayList<>();
            for (Object object : objects) {
                userList.add((User) object);
            }
            return userList;
        }else {
            return null;
        }
    }

    /**
     * 获取用户榜单排名
     * @param userId
     * @return
     */
    @Override
    public Integer selectUserRanking(int userId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setUserId(userId);
        List<Object> objects = sqlSession.selectList("UserMapper.selectUserRanking", user);
        sqlSession.close();
        return objects.size()>0?((Count)objects.get(0)).getNumber().intValue():null;
    }

    /**
     * 修改用户名
     * @param userId
     * @param nickName
     * @return
     */
    @Override
    public int updateNickNameByUserID(int userId, String nickName) {
        User user = new User();
        user.setUserId(userId);
        user.setNickName(nickName);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("UserMapper.updateNickNameByUserID", user);
        if (update>0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;
    }

    /**
     * 修改头
     * @param userId
     * @param image
     * @return
     */
    @Override
    public int updateImageByUserID(int userId, String image) {
        User user = new User();
        user.setUserId(userId);
        user.setImage(image);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("UserMapper.updateImageByUserID", user);
        if (update>0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;
    }

    /**
     * 修改手机号
     * @param userId
     * @param number
     * @return
     */
    @Override
    public int updatePhoneByUserID(int userId, String number) {
        Account account = new Account();
        account.setUserId(userId);
        account.setNumber(number);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int update = sqlSession.update("UserMapper.updatePhoneByUserID", account);
        if (update>0){
            sqlSession.commit();
        }else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return update;
    }
}
