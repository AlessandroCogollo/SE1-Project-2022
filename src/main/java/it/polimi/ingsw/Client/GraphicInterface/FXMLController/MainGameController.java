package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Message.ClientMessage;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Server.Model.Bag;
import it.polimi.ingsw.Server.Model.Characters.*;
import it.polimi.ingsw.Server.Model.Characters.Character;
import it.polimi.ingsw.Server.Model.Cloud;
import it.polimi.ingsw.Server.Model.Island;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainGameController extends Controller{

    private final DataCollector dataCollector;
    public MainGameController(Gui main, String resource) {
        super(main, resource);
        this.dataCollector = Gui.getDataCollector();
    }

    @FXML
    private Label username1;
    @FXML
    private Label username2;
    @FXML
    private Rectangle rectangle3;
    @FXML
    private Rectangle color3;
    @FXML
    private Label username3;
    @FXML
    private Rectangle rectangle4;
    @FXML
    private Rectangle color4;
    @FXML
    private Label username4;

    @FXML
    private Tab tab1;

    @FXML
    private GridPane gridRoom1;

    @FXML
    private GridPane gridTowers1;

    @FXML
    private GridPane gridProfessors1;

    @FXML
    private GridPane gridEntrance1;

    @FXML
    private Tab tab2;

    @FXML
    private GridPane gridRoom2;

    @FXML
    private GridPane gridProfessors2;

    @FXML
    private GridPane gridTowers2;

    @FXML
    private GridPane gridEntrance2;

    @FXML
    private Tab tab3;

    @FXML
    private GridPane gridRoom3;

    @FXML
    private GridPane gridProfessors3;

    @FXML
    private GridPane gridTowers3;

    @FXML
    private GridPane gridEntrance3;

    @FXML
    private Tab tab4;

    @FXML
    private GridPane gridRoom4;

    @FXML
    private GridPane gridProfessors4;

    @FXML
    private GridPane gridTowers4;

    @FXML
    private GridPane gridEntrance4;

    @FXML
    private ImageView lion;

    @FXML
    private ImageView dog;

    @FXML
    private ImageView octopus;

    @FXML
    private ImageView snake;

    @FXML
    private ImageView fox;

    @FXML
    private ImageView eagle;

    @FXML
    private ImageView cat;

    @FXML
    private ImageView goose;

    @FXML
    private ImageView turtle;

    @FXML
    private ImageView elephant;

    @FXML
    private Tab charactersTab;

    @FXML
    private ImageView cook;

    @FXML
    private ImageView knight;

    @FXML
    private ImageView jester;

    @FXML
    private ImageView minotaur;

    @FXML
    private ImageView apothecary;

    @FXML
    private ImageView postman;

    @FXML
    private ImageView herald;

    @FXML
    private ImageView cleric;

    @FXML
    private ImageView drunkard;

    @FXML
    private ImageView thief;

    @FXML
    private ImageView princess;

    @FXML
    private ImageView bard;

    @FXML
    private AnchorPane cloudAnchor1;
    @FXML
    private GridPane cloudGrid1;
    @FXML
    private AnchorPane cloudAnchor3;
    @FXML
    private GridPane cloudGrid3;
    @FXML
    private AnchorPane cloudAnchor4;
    @FXML
    private GridPane cloudGrid4;
    @FXML
    private AnchorPane cloudAnchor2;
    @FXML
    private GridPane cloudGrid2;

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

    @FXML
    private Rectangle coinsRectangle;
    @FXML
    private Label coinsLabel;
    @FXML
    private Label coinsNumberLabel;
    @FXML
    private ImageView coinsImage;

    @FXML
    private Label stepsLabel;
    @FXML
    private Label playedCharacterLabel;

    @FXML
    private Label leftYellow;
    @FXML
    private Label leftGreen;
    @FXML
    private Label leftPink;
    @FXML
    private Label leftBlue;
    @FXML
    private Label leftRed;



    private int numOfPlayers;

    private Map<Integer, String> usernames;

    private List<Island> islands;

    private ArrayList<CloudSerializable> clouds;

    public void initialize(){
        try {
            this.numOfPlayers = this.dataCollector.getNumOfPlayers();
            setUsernames();
            disableUnused();
            disableIfDreamMode();
            glowCurrent();
            setClouds();
            setSchools();
            setIslands();
            setGameStatus();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); //reset flag
        }

        elaborateModel();
    }

    public void disableIfDreamMode(){
        try {
            int gameMode = dataCollector.getGameMode();
            if(gameMode == 0){
                this.coinsRectangle.setVisible(false);
                this.coinsImage.setVisible(false);
                this.coinsImage.setDisable(true);
                this.coinsLabel.setVisible(false);
                this.coinsNumberLabel.setVisible(false);
                this.charactersTab.setDisable(true);
            }
            else{
                this.coinsNumberLabel.setText(String.valueOf(dataCollector.getModel().getPlayerById(dataCollector.getId()).getCoins()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setGameStatus(){
        String text = null;
        switch(dataCollector.getModel().getActiveCharacterId()){
            case 0 -> text = "Apothecary";
            case 1 -> text = "Bard";
            case 2 -> text = "Cleric";
            case 3 -> text = "Cook";
            case 4 -> text = "Drunkard";
            case 5 -> text = "Herald";
            case 6 -> text = "Jester";
            case 7 -> text = "Knight";
            case 8 -> text = "Minotaur";
            case 9 -> text = "Postman";
            case 10 -> text = "Princess";
            case 11 -> text = "Thief";
            default -> text = "No active character";
        }
        playedCharacterLabel.setText(text);
        int[] bag = dataCollector.getModel().getBag();
        leftBlue.setText(String.valueOf(bag[0]));
        leftGreen.setText(String.valueOf(bag[1]));
        leftPink.setText(String.valueOf(bag[2]));
        leftRed.setText(String.valueOf(bag[3]));
        leftYellow.setText(String.valueOf(bag[4]));

    }


    public void setUsernames() throws InterruptedException {
        int gameMode = dataCollector.getGameMode();
        this.usernames = dataCollector.getUsernames();
        String text = "";
        username1.setText(usernames.get(0));
        tab1.setText(usernames.get(0));
        Assistant a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(0).getActiveAssistant());
        if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(0).getActiveAssistant()).getMaxMovement());
        label1.setText("Max moves: " + text + (gameMode == 0 ? "" : ", Coins: " + dataCollector.getModel().getPlayerById(0).getCoins()));
        text = "";
        username2.setText(usernames.get(1));
        tab2.setText(usernames.get(1));
        a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(1).getActiveAssistant());
        if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(1).getActiveAssistant()).getMaxMovement());
        label2.setText("Max moves: " + text + (gameMode == 0 ? "" : ", Coins: " + dataCollector.getModel().getPlayerById(1).getCoins()));
        text = "";
        if(this.dataCollector.getNumOfPlayers()>2){
            username3.setText(usernames.get(2));
            tab3.setText(usernames.get(2));
            a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(2).getActiveAssistant());
            if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(2).getActiveAssistant()).getMaxMovement());
            label3.setText("Max moves: " + text + (gameMode == 0 ? "" : ", Coins: " + dataCollector.getModel().getPlayerById(2).getCoins()));
            text = "";
            if(this.dataCollector.getNumOfPlayers()>3){
                username4.setText(usernames.get(3));
                tab4.setText(usernames.get(3));
                a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(3).getActiveAssistant());
                if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(3).getActiveAssistant()).getMaxMovement());
                label4.setText("Max moves: " + text + (gameMode == 0 ? "" : ", Coins: " + dataCollector.getModel().getPlayerById(3).getCoins()));
            }
        }
    }

    public void disableUnused() throws InterruptedException {
        if(numOfPlayers < 4){
            this.color4.setVisible(false);
            this.color4.setDisable(true);
            this.rectangle4.setVisible(false);
            this.username4.setVisible(false);
            this.label4.setVisible(false);
            this.tab4.setDisable(true);
        }
        if(numOfPlayers < 3){
            this.color3.setVisible(false);
            this.color3.setDisable(true);
            this.rectangle3.setVisible(false);
            this.username3.setVisible(false);
            this.label3.setVisible(false);
            this.tab3.setDisable(true);
        }

    }

    public void glowCurrent(){

        int curr = this.dataCollector.getIdOfCurrentPlayer();
        System.out.println("Current is " + curr);

        ArrayList<Label> usernameLabels = new ArrayList<>();
        usernameLabels.add(username1);
        usernameLabels.add(username2);
        usernameLabels.add(username3);
        usernameLabels.add(username4);
        for(Label label : usernameLabels){
            if (label.getText().equals(dataCollector.getUsernameOfCurrentPlayer())){
                label.setEffect(new DropShadow(10, Color.GOLD));
            }
            else label.setEffect(null);
        }
    }

    //todo test when playing will be possible
    public void setSchools(){
        setProfessors();
        setRooms();
        setEntrance();
    }

    public void setProfessors(){
        int[] professors = dataCollector.getModel().getProfessorsList();
        ArrayList<GridPane> professorsGrids = new ArrayList<>();
        professorsGrids.add(gridProfessors1);
        professorsGrids.add(gridProfessors2);
        professorsGrids.add(gridProfessors3);
        professorsGrids.add(gridProfessors4);
        double height = this.gridProfessors1.getPrefHeight()/4;
        for(int i=0; i< professors.length; i++){
            Circle c = new Circle(height);
            c.setFill(convertColor(i));
            if(professors[i] != -1) professorsGrids.get(professors[i]).add(c, i, 0);
        }
    }

    public void setRooms(){
        int[] room = null;
        ArrayList<GridPane> roomGrids = new ArrayList<>();
        roomGrids.add(gridRoom1);
        roomGrids.add(gridRoom2);
        roomGrids.add(gridRoom3);
        roomGrids.add(gridRoom4);
        double height = this.gridRoom1.getPrefHeight()/40;
        for(int id=0; id<this.numOfPlayers; id++){
            room = dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfRoom();
            for(int color=0; color < room.length; color++){
                for(int students = 0; students < room[color]; students++){
                    Circle c = new Circle(height);
                    c.setFill(convertColor(color));
                    roomGrids.get(id).add(c, color, students);
                }
            }
        }
    }

    public void setEntrance(){
        int[] entrance = null;
        ArrayList<GridPane> entranceGrids = new ArrayList<>();
        entranceGrids.add(gridEntrance1);
        entranceGrids.add(gridEntrance2);
        entranceGrids.add(gridEntrance3);
        entranceGrids.add(gridEntrance4);
        double height = this.gridEntrance1.getPrefHeight()/8;
        for(int id = 0; id<this.numOfPlayers; id++){
            entrance = dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfEntrance();
            int row = 0;
            int column = 0;
            for(int color=0; color < entrance.length; color++){
                for(int student =0; student < entrance[color]; student++){
                    Circle c = new Circle(height);
                    c.setFill(convertColor(color));
                    entranceGrids.get(id).add(c, column, row);
                    column++;
                    if(column > 4){
                        column = 0;
                        row ++;
                    }
                    if(row > 1){
                        System.out.println("Entrance space exceeded!");
                    }
                }

            }
        }
    }

    public void setTowers(){
        ArrayList<GridPane> towerGrids = new ArrayList<>();
        towerGrids.add(gridTowers1);
        towerGrids.add(gridTowers2);
        towerGrids.add(gridTowers3);
        towerGrids.add(gridTowers4);
        double height = this.gridTowers1.getPrefHeight()/8;
        for(int id=0; id<this.numOfPlayers; id++){
            int towers = dataCollector.getModel().getPlayerById(id).getSchool().getTowers();
            int row = 0;
            int column = 0;
            for(int tower = 0; tower<towers; tower++){
                Circle c = new Circle(height);
                c.setFill(convertTowerColor(dataCollector.getModel().getPlayerById(0).getSchool().getTowers()));
                towerGrids.get(id).add(c, column, row);
                column++;
                if(column > 3){
                    column = 0;
                    row ++;
                }
                if(row > 1){
                    System.out.println("Tower space exceeded!");
                }
            }
        }

    }

    public Color convertTowerColor(int id){
        switch (id){
            case 0: return Color.BLACK;
            case 1: return Color.WHITE;
            case 2: return Color.GREY;
            default: return Color.GOLD;
        }
    }

    public void setClouds(){
        int num = 0;
        try {
            num = this.dataCollector.getNumOfPlayers();
            System.out.println("Num of players: " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(num < 4){
            cloudAnchor4.setDisable(true);
            cloudAnchor4.setVisible(false);
            cloudGrid4.setDisable(true);
            cloudGrid4.setVisible(false);
            if(num < 3){
                cloudAnchor3.setDisable(true);
                cloudAnchor3.setVisible(false);
                cloudGrid3.setDisable(true);
                cloudGrid3.setVisible(false);
            }
        }
        this.clouds = (ArrayList<CloudSerializable>) this.dataCollector.getModel().getCloudList();
        Integer i = 0;
        ArrayList<GridPane> grids = new ArrayList<>();
        grids.add(this.cloudGrid1);
        grids.add(this.cloudGrid2);
        grids.add(this.cloudGrid3);
        grids.add(this.cloudGrid4);
        int[] colors;
        Random rand = new Random(372);
        double height = this.cloudGrid1.getPrefHeight()/this.cloudGrid1.getRowCount();
        height = height/4;
        System.out.println("Height = " + height);
        for(int cloud = 0; cloud< clouds.size(); cloud++){
                colors = clouds.get(cloud).getDrawnStudents();
                for(int color : colors){
                    int column = rand.nextInt(grids.get(cloud).getColumnCount());
                    int row = rand.nextInt(grids.get(cloud).getRowCount());
                    Circle c = new Circle(height);
                    c.setFill(convertColor(color));
                    grids.get(cloud).add(c, column, row);
                    System.out.println("Added color[" + color + "] student in cloud " + cloud + " at column " + column + ", row " + row);
                }
            }
        }

    public Color convertColor(int id){
        switch (id){
            case 0: return Color.DEEPSKYBLUE;
            case 1: return Color.GREENYELLOW;
            case 2: return Color.PURPLE;
            case 3: return Color.RED;
            case 4: return Color.GOLD;
            default: return Color.PAPAYAWHIP;
        }
    }

    public void setIslands(){
        this.islands = this.dataCollector.getModel().getIslandList();
    }












    private void elaborateModel() {

        ModelMessage model = this.dataCollector.getModel();

        if(model.gameIsOver()){
            gameOver();
            return;
        }

        if (!this.dataCollector.isThisMyTurn()) {
            notYourTurn();
            return;
        }

        moveAsker(model);
    }

    private void moveAsker(ModelMessage model) {

        Phase p = Phase.valueOf(model.getActualPhase());
        ActionPhase aP = ActionPhase.valueOf(model.getActualActionPhase());

        if (Phase.Planning.equals(p)){
            activateAssistant();
        }
        else {
            if (model.getGameMode() == 1 && this.dataCollector.canPlayCharacter())
                activateCharacter();
            switch (aP) {
                case MoveStudent -> activateStudentsMove();
                case MoveMotherNature -> activateMotherNatureMove();
                case ChooseCloud -> activateCloud();
            }
        }
    }

    private void activateCloud() {
        //todo enable cloud click
        super.main.displayMessage("It is your turn, please click on the cloud you want to choose");
    }

    private void activateMotherNatureMove() {
        //todo enable island click
        super.main.displayMessage("It is your turn, please click on the island where you want to move Mother Nature");
    }

    private void activateStudentsMove() {
        //todo enable only the entrance, then the other message will popup after the player has choose the students
        super.main.displayMessage("It is your turn, please select a students from your entrance. Just click on It");
    }

    private void activateCharacter() {
        //todo enable character click
    }

    private void activateAssistant() {
        //todo enable character click
        super.main.displayMessage("It is your turn, please select an assistant. Just click on It");
    }

    private void notYourTurn() {

        //todo do it better

        super.main.displayMessage("It is not your turn, please wait");
    }

    private void gameOver() {
        String message = this.dataCollector.getStandardWinMessage(); //todo use a better one

        super.main.gameOver(message);
    }
}
