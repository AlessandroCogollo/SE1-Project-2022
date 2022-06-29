package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.GraphicInterface.Graphic;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.ClientMessage;
import it.polimi.ingsw.Message.LobbyInfoMessage;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data container to share the information between the back-end of client and the graphic interface.
 * It has also some method for pre-computing the data.
 * it can be accessed from both the graphic and the client side.
 */
public class DataCollector {

    private final Graphic gInstance;

    private Integer first = -1; //-1 initial case, 0 first, 1 not first
    private Runnable firstCallback = null;

    private Integer done = -1; //-1 initial or during verification/acquisition of data, 0 incorrect, 1 correct
    private String errorData = null; //error message sent from server when the data sent are not correct
    private Runnable doneCallback = null;


    private String username = null;
    private Wizard wizard = null;

    private Integer gameMode = -1;
    private Integer numOfPlayers = -1;
    private Map<Integer, String> usernames = null;
    private Map<Integer, Wizard> wizards = null;
    private Integer id = null;

    private final Object lock = new Object();

    private ModelMessage model = null;

    private Runnable callbackForModel = null;

    private ClientMessage nextMove = null;

    private Runnable graphicStopped = null;

    // Called by the Graphic

    /**
     * Constructor called by a Graphic class
     * @param gInstance the Graphic class that is creating this class
     */
    public DataCollector(Graphic gInstance) {
        this.gInstance = gInstance;
    }


    /**
     * Called by the Graphic class, get the value set by the main server, and if the value is not set, call the passed runnable with Platform.runLater() when set
     * @param whenChangedIfNotValid the runnable to be executed if the value requested is not in a valid state
     * @return the actual state of the value
     */
    public int getFirst(Runnable whenChangedIfNotValid) {
        if (whenChangedIfNotValid == null) {
            System.out.println("Warning: no runnable passed at getFirst");
        }

        if (first == -1 && whenChangedIfNotValid != null)
            firstCallback = whenChangedIfNotValid;
        return first;
    }

    /**
     * Called by the Graphic class, get the value set by the main server, and if the value is not set, call the passed runnable with Platform.runLater() when set
     * @param whenChangedIfNotValid the runnable to be executed if the value requested is not in a valid state
     * @return the actual state of the value
     */
    public int getDone(Runnable whenChangedIfNotValid) {
        if (whenChangedIfNotValid == null) {
            System.out.println("Warning: no runnable passed at getFirst");
        }
        if (done == -1 && whenChangedIfNotValid != null)
            doneCallback = whenChangedIfNotValid;
        if (done == 0) {
            this.done = -1;
            return 0;
        }
        return done;
    }

    /**
     * If the Info sent by the client to the server are not corrected, the server will send an error with this message that explained it
     * @return the string set by the main thread of client and reset the error to null
     */
    public String getErrorData() {
        String error = this.errorData;
        this.errorData = null;
        return error;
    }

    /**
     * Used after the game has started for set the message from the server if some player commit an error
     * @param errorData the string from the server
     */
    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    /**
     * Used from the graphic for set the callback to run when a model arrives
     * @param callbackForModel the callback to run when a model arrives
     */
    public void setCallbackForModel (Runnable callbackForModel) {
        this.callbackForModel = callbackForModel;
        System.out.println("CallbackForModel set");
    }

    /**
     * Get the model (usually used from graphic)
     * @return the model if it is set yet, null otherwise
     */
    public ModelMessage getModel() {
        return model;
    }

    /**
     * Get the map for username (usually used from graphic)
     * @return the map if it is set yet, null otherwise
     */
    public Map<Integer, String> getUsernames() {
        return usernames;
    }
    /**
     * Get the map for wizard (usually used from graphic)
     * @return the map if it is set yet, null otherwise
     */
    public Map<Integer, Wizard> getWizards() {
        return wizards;
    }

    /**
     * Set the next move that will be sent to server (usually used from graphic)
     * @param nextMove the move that will be sent to server
     */
    public void setNextMove(ClientMessage nextMove) {
        if (nextMove == null)
            return;
        synchronized (this.lock){
            this.nextMove = nextMove;
            this.lock.notifyAll();
        }
        System.out.println("Move Set");
    }

    /**
     * Get the current player Id (usually used from graphic)
     * @return the current player Id if it is set yet, null otherwise
     */
    public Integer getId() {
        return id;
    }

    /**
     * Call the callback set by the back-end of the client when the graphic stop, if it is set yet
     */
    public void graphicIsTerminated(){
        if (this.graphicStopped != null)
            new Thread(this.graphicStopped, "Graphic Stopper Callback Thread").start();
    }

    /**
     * Used by the graphic to set the username choose by the player
     * @param username the username choose
     */
    public void setUsername(String username) {
        synchronized (this.lock) {
            this.username = username;
            this.lock.notifyAll();
        }
    }
    /**
     * Used by the graphic to set the wizard choose by the player
     * @param wizard the wizard choose
     */
    public void setWizard(Wizard wizard) {
        synchronized (this.lock) {
            this.wizard = wizard;
            this.lock.notifyAll();
        }
    }
    /**
     * Used by the graphic to set the game mode choose by the player (if the first one)
     * @param gameMode the game mode choose
     */
    public void setGameMode(int gameMode) {
        synchronized (this.lock) {
            this.gameMode = gameMode;
            this.lock.notifyAll();
        }
    }
    /**
     * Used by the graphic to set the player number of the game choose by the player (if the first one)
     * @param numOfPlayers the player number of the game choose
     */
    public void setNumOfPlayers (int numOfPlayers) {
        synchronized (this.lock) {
            this.numOfPlayers = numOfPlayers;
            this.lock.notifyAll();
        }
    }





    // Called by the back-end client

    /**
     * Return the graphic instance that has created this dataCollector
     * @return the graphic instance that has created this dataCollector
     */
    public Graphic getGraphicInstance() {
        return gInstance;
    }

    /**
     * Used by the Client for set the info received from server, it call the callback set by the graphic if present
     * @param first if this client is the first one or not
     */
    public void setFirst (boolean first){
        if (first)
            this.first = 0;
        else
            this.first = 1;

        if (this.firstCallback != null){
            this.firstCallback.run();
        }
    }

    /**
     * Used by the Client for set the info received from server, it call the callback set by the graphic if present
     * @param done if the message sent by the client to the server during the connection setup are corrected
     * @param message if the message aren't corrected the error message from the server
     */
    public void setDone (boolean done, @Nullable String message){
        if (done) {
            this.done = 1;
            this.errorData = null;
        }
        else {
            this.done = 0;
            this.errorData = message;

            //resetting the value
            this.username = null;
            this.wizard = null;
            this.gameMode = -1;
            this.numOfPlayers = -1;
        }

        if (this.doneCallback != null){
            this.doneCallback.run();
        }
    }

    /**
     * Set the model received and call the callback if set
     * @param model the model received from the server
     */
    public void setModel(ModelMessage model) {

        if (model == null && this.model == null)
            return;

        if (model != null) {
            this.model = model;
        }

        if (this.callbackForModel != null)
            this.callbackForModel.run();
        else
            System.out.println("Callback Model null");
    }

    /**
     * set the map of usernames
     * @param usernames the map
     */
    public void setUsernames(Map<Integer, String> usernames) {
        this.usernames = usernames;
    }
    /**
     * set the map of wizard
     * @param wizards the map
     */
    public void setWizards(Map<Integer, Wizard> wizards) {
        this.wizards = wizards;
    }

    /**
     * The client wait until the graphic has set the next move
     * @return the move set by the graphic
     * @throws InterruptedException it the thread is interrupted
     */
    public ClientMessage askMove() throws InterruptedException {
        ClientMessage mess;
        synchronized (this.lock) {
            while (this.nextMove == null) {
                this.lock.wait();
            }
            mess = this.nextMove;
            this.nextMove = null;
        }

        return mess;
    }

    /**
     * set the callback that will be called if the graphic stop
     * @param graphicStopped the callback
     */
    public void setGraphicStopped(Runnable graphicStopped) {
        this.graphicStopped = graphicStopped;
    }

    /**
     * Used by the back-end client. It waits until the graphic set the username.
     * @return the username choose by the player
     * @throws InterruptedException if the thread is interrupted
     */
    public String getUsername() throws InterruptedException {
        synchronized (this.lock) {
            while (this.username == null) {
                this.lock.wait();
            }
        }
        return this.username;
    }
    /**
     * Used by the back-end client. It waits until the graphic set the wizard.
     * @return the wizard choose by the player
     * @throws InterruptedException if the thread is interrupted
     */
    public Wizard getWizard() throws InterruptedException {
        synchronized (this.lock) {
            while (this.wizard == null) {
                this.lock.wait();
            }
        }
        return this.wizard;
    }
    /**
     * Used by the back-end client. It waits until the graphic set the game mode.
     * @return the game mode choose by the player
     * @throws InterruptedException if the thread is interrupted
     */
    public int getGameMode() throws InterruptedException {
        synchronized (this.lock) {
            while (this.gameMode == -1) {
                this.lock.wait();
            }
        }
        return gameMode;
    }
    /**
     * Used by the back-end client. It waits until the graphic set the player number.
     * @return the player number choose by the player
     * @throws InterruptedException if the thread is interrupted
     */
    public int getNumOfPlayers() throws InterruptedException {
        synchronized (this.lock) {
            while (this.numOfPlayers == -1) {
                this.lock.wait();
            }
        }
        return this.numOfPlayers;
    }

    /**
     * Used by the back-end client for set the data received about all the other players and his id
     * @param gameData data from the server
     * @param id id of the player
     */
    public void setGameData(LobbyInfoMessage gameData, int id) {
        this.gameMode = gameData.getGameMode();
        this.numOfPlayers = gameData.getNumOfPlayer();
        this.usernames = gameData.getUsernames();
        this.wizards = gameData.getWizards();
        this.id = id;
    }




    //some usefully method that pre-compute some information

    /**
     * Valid only if the model has arrived. Otherwise it throws a NullPointerException.
     * @return if this player is the current player
     */
    public boolean isThisMyTurn() {
        return this.id == this.model.getCurrentPlayerId();
    }
    /**
     * Valid only if the model has arrived. Otherwise it throws a NullPointerException.
     * @return the id of the current player
     */
    public Integer getIdOfCurrentPlayer () {
        return this.model.getCurrentPlayerId();
    }

    /**
     * Valid only if the model has arrived. Otherwise it throws a NullPointerException.
     * @return the usernames of the current player
     */
    public String getUsernameOfCurrentPlayer () {
        return this.usernames.get(this.model.getCurrentPlayerId());
    }
    /**
     * Valid only if the model has arrived. Otherwise it throws a NullPointerException.
     * @return a standard win message also with the 4 player possibility (two player winner)
     */
    public String getStandardWinMessage() {
        if (!this.model.gameIsOver())
            throw new IllegalStateException("Cannot invoke this method if the game is not finished");

        int winningId = this.model.getWinnerId();

        if (this.numOfPlayers != 4){
            return "The player " + this.usernames.get(winningId) + " with id " + winningId + " has won the game, congratulation";
        }
        else {
            Set<Integer> ids = usernames.keySet();

            List<Integer> teamRed = new ArrayList<>(2);
            List<Integer> teamBlue = new ArrayList<>(2);

            int winnerTeam = -1; // 0 red, 1 blue

            for (Integer id : ids){
                if (id % 2 == 0)
                    teamRed.add(id);
                else
                    teamBlue.add(id);

                if (winningId == id){
                    if (id % 2 == 0)
                        winnerTeam = 0;
                    else
                        winnerTeam = 1;
                }
            }

            int id1, id2;

            if (winnerTeam == 0){
                id1 = teamRed.get(0);
                id2 = teamRed.get(1);
            }
            else if (winnerTeam == 1){
                id1 = teamBlue.get(0);
                id2 = teamBlue.get(1);
            }
            else{
                System.err.println("ERROR ids not valid");
                return null;
            }

            return "The players " + this.usernames.get(id1) + " and his team mate " + this.usernames.get(id2) + " have won the game, congratulation";

        }
    }
    /**
     * Valid only if the model has arrived. Otherwise it throws a NullPointerException.
     * @return true if this player could play a character
     */
    public boolean canPlayCharacter() {

        if (this.model == null || this.model.getGameMode() == 0 || this.model.getActiveCharacterId() != -1)
            return false;

        List<CharacterSerializable> list = model.getCharacterList();

        int playerCoins = model.getPlayerById(this.id).getCoins();

        if (playerCoins < 1)
            return false;

        boolean canPlayCharacter = false;

        for (CharacterSerializable c: list){
            int cost = (c.isUsed()) ? (c.getCost() + 1) : c.getCost();
            if (playerCoins >= cost){
                canPlayCharacter = true;
                break;
            }
        }

        return canPlayCharacter;
    }
}
