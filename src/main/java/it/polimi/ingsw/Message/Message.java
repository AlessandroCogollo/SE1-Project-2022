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

    private Message (String time, Errors er, String message){
        this.time = time;
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


    public Message setTime(Instant time){
        return new Message(time.toString(), Errors.getErrorsByCode(this.errorCode), this.message);
    }

    public Message setError(Errors er){
        return new Message(this.time, er, this.message);
    }

    public Message setMessage(String message){
        return new Message(this.time, Errors.getErrorsByCode(this.errorCode), message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        Message that = (Message) o;
        return this.time.equals(that.time) && this.errorCode == that.errorCode && this.message.equals(that.message);
    }

    @Override
    public String toString() {
        return "Creation Time: " + this.time + ", Error: " + this.errorCode + ", Message: " + this.message + ", Type: " + getClass() + ". " + super.toString();
    }
}
