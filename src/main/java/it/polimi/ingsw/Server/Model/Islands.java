package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Enum.Color;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Islands implements Iterable<Island>{

    private final LinkedList<Island> islands;
    private Island motherNature;

    Islands (){
        this.islands = new LinkedList<>();

        Island island = new Island(0);
        this.islands.add(island);
        this.motherNature = island;

        for (int i = 1; i < 12; i++){ //the 0 is the mn island
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

    public Islands(List<Island> islandList, int motherNatureIslandId) {
        this.islands = new LinkedList<>();

        for (Island i: islandList){
            if (i.getId() == motherNatureIslandId)
                this.motherNature = i;
            this.islands.add(i);
        }

        assert this.motherNature != null;
    }

    public int getIslandsNumber (){
        return this.islands.size();
    }

    public Island getMotherNature() {
        return motherNature;
    }

    public boolean existsIsland(int destinationId) {
        boolean found = false;
        for (Island i : islands)
            if (i.getId() == destinationId){
                found = true;
                break;
            }
        return found;
    }

    public Island getIslandFromId(int id){
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
       for (int i = 0; i < position; i++)
           this.motherNature = getNext(this.motherNature);
    }

    private Island getPrevious (Island x){
        int index = islands.indexOf(x);
        if (index == 0)
            return islands.peekLast();
        else
            return islands.get(index - 1);
    }

    private Island getNext (Island x){
        int index = islands.indexOf(x);
        if (index == (islands.size() - 1))
            return islands.peekFirst();
        else
            return islands.get(index + 1);
    }

    void aggregateIsland(Island currIsland){
        if (!existsIsland(currIsland.getId())){
            System.out.println("Error the island doesn't exist");
            return;
        }

        Island prev = getPrevious(currIsland);
        Island next = getNext(currIsland);

        if (currIsland.getTowerColor() == prev.getTowerColor() && prev.getTowerCount() != 0) {
            mergeIslands(currIsland, prev);
            islands.remove(prev);
        }
        if (currIsland.getTowerColor() == next.getTowerColor() && next.getTowerCount() != 0) {
            mergeIslands(currIsland, next);
            islands.remove(next);
        }
    }

    static private void mergeIslands (Island dest, Island src){
        if (dest.equals(src))
            System.out.println("MergeIsland: Error same src and destination");
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
        dest.setMerged(true);
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

    @Override
    public Iterator<Island> iterator() {
        return islands.iterator();
    }
}
