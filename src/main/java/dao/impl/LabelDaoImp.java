package dao.impl;

import dao.LabelDao;
import tools.easydao.core.SqlSession;
import tools.easydao.core.SqlSessionFactory;
import pojo.po.db.Label;
import tools.utils.DaoUtil;

import java.util.List;

public class LabelDaoImp implements LabelDao {

    private final SqlSessionFactory sqlSessionFactory;

    public LabelDaoImp() {
        sqlSessionFactory = DaoUtil.getSqlSessionFactory();
    }


    /**
     * 根据标签的id获取标签的名字
     * @param labelId
     * @return
     */
    @Override
    public String selectLableName(int labelId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Label label=new Label();
        label.setLabelId(labelId);

        //返回标签的名字
        String labelName=null;
        List<Object> objectList = sqlSession.selectList("LabelMapper.selectLableByLableId", label);
        sqlSession.close();

        if(objectList.size()!=0){
            //返回标签的名字
            labelName= (String) objectList.get(0);
        }
        return labelName;

    }
}
