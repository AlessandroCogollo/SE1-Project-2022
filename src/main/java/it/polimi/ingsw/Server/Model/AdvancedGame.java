package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.Phases.ActionPhase;
import it.polimi.ingsw.Server.Model.Phases.Phase;

// the sub class of Game that extends the possible interactions of players adding the play character method
public class AdvancedGame extends Game {

    //not public only created by factory in Game class
    AdvancedGame(int numOfPlayer, GameInitializer gameInit, RoundHandler round) {
        super(numOfPlayer, gameInit, round);
    }

    public int playCharacter(int playerId, int characterId, Object obj) {

        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();
        if (!gameInit.getBoard().existsCharacter(characterId))
            return Errors.NO_SUCH_CHARACTER.getCode();

        AdvancedPlayer p = (AdvancedPlayer)gameInit.getPlayerById(playerId);
        Character x = gameInit.getBoard().getCharacterById(characterId);

        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (round.getPhase().equals(Phase.Planning) || round.getActionPhase().equals(ActionPhase.NotActionPhase))
            return Errors.NOT_RIGHT_PHASE.getCode();

        //check already played a character
        if (gameInit.getBoard().isCharacterPlayed())
            return Errors.CHARACTER_YET_PLAYED.getCode();

        //todo check if is a good move (es. play a character during the choose Cloud phase is may an error) ?

        if (p.getCoins() < x.getCost())
            return Errors.NOT_ENOUGH_COINS.getCode();

        //check if the object passed is correct and if the action performed by the character it's valid
        Errors er = x.canActivateEffect(obj);
        if (!er.equals(Errors.NO_ERROR))
            return er.getCode();

        p.playCharacter(x, obj);

        return Errors.NO_ERROR.getCode();
    }

    @Override
    public int moveMotherNature (int playerId, int position){

        int errorCode = super.moveMotherNature(playerId, position);
        if (Errors.MOVEMENTS_TOO_HIGH.getCode() != errorCode)
            return errorCode;

        AdvancedPlayer p = (AdvancedPlayer)gameInit.getPlayerById(playerId);

        if (gameInit.getBoard().isCharacterPlayed() && gameInit.getBoard().getActiveCharacter().getId() == 9 && position > (p.getActiveAssistant().getMaxMovement() + 2))
            return errorCode;

        p.moveMotherNature(position);

        round.next();

        return Errors.NO_ERROR.getCode();
    }
}
