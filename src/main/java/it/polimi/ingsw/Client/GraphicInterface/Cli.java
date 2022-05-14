package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;

public class Cli implements Graphic{
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public Wizard getWizard() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public int getNumOfPLayer() {
        return 0;
    }

    @Override
    public int getGameMode() {
        return 0;
    }
}
