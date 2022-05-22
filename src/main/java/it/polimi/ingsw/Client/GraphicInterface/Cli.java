package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
//todo stop scanner after shutdown
public class Cli implements Graphic{

    private final BufferedReader input = new BufferedReader(new InputStreamReader(new InterruptibleInputStream(System.in)));
    private String userName = null;

    //private Wizard wizard = null;

    @Override
    public void displayMessage(String message) {
        System.out.println("Cli: " + message);
    }

    @Override
    public Wizard getWizard() throws IOException {
        Wizard w = null;
        while (w == null && !Thread.currentThread().isInterrupted()){
            displayMessage("Insert your Wizard");
            String s = this.input.readLine();
            if (s == null || s.isEmpty() || s.isBlank()){
                displayMessage("Insert a valid username");
                continue;
            }
            try {
                w = Wizard.valueOf(s);
            } catch (IllegalArgumentException e) {
                displayMessage("Please insert a valid name of wizard");
            }
        }
        return w;
    }

    @Override
    public String getUsername() throws IOException {
        String user = null;
        while (user == null && !Thread.currentThread().isInterrupted()){
            displayMessage("Insert your Username");
            String s = this.input.readLine();

            if (s == null || s.isEmpty() || s.isBlank()){
                displayMessage("Insert a valid username");
                continue;
            }
            user = s;
        }
        this.userName = user;
        return user;
    }

    @Override
    public int getNumOfPLayer() throws IOException {
        int numOfPlayer = -1;
        while (numOfPlayer == -1 && !Thread.currentThread().isInterrupted()){
            displayMessage("Insert the number of player for this game");
            String s = this.input.readLine();

            if (s == null || s.isEmpty() || s.isBlank()){
                displayMessage("Insert a valid number between 2 and 4");
                continue;
            }

            int temp;
            try {
                temp = Integer.parseInt(s);
                if (temp < 2 || temp > 4)
                    throw new NumberFormatException();
            }catch (NumberFormatException e){
                displayMessage("Insert a valid number between 2 and 4");
                continue;
            }
            numOfPlayer = temp;
        }
        return numOfPlayer;
    }

    @Override
    public int getGameMode() throws IOException {
        int gameMode = -1;
        while (gameMode == -1 && !Thread.currentThread().isInterrupted()){
            String s = this.input.readLine();
            displayMessage("Insert the gamemode for this game, 0 normal 1, advanced");

            if (s == null || s.isEmpty() || s.isBlank()){
                displayMessage("Insert a valid number between 0 and 1");
                continue;
            }

            int temp;
            try {
                temp = Integer.parseInt(s);
                if (temp < 0 || temp > 1)
                    throw new NumberFormatException();
            }catch (NumberFormatException e){
                displayMessage("Insert a valid number between 0 and 1");
                continue;
            }
            gameMode = temp;
        }
        return gameMode;
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
        try {
            this.input.close();
        } catch (IOException e) {
            System.err.println("Cli: Error while closing the input stream");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
