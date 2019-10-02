package com.uaito.exception;

public class AccountEmailExistException extends Exception  {
    public AccountEmailExistException(String message){
        super(message);
    }
}
