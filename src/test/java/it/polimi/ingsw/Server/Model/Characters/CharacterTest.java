package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Server.Errors;
import it.polimi.ingsw.Server.Model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class CharacterTest {

    @Test
    @DisplayName("Set Up Test")
    void setUp() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Collection<Character> allCharacters = new ArrayList<>(12);

        for (int i = 0; i < 12; i++) {
            allCharacters.add(CharacterFactory.produceCharacterById(i, g));
        }
        Assertions.assertEquals(12, allCharacters.size());
    }

    @Test
    @DisplayName("Draw Deck Test")
    void drawDeck() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
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
        Assertions.assertTrue(Apothecary instanceof it.polimi.ingsw.Server.Model.Characters.Apothecary);

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

        for (int[] testCase : testCases) {

            Character Bard = CharacterFactory.produceCharacterById(1, g);

            Errors er = Bard.canActivateEffect(testCase);
            if (er == Errors.NO_ERROR) {
                Bard.activateEffect(testCase);
            } else {
                Assertions.assertEquals(21, er.getCode());
            }
        }
    }

    @Test
    @DisplayName("Cleric Test")
    void ClericTest() {

        int[][] testCases = {
                {},                          // check empty input
                {1, 2, 3},                   // check input longer than maximum size
                {1},                         // check single value input
                {-2, 1},                     // check invalid colorId
                {1, -4},                     // check invalid island

                //try all color for have at least one no error
                {0, 1},
                {1, 1},
                {2, 1},
                {3, 1},
                {4, 1}
                // TODO: check behaviour when the number of students on this is <= 0
        };

        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Cleric = CharacterFactory.produceCharacterById(2, g);

        // check correct building
        Assertions.assertTrue(Cleric instanceof Cleric);

        int[] tempStudents = ((Cleric) Cleric).getStudentsNumber();
        int countStudents = 0;
        for (Color color: Color.values()) {
            if (tempStudents[color.getIndex()] != 0)
                countStudents += tempStudents[color.getIndex()];
        }

        // check correct number of students on this
        Assertions.assertEquals(4, countStudents);

        //wrong object
        int x = 0;
        Errors er = Cleric.canActivateEffect(x);
        Assertions.assertEquals(21, er.getCode());


        for (int i = 0; i < testCases.length; i++) {
            er = Cleric.canActivateEffect(testCases[i]);
            if (testCases[i].length != 2)
                //wrong length
                Assertions.assertEquals(21, er.getCode());
            else if (testCases[i][0] < 0 || testCases[i][0] > 4)
                //invalid colorId
                Assertions.assertEquals(14, er.getCode());
            else if (!g.getIslands().existsIsland(testCases[i][1]))
                //invalid islandId
                Assertions.assertEquals(15, er.getCode());
            else if (tempStudents[testCases[i][0]] <= 0)
                //the card hasn't the students color
                Assertions.assertEquals(10, er.getCode());
            else
                //no error
                Assertions.assertEquals(0, er.getCode());


            /*try {
                Errors er = Cleric.canActivateEffect(testCases[i]);
                if (er==Errors.NO_ERROR) {
                    // trivial
                    Assertions.assertTrue(true);
                } else {
                    if (i == 3) {
                        // check error catch: NOT_VALID_COLOR
                        Assertions.assertEquals(14, er.getCode());
                    } else {
                        // check error catch: NOT_VALID_DESTINATION
                        if
                        Assertions.assertEquals(15, er.getCode());
                    }
                }
            // catch OutOfBounds
            } catch (ArrayIndexOutOfBoundsException exception) {
                Assertions.assertTrue(true);
            }*/
        }
    }

    @Test
    @DisplayName("Cook Test")
    void CookTest() {

        int[] testCases = {
                0, 1, 2, 3, 4, 5, -1
        };

        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Cook = CharacterFactory.produceCharacterById(3, g);

        // check correct building
        Assertions.assertTrue(Cook instanceof Cook);
        Assertions.assertTrue(((Cook) Cook).getProfessor().isEmpty());

        for (int i = 0; i < testCases.length; i++) {
            Errors er = Cook.canActivateEffect(testCases[i]);
            Cook.canActivateEffect(testCases[i]);
            if (er == Errors.NO_ERROR) {
                Cook.activateEffect(testCases[i]);
            }
            else {
                if (i > 4) {
                    Assertions.assertEquals(14, er.getCode());
                }
            }
        }

        // calcInfluence() for Cook is trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Drunkard Test")
    void DrunkardTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Drunkard = CharacterFactory.produceCharacterById(4, g);

        Assertions.assertTrue(Drunkard instanceof Drunkard);

        // trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Herald Test")
    void HeraldTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Herald = CharacterFactory.produceCharacterById(5, g);

        Assertions.assertTrue(Herald instanceof Herald);

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
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Jester = CharacterFactory.produceCharacterById(6, g);

        // check correct building
        Assertions.assertTrue(Jester instanceof Jester);

        // check if number of students is correct
        int studentsCount = 0;
        int[] studentsTemp = ((Jester) Jester).getStudents();
        for (Color color: Color.values()) {
            if (studentsTemp[color.getIndex()] != 0) studentsCount += studentsTemp[color.getIndex()];
        }
        Assertions.assertEquals(6, studentsCount);

        // TODO: check activateEffects() && canActivateEffects()
    }

    @Test
    @DisplayName("Knight Test")
    void KnightTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Knight = CharacterFactory.produceCharacterById(7, g);

        // check correct building
        Assertions.assertTrue(Knight instanceof Knight);

        // trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Minotaur Test")
    void MinotaurTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Minotaur = CharacterFactory.produceCharacterById(8, g);

        // check correct building
        Assertions.assertTrue(Minotaur instanceof Minotaur);

        // trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Postman Test")
    void PostmanTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Postman = CharacterFactory.produceCharacterById(9, g);

        // check correct building
        Assertions.assertTrue(Postman instanceof Postman);

        // trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Princess Test")
    void PrincessTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Princess = CharacterFactory.produceCharacterById(10, g);

        // check correct building
        Assertions.assertTrue(Princess instanceof Princess);

        // check if number of students is correct
        int studentsCount = 0;
        int[] studentsTemp = ((Princess) Princess).getStudents();
        for (Color color: Color.values()) {
            if (studentsTemp[color.getIndex()] != 0) studentsCount += studentsTemp[color.getIndex()];
        }
        Assertions.assertEquals(4, studentsCount);

        // TODO: check activateEffects() && canActivateEffects()
    }

    @Test
    @DisplayName("Thief Test")
    void ThiefTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Thief = CharacterFactory.produceCharacterById(11, g);

        // check correct building
        Assertions.assertTrue(Thief instanceof Thief);
    }
}
