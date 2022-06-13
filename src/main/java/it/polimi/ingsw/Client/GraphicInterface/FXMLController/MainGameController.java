package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Server.Model.Cloud;
import it.polimi.ingsw.Server.Model.Island;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private Label description3;
    @FXML
    private Rectangle rectangle4;
    @FXML
    private Rectangle color4;
    @FXML
    private Label username4;
    @FXML
    private Label description4;

    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private Tab tab4;

    @FXML
    private Circle dot0;
    @FXML
    private Circle dot1;
    @FXML
    private Circle dot2;
    @FXML
    private Circle dot3;
    @FXML
    private Circle dot4;
    @FXML
    private Circle dot5;
    @FXML
    private Circle dot6;
    @FXML
    private Circle dot7;
    @FXML
    private Circle dot8;
    @FXML
    private Circle dot9;
    @FXML
    private Circle dot10;
    @FXML
    private Circle dot11;
    @FXML
    private Circle dot12;
    @FXML
    private Circle dot13;
    @FXML
    private Circle dot14;

    private int numOfPlayers;

    private Map<Integer, String> usernames;

    private List<Island> islands;

    private ArrayList<CloudSerializable> clouds;

    public void initialize(){
        try {
            this.numOfPlayers = this.dataCollector.getNumOfPlayers();
            setUsernames();
            disableUnused();
            glowCurrent();
            setClouds();
            setSchools();
            setIslands();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); //reset flag
        }
    }



    public void setUsernames() throws InterruptedException {
        this.usernames = dataCollector.getUsernames();
        username1.setText(usernames.get(0));
        tab1.setText(usernames.get(0));
        username2.setText(usernames.get(1));
        tab2.setText(usernames.get(1));
        if(this.dataCollector.getNumOfPlayers()>2){
            username3.setText(usernames.get(2));
            tab3.setText(usernames.get(2));
            if(this.dataCollector.getNumOfPlayers()>3){
                username4.setText(usernames.get(3));
                tab4.setText(usernames.get(3));
            }
        }
    }

    public void disableUnused() throws InterruptedException {
        if(numOfPlayers < 4){
            this.color4.setVisible(false);
            this.color4.setDisable(true);
            this.rectangle4.setVisible(false);
            this.username4.setVisible(false);
            this.description4.setVisible(false);
            this.tab4.setDisable(true);
        }
        if(numOfPlayers < 3){
            this.color3.setVisible(false);
            this.color3.setDisable(true);
            this.rectangle3.setVisible(false);
            this.username3.setVisible(false);
            this.description3.setVisible(false);
            this.tab3.setDisable(true);
        }

    }

    public void glowCurrent(){
        username1.setEffect(null);
        username2.setEffect(null);
        username3.setEffect(null);
        username4.setEffect(null);

        switch (this.dataCollector.getIdOfCurrentPlayer()){
            case 0: username1.setEffect(new DropShadow(10, Color.GOLD));
            case 1: username2.setEffect(new DropShadow(10, Color.GOLD));
            case 2: username3.setEffect(new DropShadow(10, Color.GOLD));
            case 3: username4.setEffect(new DropShadow(10, Color.GOLD));
        }
    }

    public void setSchools(){

    }

    public void setClouds(){
        this.clouds = (ArrayList<CloudSerializable>) this.dataCollector.getModel().getCloudList();
        Integer i = 0;
        int[] colors = clouds.get(0).getDrawnStudents();
        this.dot0.setFill(convertColor(colors[0]));
        //todo create circles in scenebuilder
        /*
        this.dot1.setFill(convertColor(colors[1]));
        this.dot2.setFill(convertColor(colors[2]));
        if(this.numOfPlayers == 3){
            this.dot3.setFill(convertColor(colors[3]));
        }

        colors = clouds.get(1).getDrawnStudents();
        this.dot4.setFill(convertColor(colors[0]));
        this.dot5.setFill(convertColor(colors[1]));
        this.dot6.setFill(convertColor(colors[2]));
        if(this.numOfPlayers == 3){
            this.dot7.setFill(convertColor(colors[3]));
        }

        colors = clouds.get(2).getDrawnStudents();
        this.dot8.setFill(convertColor(colors[0]));
        this.dot9.setFill(convertColor(colors[1]));
        this.dot10.setFill(convertColor(colors[2]));
        if(this.numOfPlayers == 3){
            this.dot11.setFill(convertColor(colors[3]));
        }

        if(this.numOfPlayers == 4){
            colors = clouds.get(2).getDrawnStudents();
            this.dot12.setFill(convertColor(colors[0]));
            this.dot13.setFill(convertColor(colors[1]));
            this.dot14.setFill(convertColor(colors[2]));
        }

         */



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
