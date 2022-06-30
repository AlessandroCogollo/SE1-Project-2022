package it.polimi.ingsw.Message.ModelMessage;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Server.Model.AdvancedPlayer;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.School;

/**
 * message that contains all the info of a player:
 * id, tower color, assistant deck, active assistant, school, coins.
 */
public class PlayerSerializable {
    private final int id;
    private final int towerColor;
    private final int[] assistantDeck;
    private final int activeAssistant;
    private final School school;
    private final int coins;

    PlayerSerializable(Player p, int mode) {
        this.id = p.getId();
        this.towerColor = p.getTowerColor();
        this.school = p.getSchool();
        if (p.getActiveAssistant() == null)
            this.activeAssistant = -1;
        else
            this.activeAssistant = p.getActiveAssistant().getValue();

        int i = 0;
        for (Assistant ignored : p)
            i++;
        this.assistantDeck = new int[i];
        i = 0;
        for (Assistant a : p)
            this.assistantDeck[i++] = a.getValue();
        if (mode == 0)
            this.coins = 0;
        else
            this.coins = ((AdvancedPlayer) p).getCoins();
    }

    public int getId() {
        return id;
    }

    public int getTowerColor() {
        return towerColor;
    }

    public int[] getAssistantDeck() {
        return assistantDeck;
    }

    public int getActiveAssistant() {
        return activeAssistant;
    }

    public School getSchool() {
        return school;
    }

    public int getCoins() {
        return coins;
    }
}
