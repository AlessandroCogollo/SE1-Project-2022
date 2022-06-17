package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Server.Model.Characters.*;
import it.polimi.ingsw.Server.Model.Characters.Character;
import it.polimi.ingsw.Server.Model.Cloud;
import it.polimi.ingsw.Server.Model.Island;
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
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private Tab tab4;

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
    private Tab charactersTab;

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
    }

    public void disableIfDreamMode(){
        this.coinsRectangle.setVisible(false);
        this.coinsImage.setVisible(false);
        this.coinsImage.setDisable(true);
        this.coinsLabel.setVisible(false);
        this.coinsNumberLabel.setVisible(false);
        this.charactersTab.setDisable(true);
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

    }



    public void setUsernames() throws InterruptedException {
        int gamemode = dataCollector.getGameMode();
        this.usernames = dataCollector.getUsernames();
        String text = "";
        username1.setText(usernames.get(0));
        tab1.setText(usernames.get(0));
        Assistant a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(0).getActiveAssistant());
        if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(0).getActiveAssistant()).getMaxMovement());
        label1.setText("Max moves: " + text + ", Coins: " + (gamemode == 0 ? "" : dataCollector.getModel().getPlayerById(0).getCoins()));
        text = "";
        username2.setText(usernames.get(1));
        tab2.setText(usernames.get(1));
        a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(1).getActiveAssistant());
        if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(1).getActiveAssistant()).getMaxMovement());
        label2.setText("Max moves: " + text + ", Coins: " + (gamemode == 0 ? "" : dataCollector.getModel().getPlayerById(1).getCoins()));
        text = "";
        if(this.dataCollector.getNumOfPlayers()>2){
            username3.setText(usernames.get(2));
            tab3.setText(usernames.get(2));
            a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(2).getActiveAssistant());
            if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(2).getActiveAssistant()).getMaxMovement());
            label3.setText("Max moves: " + text + ", Coins: " + (gamemode == 0 ? "" : dataCollector.getModel().getPlayerById(2).getCoins()));
            text = "";
            if(this.dataCollector.getNumOfPlayers()>3){
                username4.setText(usernames.get(3));
                tab4.setText(usernames.get(3));
                a = Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(3).getActiveAssistant());
                if (a != null) text = String.valueOf(Assistant.getAssistantByValue(dataCollector.getModel().getPlayerById(3).getActiveAssistant()).getMaxMovement());
                label4.setText("Max moves: " + text + ", Coins: " + (gamemode == 0 ? "" : dataCollector.getModel().getPlayerById(3).getCoins()));
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

    public void setSchools(){

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
        }
        return Color.WHITE;
    }

    public void setIslands(){
        this.islands = this.dataCollector.getModel().getIslandList();
    }
}
