package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Server.Model.Cloud;
import it.polimi.ingsw.Server.Model.Island;
import javafx.fxml.FXML;
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
        this.clouds = (ArrayList<CloudSerializable>) this.dataCollector.getModel().getCloudList();
        Integer i = 0;
        ArrayList<GridPane> grids = new ArrayList<>();
        grids.add(this.cloudGrid1);
        grids.add(this.cloudGrid2);
        grids.add(this.cloudGrid3);
        grids.add(this.cloudGrid4);
        int[] colors;
        Random rand = new Random(372);
        ArrayList<Image> colorImages = new ArrayList<>();
        colorImages.add(new Image("Raw/Animali/Struzzo.jpg"));
        colorImages.add(new Image("Raw/Animali/Volpe.jpg"));
        colorImages.add(new Image("Raw/Animali/Lucertola.jpg"));
        colorImages.add(new Image("Raw/Animali/Tartaruga.jpg"));
        colorImages.add(new Image("Raw/Animali/Ghepardo.jpg"));
        double height = this.cloudAnchor1.getHeight()/this.cloudGrid1.getRowCount();
        for(GridPane grid : grids){
            for(CloudSerializable cloud : clouds){
                colors = cloud.getDrawnStudents();
                for(int color : colors){
                    ImageView img = new ImageView(colorImages.get(color));
                    img.setFitHeight(height);
                    img.setFitWidth(height);
                    grid.getChildren().add(rand.nextInt(grid.getColumnCount()*grid.getRowCount()), img);
                    }
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
