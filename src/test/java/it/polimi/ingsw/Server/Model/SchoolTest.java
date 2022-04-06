package it.polimi.ingsw.Server.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SchoolTest {

    private static School getSchool (int towers, int entranceStudent, int mode, int[] stu){
        if (mode == 0) {
            Bag bag = new Bag(null);
            return new School(towers, entranceStudent, bag);
        }
        if (countStudent(stu) == entranceStudent) {
            Bag bag = new Bag(null);
            return new School(towers, entranceStudent, bag);
        }
        return null;
    }

    private static int countStudent (int[] x){
        int sum = 0;
        for (int i = 0; i < x.length; i++)
            sum += x[i];
        return sum;
    }

    private static int[] getStudentInRoom (School s){
        int[] studentsInRoom = new int[Color.getNumberOfColors()];
        for (Color c: Color.values()){
            studentsInRoom[c.getIndex()] = s.getNumberOfStudentInRoomByColor(c);
        }
        return studentsInRoom;
    }

    private static int[] getStudentInRoom (Player p){
        int[] studentsInRoom = new int[Color.getNumberOfColors()];
        for (Color c: Color.values()){
            studentsInRoom[c.getIndex()] = p.getNumberOfStudentInRoomByColor(c);
        }
        return studentsInRoom;
    }

    //destructive method
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

    //destructive method
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
    void moveStudentToRoom() {
        School s;

        for (Color c: Color.values()){
            s = getSchool(0, 7, 0, null);
            while (!s.hasStudentInEntrance(c))
                s = getSchool(0, 7, 0, null);

            int[] studentsInRoomInit = getStudentInRoom(s);
            s.moveStudentToRoom(c);
            int[] studentsInRoomFinal = getStudentInRoom(s);
            assertEquals(countStudent(studentsInRoomInit) + 1, countStudent(studentsInRoomFinal), "Test 1 - move a student");
        }

        s = getSchool(0, 7, 0, null);
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

        //todo, need Cloud class
    }

    @Test
    void addTowers() {
        School s = getSchool(8, 0, 0, null);
        assertEquals(8, s.getTowers());
        s.addTowers(2);
        assertEquals(10, s.getTowers());
    }

    @Test
    void removeTowers() {
        School s = getSchool(8, 0, 0, null);
        assertEquals(8, s.getTowers());
        s.removeTowers(2);
        assertEquals(6, s.getTowers());
    }

    @Test
    void moveStudentFromEntrance() {
        School s = getSchool(8, 7, 0, null);
        int i = 0;
        for (Color c: Color.values()){
            while (s.hasStudentInEntrance(c)) {
                s.moveStudentFromEntrance(c);
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
    void getSchool () {

        //testing only School parameter, other test has been done in player

        GameBoard board = null;
        Bag bag = new Bag(null);

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
        for (int i = 0; i < id2.length; i++)
            players.add(g.getPlayerById(id2[i]));

        for (Player p: players){
            assertEquals(0, countStudent(getStudentInRoom(p)), "Test 1 - no student in room initially");
            assertEquals(8, p.getTowers(), "Test 2 - towers number");
            assertEquals(7, countStudent(getStudentInEntrance_destructive(p)), "Test 3 - entrance student number");
        }

        g = new GameInitializer(0, 3);
        g.createAllGame(id3, null);
        players = new ArrayList<>();
        for (int i = 0; i < id3.length; i++)
            players.add(g.getPlayerById(id3[i]));

        for (Player p: players){
            assertEquals(0, countStudent(getStudentInRoom(p)), "Test 1 - no student in room initially");
            assertEquals(6, p.getTowers(), "Test 2 - towers number");
            assertEquals(9, countStudent(getStudentInEntrance_destructive(p)), "Test 3 - entrance student number");
        }

        //the fact that 2 player doesn't have the tower will be hide from the outside
        g = new GameInitializer(0, 4);
        g.createAllGame(id4, null);
        players = new ArrayList<>();
        for (int i = 0; i < id4.length; i++)
            players.add(g.getPlayerById(id4[i]));

        for (Player p: players){
            assertEquals(0, countStudent(getStudentInRoom(p)), "Test 1 - no student in room initially");
            assertEquals(8, p.getTowers(), "Test 2 - towers number");
            assertEquals(7, countStudent(getStudentInEntrance_destructive(p)), "Test 3 - entrance student number");
        }
    }
}