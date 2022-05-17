package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;

import java.io.*;

public class Cli implements Graphic{

    private final Console console = System.console();

    @Override
    public void displayMessage(String message) {
        this.console.writer().println(message);
    }

    @Override
    public Wizard getWizard() {
        Wizard w = null;
        while (w == null){
            String s = this.console.readLine();
            try {
                w = Wizard.valueOf(s);
            }catch (IllegalArgumentException e){
                w = null;
            }
        }
        return w;
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
