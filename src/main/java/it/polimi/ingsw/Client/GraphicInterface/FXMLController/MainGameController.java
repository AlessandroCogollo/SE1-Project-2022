package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.PlayerSerializable;
import it.polimi.ingsw.Server.Model.Island;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.*;

/**
 * The controller for the main stage of the game, the stage is loaded only one time with the FXML loader and anything that cannot be updated in the future will be set only one time.
 * Then any time we call the update methods that check the new information from the model and updates the view
 */
public class MainGameController extends Controller implements Initializable {

    private final DataCollector dataCollector;
    private boolean set = false;
    public MainGameController(Gui main, String resource) {
        super(main, resource);
        this.dataCollector = Gui.getDataCollector();
    }





    /*public Color convertTowerColor(int id){
        return switch (id) {
            case 1 -> Color.BLACK;
            case 2 -> Color.WHITE;
            case 3 -> Color.GREY;
            default -> Color.TRANSPARENT;
        };
    }*/

    /*public Image convertDeckImage(Wizard wizard) {
       return LazyImageLoader.getInstance().getWizard(wizard);
    }*/

    /*public Color convertColor(int id){
        return switch (id) {
            case 0 -> Color.DEEPSKYBLUE;
            case 1 -> Color.PURPLE;
            case 2 -> Color.GOLD;
            case 3 -> Color.RED;
            case 4 -> Color.GREENYELLOW;
            default -> Color.TRANSPARENT;
        };
    }*/

    /**
     * Utility method for convert a color with his id to an image, use the lazy image loader
     * @param id the id of the color
     * @param size the size for the new image (the size of the tower will be width: size, height: size*2)
     * @return the image requested
     */
    public Image convertTo3DTowerColor(int id, double size){
        LazyImageLoader l = LazyImageLoader.getInstance();
        return switch (id) {
            case 1 -> l.getTower3DBlack(size);
            case 2 -> l.getTower3DWhite(size);
            case 3 -> l.getTower3DGrey(size);
            default -> null;
        };
    }
    /**
     * Utility method for convert a color with his id to an image, use the lazy image loader
     * @param id the id of the color
     * @param size the size for the new image
     * @return the image requested
     */
    public Image convertTo3DCircle(int id, double size) {
        LazyImageLoader l = LazyImageLoader.getInstance();
        return switch (id) {
            case 0 -> l.get3DCircleBlue(size);
            case 1 -> l.get3DCirclePink(size);
            case 2 -> l.get3DCircleYellow(size);
            case 3 -> l.get3DCircleRed(size);
            case 4 -> l.get3DCircleGreen(size);
            default -> null;
        };
    }

    /**
     * Utility method for convert a color with his id to an image, use the lazy image loader
     * @param id the id of the color
     * @param size the size for the new image
     * @return the image requested
     */
    public Image convertTo3DHexagon(int id, double size) {
        LazyImageLoader l = LazyImageLoader.getInstance();
        return switch (id) {
            case 0 -> l.get3DHexagonBlue(size);
            case 1 -> l.get3DHexagonPink(size);
            case 2 -> l.get3DHexagonYellow(size);
            case 3 -> l.get3DHexagonRed(size);
            case 4 -> l.get3DHexagonGreen(size);
            default -> null;
        };
    }

    /**
     * Utility method for convert a color with his id to an image, use the lazy image loader
     * @param id the id of the color
     * @param size the size for the new image
     * @return the image requested
     */
    public Image convertToCircle(int id, double size) {
        LazyImageLoader l = LazyImageLoader.getInstance();
        return switch (id) {
            case 0 -> l.getCircleBlue(size);
            case 1 -> l.getCirclePink(size);
            case 2 -> l.getCircleYellow(size);
            case 3 -> l.getCircleRed(size);
            case 4 -> l.getCircleGreen(size);
            default -> null;
        };
    }


    /**
     * initialize method of controller
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Scene s = super.main.getMainStage().getScene();
        //if (s != null)
        //s.setCursor(Cursor.WAIT);

        System.out.println("Initialize Started");

        setStage();

        System.out.println("Set stage");

        update();

        //if (s != null)
            //s.setCursor(Cursor.DEFAULT);
    }


    /**
     * Update all information in the stage
     */
    public void update(){

        System.out.println("Update Stage");

        updateGameStatus();
        updateUsernames();
        updateClouds();
        updateProfessors();
        updateRooms();
        updateEntrances();
        updateTowers();
        updateIslands();
        updateAssistants();
        updateCharacters();

        System.out.println("Elaborate Model");
        elaborateModel();
    }


    @FXML
    private Label messageLabel;
    private void displayMessage(String s){
        this.messageLabel.setText(s);
    }



    /*
        Data set to null after set stage finish
    */



    @FXML
    private Rectangle coinsRectangle;
    @FXML
    private Label coinsLabel;
    @FXML
    private ImageView coinsImage;
    @FXML
    private Label characterPlayedTitle;
    @FXML
    private ImageView colorBlue;
    @FXML
    private ImageView colorYellow;
    @FXML
    private ImageView colorGreen;
    @FXML
    private ImageView colorPink;
    @FXML
    private ImageView colorRed;
    @FXML
    private Label username1;
    @FXML
    private Label username2;
    @FXML
    private Label username3;
    @FXML
    private Label username4;
    @FXML
    private Rectangle icon1;
    @FXML
    private Rectangle icon2;
    @FXML
    private Rectangle icon3;
    @FXML
    private Rectangle icon4;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Rectangle rectangle1;
    @FXML
    private Rectangle rectangle2;
    @FXML
    private Rectangle rectangle3;
    @FXML
    private Rectangle rectangle4;
    @FXML
    private GridPane cloudGrid1;
    @FXML
    private GridPane cloudGrid2;
    @FXML
    private AnchorPane cloudAnchor3;
    @FXML
    private GridPane cloudGrid3;
    @FXML
    private AnchorPane cloudAnchor4;
    @FXML
    private GridPane cloudGrid4;
    @FXML
    private TabPane tabPaneSchools;
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
    private AnchorPane islandAnchor1;
    @FXML
    private GridPane islandGrid1;
    @FXML
    private AnchorPane islandAnchor2;
    @FXML
    private GridPane islandGrid2;
    @FXML
    private AnchorPane islandAnchor3;
    @FXML
    private GridPane islandGrid3;
    @FXML
    private AnchorPane islandAnchor4;
    @FXML
    private GridPane islandGrid4;
    @FXML
    private AnchorPane islandAnchor5;
    @FXML
    private GridPane islandGrid5;
    @FXML
    private AnchorPane islandAnchor6;
    @FXML
    private GridPane islandGrid6;
    @FXML
    private AnchorPane islandAnchor7;
    @FXML
    private GridPane islandGrid7;
    @FXML
    private AnchorPane islandAnchor8;
    @FXML
    private GridPane islandGrid8;
    @FXML
    private AnchorPane islandAnchor9;
    @FXML
    private GridPane islandGrid9;
    @FXML
    private AnchorPane islandAnchor10;
    @FXML
    private GridPane islandGrid10;
    @FXML
    private AnchorPane islandAnchor11;
    @FXML
    private GridPane islandGrid11;
    @FXML
    private AnchorPane islandAnchor12;
    @FXML
    private GridPane islandGrid12;
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
    private AnchorPane cleric;
    @FXML
    private ImageView clericCoin;
    @FXML
    private AnchorPane herald;
    @FXML
    private ImageView heraldCoin;
    @FXML
    private AnchorPane postman;
    @FXML
    private ImageView postmanCoin;
    @FXML
    private AnchorPane apothecary;
    @FXML
    private ImageView apothecaryCoin;
    @FXML
    private AnchorPane minotaur;
    @FXML
    private ImageView minotaurCoin;
    @FXML
    private AnchorPane jester;
    @FXML
    private ImageView jesterCoin;
    @FXML
    private AnchorPane knight;
    @FXML
    private ImageView knightCoin;
    @FXML
    private AnchorPane cook;
    @FXML
    private ImageView cookCoin;
    @FXML
    private AnchorPane bard;
    @FXML
    private ImageView bardCoin;
    @FXML
    private AnchorPane princess;
    @FXML
    private ImageView princessCoin;
    @FXML
    private AnchorPane thief;
    @FXML
    private ImageView thiefCoin;
    @FXML
    private AnchorPane drunkard;
    @FXML
    private ImageView drunkardCoin;
    @FXML
    private AnchorPane charactersPane;
    @FXML
    private Tab charactersTab;


    /**
     * Set anything in the stage that will not change and then set anything not needed to null so the gc could free some space.
     * Should be called only from the first initialize method (throws IllegalStateException).
     */
    public void setStage() {

        if (set)
            throw new IllegalStateException("The root is already set");

        set = true;

        ModelMessage model = dataCollector.getModel();
        int myId = dataCollector.getId();
        int gM = model.getGameMode();

        // Game Status


        if (gM == 0) {
            playedCharacterLabel.setVisible(false);
            playedCharacterLabel = null; //updated later
            coinsNumberLabel.setVisible(false);
            coinsNumberLabel = null; //updated later
            characterPlayedTitle.setVisible(false);
            coinsRectangle.setVisible(false);
            coinsImage.setVisible(false);
            coinsLabel.setVisible(false);
        }

        characterPlayedTitle = null;
        coinsRectangle = null;
        coinsImage = null;
        coinsLabel = null;

        colorBlue.setUserData(it.polimi.ingsw.Enum.Color.Blue);
        colorYellow.setUserData(it.polimi.ingsw.Enum.Color.Yellow);
        colorGreen.setUserData(it.polimi.ingsw.Enum.Color.Green);
        colorPink.setUserData(it.polimi.ingsw.Enum.Color.Purple);
        colorRed.setUserData(it.polimi.ingsw.Enum.Color.Red);

        this.colors = new ArrayList<>(5);
        this.colors.add(colorBlue);
        colorBlue = null;
        this.colors.add(colorYellow);
        colorYellow = null;
        this.colors.add(colorGreen);
        colorGreen = null;
        this.colors.add(colorPink);
        colorPink = null;
        this.colors.add(colorRed);
        colorRed = null;



        // Usernames

        int playerNumber = model.getPlayerNumber();
        Map<Integer, String> usernames = dataCollector.getUsernames();
        Map<Integer, Wizard> wizards = dataCollector.getWizards();

        List<Label> tempNames = new ArrayList<>(playerNumber);
        List<Rectangle> tempIcons = new ArrayList<>(playerNumber);
        List<Rectangle> tempRect = new ArrayList<>(playerNumber);
        List<Label> tempAssistantLabel = new ArrayList<>(playerNumber);

        switch (playerNumber){
            case 2 -> {
                this.icon4.setVisible(false);
                this.rectangle4.setVisible(false);
                this.username4.setVisible(false);
                this.label4.setVisible(false);
                this.icon3.setVisible(false);
                this.rectangle3.setVisible(false);
                this.username3.setVisible(false);
                this.label3.setVisible(false);

                tempNames.add(username1);
                tempNames.add(username2);

                tempIcons.add(icon1);
                tempIcons.add(icon2);

                tempRect.add(rectangle1);
                tempRect.add(rectangle2);

                tempAssistantLabel.add(label1);
                tempAssistantLabel.add(label2);
            }
            case 3 -> {
                this.icon4.setVisible(false);
                this.rectangle4.setVisible(false);
                this.username4.setVisible(false);
                this.label4.setVisible(false);

                tempNames.add(username1);
                tempNames.add(username2);
                tempNames.add(username3);

                tempIcons.add(icon1);
                tempIcons.add(icon2);
                tempIcons.add(icon3);

                tempRect.add(rectangle1);
                tempRect.add(rectangle2);
                tempRect.add(rectangle3);

                tempAssistantLabel.add(label1);
                tempAssistantLabel.add(label2);
                tempAssistantLabel.add(label3);
            }
            case 4 -> {
                tempNames.add(username1);
                tempNames.add(username2);
                tempNames.add(username3);
                tempNames.add(username4);

                tempIcons.add(icon1);
                tempIcons.add(icon2);
                tempIcons.add(icon3);
                tempIcons.add(icon4);

                tempRect.add(rectangle1);
                tempRect.add(rectangle2);
                tempRect.add(rectangle3);
                tempRect.add(rectangle4);

                tempAssistantLabel.add(label1);
                tempAssistantLabel.add(label2);
                tempAssistantLabel.add(label3);
                tempAssistantLabel.add(label4);
            }
        }

        this.usernames = new HashMap<>(playerNumber);
        this.assistantLabel = new HashMap<>(playerNumber);
        this.usernameRectangles = new HashMap<>(playerNumber);

        for (Integer i : usernames.keySet()){
            tempNames.get(i).setText(usernames.get(i));
            tempIcons.get(i).setFill(new ImagePattern(LazyImageLoader.getInstance().getWizardSquare(wizards.get(i))));

            this.usernames.put(i, tempNames.get(i));
            this.usernameRectangles.put(i, tempRect.get(i));
            this.assistantLabel.put(i, tempAssistantLabel.get(i));
        }

        if (playerNumber == 4){ // mate effect
            Set<Integer> ids = usernames.keySet();

            List<Integer> teamRed = new ArrayList<>(2);
            List<Integer> teamBlue = new ArrayList<>(2);

            for (Integer id : ids){
                if (id % 2 == 0)
                    teamRed.add(id);
                else
                    teamBlue.add(id);
            }

            if (teamBlue.size() != 2 || teamRed.size() != 2){
                System.out.println("Error id not valid");
                ids.forEach(System.out::println);
                return;
            }

            System.out.println("Team Blue");
            for (Integer id : teamBlue){
                System.out.print(this.usernames.get(id).getText() + " ");
                this.usernames.get(id).setTextFill(Color.BLUE);
            }
            System.out.println();
            System.out.println("Team Red");
            for (Integer id : teamRed){
                System.out.print(this.usernames.get(id).getText() + " ");
                this.usernames.get(id).setTextFill(Color.RED);
            }
            System.out.println();
        }
        username1 = null;
        username2 = null;
        username3 = null;
        username4 = null;
        icon1 = null;
        icon2 = null;
        icon3 = null;
        icon4 = null;
        label1 = null;
        label2 = null;
        label3 = null;
        label4 = null;
        rectangle1 = null;
        rectangle2 = null;
        rectangle3 = null;
        rectangle4 = null;






        // Clouds

        List<CloudSerializable> clouds =  model.getCloudList();
        List<GridPane> cloudGrids = new ArrayList<>(clouds.size());

        switch (clouds.size()){
            case 2 -> {
                cloudAnchor4.setVisible(false);
                cloudGrid4.setVisible(false);
                cloudAnchor3.setVisible(false);
                cloudGrid3.setVisible(false);

                cloudGrids.add(this.cloudGrid1);
                cloudGrids.add(this.cloudGrid2);
            }
            case 3 -> {
                cloudAnchor4.setVisible(false);
                cloudGrid4.setVisible(false);

                cloudGrids.add(this.cloudGrid1);
                cloudGrids.add(this.cloudGrid2);
                cloudGrids.add(this.cloudGrid3);
            }
            case 4 -> {
                cloudGrids.add(this.cloudGrid1);
                cloudGrids.add(this.cloudGrid2);
                cloudGrids.add(this.cloudGrid3);
                cloudGrids.add(this.cloudGrid4);
            }
        }

        this.usedClouds = cloudGrids;

        for(int i = 0; i < clouds.size(); i++){
            GridPane grid = cloudGrids.get(i);
            grid.setUserData(clouds.get(i));
        }
        cloudGrid1 = null;
        cloudGrid2 = null;
        cloudGrid3 = null;
        cloudGrid4 = null;
        cloudAnchor3 = null;
        cloudAnchor4 = null;






        // Schools

        List<Tab> tabs = new ArrayList<>(playerNumber);
        List<GridPane> professorsGrids = new ArrayList<>(playerNumber);
        List<GridPane> roomGrids = new ArrayList<>(playerNumber);
        List<GridPane> entranceGrids = new ArrayList<>(playerNumber);
        List<GridPane> towerGrids = new ArrayList<>(playerNumber);

        switch (playerNumber){
            case 2 -> {
                this.tab3.setDisable(true);
                this.tab4.setDisable(true);

                tabs.add(tab1);
                tabs.add(tab2);

                professorsGrids.add(gridProfessors1);
                professorsGrids.add(gridProfessors2);

                roomGrids.add(gridRoom1);
                roomGrids.add(gridRoom2);

                entranceGrids.add(gridEntrance1);
                entranceGrids.add(gridEntrance2);

                towerGrids.add(gridTowers1);
                towerGrids.add(gridTowers2);
            }
            case 3 -> {
                this.tab4.setDisable(true);

                tabs.add(tab1);
                tabs.add(tab2);
                tabs.add(tab3);

                professorsGrids.add(gridProfessors1);
                professorsGrids.add(gridProfessors2);
                professorsGrids.add(gridProfessors3);

                roomGrids.add(gridRoom1);
                roomGrids.add(gridRoom2);
                roomGrids.add(gridRoom3);

                entranceGrids.add(gridEntrance1);
                entranceGrids.add(gridEntrance2);
                entranceGrids.add(gridEntrance3);

                towerGrids.add(gridTowers1);
                towerGrids.add(gridTowers2);
                towerGrids.add(gridTowers3);
            }
            case 4 -> {
                tabs.add(tab1);
                tabs.add(tab2);
                tabs.add(tab3);
                tabs.add(tab4);

                professorsGrids.add(gridProfessors1);
                professorsGrids.add(gridProfessors2);
                professorsGrids.add(gridProfessors3);
                professorsGrids.add(gridProfessors4);

                roomGrids.add(gridRoom1);
                roomGrids.add(gridRoom2);
                roomGrids.add(gridRoom3);
                roomGrids.add(gridRoom4);

                entranceGrids.add(gridEntrance1);
                entranceGrids.add(gridEntrance2);
                entranceGrids.add(gridEntrance3);
                entranceGrids.add(gridEntrance4);

                towerGrids.add(gridTowers1);
                towerGrids.add(gridTowers2);
                towerGrids.add(gridTowers3);
                towerGrids.add(gridTowers4);
            }
        }

        this.professorGrids = new HashMap<>(playerNumber);
        this.roomGrids = new HashMap<>(playerNumber);
        this.entranceGrids = new HashMap<>(playerNumber);
        this.towerGrids = new HashMap<>(playerNumber);

        for (Integer i : usernames.keySet()){
            tabs.get(i).setText(usernames.get(i));
            if (i.equals(this.dataCollector.getId())) {
                Tab actualTab = tabs.get(i);
                this.tabPaneSchools.getSelectionModel().select(tabs.get(i));
            }
            this.professorGrids.put(i, professorsGrids.get(i));
            this.roomGrids.put(i, roomGrids.get(i));
            this.entranceGrids.put(i, entranceGrids.get(i));
            this.towerGrids.put(i, towerGrids.get(i));

            if (i == myId) {
                this.actualRoom = roomGrids.get(i);
            }
        }

        tabPaneSchools = null;
        tab1 = null;
        tab2 = null;
        tab3 = null;
        tab4 = null;
        gridRoom1 = null;
        gridRoom2 = null;
        gridRoom3 = null;
        gridRoom4 = null;
        gridTowers1 = null;
        gridTowers2 = null;
        gridTowers3 = null;
        gridTowers4 = null;
        gridProfessors1 = null;
        gridProfessors2 = null;
        gridProfessors3 = null;
        gridProfessors4 = null;
        gridEntrance1 = null;
        gridEntrance2 = null;
        gridEntrance3 = null;
        gridEntrance4 = null;



        //Islands
        this.activeIslandGrids = new HashMap<>(12);
        this.activeIslandGrids.put(0, islandGrid1);
        this.activeIslandGrids.put(1, islandGrid2);
        this.activeIslandGrids.put(2, islandGrid3);
        this.activeIslandGrids.put(3, islandGrid4);
        this.activeIslandGrids.put(4, islandGrid5);
        this.activeIslandGrids.put(5, islandGrid6);
        this.activeIslandGrids.put(6, islandGrid7);
        this.activeIslandGrids.put(7, islandGrid8);
        this.activeIslandGrids.put(8, islandGrid9);
        this.activeIslandGrids.put(9, islandGrid10);
        this.activeIslandGrids.put(10, islandGrid11);
        this.activeIslandGrids.put(11, islandGrid12);

        this.activeIslands =  new HashMap<>(12);
        this.activeIslands.put(0, islandAnchor1);
        this.activeIslands.put(1, islandAnchor2);
        this.activeIslands.put(2, islandAnchor3);
        this.activeIslands.put(3, islandAnchor4);
        this.activeIslands.put(4, islandAnchor5);
        this.activeIslands.put(5, islandAnchor6);
        this.activeIslands.put(6, islandAnchor7);
        this.activeIslands.put(7, islandAnchor8);
        this.activeIslands.put(8, islandAnchor9);
        this.activeIslands.put(9, islandAnchor10);
        this.activeIslands.put(10, islandAnchor11);
        this.activeIslands.put(11, islandAnchor12);

        islandAnchor1 = null;
        islandAnchor2 = null;
        islandAnchor3 = null;
        islandAnchor4 = null;
        islandAnchor5 = null;
        islandAnchor6 = null;
        islandAnchor7 = null;
        islandAnchor8 = null;
        islandAnchor9 = null;
        islandAnchor10 = null;
        islandAnchor11 = null;
        islandAnchor12 = null;
        islandGrid1 = null;
        islandGrid2 = null;
        islandGrid3 = null;
        islandGrid4 = null;
        islandGrid5 = null;
        islandGrid6 = null;
        islandGrid7 = null;
        islandGrid8 = null;
        islandGrid9 = null;
        islandGrid10 = null;
        islandGrid11 = null;
        islandGrid12 = null;





        //Assistant
        this.assistants = new HashMap<>(10);
        this.assistants.put(1, lion);
        this.assistants.put(2, goose);
        this.assistants.put(3, cat);
        this.assistants.put(4, eagle);
        this.assistants.put(5, fox);
        this.assistants.put(6, snake);
        this.assistants.put(7, octopus);
        this.assistants.put(8, dog);
        this.assistants.put(9, elephant);
        this.assistants.put(10, turtle);

        for (Integer value: this.assistants.keySet())
            this.assistants.get(value).setUserData(Assistant.getAssistantByValue(value));

        lion = null;
        goose = null;
        cat = null;
        eagle = null;
        fox = null;
        snake = null;
        octopus = null;
        dog = null;
        elephant = null;
        turtle = null;



        // Characters



        if (gM == 0) {
            this.charactersTab.setDisable(true);
            clericGrid = null;
            labelApothecary = null;
            jesterGrid = null;
            cookActiveColor = null;
            princessGrid = null;
        }
        else {

            List<AnchorPane> list = new ArrayList<>(12);
            list.add(apothecary);
            list.add(bard);
            list.add(cleric);
            list.add(cook);
            list.add(drunkard);
            list.add(herald);
            list.add(jester);
            list.add(knight);
            list.add(minotaur);
            list.add(postman);
            list.add(princess);
            list.add(thief);

            List<ImageView> coins = new ArrayList<>(12);
            coins.add(apothecaryCoin);
            coins.add(bardCoin);
            coins.add(clericCoin);
            coins.add(cookCoin);
            coins.add(drunkardCoin);
            coins.add(heraldCoin);
            coins.add(jesterCoin);
            coins.add(knightCoin);
            coins.add(minotaurCoin);
            coins.add(postmanCoin);
            coins.add(princessCoin);
            coins.add(thiefCoin);

            this.characters = new HashMap<>(3);
            this.charactersCoins = new HashMap<>(3);

            for (int i = 0; i < 12; i++){
                if (model.isCharacterIdValid(i)){
                    AnchorPane c = list.get(i);
                    ImageView cC = coins.get(i);

                    this.characters.put(i, c);
                    this.charactersCoins.put(i, cC);
                }
                else {

                    AnchorPane aP = list.get(i);

                    switch (i){
                        case 2 -> clericGrid = null;
                        case 0 -> labelApothecary = null;
                        case 6 -> jesterGrid = null;
                        case 3 -> cookActiveColor = null;
                        case 10 -> princessGrid = null;
                    }

                    charactersPane.getChildren().remove(aP);
                    //c.setVisible(false);
                }

            }
        }

        charactersPane = null;
        charactersTab = null;
        cleric = null;
        clericCoin = null;
        herald = null;
        heraldCoin = null;
        postman = null;
        postmanCoin = null;
        apothecary = null;
        apothecaryCoin = null;
        minotaur = null;
        minotaurCoin = null;
        jester = null;
        jesterCoin = null;
        knight = null;
        knightCoin = null;
        cook = null;
        cookCoin = null;
        bard = null;
        bardCoin = null;
        princess = null;
        princessCoin = null;
        thief = null;
        thiefCoin = null;
        drunkard = null;
        drunkardCoin = null;
    }

    /*
        Update Methods
     */


    @FXML
    private Label stepsLabel;
    @FXML
    private Label phaseLabel;
    @FXML
    private Label coinsNumberLabel;
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

    private void updateGameStatus() {

        ModelMessage model = dataCollector.getModel();
        int myId = dataCollector.getId();

        //character status
        int characterId = model.getActiveCharacterId();
        if (model.getGameMode() == 1){
            CharacterSerializable c = model.getCharacterById(characterId);
            if (c == null)
                playedCharacterLabel.setText("No active character");
            else
                playedCharacterLabel.setText(c.getName());

            coinsNumberLabel.setText(String.valueOf(model.getPlayerById(myId).getCoins()));
        }

        //phase
        if (model.getActualPhase().equals("Action"))
            this.phaseLabel.setText(model.getActualPhase() + " - " + model.getActualActionPhase());
        else
            this.phaseLabel.setText(model.getActualPhase());

        //assistant
        Assistant a = Assistant.getAssistantByValue(model.getPlayerById(myId).getActiveAssistant());
        if (a != null) {
            int max = a.getMaxMovement();
            if (characterId == 9) max += 2;
            this.stepsLabel.setText("Max " + max + " steps");
        }
        else
            this.stepsLabel.setText("");

        //bag
        int[] bag = dataCollector.getModel().getBag();
        leftBlue.setText(String.valueOf(bag[0]));
        leftGreen.setText(String.valueOf(bag[1]));
        leftPink.setText(String.valueOf(bag[2]));
        leftRed.setText(String.valueOf(bag[3]));
        leftYellow.setText(String.valueOf(bag[4]));

        disableColorChoose();
    }

    private Map<Integer, Label> assistantLabel = null;
    private Map<Integer, Label> usernames = null;
    private Map<Integer, Rectangle> usernameRectangles = null;
    private void updateUsernames() {

        if (this.assistantLabel == null || this.usernames == null || this.usernameRectangles == null)
            throw new IllegalStateException("setStage() method not called");

        ModelMessage model = this.dataCollector.getModel();
        int playerNumber = model.getPlayerNumber();
        Map<Integer, String> usernames = dataCollector.getUsernames();

        for (Integer i : usernames.keySet()){
            Assistant a = Assistant.getAssistantByValue(model.getPlayerById(i).getActiveAssistant());
            if (a != null)
                this.assistantLabel.get(i).setText(a.name() + " - " + a.getValue());
            else
                this.assistantLabel.get(i).setText("");
        }

        int curr = model.getCurrentPlayerId();
        int myId = dataCollector.getId();
        System.out.println("Current is " + curr);

        for (Integer i : this.usernames.keySet()){
            this.usernames.get(i).setEffect(null);
        }

        for (Integer i : this.usernameRectangles.keySet()){
            this.usernameRectangles.get(i).setEffect(null);
        }

        //current player has the rectangles glow, this player has his name glow
        this.usernames.get(myId).setEffect(new DropShadow(10, Color.GOLD));
        this.usernameRectangles.get(curr).setEffect(new DropShadow(10, Color.GOLD));
    }

    List<GridPane> usedClouds = null;
    private void updateClouds () {
        if (this.usedClouds == null)
            throw new IllegalStateException("setStage() method not called");

        ModelMessage model = dataCollector.getModel();
        List<CloudSerializable> clouds =  model.getCloudList();

        for (GridPane grid : this.usedClouds){
            grid.getChildren().clear();
        }

        for (int c = 0; c < clouds.size(); c++){
            CloudSerializable cloud = clouds.get(c);
            GridPane grid = this.usedClouds.get(c);
            int[] students = cloud.getDrawnStudents();

            int added = 0;
            for (int i = 0; i < students.length; i++){
                for (int j = 0; j < students[i]; j++){
                    int col = (grid.getColumnCount() - 1);
                    int column = added % col;
                    column++;
                    int row = added / col;
                    row++;

                    ImageView tokenView = new ImageView();
                    tokenView.setFitHeight(20);
                    tokenView.setFitWidth(20);
                    Image token = convertTo3DCircle(i, 20);
                    tokenView.setImage(token);
                    grid.add(tokenView, column, row);

                    added++;
                    //System.out.println("Added color[" + it.polimi.ingsw.Enum.Color.getColorById(i).name() + "] student in cloud " + grid.getId() + " at column " + column + ", row " + row);
                }
            }
        }

        disableClouds();
    }

    Map<Integer, GridPane> professorGrids = null;
    private void updateProfessors() {
        if (this.professorGrids == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.professorGrids.keySet()){
            GridPane grid = this.professorGrids.get(i);
            grid.getChildren().clear();
        }

        int[] professors = dataCollector.getModel().getProfessorsList();
        for(int i = 0; i < professors.length; i++){

            if(professors[i] != -1) {
                ImageView tokenView = new ImageView();
                tokenView.setFitHeight(20);
                tokenView.setFitWidth(20);
                Image token = convertTo3DHexagon(i, 20);
                tokenView.setImage(token);
                this.professorGrids.get(professors[i]).add(tokenView, i, 0);
            }
        }
    }

    Map<Integer, GridPane> roomGrids = null;
    private void updateRooms() {
        if (this.roomGrids == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.roomGrids.keySet()){
            GridPane grid = this.roomGrids.get(i);
            grid.getChildren().clear();
        }

        this.room = null;

        ModelMessage model = dataCollector.getModel();

        for (Integer id : this.roomGrids.keySet()){

            int[] room = model.getPlayerById(id).getSchool().getCopyOfRoom();

            for(it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){

                for(int students = 0; students < room[color.getIndex()]; students++){

                    ImageView tokenView = new ImageView();
                    tokenView.setFitHeight(20);
                    tokenView.setFitWidth(20);
                    Image token = convertTo3DCircle(color.getIndex(), 20);
                    tokenView.setImage(token);


                    //if this in the player room set the color data to each circle and add all of them to a matrix
                    if (id.equals(this.dataCollector.getId())){
                        tokenView.setUserData(color);

                        if (this.room == null){
                            this.room = new ArrayList<>(5);
                            for (int i = 0; i < it.polimi.ingsw.Enum.Color.getNumberOfColors(); i++)
                                this.room.add(new ArrayList<>(10));
                        }

                        this.room.get(color.getIndex()).add(students, tokenView);
                    }
                    roomGrids.get(id).add(tokenView, color.getIndex(), students);
                }
            }
        }
        disableRoom();
    }

    Map<Integer, GridPane> entranceGrids = null;
    private void updateEntrances() {
        if (this.entranceGrids == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.entranceGrids.keySet()){
            GridPane grid = this.entranceGrids.get(i);
            grid.getChildren().clear();
        }

        this.entrance = null;

        ModelMessage model = dataCollector.getModel();

        for (Integer id : this.entranceGrids.keySet()) {

            int[] entrance = model.getPlayerById(id).getSchool().getCopyOfEntrance();
            GridPane grid = this.entranceGrids.get(id);

            int added = 0;
            for (it.polimi.ingsw.Enum.Color color : it.polimi.ingsw.Enum.Color.values()) {
                for (int student = 0; student < entrance[color.getIndex()]; student++) {

                    ImageView tokenView = new ImageView();
                    tokenView.setFitHeight(20);
                    tokenView.setFitWidth(20);
                    Image token = convertTo3DCircle(color.getIndex(), 20);
                    tokenView.setImage(token);

                    if (added == 4) //position (4, 0) not allowed
                        added++;

                    int column = added % grid.getColumnCount();
                    int row = added / grid.getColumnCount();

                    //System.out.println("Color " + color.name() + " added " + added + " [ " + column + ", " + row + "]");
                    if (row > 1 || column > 4)
                        System.err.println("Entrance space exceeded!");

                    grid.add(tokenView, column, row);


                    added++;

                    if (id.equals(this.dataCollector.getId())){
                        tokenView.setUserData(color);
                        if (this.entrance == null) {
                            this.entrance = new ArrayList<>(9);
                        }
                        this.entrance.add(tokenView);
                    }
                }
            }
        }
        disableEntrance();
    }

    Map<Integer, GridPane> towerGrids = null;
    private void updateTowers() {
        if (this.towerGrids == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.towerGrids.keySet()){
            GridPane grid = this.towerGrids.get(i);
            grid.getChildren().clear();
        }

        ModelMessage model = this.dataCollector.getModel();

        for (Integer id : this.towerGrids.keySet()){

            PlayerSerializable p = model.getPlayerById(id);
            int towers = p.getSchool().getTowers();
            GridPane grid = this.towerGrids.get(id);

            for(int i = 0; i < towers; i++){
                ImageView tokenView = new ImageView();
                Image token = convertTo3DTowerColor(p.getTowerColor(), 20);
                tokenView.setImage(token);
                int column = i % grid.getColumnCount();
                int row = i / grid.getColumnCount();
                grid.add(tokenView, column, row);

                if(column > 3 || row > 1)
                    System.out.println("Tower space exceeded!");
            }
        }
    }

    HashMap<Integer, GridPane> activeIslandGrids = null;
    HashMap<Integer, AnchorPane> activeIslands = null;
    private void updateIslands(){
        if (this.activeIslandGrids == null || this.activeIslands == null)
            throw new IllegalStateException("setStage() method not called");

        ModelMessage model = dataCollector.getModel();

        for (int i = 0; i < 12; i++){
            if (!model.isIslandIdValid(i)){
                AnchorPane aP = activeIslands.remove(i);
                GridPane gP = activeIslandGrids.remove(i);
                if (aP != null)
                    aP.setVisible(false);
                if (gP != null)
                    gP.setVisible(false);
            }
            else{
                Island is = model.getIslandFromId(i);
                AnchorPane aP = this.activeIslands.get(i);
                if (is.isMerged()){
                    ImageView img = (ImageView) aP.getChildren().get(0);
                    img.setEffect(new DropShadow(15, Color.GOLD));
                    img.setScaleX(1.5);
                    img.setScaleY(1.5);
                }
                GridPane grid = this.activeIslandGrids.get(i);
                grid.setUserData(is);
                grid.getChildren().clear();


                /*
                       0                   1               2
                0   [ Mother Nature     Ban Cards       Towers]
                    [                                         ]
                1   [ Blue              Purple          Yellow]
                    [                                         ]
                2   [ Red               Green                 ]

                */

                if (i == model.getMotherNatureIslandId() || model.getMotherNatureIslandId() == -1){ //-1 is used for test the visualization of mother nature not possible in normal game
                    Image image = LazyImageLoader.getInstance().getMotherNature(16);
                    ImageView imageView = new ImageView();
                    imageView.setFitHeight(16);
                    imageView.setFitWidth(16);
                    imageView.setImage(image);
                    imageView.setTranslateY(-11);
                    grid.add(imageView, 0, 0);
                }

                if (model.getGameMode() == 1 && is.getBanCard() > 0){
                    ImageView banCard = new ImageView();
                    banCard.setFitHeight(16);
                    banCard.setFitWidth(16);
                    banCard.setTranslateY(-11);
                    banCard.setImage(LazyImageLoader.getInstance().getBanCard(16));
                    Label banCardLabel = new Label(String.valueOf(is.getBanCard()));
                    banCardLabel.setTranslateY(-24);
                    banCardLabel.setTranslateX(4);
                    banCardLabel.setOpacity(1);
                    banCardLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
                    banCardLabel.setTextFill(Color.WHITE);
                    grid.add(banCard, 1, 0);
                    grid.add(banCardLabel, 1, 0);
                }

                if (is.getTowerCount() > 0){
                    ImageView tower = new ImageView();
                    tower.setFitWidth(16);
                    Image token = convertTo3DTowerColor(is.getTowerColor(), 16);
                    tower.setImage(token);
                    Label towerLabel = new Label(String.valueOf(is.getTowerCount()));
                    towerLabel.setTranslateY(-30);
                    towerLabel.setTranslateX(4);
                    towerLabel.setOpacity(1);
                    towerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
                    towerLabel.setTextFill(Color.WHITE);
                    grid.add(tower, 2, 0);
                    grid.add(towerLabel, 2, 0);
                }

                int[] students = is.getStudents();
                for (it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()) {
                    if (students[color.getIndex()] > 0) {
                        int position = color.getIndex() + 3; //first 3 position occupied
                        int column = (position % 3);
                        int row = (position / 3);
                        ImageView tokenView = new ImageView();
                        tokenView.setFitHeight(16);
                        tokenView.setFitWidth(16);
                        Image token = convertTo3DCircle(color.getIndex(), 16);
                        tokenView.setImage(token);
                        Label label = new Label(String.valueOf(students[color.getIndex()]));
                        label.setTranslateY(-11);
                        label.setTranslateX(4);
                        label.setOpacity(1);
                        label.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
                        label.setTextFill(Color.WHITE);
                        grid.add(tokenView, column, row);
                        grid.add(label, column, row);
                    }
                }
            }
        }
        disableIslands();
    }

    @FXML
    private AnchorPane assistantPane;

    private Map<Integer, ImageView> assistants = null;
    private void updateAssistants() {
        if (this.assistants == null)
            throw new IllegalStateException("setStage() method not called");

        ModelMessage model = dataCollector.getModel();
        int myId = dataCollector.getId();

        int[] assistant = model.getPlayerById(myId).getAssistantDeck();

        for (Assistant a: Assistant.values()){
            boolean find = false;
            for (int i : assistant)
                if (i == a.getValue()){
                    find = true;
                    break;
                }

            if (!find){
                ImageView i = this.assistants.remove(a.getValue());
                if (i != null)
                    //i.setVIsible(false);
                    assistantPane.getChildren().remove(i);
            }
        }
        disableAssistants();
    }

    private Map<Integer, AnchorPane> characters = null;
    private Map<Integer, ImageView> charactersCoins = null;

    private void updateCharacters () {

        ModelMessage model = dataCollector.getModel();

        if (this.characters == null || this.charactersCoins == null){
            if (model.getGameMode() == 1)
                throw new IllegalStateException("setStage() method not called");
            else
                return;
        }


        for (Integer id : this.characters.keySet()){
            CharacterSerializable s = model.getCharacterById(id);
            this.characters.get(id).setUserData(s);
            updatesCharacter(s);
        }
    }

    @FXML
    private GridPane clericGrid;
    @FXML
    private Label labelApothecary;
    @FXML
    private GridPane jesterGrid;
    @FXML
    private ImageView cookActiveColor;
    @FXML
    private GridPane princessGrid;

    private List<ImageView> clericStudents = null;
    private List<ImageView> jesterStudents = null;
    private List<ImageView> princessStudents = null;

    private void updatesCharacter(CharacterSerializable c){

        ModelMessage model = this.dataCollector.getModel();
        int id = c.getId();

        if (!model.isCharacterIdValid(id))
            return;

        this.charactersCoins.get(id).setVisible(c.isUsed());


        switch (id){
            case 0 -> {
                int banCard = c.getBanCard();
                this.labelApothecary.setText("x" + banCard);
            }
            case 2 -> {

                this.clericGrid.getChildren().clear();
                this.clericStudents = null;

                // cleric
                int[] students = c.getStudents();
                int added = 0;

                for (it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){
                    for (int i = 0; i < students[color.getIndex()]; i++){
                        int column = added % this.clericGrid.getColumnCount();
                        int row = added / this.clericGrid.getColumnCount();
                        ImageView tokenView = new ImageView();
                        tokenView.setFitHeight(32);
                        tokenView.setFitWidth(32);
                        Image token = convertTo3DCircle(color.getIndex(), 32);
                        tokenView.setImage(token);

                        this.clericGrid.add(tokenView, column, row);
                        if (this.clericStudents == null)
                            this.clericStudents = new ArrayList<>(4);

                        this.clericStudents.add(tokenView);

                        tokenView.setUserData(color);

                        added++;
                    }
                }
            }
            case 3 -> {
                int color = c.getColorId();
                if (color != -1){
                    Image image = convertToCircle(color, this.cookActiveColor.getFitHeight());
                    this.cookActiveColor.setImage(image);
                    this.cookActiveColor.setVisible(true);
                }
                else
                    this.cookActiveColor.setVisible(false);
            }
            case 6 -> {
                // jester

                this.jesterGrid.getChildren().clear();
                this.jesterStudents = null;

                int[] students = c.getStudents();
                int added = 0;
                for (it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){
                    for (int i = 0; i < students[color.getIndex()]; i++){
                        int column = added % this.jesterGrid.getColumnCount();
                        int row = added / this.jesterGrid.getColumnCount();
                        ImageView tokenView = new ImageView();
                        tokenView.setFitHeight(32);
                        tokenView.setFitWidth(32);
                        Image token = convertTo3DCircle(color.getIndex(), 32);
                        tokenView.setImage(token);
                        this.jesterGrid.add(tokenView, column, row);
                        if (this.jesterStudents == null)
                            this.jesterStudents = new ArrayList<>(6);

                        this.jesterStudents.add(tokenView);
                        tokenView.setUserData(color);
                        added++;
                    }
                }
            }
            case 10 -> {

                this.princessGrid.getChildren().clear();
                this.princessStudents = null;

                // princess
                int[] students = c.getStudents();
                int added = 0;
                for (it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){
                    for (int i = 0; i < students[color.getIndex()]; i++){
                        int column = added % this.princessGrid.getColumnCount();
                        int row = added / this.princessGrid.getColumnCount();
                        ImageView tokenView = new ImageView();
                        tokenView.setFitHeight(32);
                        tokenView.setFitWidth(32);
                        Image token = convertTo3DCircle(color.getIndex(), 32);
                        tokenView.setImage(token);
                        this.princessGrid.add(tokenView, column, row);
                        if (this.princessStudents == null)
                            this.princessStudents = new ArrayList<>(4);

                        this.princessStudents.add(tokenView);
                        tokenView.setUserData(color);
                        added++;
                    }
                }
            }
        }
    }



    /*
        Enabling methods
     */

    List<ImageView> colors = null;
    private void enableColorChoose(EventHandler<MouseEvent> handler) {
        if (this.colors == null)
            throw new IllegalStateException("setStage() method not called");

        for (ImageView i : colors){
            i.setOnMouseClicked(handler);
            enabledEffect(i);
        }
    }

    private void activateCloud() {
        if (this.usedClouds == null)
            throw new IllegalStateException("setStage() method not called");

        DataCollector dC = this.dataCollector;

        for (GridPane g: this.usedClouds) {

            g.setDisable(false);
            g.setOnMouseClicked(mouseEvent -> {
                ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                CloudSerializable c = (CloudSerializable) g.getUserData();
                dC.setNextMove(new ChooseCloudMessage(Errors.NO_ERROR, "Cloud choosed", c.getId()));
                disableClouds();
            });
            enabledEffect(g);
            //enabledEffectCard(g.getParent());
        }

        displayMessage("It is your turn, please click on the cloud you want to choose");
    }

    private List<List<ImageView>> room = null;
    private void enableRoom (EventHandler<MouseEvent> handler) {
        if (this.room == null)
            return;

        for (List<ImageView> l : this.room){
            for (ImageView c : l){
                c.setDisable(false);
                c.setOnMouseClicked(handler);
                enabledEffect(c);
                //enabledEffectRoom(this.actualRoom.getParent());
            }
        }
    }

    private List<ImageView> entrance = null;
    private void enableEntrance (EventHandler<MouseEvent> handler){

        if (this.entrance == null)
            throw new IllegalStateException("setStage() method not called");

        for (ImageView c : this.entrance){
            c.setDisable(false);
            c.setOnMouseClicked(handler);
            enabledEffect(c);
        }
    }

    public void enableIslands(EventHandler<MouseEvent> handler){
        if (this.activeIslandGrids == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i: this.activeIslandGrids.keySet()) {
            GridPane g = this.activeIslandGrids.get(i);
            g.setOnMouseClicked(handler);
            g.setDisable(false);
            enabledEffect(g);
            enabledEffectCard(g.getParent());
        }
    }

    private GridPane actualRoom = null;
    private void enableStudentsDestination() {
        if (this.colorChoose == null){
            return;
        }

        final it.polimi.ingsw.Enum.Color colorChoose = this.colorChoose;
        DataCollector dC = this.dataCollector;

        EventHandler<MouseEvent> handler = mouseEvent -> {
            ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
            dC.setNextMove(new MoveStudentMessage(Errors.NO_ERROR, "Moved Student", colorChoose.getIndex(), -1));
            disableStudentsDestination();
        };

        int[] room = this.dataCollector.getModel().getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfRoom();
        if (room[this.colorChoose.getIndex()] < 10) {
            this.actualRoom.setOnMouseClicked(handler);
            enabledEffect(this.actualRoom);
            enabledEffectRoom(this.actualRoom.getParent());
        }

        handler = mouseEvent -> {
            ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
            Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
            dC.setNextMove(new MoveStudentMessage(Errors.NO_ERROR, "Moved Student", colorChoose.getIndex(), i.getId()));
            disableStudentsDestination();
        };

        enableIslands(handler);

        displayMessage("Please select where to put the students: on an island or on your room.");
    }











    /*
        Enabling methods
     */


    private void disableColorChoose() {
        if (this.colors == null)
            throw new IllegalStateException("setStage() method not called");

        for (ImageView i : colors){
            i.setOnMouseClicked(null);
            disabledEffect(i);
        }
    }

    private void disableClouds() {
        if (this.usedClouds == null)
            throw new IllegalStateException("setStage() method not called");

        for (GridPane g: this.usedClouds) {
            g.setOnMouseClicked(null);
            g.setDisable(true);
            disabledEffect(g);
            //disabledEffect(g.getParent());
        }
    }

    private void disableRoom() {
        if (this.room == null)
            return;

        for (List<ImageView> list: this.room){
            for (ImageView c : list){
                c.setDisable(true);
                c.setOnMouseClicked(null);
                disabledEffect(c);
            }
        }
    }

    private void disableEntrance() {
        if (this.entrance == null)
            throw new IllegalStateException("setStage() method not called");

        for (ImageView c : this.entrance){
            c.setDisable(true);
            c.setOnMouseClicked(null);
            disabledEffect(c);
        }
    }

    public void disableIslands(){
        if (this.activeIslands == null)
            throw new IllegalStateException("setStage() method not called");

        int id = this.dataCollector.getModel().getMotherNatureIslandId();

        for (Integer i: this.activeIslandGrids.keySet()) {
            GridPane g = this.activeIslandGrids.get(i);
            Island is = (Island) g.getUserData();
            g.setOnMouseClicked(null);
            g.setDisable(true);
            disabledEffect(g);
            disabledEffect(g.getParent());
            if (is.getId() == id)
                g.getParent().setEffect(new DropShadow(10, Color.ORANGERED));
        }
    }

    private void disableAssistants () {
        if (this.assistants == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.assistants.keySet()){
            ImageView a = this.assistants.get(i);
            a.setOnMouseClicked(null);
            a.setDisable(true);
            disabledEffect(a);
        }
    }

    private void disableCharacters() {
        if (this.characters == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.characters.keySet()){

            AnchorPane c = this.characters.get(i);
            c.setOnMouseClicked(null);
            disabledEffect(c);
        }
    }

    private void disableStudentsDestination() {
        if (this.actualRoom != null) {
            this.actualRoom.setOnMouseClicked(null);
            disabledEffect(this.actualRoom);
            disabledEffect(this.actualRoom.getParent());
        }

        disableIslands();
    }





    // Character Effects


    private boolean checkCoins(CharacterSerializable character) {

        int cost = character.getCost();
        if (character.isUsed())
            cost++;

        int coins = this.dataCollector.getModel().getPlayerById(this.dataCollector.getId()).getCoins();

        if (coins < cost) {
            displayMessage("Sorry you don't have enough coins to play the " + character.getName() + " its cost is " + cost + " and your coins are " + coins);
            return true;
        }

        return false;
    }

    private void askCharacterAttributes(CharacterSerializable character) {

        if (checkCoins(character))
            return;

        ModelMessage model = dataCollector.getModel();

        int characterId = character.getId();

        if (!model.isCharacterIdValid(characterId)){
            return;
        }

        if (characterId != 1 && characterId != 6)
            disableCharacters();

        EventHandler<MouseEvent> handler;

        switch (characterId){
            case 0 -> {
                //apothecary - ban card
                disableIslands();

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose Island for apothecary  " + i.getId());
                    int[] obj = new int[1];
                    obj[0] = i.getId();
                    disableIslands();
                    setCharacterMove(character, obj);
                };

                enableIslands(handler);

                displayMessage("Please click on the island where put the ban card");

                return;
            }
            case 1 -> {
                //bard - swap between entrance and room

                int[] e = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfEntrance();
                int[] r = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfRoom();

                if (Arrays.stream(e).sum() < 1 || Arrays.stream(r).sum() < 1){
                    displayMessage("Cannot play Bard because you don't have enough students in your room");
                    return;
                }

                disableCharacters();
                disableEntrance();

                handler = mouseEvent -> {
                    Node node = (Node) mouseEvent.getSource();
                    node.setOnMouseClicked(null);
                    disabledEffect(node);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) node.getUserData();
                    System.out.println("Choose color " + color + " from entrance for bard Effect");
                    bardEffect(color, null);
                };

                enableEntrance(handler);

                displayMessage("Click on the students from your entrance (max 2), then click on the students in the room for swap them");

                return;
            }
            case 2 -> {
                //cleric - move a students from the character to an island

                if (this.clericStudents == null)
                    return;

                disableIslands();

                handler = mouseEvent -> {
                    mouseEvent.consume();
                    Node node = (Node) mouseEvent.getSource();
                    node.setOnMouseClicked(null);
                    disabledEffect(node);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) node.getUserData();
                    System.out.println("Choose color " + color + " from cleric for cleric Effect");
                    clericEffect(color, null);
                };

                for (ImageView c: this.clericStudents){
                    c.setOnMouseClicked(handler);
                    enabledEffectCard(c);
                }

                displayMessage("Please select a student from the cleric, then click on the island where you want to put it");
                return;
            }
            case 3 -> {
                //cook - color that not count as influence
                disableColorChoose();

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " for cook Effect");
                    int[] obj = new int[1];
                    obj[0] = color.getIndex();
                    disableColorChoose();
                    setCharacterMove(character, obj);
                };

                enableColorChoose(handler);

                displayMessage("Please select a color from the list on the left");

                return;
            }
            case 5 -> {
                //herald calc influence of an island

                disableIslands();

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose Island for herald  " + i.getId());
                    int[] obj = new int[1];
                    obj[0] = i.getId();
                    disableIslands();
                    setCharacterMove(character, obj);
                };

                enableIslands(handler);

                displayMessage("Please click on the island where calc the influence");

                return;
            }
            case 6 -> {
                //jester - swap between jester and entrance

                int[] e = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfEntrance();

                if (Arrays.stream(e).sum() < 1){
                    displayMessage("Cannot play Jester because you don't have enough students in your entrance");
                    return;
                }

                if (this.jesterStudents == null)
                    return;

                disableCharacters();
                disableEntrance();

                handler = mouseEvent -> {
                    mouseEvent.consume();
                    Node node = (Node) mouseEvent.getSource();
                    node.setOnMouseClicked(null);
                    disabledEffect(node);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) node.getUserData();
                    System.out.println("Choose color " + color + " from jester for jester Effect");
                    jesterEffect(color, null);
                };

                for (ImageView c: this.jesterStudents){
                    c.setOnMouseClicked(handler);
                    enabledEffectCard(c);
                }

                displayMessage("Please select a max of 3 students from the jester, then click on your students on the entrance to swap them");
                return;
            }
            case 10 -> {
                //princess - add a students from this character to your room

                if (this.princessStudents == null)
                    return;

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " from princess for princess Effect");
                    int[] obj = new int[1];
                    obj[0] = color.getIndex();
                    disablePrincessEffect();
                    setCharacterMove(character, obj);
                };

                for (ImageView c: this.princessStudents){
                    c.setOnMouseClicked(handler);
                    enabledEffectCard(c);
                }

                displayMessage("Please select a student from the princess that will be added to your room");
                return;
            }
            case 11 -> {
                //thief - choose a color

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " for thief Effect");
                    int[] obj = new int[1];
                    obj[0] = color.getIndex();
                    disableColorChoose();
                    setCharacterMove(character, obj);
                };

                enableColorChoose(handler);

                displayMessage("Please select a color from the list on the left");

                return;
            }
        }

        setCharacterMove(character, null);
    }


    private int[] bardObject = null;

    private void bardEffect (it.polimi.ingsw.Enum.Color entrance, it.polimi.ingsw.Enum.Color room){
        ModelMessage model = this.dataCollector.getModel();

        if (bardObject == null && room == null && entrance != null) { //chose first entrance color
            bardObject = new int[2];
            bardObject[0] = entrance.getIndex();
            bardObject[1] = -1;

            EventHandler<MouseEvent> handler = mouseEvent -> {
                ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                System.out.println("Choose color " + color + " from room for bard Effect");
                bardEffect(null, color);
            };

            int[] e = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfEntrance();
            int[] r = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfRoom();

            if (Arrays.stream(e).sum() < 2 || Arrays.stream(r).sum() < 2){
                disableEntrance();
            }

            enableRoom(handler);
        }
        else if (bardObject != null && room == null && entrance != null) { //chose second entrance color
            int first = bardObject[0];

            bardObject = new int[4];

            bardObject[0] = first;
            bardObject[1] = -1;
            bardObject[2] = entrance.getIndex();
            bardObject[3] = -1;

            disableEntrance();
            //room already activated
        }
        else if (bardObject != null && room != null && entrance == null) { //choose color from room
            if (bardObject.length == 2){ // choose only one color
                bardObject[1] = room.getIndex();
                disableEntrance();
                disableRoom();
                System.out.println("Activated Bard");
                this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Bard", 1, bardObject));
                disableCharacters();
            }
            else { //choose 2 color
                if (bardObject[1] == -1){ // first setted
                    bardObject[1] = room.getIndex();
                }
                else { //last setted
                    bardObject[3] = room.getIndex();
                    disableRoom();
                    System.out.println("Activated Bard");
                    this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Bard", 1, bardObject));
                    disableCharacters();
                }
            }
        }
        else {
            System.out.println("Not valid for bard Effect");
        }

    }


    private int[] clericObject = null;

    private void clericEffect(it.polimi.ingsw.Enum.Color color, Island i){
        if (clericObject == null && color != null && i == null){
            clericObject = new int[2];
            clericObject[0] = color.getIndex();
            disableClericEffect();

            EventHandler<MouseEvent> handler = mouseEvent -> {
                ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                Island is = (Island) ((Node) mouseEvent.getSource()).getUserData();
                System.out.println("Choose island " + is.getId() + " for cleric Effect");
                clericEffect(null, is);
            };

            enableIslands(handler);
        }
        else if (clericObject != null && color == null && i != null){
            clericObject[1] = i.getId();
            System.out.println("Activated Cleric");
            this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Cleric", 2, clericObject));
            disableIslands();
            disableCharacters();
        }
        else{
            System.out.println("Not valid for cleric Effect");
        }
    }

    private void disableClericEffect (){

        if (this.clericStudents == null)
            return;

        for (ImageView c: this.clericStudents){
            c.setOnMouseClicked(null);
            disabledEffect(c);
        }
    }


    private int[] jesterObject = null;

    private void jesterEffect (it.polimi.ingsw.Enum.Color jester, it.polimi.ingsw.Enum.Color entrance){
        if (entrance == null && jester != null) { //chose color from jester

            if (jesterObject == null) { // first
                jesterObject = new int[2];
                jesterObject[0] = jester.getIndex();
                jesterObject[1] = -1;

                EventHandler<MouseEvent> handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " from entrance for jester Effect");
                    jesterEffect(null, color);
                };

                enableEntrance(handler);
            }
            else if (jesterObject.length == 2){ // 2
                int first = jesterObject[0];

                jesterObject = new int[4];
                jesterObject[0] = first;
                jesterObject[1] = -1;
                jesterObject[2] = jester.getIndex();
                jesterObject[3] = -1;
            }
            else { // 3
                int first = jesterObject[0];
                int second = jesterObject[2];

                jesterObject = new int[6];
                jesterObject[0] = first;
                jesterObject[1] = -1;
                jesterObject[2] = second;
                jesterObject[3] = -1;
                jesterObject[4] = jester.getIndex();
                jesterObject[5] = -1;

                disableJesterEffect();
            }

        }
        else if (entrance != null && jester == null && jesterObject != null){
            disableJesterEffect();

            int length = jesterObject.length;
            if (length == 2){
                jesterObject[1] = entrance.getIndex();
                this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Jester", 6, jesterObject));
                disableEntrance();
                disableCharacters();
            }
            else if (length == 4){
                if (jesterObject[1] == -1)
                    jesterObject[1] = entrance.getIndex();
                else{
                    jesterObject[3] = entrance.getIndex();
                    this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Jester", 6, jesterObject));
                    disableEntrance();
                    disableCharacters();
                }
            }
            else { // length == 6
                if (jesterObject[1] == -1)
                    jesterObject[1] = entrance.getIndex();
                else if (jesterObject[3] == -1)
                    jesterObject[3] = entrance.getIndex();
                else{
                    jesterObject[5] = entrance.getIndex();
                    this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Jester", 6, jesterObject));
                    disableEntrance();
                    disableCharacters();
                }
            }
        }
        else {
            System.out.println("Not valid for jester Effect");
        }
    }

    private void disableJesterEffect(){

        if (this.jesterStudents == null)
            return;

        for (ImageView c: this.jesterStudents){
            c.setOnMouseClicked(null);
            disabledEffect(c);
        }
    }

    private void disablePrincessEffect() {

        if (this.princessStudents == null)
            return;

        for (ImageView c: this.princessStudents){
            c.setOnMouseClicked(null);
            disabledEffect(c);
        }
    }


    private void setCharacterMove (CharacterSerializable characterSerializable, int[] obj){
        disableCharacters();
        this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played " + characterSerializable.getName(), characterSerializable.getId(), obj));
    }




    //Effect

    private void enabledEffectCard(Node node){
        Effect enabled = new DropShadow(20, Color.DEEPSKYBLUE);
        node.setEffect(enabled);
    }
    private void enabledEffect (Node node){
        Effect enabled = new Glow(10);
        node.setEffect(enabled);
    }
    private void enabledEffectRoom(Node node){
        Effect enabled = new Bloom(0.8);
        node.setEffect(enabled);
    }
    private void disabledEffect (Node node){
        node.setEffect(null);
    }








    // Elaborate Model

    private void elaborateModel() {

        ModelMessage model = this.dataCollector.getModel();

        if(model.gameIsOver()){
            System.out.println("Gui Game Over");
            gameOver();
            return;
        }

        if (!this.dataCollector.isThisMyTurn()) {
            System.out.println("Gui Not your Turn");
            notYourTurn();
            return;
        }

        System.out.println("Gui your Turn");
        moveAsker(model);

        String s = this.dataCollector.getErrorData();

        if (s != null){
            displayMessage(s);
        }

        String musicFile = "sound/notification-sound-7062.mp3";

        Media sound = new Media(getClass().getClassLoader().getResource(musicFile).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }




    private void moveAsker(ModelMessage model) {

        Phase p = Phase.valueOf(model.getActualPhase());
        ActionPhase aP = ActionPhase.valueOf(model.getActualActionPhase());

        if (Phase.Planning.equals(p)){
            activateAssistant();
        }
        else {
            if (model.getGameMode() == 1 && this.dataCollector.canPlayCharacter() && !aP.equals(ActionPhase.NotActionPhase))
                activateCharacter();
            switch (aP) {
                case MoveStudent -> activateStudentsMove();
                case MoveMotherNature -> activateMotherNatureMove();
                case ChooseCloud -> activateCloud();
            }
        }
    }
    private void activateAssistant() {

        if (this.assistants == null) {
            throw new IllegalStateException("setStage() method not called");
        }

        DataCollector dC = this.dataCollector;
        EventHandler<MouseEvent> handler = mouseEvent -> {
            ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
            Assistant a = (Assistant) ((Node) mouseEvent.getSource()).getUserData();
            System.out.println("Selected assistant " + a.name());
            dC.setNextMove(new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", a.getValue()));
            disableAssistants();
        };

        for (Integer i : this.assistants.keySet()){
            ImageView a = this.assistants.get(i);
            a.setDisable(false);
            a.setOnMouseClicked(handler);
            enabledEffectCard(a);
        }

        displayMessage("It is your turn, please select an assistant. Just click on It");
    }

    private it.polimi.ingsw.Enum.Color colorChoose = null;
    public void setColorChoose(it.polimi.ingsw.Enum.Color colorChoose) {
        this.colorChoose = colorChoose;
    }
    private void activateStudentsMove() {

        EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

            private MainGameController c = null;
            public  EventHandler<MouseEvent> init (MainGameController c){
                this.c = c;
                return this;
            }
            @Override
            public void handle(MouseEvent mouseEvent) {
                ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                c.setColorChoose((it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData());
                c.disableEntrance();
                c.enableStudentsDestination();
            }
        }.init(this);

        enableEntrance(handler);

        displayMessage("It is your turn, please select a students from your entrance. Just click on It ( " + this.dataCollector.getModel().getStudentsToMove() + " students remaining)");
    }
    private void activateMotherNatureMove() {

        DataCollector dC = this.dataCollector;

        EventHandler<MouseEvent> handler = mouseEvent -> {
            ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
            Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
            int distance = dC.getModel().calcIslandDistance(i);
            System.out.println("Moved mother nature for " + distance);
            dC.setNextMove(new MoveMotherNatureMessage(Errors.NO_ERROR, "Mother Nature Moved", distance));
            disableStudentsDestination();
        };

        enableIslands(handler);

        displayMessage("It is your turn, please click on the island where you want to move Mother Nature");
    }
    private void activateCharacter() {
        if (this.characters == null)
            throw new IllegalStateException("setStage() method not called");

        for (Integer i : this.characters.keySet()){

            AnchorPane c = this.characters.get(i);

            c.setOnMouseClicked(mouseEvent -> {
                //((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                CharacterSerializable character = (CharacterSerializable) ((Node)mouseEvent.getSource()).getUserData();
                System.out.println("Selected character " + character.getName());

                askCharacterAttributes(character);
            });
            enabledEffectCard(c);
        }
    }
    private void notYourTurn() {

        String musicFile = "sound/mixkit-correct-answer-tone-2870.wav";

        Media sound = new Media(getClass().getClassLoader().getResource(musicFile).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        displayMessage("It is not your turn, please wait");
    }
    private void gameOver() {
        String message = this.dataCollector.getStandardWinMessage();

        super.main.gameOver(message);
    }
}
