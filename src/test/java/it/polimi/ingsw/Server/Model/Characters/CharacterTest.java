package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Server.Model.GameInitializer;
import it.polimi.ingsw.Server.Model.GameInitializerTest;
import it.polimi.ingsw.Server.Model.School;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(Apothecary instanceof it.polimi.ingsw.Server.Model.Characters.Apothecary);

        for (int i = 0; i < testCases.length; i++) {

                // check correct number of banCard on this
                Assertions.assertEquals(((Apothecary) Apothecary).getBanCard(), 4);

                for (int j = 0; j < testCases[i].length; j++) {
                    int[] te = new int[1];
                    te[0] = testCases[i][j];
                    Errors er = Apothecary.canActivateEffect(te);
                    if (er==Errors.NO_ERROR) {
                        int temp = ((Apothecary) Apothecary).getBanCard();
                        Apothecary.activateEffect(te);
                        // check if a banCard has been removed
                        Assertions.assertEquals(temp-1, ((Apothecary) Apothecary).getBanCard());
                    } else {
                        if (i == 0) {
                            // check error catch: NOT_ENOUGH_TOKEN
                            Assertions.assertEquals(Errors.NOT_ENOUGH_TOKEN, er);
                        } else {
                            // check error catch: NOT_VALID_DESTINATION
                            Assertions.assertEquals(Errors.NOT_VALID_DESTINATION, er);
                        }
                    }
                }

                // used to increment number of banCard to repeat test, and to test addBanCard() method
                while (((Apothecary) Apothecary).getBanCard() < 4) {
                    ((Apothecary) Apothecary).addBanCard();
                }
            }
        }

    public static Stream<Arguments> bard (){
        List<int[]> testCases = new ArrayList<>(653);
        testCases.add(new int[]{});
        testCases.add(new int[]{1, 2, 3, 4, 5, 6});
        testCases.add(new int[]{1, 2, 3});

        int colorNumber = Color.getNumberOfColors();

        for (long i = 0; i < Math.pow(colorNumber, 4); i++){
            int[] x = new int[4];
            x[3] = (int) (i % colorNumber);
            x[2] = (int) ((i / colorNumber) % colorNumber);
            x[1] = (int) ((i / Math.pow(colorNumber, 2)) % colorNumber);
            x[0] = (int) ((i / Math.pow(colorNumber, 3)) % colorNumber);
            testCases.add(x);
        }

        for (long i = 0; i < Math.pow(colorNumber, 2); i++){
            int[] x = new int[2];
            x[1] = (int) (i % colorNumber);
            x[0] = (int) ((i / colorNumber) % colorNumber);
            testCases.add(x);
        }


        return testCases.stream().map(Arguments::of);
    }

    private static GameInitializer bardG (){
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        School s = g.getRoundHandler().getCurrent().getSchool();
        Random r = new Random(System.currentTimeMillis());
        for (Color c : Color.values()){
            for (int i = 0; i < r.nextInt(10); i++){
                s.addStudentToRoom(c);
            }
        }
        return g;
    }
    @ParameterizedTest
    @MethodSource("bard")
    @DisplayName("Bard Test")
    void BardTest(int[] testCase) {


        Character Bard = CharacterFactory.produceCharacterById(1, bardG());

        Errors er = Bard.canActivateEffect(testCase);
        if (er == Errors.NO_ERROR) {
            Bard.activateEffect(testCase);
            Bard.use(testCase);
        } else {
            assertTrue(Errors.NOT_RIGHT_PARAMETER.equals(er) || Errors.NOT_ENOUGH_TOKEN.equals(er));
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
        };

        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Cleric = CharacterFactory.produceCharacterById(2, g);

        // check correct building
        assertTrue(Cleric instanceof Cleric);

        int[] tempStudents = ((Cleric) Cleric).getStudentsNumber();
        int countStudents = 0;
        for (Color color: Color.values()) {
            if (tempStudents[color.getIndex()] != 0)
                countStudents += tempStudents[color.getIndex()];
        }

        // check correct number of students on this
        Assertions.assertEquals(4, countStudents);

        Errors er;

        for (int[] testCase : testCases) {
            er = Cleric.canActivateEffect(testCase);
            if (testCase.length != 2)
                //wrong length
                Assertions.assertEquals(Errors.NOT_RIGHT_PARAMETER, er);
            else if (testCase[0] < 0 || testCase[0] > 4)
                //invalid colorId
                Assertions.assertEquals(Errors.NOT_VALID_COLOR, er);
            else if (!Objects.requireNonNull(g).getIslands().existsIsland(testCase[1]))
                //invalid islandId
                Assertions.assertEquals(Errors.NOT_VALID_DESTINATION, er);
            else if (tempStudents[testCase[0]] <= 0)
                //the card hasn't the students color
                Assertions.assertEquals(Errors.NO_STUDENT, er);
            else {
                //no error
                Assertions.assertEquals(Errors.NO_ERROR, er);
                Cleric.use(testCase);
            }
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
        assertTrue(Cook instanceof Cook);
        assertNull(((Cook) Cook).getColor());

        for (int i = 0; i < testCases.length; i++) {
            int[] x = new int[1];
            x[0] = testCases[i];
            Errors er = Cook.canActivateEffect(x);
            Cook.canActivateEffect(x);
            if (er == Errors.NO_ERROR) {
                Cook.activateEffect(x);
            }
            else {
                if (i > 4) {
                    Assertions.assertEquals(Errors.NOT_VALID_COLOR, er);
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

        assertTrue(Drunkard instanceof Drunkard);

        assertEquals(Errors.NO_ERROR, Drunkard.canActivateEffect(null));

        Drunkard.use(null);
    }

    @Test
    @DisplayName("Herald Test")
    void HeraldTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Herald = CharacterFactory.produceCharacterById(5, g);

        assertTrue(Herald instanceof Herald);

        int[] testCases = {
                1, 2, 3, 4
        };

        for (int i: testCases) {
            int[] x = new int[1];
            x[0] = i;
            Herald.canActivateEffect(x);
            Herald.activateEffect(x);
        }
    }


    public static Stream<Arguments> jester (){
        List<int[]> testCases = new ArrayList<>(16278);
        testCases.add(new int[]{});
        testCases.add(new int[]{1, 2, 3, 4, 5, 6});
        testCases.add(new int[]{1, 2, 3});

        int colorNumber = Color.getNumberOfColors();

        for (long i = 0; i < Math.pow(colorNumber, 6); i++){
            int[] x = new int[6];
            x[5] = (int) (i % colorNumber);
            x[4] = (int) ((i / colorNumber) % colorNumber);
            x[3] = (int) ((i / Math.pow(colorNumber, 2)) % colorNumber);
            x[2] = (int) ((i / Math.pow(colorNumber, 3)) % colorNumber);
            x[1] = (int) ((i / Math.pow(colorNumber, 4)) % colorNumber);
            x[0] = (int) ((i / Math.pow(colorNumber, 5)) % colorNumber);
            testCases.add(x);
        }

        for (long i = 0; i < Math.pow(colorNumber, 4); i++){
            int[] x = new int[4];
            x[3] = (int) (i % colorNumber);
            x[2] = (int) ((i / colorNumber) % colorNumber);
            x[1] = (int) ((i / Math.pow(colorNumber, 2)) % colorNumber);
            x[0] = (int) ((i / Math.pow(colorNumber, 3)) % colorNumber);
            testCases.add(x);
        }

        for (long i = 0; i < Math.pow(colorNumber, 2); i++){
            int[] x = new int[2];
            x[1] = (int) (i % colorNumber);
            x[0] = (int) ((i / colorNumber) % colorNumber);
            testCases.add(x);
        }


        return testCases.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("jester")
    @DisplayName("Jester Test")
    void JesterTest(int [] testCase) {
        Character Jester = CharacterFactory.produceCharacterById(6, GameInitializerTest.setGameInitializer(2, 1));

        // check correct building
        assertTrue(Jester instanceof Jester);

        // check if number of students is correct
        int studentsCount = 0;
        int[] studentsTemp = ((Jester) Jester).getStudents();
        for (Color color: Color.values()) {
            if (studentsTemp[color.getIndex()] != 0) studentsCount += studentsTemp[color.getIndex()];
        }
        Assertions.assertEquals(6, studentsCount);

        Errors er = Jester.canActivateEffect(testCase);
        if (er.equals(Errors.NO_ERROR)) {
            Jester.use(testCase);
            assertTrue(true);
        }
        else{
            assertTrue(er.equals(Errors.NOT_RIGHT_PARAMETER) || er.equals(Errors.NOT_ENOUGH_TOKEN));
        }
    }

    @Test
    @DisplayName("Knight Test")
    void KnightTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Knight = CharacterFactory.produceCharacterById(7, g);

        // check correct building
        assertTrue(Knight instanceof Knight);

        Knight.canActivateEffect(null);
        Knight.use(null);
        // trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Minotaur Test")
    void MinotaurTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Minotaur = CharacterFactory.produceCharacterById(8, g);

        // check correct building
        assertTrue(Minotaur instanceof Minotaur);
        Minotaur.canActivateEffect(null);
        Minotaur.use(null);
        // trivial, already tested in GameBoard
    }

    @Test
    @DisplayName("Postman Test")
    void PostmanTest() {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Postman = CharacterFactory.produceCharacterById(9, g);

        // check correct building
        assertTrue(Postman instanceof Postman);
        Postman.canActivateEffect(null);
        Postman.use(null);
        // trivial, already tested in GameBoard
    }

    public static Stream<Arguments> princess (){
        List<int[]> testCases = new ArrayList<>(16278);
        testCases.add(new int[]{});
        testCases.add(new int[]{1, 2, 3, 4, 5, 6});
        testCases.add(new int[]{1, 2, 3});
        testCases.add(new int[]{-2});
        testCases.add(new int[]{5});

        for (Color c: Color.values()){
            int[] x = new int[1];
            x[0] = c.getIndex();
            testCases.add(x);
        }


        return testCases.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("princess")
    @DisplayName("Princess Test")
    void PrincessTest(int[] testCase) {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Princess = CharacterFactory.produceCharacterById(10, g);

        // check correct building
        assertTrue(Princess instanceof Princess);

        // check if number of students is correct
        int studentsCount = 0;
        int[] studentsTemp = ((Princess) Princess).getStudents();
        for (Color color: Color.values()) {
            if (studentsTemp[color.getIndex()] != 0) studentsCount += studentsTemp[color.getIndex()];
        }
        Assertions.assertEquals(4, studentsCount);

        Errors er = Princess.canActivateEffect(testCase);
        if (er.equals(Errors.NO_ERROR)) {
            Princess.use(testCase);
            assertTrue(true);
        }
        else{
            assertTrue(er.equals(Errors.NOT_RIGHT_PARAMETER) || er.equals(Errors.NOT_ENOUGH_TOKEN) || er.equals(Errors.NO_MORE_MOVEMENT));
        }
    }

    @ParameterizedTest
    @MethodSource("princess")
    @DisplayName("Thief Test")
    void ThiefTest(int[] testCase) {
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 1);
        Character Thief = CharacterFactory.produceCharacterById(11, g);

        // check correct building
        assertTrue(Thief instanceof Thief);
        Errors er = Thief.canActivateEffect(testCase);
        if (er.equals(Errors.NO_ERROR)) {
            Thief.use(testCase);
            assertTrue(true);
        }
        else{
            assertTrue(er.equals(Errors.NOT_RIGHT_PARAMETER) || er.equals(Errors.NOT_ENOUGH_TOKEN));
        }
    }
}
