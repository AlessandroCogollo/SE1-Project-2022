package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

/**
 * message that can execute a cloud choice
 */
public final class ChooseCloudMessage extends ClientMessage{

    private final int cloudId;

    public ChooseCloudMessage(Errors er, String message, int cloudId) {
        super(er, message, 4);
        this.cloudId = cloudId;
    }

    @Override
    public int executeMove(Game game, int playerId) {
        return game.chooseCloud(playerId,  cloudId);
    }
}
