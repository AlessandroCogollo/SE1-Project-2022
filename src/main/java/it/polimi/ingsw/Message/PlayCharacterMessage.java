package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.Game;

public final class PlayCharacterMessage extends ClientMessage{

    private final int characterId;
    //todo
    private final Object attributes;

    public PlayCharacterMessage(Errors er, String message, int characterId, Object attributes) {
        super(er, message, 5);
        this.characterId = characterId;
        this.attributes = attributes;
    }

    @Override
    public int executeMove(Game game, int playerId) {
        return game.playCharacter(playerId, characterId, attributes);
    }
}
