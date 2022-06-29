package it.polimi.ingsw.Client.GraphicInterface;

/**
 * Interface of the client Graphic
 */
public interface Graphic {
    /*
    A real Graphic class need to call all this method in the dataCollector class that he has created:
     - setUsername()
     - setWizard()
     (if is it first)
         - setGameMode()
         - setNumOfPlayers()

     and after the start of the game
      - setNextMove()

     All that in order to let the game go.
    */
    /**
     * Display a message from the main thread of Client asynchronously
     * @param message message to be displayed
     */
    void displayMessage (String message);

    /**
     * Display the error committed by one player
     * @param message the error message received from the server
     */
    void displayError (String message);

    /**
     * Stop any io method and the Graphic thread if is running
     */
    void stopGraphic();

    /**
     * Display the message and stop the graphic using the stopGraphic Method
     * @param message the message that will be displayed
     */
    void gameOver (String message);
}
