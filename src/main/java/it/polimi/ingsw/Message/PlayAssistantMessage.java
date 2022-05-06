package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

public final class PlayAssistantMessage extends ClientMessage{

    private final int assistantValue;

    public PlayAssistantMessage(Errors er, String message, int assistantValue) {
        super(er, message, 1);
        this.assistantValue = assistantValue;
    }

    @Override
    public int executeMove(Game game, int playerId) {
        return game.playAssistant(playerId, assistantValue);
    }
}
