package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Demo {
    public static void main(String[] args) {
        CharacterFactory charFactory = new CharacterFactory();

        Collection<Character> c = charFactory.getNewGameDeck();

        for (Character i : c)
            i.activateEffect(Optional.empty());
    }
}
