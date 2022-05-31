package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Interface of the client used for display everything needed for the game (error and other debug are write in the std output)
 */
public interface Graphic {

    void setFirst (boolean first);

    void setDone(boolean done);

    String askString (String askMessage) throws InterruptedException, IOException;

    void displayError (String errorMessage);

    void displayMessage (String message);



    /**
     * Ask the player to choose a valid wizard
     * @return the wizard chosen to the caller
     */
    Wizard getWizard() throws IOException, InterruptedException;

    /**
     * Ask the player to choose a username
     * @return the username chosen to the caller
     */
    String getUsername() throws IOException, InterruptedException;

    /**
     * Ask the player to choose the player number of the game, 2, 3 or 4
     * @return the number chosen
     */
    int getNumOfPlayers() throws IOException, InterruptedException;

    /**
     * Ask the player to choose the gamemode of the game: 0 normal, 1 advanced
     * @return the number chosen
     */
    int getGameMode() throws IOException, InterruptedException;


    /**
     * Stop any io method that is running throwing some exception
     */
    void stopInput();


    /**
     * Send the information about the model
     * @param model model to display or the update of it
     */
    void updateModel(ModelMessage model);
}
