package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;

// the sub class of Game that extends the possible interactions of players adding the play character method
public class AdvancedGame extends Game {

    AdvancedGame(int numOfPlayer, GameInitializer gameInit, RoundHandler round) {
        super(numOfPlayer, gameInit, round);
    }

    public int playCharacter(int playerId, Character x) {

        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();
        if (x == null)
            return Errors.NULL_POINTER.getCode();

        AdvancedPlayer p = (AdvancedPlayer)gameInit.getPlayerById(playerId);

        //todo check if is a playable character

        if (p != round.getCurrent())
            return Errors.NOT_CURRENT_PLAYER.getCode();

        //check already played a character
        if (p.getActiveCharacter().isPresent())
            return Errors.CHARACTER_YET_PLAYED.getCode();

        //todo check if is a good move (es. play a character during the choose Cloud phase is may an error) ?

        //todo check can play character, need Character enum
        if (true) //(p.getCoins() < x.getCost())
            return Errors.NOT_ENOUGH_COINS.getCode();

        p.playCharacter(x);

        //no next because it doesn't change the round in the game

        return Errors.NO_ERROR.getCode();
    }

    // only one override because the character can only modify this method
    @Override
    public int moveMotherNature (int playerId, int position){

        AdvancedPlayer p = (AdvancedPlayer)gameInit.getPlayerById(playerId);
        Character c = p.getActiveCharacter().get();

        //todo check active character that modify methods, need Character enum
        //no character that change methods
        if (c.getId() == 9)
            // modify movement of mother nature, +2
            return super.moveMotherNature(playerId, position);

        //with character that change methods
        if (!gameInit.existsPlayer(playerId))
            return Errors.PLAYER_NOT_EXIST.getCode();

        // AdvancedPlayer p = (AdvancedPlayer)gameInit.getPlayerById(playerId);
        //todo move mother nature special, need Character enum



        //p.moveMotherNature(position);

        round.next();

        return Errors.NO_ERROR.getCode();
    }

}
