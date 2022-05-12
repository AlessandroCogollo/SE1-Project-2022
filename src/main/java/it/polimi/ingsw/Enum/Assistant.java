package it.polimi.ingsw.Enum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Enum for the Assistant of the Game
 */
public enum Assistant {
    Lion (1, 1),
    Goose (2,1),
    Cat (3, 2),
    Eagle (4, 2),
    Fox (5, 3),
    Snake (6, 3),
    Octopus (7, 4),
    Dog (8, 4),
    Elephant (9, 5),
    Turtle (10, 5);

    /**
     * Value of the assistant, it is used during the Planning phase to choose the order of the player (before the lower value)
     */
    private final int value;
    /**
     * Max Movement that mother nature could do
     */
    private final int maxMovement;

    /**
     * Enum Constructor
     * @param value value
     * @param maxMovement maxMovement
     */
    Assistant (int value, int maxMovement){
        this.value = value;
        this.maxMovement = maxMovement;
    }

    /**
     *
     * @return the value of the Assistant
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return the max movement that the Assistant lets you do
     */
    public int getMaxMovement() {
        return maxMovement;
    }

    /**
     *
     * @return the number of Assistant
     */
    public static int getNumberOfAssistants (){
        return Assistant.values().length;
    }

    /**
     *
     * @param value the value of the assistant needed
     * @return the corresponding Assistant
     */
    public static Assistant getAssistantByValue (int value){
        for (Assistant a: Assistant.values()){
            if (a.getValue() == value)
                return a;
        }
        return null;
    }

    /**
     * Create a new full deck to use at the start of the game
     * @return a Collection with all the Assistant, one for each type
     */
    public static Collection<Assistant> getNewAssistantDeck (){
        Collection<Assistant> c = new ArrayList<>(getNumberOfAssistants());
        Collections.addAll(c, Assistant.values());
        return c;
    }
}
