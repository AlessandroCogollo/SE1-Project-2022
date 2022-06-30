package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

/**
 * Message abstract class for client moves.
 *
 * All the subClass of it are a possible type of move that a player can do in the game.
 */
public abstract class ClientMessage extends Message{

    private final int moveId;

    /**
     * The constructor of the class
     * @param er Error, normally is Errors.NO_ERROR
     * @param message a string with further information about the moves
     * @param moveId the id of the moves
     */
    public ClientMessage(Errors er, String message, int moveId) {
        super(er, message);
        this.moveId = moveId;
    }

    /**
     * Getter
     * @return the id of the move
     */
    public int getMoveId() {
        return moveId;
    }

    /**
     * Method that implements the execution of the move corresponding to the moveId
     * @param game the model where execute the move
     * @param playerId the id of the player that has send the moves
     * @return the error code from the game (0 if the moves is correctly executed)
     */
    public abstract int executeMove (Game game, int playerId);
}
