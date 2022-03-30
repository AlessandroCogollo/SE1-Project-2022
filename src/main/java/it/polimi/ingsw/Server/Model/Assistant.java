package it.polimi.ingsw.Server.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    private final int value;
    private final int maxMovement;

    Assistant (int value, int maxMovement){
        this.value = value;
        this.maxMovement = maxMovement;
    }

    public int getValue() {
        return value;
    }

    public int getMaxMovement() {
        return maxMovement;
    }

    public static int getNumberOfAssistants (){
        return Assistant.values().length;
    }

    public static Assistant getAssistantByValue (int value){
        for (Assistant a: Assistant.values()){
            if (a.getValue() == value)
                return a;
        }
        return null;
    }

    public static Collection<Assistant> getNewAssistantDeck (){
        Collection<Assistant> c = new ArrayList<>(getNumberOfAssistants());
        Collections.addAll(c, Assistant.values());
        return c;
    }
}
