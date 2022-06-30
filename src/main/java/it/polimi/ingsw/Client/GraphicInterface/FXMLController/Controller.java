package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;

/**
 * Controllers abstract class for all the fxml scenes created
 */
public abstract class Controller {

    protected final Gui main;
    private final String resource;

    /**
     * Contructor
     * @param main the gui main application class
     * @param resource the url for the scene fxml file
     */
    public Controller(Gui main, String resource) {
        this.main = main;
        this.resource = resource;
    }

    /**
     * Getter
     * @return the url for the scene fxml file
     */
    public String getResource() {
        return resource;
    }
}
