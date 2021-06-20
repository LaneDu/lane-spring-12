package com.lagou.edu.service;

import com.lagou.edu.dao.AccountDao;

/**
 * @author 应癫
 */
public interface TransferService {
    public void setAccountDao(AccountDao accountDao);
    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;
}
