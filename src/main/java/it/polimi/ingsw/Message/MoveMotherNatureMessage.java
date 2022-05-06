package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

public final class MoveMotherNatureMessage extends ClientMessage{

    private final int position;

    public MoveMotherNatureMessage(Errors er, String message, int position) {
        super(er, message, 3);
        this.position = position;
    }

    @Override
    public int executeMove(Game game, int playerId) {
        return game.moveMotherNature(playerId, position);
    }
}
