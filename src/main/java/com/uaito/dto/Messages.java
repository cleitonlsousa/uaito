package com.uaito.dto;

public enum Messages {

    DEFAULT("UT0000 - Operation not Executed"),
    NOT_FOUND("UT0001 - Not found "),
    CREATED("UT0002 - Created "),
    TOURNAMENT_NAME_EXIST("UT003 - There is an tournament with that name: "),
    ACCOUNT_EMAIL_EXIST("UT004 - There is an account with that e-mail: "),
    PLAYER_EXIST("UT005 - Player already exist in this tournament"),
    MATCH_NOT_FINISH("UT006 - All match not finish yet"),
    TOURNAMENT_TICKET_EXCEEDED("UT007 - Ticket limit exceeded")
    ;

   private String message;

   Messages(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

}
