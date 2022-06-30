package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

/**
 * message that can execute a student move
 */
public final class MoveStudentMessage extends ClientMessage{

    private final int indexColorToMove;
    private final int destinationId;

    public MoveStudentMessage(Errors er, String message, int indexColorToMove, int destinationId) {
        super(er, message, 2);
        this.indexColorToMove = indexColorToMove;
        this.destinationId = destinationId;
    }

    @Override
    public int executeMove(Game game, int playerId) {
        return game.moveStudent(playerId, indexColorToMove, destinationId);
    }
}
