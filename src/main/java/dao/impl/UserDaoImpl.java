package dao.impl;

import com.zz.core.SqlSession;
import com.zz.core.SqlSessionFactory;
import dao.UserDao;
import pojo.po.Account;
import pojo.po.User;
import utils.DaoUtil;

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
}
