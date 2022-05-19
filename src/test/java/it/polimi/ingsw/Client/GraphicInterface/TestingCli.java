package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;

public class TestingCli implements Graphic {

    static int wI = 0;
    static int uI = 0;


    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public Wizard getWizard() {
        int value = wI;
        wI++;
        switch (value){
            case 0 -> {
                return Wizard.Sorcerer;
            }
            case 1 -> {
                return Wizard.King;
            }
            case 2 -> {
                return Wizard.Witch;
            }
            case 3 -> {
                return Wizard.Wise;
            }
            case 4 -> {
                wI = 0;
                return getWizard();
            }
        }
        return null;
    }

    @Override
    public String getUsername() {
        int value = uI;
        uI++;
        switch (value){
            case 0 -> {
                return "Test1";
            }
            case 1 -> {
                return "Test2";
            }
            case 2 -> {
                return "Test3";
            }
            case 3 -> {
                return "Test4";
            }
            case 4 -> {
                uI = 0;
                return getUsername();
            }
        }
        return null;
    }

    @Override
    public int getNumOfPLayer() {
        return 4;
    }

    @Override
    public int getGameMode() {
        return 1;
    }
}
