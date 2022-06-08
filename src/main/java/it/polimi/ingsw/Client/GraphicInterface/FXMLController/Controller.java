package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;

public abstract class Controller {

    protected final Gui main;
    private final String resource;

    public Controller(Gui main, String resource) {
        this.main = main;
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
