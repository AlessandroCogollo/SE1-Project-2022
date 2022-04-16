package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Errors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class CharacterTest {

    GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);

    @Test
    @DisplayName("Set Up Test")
    void setUp() {
        Collection<Character> allCharacters = new ArrayList<>(12);

        for (int i = 0; i < 12; i++) {
            allCharacters.add(CharacterFactory.produceCharacterById(i, g));
        }
        Assertions.assertEquals(12, allCharacters.size());
    }

    @Test
    @DisplayName("Draw Deck Test")
    void drawDeck() {
        Collection<Character> charactersDeck = CharacterFactory.getNewGameDeck(g);
        Assertions.assertEquals(3, charactersDeck.size());
    }

    @Test
    @DisplayName("Apothecary Test")
    void ApothecaryTest() {

        int[][] testCases = {
                {1, 2, 3, 4, 5},      // check illegal input
                {12, 3, -3},          // check illegal island's index
        };

        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Apothecary = CharacterFactory.produceCharacterById(0, g);

        // check correct building
        Assertions.assertTrue(Apothecary instanceof Apothecary);

        for (int i = 0; i < testCases.length; i++) {

                // check correct number of banCard on this
                Assertions.assertEquals(((Apothecary) Apothecary).getBanCard(), 4);

                for (int j = 0; j < testCases[i].length; j++) {
                    Errors er = Apothecary.canActivateEffect(testCases[i][j]);
                    if (er==Errors.NO_ERROR) {
                        int temp = ((Apothecary) Apothecary).getBanCard();
                        Apothecary.activateEffect(testCases[i][j]);
                        // check if a banCard has been removed
                        Assertions.assertEquals(temp-1, ((Apothecary) Apothecary).getBanCard());
                    } else {
                        if (i == 0) {
                            // check error catch: NOT_ENOUGH_TOKEN
                            Assertions.assertEquals(18, er.getCode());
                        } else {
                            // check error catch: NOT_VALID_DESTINATION
                            Assertions.assertEquals(15, er.getCode());
                        }
                    }
                }

                // used to increment number of banCard to repeat test, and to test addBanCard() method
                while (((Apothecary) Apothecary).getBanCard() < 4) {
                    ((Apothecary) Apothecary).addBanCard();
                }
            }
        }

    @Test
    @DisplayName("Bard Test")
    void BardTest() {

        int[][] testCases = {
                {},                          // check empty input
                {1, 2, 3, 4, 5, 6},          // check input longer than maximum size
                {1, 2, 3},                   // check odd input
                // TODO: check if it is correctly moving students
        };

        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);

        for (int i = 0; i < testCases.length; i++) {

            System.out.println("Test N° " + i);
            Character Bard = CharacterFactory.produceCharacterById(1, g);

            Errors er = Bard.canActivateEffect(testCases[i]);
            if (er == Errors.NO_ERROR) {
                Bard.activateEffect(testCases[i]);
            } else {
                Assertions.assertEquals(21, er.getCode());
            }
        }
    }

    @Test
    @DisplayName("Cleric Test")
    void ClericTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Cleric = CharacterFactory.produceCharacterById(2, g);

        // TODO: implement class

    }

    @Test
    @DisplayName("Cook Test")
    void CookTest() {

    }

    @Test
    @DisplayName("Drunkard Test")
    void DrunkardTest() {

    }

    @Test
    @DisplayName("Herald Test")
    void HeraldTest() {
        Character Herald = CharacterFactory.produceCharacterById(5, g);

        int[] testCases = {
                1, 2, 3, 4
        };

        for (int i: testCases) {
            Herald.canActivateEffect(i);
            Herald.activateEffect(i);
        }
    }

    @Test
    @DisplayName("Jester Test")
    void JesterTest() {

    }

    @Test
    @DisplayName("Knight Test")
    void KnightTest() {

    }

    @Test
    @DisplayName("Minotaur Test")
    void MinotaurTest() {

    }

    @Test
    @DisplayName("Postman Test")
    void PostmanTest() {

    }

    @Test
    @DisplayName("Princess Test")
    void PrincessTest() {

    }

    @Test
    @DisplayName("Thief Test")
    void ThiefTest() {

    }
}
