package com.uaito.service;

import com.uaito.repository.AccountRepository;
import com.uaito.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;


    public Account findById(Long id){

        return accountRepository.findById(id).get();
    }

    public void save(Account account){

        accountRepository.save(account);

    }

}
