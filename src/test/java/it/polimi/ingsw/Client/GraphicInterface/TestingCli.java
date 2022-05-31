package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;

public class TestingCli extends GraphicV2 {

    static int wI = 0;
    static int uI = 0;


    @Override
    public void startGraphic() {

    }

    @Override
    public void setFirst(boolean first) {

    }

    @Override
    public void setDone(boolean done) {

    }

    @Override
    public String askString(String askMessage) {
        return null;
    }

    @Override
    public void displayError(String errorMessage) {

    }

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
    public int getNumOfPlayers(){
        return 4;
    }

    @Override
    public int getGameMode() {
        return 1;
    }

    @Override
    public void stopInput() {

    }

    @Override
    public void updateModel(ModelMessage model) {
        new Cli().updateModel(model);
    }
}
