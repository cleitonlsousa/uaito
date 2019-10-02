package com.uaito.service;

import com.uaito.domain.Account;
import com.uaito.dto.Messages;
import com.uaito.exception.AccountEmailExistException;
import com.uaito.exception.NotFoundException;
import com.uaito.repository.AccountRepository;
import com.uaito.request.AccountRequest;
import com.uaito.response.AccountResponse;
import com.uaito.util.ParseObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ParseObjectUtil parseObjectUtil;

    public Account findById(Long id) throws NotFoundException {

        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));

    }

    private Account findByEmail(String email) throws NotFoundException {

        return accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));

    }

    public void save(AccountRequest account) throws AccountEmailExistException {

        try {

            accountRepository.save(parseObjectUtil.deParaAccount(account));

        }catch (DataIntegrityViolationException d){

            throw new AccountEmailExistException(Messages.ACCOUNT_EMAIL_EXIST.getMessage());

        }

    }

    private boolean emailExist(String email) throws NotFoundException {

        return findByEmail(email) != null;

    }

    public AccountResponse findByIdOrEmail(String value) throws NotFoundException {

        if(value.contains("@")){

            return parseObjectUtil.deParaResponse(findByEmail(value));

        }else{

            return parseObjectUtil.deParaResponse(findById(Long.valueOf(value)));

        }

    }

}
