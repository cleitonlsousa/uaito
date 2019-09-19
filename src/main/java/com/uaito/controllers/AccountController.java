package com.uaito.controllers;

import com.uaito.domain.Account;
import com.uaito.dto.Messages;
import com.uaito.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(value = "id") String id) {

        try{

            Account account = accountService.findById(Long.valueOf(id));

            return new ResponseEntity(account, HttpStatus.OK);

        } catch (NoSuchElementException e) {

            return Messages.NOT_FOUND.returnMessageResponse();

        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Account account) {

        try {

            accountService.save(account);

            return Messages.ACCOUNT_CREATED.returnMessageResponse();

        }catch (Exception e){

            return Messages.DEFAULT.returnMessageResponse(e);

        }
    }
}
