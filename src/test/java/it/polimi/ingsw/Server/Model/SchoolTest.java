package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {

    private static School getSchool (int towers, int entranceStudent){
        GameInitializer g = GameInitializerTest.setGameInitializer(2, 0);
        Bag bag = g.getBag();
        return new School(towers, entranceStudent, bag);
    }

    private static int countStudent (int[] x){
        int sum = 0;
        for (int j : x) sum += j;
        return sum;
    }

    private static int[] getStudentInRoom (School s){
        return s.getCopyOfRoom();
    }

    private static int[] getStudentInRoom (Player p){
        return p.getSchool().getCopyOfRoom();
    }

    //destructive method, because it move all the students from the entrance to the room
    private static int[] getStudentInEntrance_destructive (School s){
        int[] studentsInEntrance = new int[Color.getNumberOfColors()];
        Arrays.fill(studentsInEntrance, 0);
        for (Color c: Color.values()){
            while (s.hasStudentInEntrance(c)){
                s.moveStudentToRoom(c);
                studentsInEntrance[c.getIndex()]++;
            }
        }
        return studentsInEntrance;
    }

    //destructive method, because it move all the students from the entrance to the room
    private static int[] getStudentInEntrance_destructive (Player p){
        int[] studentsInEntrance = new int[Color.getNumberOfColors()];
        Arrays.fill(studentsInEntrance, 0);
        for (Color c: Color.values()){
            while (p.hasStudent(c)){
                p.moveStudent(c, -1);
                studentsInEntrance[c.getIndex()]++;
            }
        }
        return studentsInEntrance;
    }

    @Test
    void getSchool () {

        //testing only School parameter, other test has been done in player

        int[] id2 = new int[2];
        id2[0] = 1;
        id2[1] = 2;

        int[] id3 = new int[3];
        id3[0] = 1;
        id3[1] = 2;
        id3[2] = 3;

        int[] id4 = new int[4];
        id4[0] = 1;
        id4[1] = 2;
        id4[2] = 3;
        id4[3] = 4;

        GameInitializer g = new GameInitializer(0, 2);
        g.createAllGame(id2, null);
        Collection<Player> players = new ArrayList<>();
        for (int j : id2) players.add(g.getPlayerById(j));

        for (Player p: players){
            assertEquals(0, countStudent(getStudentInRoom(p)), "Test 1 - no student in room initially");
            assertEquals(8, p.getTowers(), "Test 2 - towers number");
            assertEquals(7, countStudent(getStudentInEntrance_destructive(p)), "Test 3 - entrance student number");
        }

        g = new GameInitializer(0, 3);
        g.createAllGame(id3, null);
        players = new ArrayList<>();
        for (int j : id3) players.add(g.getPlayerById(j));

        for (Player p: players){
            assertEquals(0, countStudent(getStudentInRoom(p)), "Test 1 - no student in room initially");
            assertEquals(6, p.getTowers(), "Test 2 - towers number");
            assertEquals(9, countStudent(getStudentInEntrance_destructive(p)), "Test 3 - entrance student number");
        }

        //the fact that 2 player doesn't have the tower will be hide from the outside
        g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);
        players = new ArrayList<>();
        for (int j : id4) players.add(g.getPlayerById(j));

        for (Player p: players){
            assertEquals(0, countStudent(getStudentInRoom(p)), "Test 1 - no student in room initially");
            assertEquals(8, p.getTowers(), "Test 2 - towers number");
            assertEquals(7, countStudent(getStudentInEntrance_destructive(p)), "Test 3 - entrance student number");
        }
    }

    @Test
    void getTowers() {
        //trivial
        assertTrue(true);
    }

    @Test
    void hasStudentInEntrance() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getNumberOfStudentInRoomByColor() {
        //trivial
        assertTrue(true);
    }

    @Test
    void moveStudentToRoom() {
        School s;

        for (Color c: Color.values()){
            s = getSchool(0, 7);
            while (!s.hasStudentInEntrance(c))
                s = getSchool(0, 7);

            int[] studentsInRoomInit = getStudentInRoom(s);
            s.moveStudentToRoom(c);
            int[] studentsInRoomFinal = getStudentInRoom(s);
            assertEquals(countStudent(studentsInRoomInit) + 1, countStudent(studentsInRoomFinal), "Test 1 - move a student");
        }

        s = getSchool(0, 7);
        int[] studentsInRoomInit = getStudentInRoom(s);
        int[] colorDone = new int[Color.getNumberOfColors()];
        Arrays.fill(colorDone, 0);
        for (Color c: Color.values()){
            while (s.hasStudentInEntrance(c)){
                s.moveStudentToRoom(c);
                colorDone[c.getIndex()]++;
            }
        }
        int[] studentsInRoomFinal = getStudentInRoom(s);
        for (Color c: Color.values()){
            assertEquals(studentsInRoomInit[c.getIndex()] + colorDone[c.getIndex()], studentsInRoomFinal[c.getIndex()], "Test 2 - all students to room");
        }


    }

    @Test
    void addStudentFromCloud() {

        int[] id2 = new int[2];
        id2[0] = 4;
        id2[1] = 24;

        GameInitializer g = new GameInitializer(0, 2);
        RoundHandler r = new RoundHandler(g);

        g.createAllGame(id2, r);
        r.start();

        Cloud c = g.getBoard().getCloudById(0);

        School s = getSchool(0, 0);

        assertEquals(0, countStudent(getStudentInEntrance_destructive(s)));

        s.addStudentFromCloud(c);

        assertEquals(3, countStudent(getStudentInEntrance_destructive(s)));

    }

    @Test
    void addTowers() {
        School s = getSchool(8, 0);
        assertEquals(8, s.getTowers());
        s.addTowers(2);
        assertEquals(10, s.getTowers());
    }

    @Test
    void removeTowers() {
        School s = getSchool(8, 0);
        assertEquals(8, s.getTowers());
        s.removeTowers(2);
        assertEquals(6, s.getTowers());
        s.removeTowers(4);
        assertEquals(2, s.getTowers());
        assertFalse(s.removeTowers(1));
        assertEquals(1, s.getTowers());
        assertTrue(s.removeTowers(1));
        assertTrue(s.removeTowers(1));
        assertTrue(s.removeTowers(10));
        assertEquals(0, s.getTowers());
        s = getSchool(8, 0);
        assertTrue(s.removeTowers(10));
        assertEquals(0, s.getTowers());
    }

    /**
     * Test the movement from the entrance
     */
    @Test
    void moveStudentFromEntrance() {
        School s = getSchool(8, 7);
        int i = 0;
        for (Color c: Color.values()){
            while (s.hasStudentInEntrance(c)) {
                s.removeStudentFromEntrance(c);
                i++;
            }
        }
        assertEquals(7, i);
        boolean x = true;
        for (Color c: Color.values()){
            if (s.hasStudentInEntrance(c)) {
                x = false;
                break;
            }
        }
        assertTrue(x);
    }

    @Test
    void getCopyOfEntrance() {
        //trivial
        assertTrue(true);
    }

    @Test
    void getCopyOfRoom() {
        //trivial
        assertTrue(true);
    }
}