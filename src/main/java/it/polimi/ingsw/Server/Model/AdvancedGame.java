package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

// the sub class of Game that extends the possible interactions of players adding the play character method
public class AdvancedGame extends Game {

    AdvancedGame(int numOfPlayer, GameInitializer gameInit, RoundHandler round) {
        super(numOfPlayer, gameInit, round);
    }

    public int playCharacter(int playerId, Character x) {

        AdvancedPlayer p = (AdvancedPlayer)gameInit.getPlayerById(playerId);

        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();

        // todo check if is a good move (es. play a character during the choose Cloud phase is may an error)

        if (true/*p.getCoins() < x.getCost()*/)
            return Errors.NOT_ENOUGH_COINS.getCode();

        p.playCharacter();

        //no next because it doesn't change the round in the game

        return Errors.NO_ERROR.getCode();
    }

    // only one override because the character can only modify this method
    @Override
    public int moveMotherNature (int playerId, int position){

        Player p = gameInit.getPlayerById(playerId);

        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();
        if (!round.getPhase().equals(Phase.Action) || !round.getActionPhase().equals(ActionPhase.MoveMotherNature))
            return Errors.NOT_RIGHT_PHASE.getCode();

        //todo check for active character that change the max number of movement

        p.moveMotherNature(position); //in this case the player are certainly AdvancePlayer so this call the overrided method in advance player

        round.next();

        return Errors.NO_ERROR.getCode();
    }

}
