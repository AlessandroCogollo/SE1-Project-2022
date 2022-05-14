package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;

public class NotFirstPlayerMessage extends Message{

    private final String username;
    private final Wizard wizard;

    public NotFirstPlayerMessage (String message, String username, Wizard wizard) {
        super(Errors.NOT_FIRST_CLIENT, message);
        this.username = username;
        this.wizard = wizard;
    }

    public String getUsername() {
        return username;
    }

    public Wizard getWizard() {
        return wizard;
    }
}
