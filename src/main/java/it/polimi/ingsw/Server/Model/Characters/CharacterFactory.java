package it.polimi.ingsw.Server.Model.Characters;

import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Server.Model.GameInitializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class CharacterFactory {
    //method to create a character
    protected static Character produceCharacterById(int CharacterId, GameInitializer gameInitializer){
        return switch (CharacterId) {
            case 0 -> new Apothecary(gameInitializer);
            case 1 -> new Bard(gameInitializer);
            case 2 -> new Cleric(gameInitializer);
            case 3 -> new Cook(gameInitializer);
            case 4 -> new Drunkard(gameInitializer);
            case 5 -> new Herald(gameInitializer);
            case 6 -> new Jester(gameInitializer);
            case 7 -> new Knight(gameInitializer);
            case 8 -> new Minotaur(gameInitializer);
            case 9 -> new Postman(gameInitializer);
            case 10 -> new Princess(gameInitializer);
            case 11 -> new Thief(gameInitializer);
            default -> null;
        };
    }

    protected static Character produceCharacterBySerializable(GameInitializer gameInitializer, CharacterSerializable character){
        return switch (character.getId()) {
            case 0 -> new Apothecary(gameInitializer, character);
            case 1 -> new Bard(gameInitializer, character);
            case 2 -> new Cleric(gameInitializer, character);
            case 3 -> new Cook(gameInitializer, character);
            case 4 -> new Drunkard(gameInitializer, character);
            case 5 -> new Herald(gameInitializer, character);
            case 6 -> new Jester(gameInitializer, character);
            case 7 -> new Knight(gameInitializer, character);
            case 8 -> new Minotaur(gameInitializer, character);
            case 9 -> new Postman(gameInitializer, character);
            case 10 -> new Princess(gameInitializer, character);
            case 11 -> new Thief(gameInitializer, character);
            default -> null;
        };
    }

    // return a collection of 3 characters associated to the game
    public static Collection<Character> getNewGameDeck(GameInitializer gameInitializer) {
        Random rand = new Random(System.currentTimeMillis()); //instance of random class
        Collection<Character> c = new ArrayList<>(3);
        List<Integer> pickedNumbers = new ArrayList<>();

        int upperbound = 12;

        for (int i = 0; i < 3; i++) {
            int pickedCard;
            do {
                pickedCard = rand.nextInt(upperbound);
            } while (pickedNumbers.contains(pickedCard));
            pickedNumbers.add(pickedCard);
            c.add(produceCharacterById(pickedCard, gameInitializer));
        }
        return c;
    }

    public static Collection<Character> getResumedDeck(GameInitializer gInit, List<CharacterSerializable> characters) {
        Collection<Character> c = new ArrayList<>(3);

        for (CharacterSerializable cS: characters){
            c.add(produceCharacterBySerializable(gInit, cS));
        }

        return c;
    }
}