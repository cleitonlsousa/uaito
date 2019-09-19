package com.uaito.service;

import com.uaito.dao.AccountDAO;
import com.uaito.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountDAO accountDAO;


    public Account findById(Long id){

        return accountDAO.findById(id).get();
    }

    public void save(Account account){

        accountDAO.save(account);

    }

}
