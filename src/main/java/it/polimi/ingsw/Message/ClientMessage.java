package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

/**
 * message abstract class for client
 */
public abstract class ClientMessage extends Message{

    private final int moveId;

    public ClientMessage(Errors er, String message, int moveId) {
        super(er, message);
        this.moveId = moveId;
    }

    public int getMoveId() {
        return moveId;
    }

    public abstract int executeMove (Game game, int playerId);
}
