package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;

import java.io.IOException;

/**
 * Interface of the client used for display everything needed for the game (error and other debug are write in the std output)
 */
public interface Graphic {

    /**
     * Display a generic message
     * @param message message to display
     */
    void displayMessage (String message);

    /**
     * Ask the player to choose a valid wizard
     * @return the wizard chosen to the caller
     */
    Wizard getWizard() throws IOException;

    /**
     * Ask the player to choose a username
     * @return the username chosen to the caller
     */
    String getUsername() throws IOException, InterruptedException;

    /**
     * Ask the player to choose the player number of the game, 2, 3 or 4
     * @return the number chosen
     */
    int getNumOfPLayer() throws IOException;

    /**
     * Ask the player to choose the gamemode of the game: 0 normal, 1 advanced
     * @return the number chosen
     */
    int getGameMode() throws IOException;

    /**
     * Display some information for help the player to choose, and ask him what types of Assistant he want to play
     * @param model model to take the information
     * @param playerId id of the current player
     * @return the move with the assistant chosen
     */
    PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException;

    /**
     * Display some information for help the player to choose, and ask him the movement of a student
     * @param model model to take the information
     * @param playerId id of the current player
     * @return the move with the student movement chosen
     */
    MoveStudentMessage askStudentMovement(ModelMessage model, int playerId) throws IOException, InterruptedException;

    /**
     * Display some information for help the player to choose, and ask him the movement of mother nature
     * @param model model to take the information
     * @param playerId id of the current player
     * @return the movement of mother nature
     */
    MoveMotherNatureMessage askMNMovement(ModelMessage model, int playerId) throws IOException, InterruptedException;

    /**
     * Display some information for help the player to choose, and ask him the cloud to take
     * @param model model to take the information
     * @param playerId id of the current player
     * @return the move with the cloud choosen
     */
    ClientMessage askCloud(ModelMessage model, int playerId) throws IOException, InterruptedException;

    /**
     * Method usable only during the player's action phase and could be call in the other actionPhase method
     * @param model model to take the information
     * @param playerId id of the current player
     * @return the move to play the character chosen
     */
    PlayCharacterMessage askCharacter (ModelMessage model, int playerId) throws IOException, InterruptedException;
}
