package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;

/**
 * class of the ping message, used to check if the connection is alive
 */
public class Ping extends Message{

    public Ping() {
        super(Errors.PING, Errors.PING.getDescription());
    }
}
