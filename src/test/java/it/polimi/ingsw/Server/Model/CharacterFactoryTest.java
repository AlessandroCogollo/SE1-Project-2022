package it.polimi.ingsw.Server.Model;

import java.util.Collection;
import java.util.Optional;

public class CharacterFactoryTest {
    public static void main(String[] args) {

        // Collection<Character> c = charFactory.getNewGameDeck();

        int[] ids = new int[]{1,2};
        GameInitializer gInit = new GameInitializer(ids, 2);

        GameBoard gBoard = gInit.getGameBoard();
        Islands gIslands = gBoard.getIslands();
        Island gIsland = gIslands.getIslandFromId(2);

        // build Apothecary
        Character Apothecary = CharacterFactory.produceCharacterById(0);
        Apothecary.activateEffect(gIsland);
        /*
        for (Character i : c)
            i.activateEffect(Optional.empty());
            //i.activateEffect(Color.getColorById(2));
        */
    }
}
