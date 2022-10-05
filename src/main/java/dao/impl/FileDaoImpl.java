package dao.impl;

import easydao.core.SqlSession;
import easydao.core.SqlSessionFactory;
import dao.FileDao;
import pojo.po.File;
import utils.DaoUtil;

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
}
