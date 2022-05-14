package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;

public interface Graphic {

    void displayMessage (String message);

    Wizard getWizard();

    String getUsername();

    int getNumOfPLayer();

    int getGameMode();
}
