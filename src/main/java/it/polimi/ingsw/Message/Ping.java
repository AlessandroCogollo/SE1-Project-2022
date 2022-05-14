package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;

public class Ping extends Message{

    public Ping() {
        super(Errors.PING, Errors.PING.getDescription());
    }
}
