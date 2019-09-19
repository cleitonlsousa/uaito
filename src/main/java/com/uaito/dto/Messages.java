package com.uaito.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@JsonSerialize(using = MessageSerialize.class)
public enum Messages {

    DEFAULT("UT0000", "Operation not Executed", HttpStatus.BAD_GATEWAY),
    NOT_FOUND("UT0001", "Account not found ", HttpStatus.NOT_FOUND),
    ACCOUNT_CREATED("UT0002", "Account created ", HttpStatus.CREATED),

    ;

   private String code;

   private String message;

   @JsonIgnore
   private HttpStatus httpStatus;

   Messages(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ResponseEntity<?> returnMessageResponse() {
        return new ResponseEntity(this, this.httpStatus);
    }

    public ResponseEntity<?> returnMessageResponse(Exception e) {
        return new ResponseEntity(this + e.getMessage(), this.httpStatus);
    }

    public String getErrorCode() { return code; }

    public String getErrorMessage() { return message; }

}
