package com.uaito.controllers;

import com.uaito.domain.Account;
import com.uaito.dto.Messages;
import com.uaito.exception.AccountEmailExistException;
import com.uaito.exception.NotFoundException;
import com.uaito.repository.AccountRepository;
import com.uaito.request.AccountRequest;
import com.uaito.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{value}")
    public ResponseEntity<?> get(@PathVariable(value = "value") String value) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountService.findByIdOrEmail(value));

        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NOT_FOUND);

        }

    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody AccountRequest account) {

        try {

            accountService.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(Messages.CREATED);

        } catch (AccountEmailExistException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.ACCOUNT_EMAIL_EXIST);

        }

    }
}
