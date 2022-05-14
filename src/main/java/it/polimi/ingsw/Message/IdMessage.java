package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;

public class IdMessage extends Message{

    private final int playerId;

    public IdMessage(String message, int playerId) {
        super(Errors.INFO_RECEIVED, message);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
