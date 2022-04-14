package it.polimi.ingsw.Server.Model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

class Islands {
    private final LinkedList<Island> islands;
    private Island motherNature;

    Islands (){
        this.islands = new LinkedList<>();

        Island island = new Island(0);
        this.islands.add(island);
        this.motherNature = island;

        for (int i = 1; i < 12; i++){
            this.islands.add(new Island(i));
        }

        //set all students in island
        Color[] firstStudents = new Color[Color.getNumberOfColors() * 2];
        int i = 0;
        for (Color c : Color.values())
            firstStudents[i++] = c;
        for (Color c : Color.values())
            firstStudents[i++] = c;

        rand(firstStudents);

        i = 0;
        for (Island is : this.islands)
            if (is.getId() != this.islands.size() / 2 && is.getId() != 0)
                is.addStudent(firstStudents[i++]);
    }

    int getIslandsNumber (){
        return this.islands.size();
    }

    Island getMotherNature() {
        return motherNature;
    }

    boolean existsIsland(int destinationId) {
        boolean found = false;
        for (Island i : islands)
            if (i.getId() == destinationId){
                found = true;
                break;
            }
        return found;
    }

    Island getIslandFromId(int id){
        for (Island i : this.islands)
            if (i.getId() == id)
                return i;
        return null;
    }

    void addStudentToIsland(Color color, int id){
        Island island = getIslandFromId(id);
        island.addStudent(color);
    }

    void nextMotherNature(int position) {
        int size = islands.size();
        if (position >= size)
            position -= size;
        //get the iterator from motherNature
        Iterator<Island> iterator;
        if (motherNature.equals(islands.peekLast()))
            iterator = islands.listIterator(0);
        else
            iterator = islands.listIterator(islands.indexOf(motherNature) + 1);
        for (int i = 0; i < position; i++) {
            if (!iterator.hasNext())
                iterator = islands.listIterator(0);
            motherNature = iterator.next();
        }
    }

    void aggregateIsland(Island currIsland){
        if (!existsIsland(currIsland.getId())){
            System.out.println("Error the island doesn't exist");
            return;
        }

        //check previous
        Island temp;
        boolean special = false;
        ListIterator<Island> iterator = islands.listIterator(islands.indexOf(currIsland));
        if (currIsland.equals(islands.peekFirst())) {
            temp = islands.peekLast();
            special = true;
        }
        else
            temp = iterator.previous();

        if (currIsland.getTowerColor() == temp.getTowerColor()) {
            mergeIslands(currIsland, temp);
            if (special) {
                islands.remove(temp);
                iterator = islands.listIterator(islands.indexOf(currIsland));
            }
            else
                iterator.remove();
        }

        iterator.next(); //current island

        special = false;
        //check next island
        if (currIsland.equals(islands.peekLast())) {
            temp = islands.peekFirst();
            special = true;
        }
        else
            temp = iterator.next();

        if (currIsland.getTowerColor() == temp.getTowerColor()) {
            mergeIslands(currIsland, temp);
            if (special)
                islands.remove(temp);
            else
                iterator.remove();
        }
    }

    static private void mergeIslands (Island dest, Island src){
        //retrieve the pointer to the students of the islands
        int[] studentsSrc = src.getStudents();
        int[] studentsDest = dest.getStudents();

        //add all students from the src island that would be deleted to the dest island
        for (Color c: Color.values())
            studentsDest[c.getIndex()] += studentsSrc[c.getIndex()];

        int bancardCount = src.getBanCard() + dest.getBanCard(); //technically the dest island never have bancard

        for (int i = 0; i < bancardCount; i++)
            dest.setBanCard();

        dest.setTowerCount(src.getTowerCount() + dest.getTowerCount());
    }

    static private <T> void rand( T[] array) {
        // Creating object for Random class
        Random rd = new Random(System.currentTimeMillis());

        // Starting from the last element and swapping one by one.
        for (int i = array.length - 1; i > 0; i--) {

            // Pick a random index from 0 to i
            int j = rd.nextInt(i + 1);

            // Swap array[i] with the element at random index
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
