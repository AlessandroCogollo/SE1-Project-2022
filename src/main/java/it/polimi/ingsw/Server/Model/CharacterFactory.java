package it.polimi.ingsw.Server.Model;

import java.util.*;

public class CharacterFactory {

    //method to create a character
    public static Character getCharacterById(int CharacterId){
        switch(CharacterId) {
            case 0:
                return new Apothecary();
            case 1:
                return new Bard();
            case 2:
                return new Cleric();
            case 3:
                return new Cook();
            case 4:
                return new Drunkard();
            case 5:
                return new Herald();
            case 6:
                return new Jester();
            case 7:
                return new Knight();
            case 8:
                return new Minotaur();
            case 9:
                return new Postman();
            case 10:
                return new Princess();
            case 11:
                return new Thief();
            default:
                return null;
        }
    }

    // return a collection of 3 characters associated to the game
    public static Collection<Character> getNewGameDeck() {
        Random rand = new Random(); //instance of random class
        Collection<Character> c = new ArrayList<>(12);
        List<Integer> pickedNumbers = new ArrayList<>(1);

        int upperbound = 12;

        for (int i=0; i<3; i++) {
            int pickedCard;
            do {
                pickedCard = rand.nextInt(upperbound);
            } while (pickedNumbers.contains(pickedCard));
            pickedNumbers.add(pickedCard);
            Collections.addAll(c, getCharacterById(pickedCard));
        }
        return c;
    }
}