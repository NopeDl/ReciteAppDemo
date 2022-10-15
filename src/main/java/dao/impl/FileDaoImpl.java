package dao.impl;

import easydao.core.SqlSession;
import easydao.core.SqlSessionFactory;
import dao.FileDao;
import pojo.po.File;
import utils.DaoUtil;

import java.util.List;

public class FileDaoImpl implements FileDao {
    private final SqlSessionFactory sqlSessionFactory;

    public FileDaoImpl() {
        sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }

    @Override
    public int insertFileByUserId(int userId, String path) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        File file = new File();
        file.setUserId(userId);
        file.setFileContext(path);
        int insert = sqlSession.insert("FileMapper.insertFile", file);
        if (insert > 0) {
            sqlSession.commit();
        } else {
            sqlSession.rollBack();
        }
        sqlSession.close();
        return 0;
    }

    /**
     * 获取文件总个数
     *
     * @return
     */
    @Override
    public Integer selectCount() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<Object> userId = sqlSession.selectList("FileMapper.selectCount", "userId");
        sqlSession.close();
        if (userId.size() == 0) {
            return null;
        } else {
            return (Integer) userId.get(0);
        }
    }
}
