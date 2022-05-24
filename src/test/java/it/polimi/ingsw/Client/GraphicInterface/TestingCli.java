package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;

import java.io.IOException;

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

    @Override
    public PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public MoveStudentMessage askStudentMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public MoveMotherNatureMessage askMNMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public ClientMessage askCloud(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public PlayCharacterMessage askCharacter(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public void stopInput() {

    }

    @Override
    public void displayModel(ModelMessage model) {
        new Cli().displayModel(model);
    }
}
