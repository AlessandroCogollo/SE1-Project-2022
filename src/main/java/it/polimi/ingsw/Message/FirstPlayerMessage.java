package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;

public class FirstPlayerMessage extends Message{

    private final String username;
    private final Wizard wizard;
    private final int numOfPlayer;
    private final int gameMode;

    public FirstPlayerMessage (String message, String username, Wizard wizard, int numOfPlayer, int gameMode) {
        super(Errors.FIRST_CLIENT, message);
        this.username = username;
        this.wizard = wizard;
        this.numOfPlayer = numOfPlayer;
        this.gameMode = gameMode;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public int getGameMode() {
        return gameMode;
    }

    public String getUsername() {
        return username;
    }
}
