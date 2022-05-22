package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Server.Model.Island;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

//todo stop scanner after shutdown
public class Cli implements Graphic{

    private final BufferedReader input = new BufferedReader(new InputStreamReader(new InterruptibleInputStream(System.in)));
    private String userName = null;

    //private Wizard wizard = null;

    private String askString (@Nullable String askMessage, @Nullable String errorMessage) throws IOException, InterruptedException {
        if (askMessage != null){
            displayMessage(askMessage);
        }
        String s = null;
        boolean done = false;
        while (!done && !Thread.currentThread().isInterrupted()){
            s = this.input.readLine();
            if (s == null || s.isEmpty() || s.isBlank()){
                if (askMessage != null){
                    displayMessage(errorMessage);
                }
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return s;
    }

    private int askInteger (@Nullable String askMessage, @Nullable String errorMessage) throws IOException, InterruptedException {
        int temp = 0;
        boolean done = false;

        while (!done  && !Thread.currentThread().isInterrupted()){
            String s = askString(askMessage, errorMessage);
            try {
                temp = Integer.parseInt(s);
            }catch (NumberFormatException e){
                if (askMessage != null){
                    displayMessage(errorMessage);
                }
                continue;
            }
            done = true;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: Thread Interrupted");

        return temp;
    }

    @Override
    public void displayMessage(String message) {
        System.out.println("Cli: " + message);
    }

    @Override
    public Wizard getWizard() throws IOException, InterruptedException {
        Wizard w = null;
        while (w == null && !Thread.currentThread().isInterrupted()){

            String s = askString("Insert your Wizard", "Please insert a valid name of wizard");

            try {
                w = Wizard.valueOf(s);
            } catch (IllegalArgumentException e) {
                displayMessage("Please insert a valid name of wizard");
            }
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getWizard interrupted");

        return w;
    }

    @Override
    public String getUsername() throws IOException, InterruptedException {
        String user = askString("Insert your Username", "Insert a valid username");

        this.userName = user;

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getUsername interrupted");

        return user;
    }

    @Override
    public int getNumOfPLayer() throws IOException, InterruptedException {
        int numOfPlayer = -1;

        while (numOfPlayer == -1 && !Thread.currentThread().isInterrupted()){

            int temp = askInteger("Insert the number of player for this game", "Insert a valid number between 2 and 4");

            if (temp < 2 || temp > 4){
                displayMessage("Insert a valid number between 2 and 4");
                continue;
            }

            numOfPlayer = temp;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getPlayerNumber interrupted");

        return numOfPlayer;
    }

    @Override
    public int getGameMode() throws IOException, InterruptedException {
        int gameMode = -1;
        while (gameMode == -1 && !Thread.currentThread().isInterrupted()){

            int temp = askInteger("Insert the game mode for this game, 0 normal 1, advanced", "Insert a valid number between 0 and 1");

            if (temp < 0 || temp > 1){
                displayMessage("Insert a valid number between 0 and 1");
                continue;
            }

            gameMode = temp;
        }

        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: getGameMode interrupted");

        return gameMode;
    }

    @Override
    public PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException {
        displayModel(model);
        //cannot play character in planning phase

        List<Integer> right = Arrays.stream(displayAssistant(model, playerId)).boxed().toList();


        int choose = -1;
        while (!right.contains(choose) && !Thread.currentThread().isInterrupted()){
            choose = askInteger("Select your Assistant from the list above", "Please select a valid assistant");
        }


        if (Thread.currentThread().isInterrupted())
            throw new InterruptedException("Cli: askAssistant interrupted");

        return new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", choose);
    }

    private int[] displayAssistant (ModelMessage model, int playerId) {
        displayMessage(this.userName + ", this are your assistant");
        System.out.println();
        ModelMessage.PlayerSerializable actual = null;
        for (ModelMessage.PlayerSerializable pl : model.getPlayerList()){
            if (pl.getId() == playerId){
                actual = pl;
                break;
            }
        }
        //null pointer if no player in the game
        int[] ass = actual.getAssistantDeck();

        for (int a : ass){
            Assistant x = Assistant.getAssistantByValue(a);
            System.out.println("# "  + x.name() + ": Value " + x.getValue() + " Max Movement " + x.getMaxMovement());
        }
        System.out.println();
        return ass;
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

    @Override
    public void displayModel(ModelMessage model) {
        System.out.println();
        System.out.println();
        System.out.println("############################## Model ##############################");
        System.out.println();
        //print game information
        System.out.println("#################### ISLANDS ###################");
        //printing islands
        String towerColor = null;
        for(Island is : model.getIslandList()){
            if (is.getTowerColor() == -1) {
                towerColor = " ";
            }
            else if (is.getTowerColor() == 1){
                towerColor = " Black";
            }
            else if (is.getTowerColor() == 2){
                towerColor = " White";
            }
            else if (is.getTowerColor() == 3){
                towerColor = " Grey";
            }
            System.out.println(
                    "{ID: " + is.getId() +
                            " , STUDENTS: " + Arrays.toString(is.getStudents()) +
                            " , TOWERS: " + is.getTowerCount()  + towerColor +
                            " , BAN CARD: " + is.getBanCard() +
                            "}" +
                            (model.getMotherNatureIslandId() == is.getId() ? "  <-- MOTHER NATURE" : ""));

        }
        System.out.println("#####################################################################");
        System.out.println("###### PROFESSORS ######");
        System.out.println(Arrays.toString(model.getProfessorsList()));
        System.out.println("########################");
        System.out.println("###### BAG ######");
        System.out.println("Students in bag: " + Arrays.stream(model.getBag()).sum()  + " -> " + Arrays.toString(model.getBag()));
        System.out.println("########################");

        System.out.println("###### SCHOOL ######");
        for (ModelMessage.PlayerSerializable pl : model.getPlayerList()){
            int tColor = pl.getTowerColor();
            if (tColor == -1) {
                towerColor = " ";
            }
            else if (tColor == 1){
                towerColor = " Black";
            }
            else if (tColor == 2){
                towerColor = " White";
            }
            else if (tColor == 3){
                towerColor = " Grey";
            }
            System.out.println("#" + pl.getId() + " with " + pl.getSchool().getTowers() + towerColor + " towers");

            System.out.println("# ENTRANCE -> " + Arrays.toString(pl.getSchool().getCopyOfEntrance()));
            System.out.println("# ROOM -> " + Arrays.toString(pl.getSchool().getCopyOfRoom()));
        }
        System.out.println("###########################################################################");


        System.out.println();
        System.out.println();
    }
}
