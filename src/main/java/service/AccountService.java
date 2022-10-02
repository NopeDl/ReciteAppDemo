package service;

import pojo.po.Account;
import pojo.vo.Message;

public interface AccountService {
    /**
     * 通过number和password查找账户
     *
     * @param number
     * @param password
     * @return number和password对的上返回account，否则返回null
     */
    Message checkAccount(String number, String password);
}
