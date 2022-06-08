package it.polimi.ingsw.Message;

import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;

import java.util.Map;

public class LobbyInfoMessage extends Message{

    private final Map<Integer, String> usernames;
    private final Map<Integer, Wizard> wizards;
    private final int gameMode;
    private final int numOfPlayer;

    public LobbyInfoMessage(String message, Map<Integer, String> usernames, Map<Integer, Wizard> wizards, int gameMode, int numOfPlayer) {
        super(Errors.LOBBY_DATA, message);
        this.usernames = usernames;
        this.wizards = wizards;
        this.gameMode = gameMode;
        this.numOfPlayer = numOfPlayer;
    }

    public Map<Integer, String> getUsernames() {
        return usernames;
    }

    public Map<Integer, Wizard> getWizards() {
        return wizards;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }
}
