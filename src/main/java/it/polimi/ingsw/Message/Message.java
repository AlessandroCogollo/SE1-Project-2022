package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;

import java.time.Instant;

public class Message {
    private final String time;
    private final int errorCode;
    private final String message;

    public Message (Errors er, String message){
        this.time = Instant.now().toString();
        this.errorCode = er.getCode();
        this.message = message;
    }

    public Instant getTime() {
        return Instant.parse(time);
    }

    public Errors getError() {
        return Errors.getErrorsByCode(errorCode);
    }

    public String getMessage() {
        return message;
    }
}
