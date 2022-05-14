package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;

public class NewPlayerMessage extends Message{

    private final boolean youAreFirst;

    public NewPlayerMessage(String message, boolean youAreFirst) {
        super(Errors.FIRST_MESSAGE_SERVER, message);
        this.youAreFirst = youAreFirst;
    }

    public boolean isYouAreFirst() {
        return youAreFirst;
    }
}
