package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;

final public class Minotaur extends Character {

    Minotaur(GameInitializer gameInitializer) {
        super (8, 3, gameInitializer, "Minotaur");
    }

    public Minotaur(GameInitializer gameInitializer, CharacterSerializable character) {
        super(gameInitializer, character);
    }

    @Override
    protected void activateEffect(int[] object) {
        // calcInfluence() method already implemented in GameBoard
    }

    @Override
    public Errors canActivateEffect(int[] obj) {
        // no further checks needed
        return Errors.NO_ERROR;
    }

}
