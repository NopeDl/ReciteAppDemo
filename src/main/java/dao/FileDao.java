package dao;

public interface FileDao {
    /**
     * 通过用户id上传文件
     *
     * @param userId
     * @return
     */
    int insertFileByUserId(int userId,String path);
}
