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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
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
import javafx.scene.transform.Scale;

import java.io.File;
import java.net.URL;
import java.util.*;

/*
   todo
    - mettere a posto colori nomi per 4 giocatori (ora sono banalmente rossi e blu e visualizzandoli non sono bellissimi)
    - isole ingrandite se sono aggregate

    - evindeziare in qualche modo quali sono le zone interagibili della plancia - done
    - mandare i messaggi al player non come alert (lasciandoli solo per degli errori o per casi particolari) ma o scriverli da qualche parte sempre visibili, o visualizzarli in risalto per tot secondi e poi farli scomparire - done
    - aggiungere Wizard alla visualizzazione negli username - done
    - aggiungere le immagini delle torri al posto dei cerchi sia nelle scuole sia nelle isole - done
    - aggiungere visualizzazione del motivo di chiusura del programma - done
    - visualizzare tutte le informazioni sulle varie isole - done
    - aggiungere a ogni character o il suo costo aggiornato se è stato usato o come nel gioco vero una moneta su di esso per indicare che costa di più - done
    - mettere a posto le informazioni visualizzate sui character che per ora sono fatte un po' male - done (vedete se vi piacciono)
    - aggiungere una visualizzazione dello stato di gioco (basta scrivere da qualche parte la fase di gioco) - done
    - aggiungere un metodo per far scegliere ai player un colore, io pensavo al cliccare sui colori a sinistra nella bag - done
    - resize text lenght in status bar and assistant below name to match new dimension of window - done
*/

public class MainGameController extends Controller implements Initializable {

    private final DataCollector dataCollector;
    public MainGameController(Gui main, String resource) {
        super(main, resource);
        this.dataCollector = Gui.getDataCollector();
    }

    public Color convertTowerColor(int id){
        return switch (id) {
            case 1 -> Color.BLACK;
            case 2 -> Color.WHITE;
            case 3 -> Color.GREY;
            default -> Color.TRANSPARENT;
        };
    }

    public Image convertTo3DTowerColor(int id, double size){
        return switch (id) {
            case 1 -> new Image("/token/black_tower.png", size, 2*size, false, false);
            case 2 -> new Image("/token/white_tower.png", size, 2*size, false, false);
            case 3 -> new Image("/token/grey_tower.png", size, 2*size, false, false);
            default -> null;
        };
    }

    public Image convertDeckImage(Wizard wizard) {
       return new Image("/Assistenti/retro/" + wizard.getFileName());
    }

    public Color convertColor(int id){
        return switch (id) {
            case 0 -> Color.DEEPSKYBLUE;
            case 1 -> Color.PURPLE;
            case 2 -> Color.GOLD;
            case 3 -> Color.RED;
            case 4 -> Color.GREENYELLOW;
            default -> Color.TRANSPARENT;
        };
    }

    public Image convertTo3DCircle(int id, double size) {
        return switch (id) {
            case 0 -> new Image("/token/circle_pawn_blue.png", size, size, false, false);
            case 1 -> new Image("/token/circle_pawn_pink.png", size, size, false, false);
            case 2 -> new Image("/token/circle_pawn_yellow.png", size, size, false, false);
            case 3 -> new Image("/token/circle_pawn_red.png", size, size, false, false);
            case 4 -> new Image("/token/circle_pawn_green.png", size, size, false, false);
            default -> null;
        };
    }

    public Image convertTo3DHexagon(int id, double size) {
        return switch (id) {
            case 0 -> new Image("/token/hexa_pawn_blue.png", size, size, false, false);
            case 1 -> new Image("/token/hexa_pawn_pink.png", size, size, false, false);
            case 2 -> new Image("/token/hexa_pawn_yellow.png", size, size, false, false);
            case 3 -> new Image("/token/hexa_pawn_red.png", size, size, false, false);
            case 4 -> new Image("/token/hexa_pawn_green.png", size, size, false, false);
            default -> null;
        };
    }

    public Image convertToCircle(int id, double size) {
        return switch (id) {
            case 0 -> new Image("/token/circle_blue.png", size, size, false, false);
            case 1 -> new Image("/token/circle_pink.png", size, size, false, false);
            case 2 -> new Image("/token/circle_yellow.png", size, size, false, false);
            case 3 -> new Image("/token/circle_red.png", size, size, false, false);
            case 4 -> new Image("/token/circle_green.png", size, size, false, false);
            default -> null;
        };
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Scene s = super.main.getMainStage().getScene();

        System.out.println("Initialize Started");

        //if (s != null)
            //s.setCursor(Cursor.WAIT);

        setGameStatus();
        setUsernames();
        setClouds();
        setSchools();
        setIslands();
        setAssistant();
        setCharacters();

        System.out.println("Elaborate Model");
        elaborateModel();
        //if (s != null)
            //s.setCursor(Cursor.DEFAULT);
    }

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
    private Label phaseLabel;
    @FXML
    private Label playedCharacterLabel;
    @FXML
    private Label characterPlayedTitle;

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

    public void setGameStatus(){

        ModelMessage model = dataCollector.getModel();

        if (model.getGameMode() == 0) {
            this.playedCharacterLabel.setVisible(false);
            this.characterPlayedTitle.setVisible(false);
            this.coinsRectangle.setVisible(false);
            this.coinsImage.setVisible(false);
            this.coinsImage.setDisable(true);
            this.coinsLabel.setVisible(false);
            this.coinsNumberLabel.setVisible(false);
        }
        else {
            CharacterSerializable c = model.getCharacterById(model.getActiveCharacterId());
            if (c == null)
                playedCharacterLabel.setText("No active character");
            else
                playedCharacterLabel.setText(c.getName());

            this.coinsNumberLabel.setText(String.valueOf(model.getPlayerById(dataCollector.getId()).getCoins()));
        }

        if (Objects.equals(model.getActualPhase(), "Action")) {
            this.phaseLabel.setText(model.getActualPhase() + " - " + model.getActualActionPhase());
        } else {
            this.phaseLabel.setText(model.getActualPhase());
        }

        Assistant a = Assistant.getAssistantByValue(model.getPlayerById(this.dataCollector.getId()).getActiveAssistant());
        if (a != null) {
            int max = a.getMaxMovement();
            if (model.getActiveCharacterId() == 9)
                max += 2;
            this.stepsLabel.setText("Max " + max + " steps");
        }
        else
            this.stepsLabel.setText("");

        int[] bag = dataCollector.getModel().getBag();
        leftBlue.setText(String.valueOf(bag[0]));
        leftGreen.setText(String.valueOf(bag[1]));
        leftPink.setText(String.valueOf(bag[2]));
        leftRed.setText(String.valueOf(bag[3]));
        leftYellow.setText(String.valueOf(bag[4]));

        this.colorBlue.setUserData(it.polimi.ingsw.Enum.Color.Blue);
        this.colorYellow.setUserData(it.polimi.ingsw.Enum.Color.Yellow);
        this.colorGreen.setUserData(it.polimi.ingsw.Enum.Color.Green);
        this.colorPink.setUserData(it.polimi.ingsw.Enum.Color.Purple);
        this.colorRed.setUserData(it.polimi.ingsw.Enum.Color.Red);
    }

    private void enableColorChoose(EventHandler<MouseEvent> handler) {
        this.colorBlue.setOnMouseClicked(handler);
        enabledEffect(this.colorBlue);
        this.colorYellow.setOnMouseClicked(handler);
        enabledEffect(this.colorYellow);
        this.colorGreen.setOnMouseClicked(handler);
        enabledEffect(this.colorGreen);
        this.colorPink.setOnMouseClicked(handler);
        enabledEffect(this.colorPink);
        this.colorRed.setOnMouseClicked(handler);
        enabledEffect(this.colorRed);
    }

    private void disableColorChoose() {
        this.colorBlue.setOnMouseClicked(null);
        disabledEffect(this.colorBlue);
        this.colorYellow.setOnMouseClicked(null);
        disabledEffect(this.colorYellow);
        this.colorGreen.setOnMouseClicked(null);
        disabledEffect(this.colorGreen);
        this.colorPink.setOnMouseClicked(null);
        disabledEffect(this.colorPink);
        this.colorRed.setOnMouseClicked(null);
        disabledEffect(this.colorRed);
    }

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
    private Rectangle color3;
    @FXML
    private Rectangle color4;

    public void setUsernames() {

        ModelMessage model = dataCollector.getModel();
        int playerNumber = model.getPlayerNumber();
        Map<Integer, String> usernames = dataCollector.getUsernames();
        Map<Integer, Wizard> wizards = dataCollector.getWizards();

        List<Label> names = new ArrayList<>(playerNumber);
        names.add(username1);
        names.add(username2);
        names.add(username3);
        names.add(username4);
        List<Label> labels = new ArrayList<>(playerNumber);
        labels.add(label1);
        labels.add(label2);
        labels.add(label3);
        labels.add(label4);
        List<Rectangle> icons = new ArrayList<>(playerNumber);
        icons.add(icon1);
        icons.add(icon2);
        icons.add(icon3);
        icons.add(icon4);

        if (playerNumber < 4){
            this.icon4.setVisible(false);
            this.icon4.setDisable(true);
            this.rectangle4.setVisible(false);
            this.username4.setVisible(false);
            this.label4.setVisible(false);
        }
        if (playerNumber < 3){
            this.icon3.setVisible(false);
            this.icon3.setDisable(true);
            this.rectangle3.setVisible(false);
            this.username3.setVisible(false);
            this.label3.setVisible(false);
        }

        for (Integer i : usernames.keySet()){

            names.get(i).setText(usernames.get(i));


            //resize image for have a square
            Image img = convertDeckImage(wizards.get(i));
            PixelReader reader = img.getPixelReader();
            WritableImage newImage = new WritableImage(reader, 0, 0, 494, 494);
            icons.get(i).setFill(new ImagePattern(newImage));

            Assistant a = Assistant.getAssistantByValue(model.getPlayerById(i).getActiveAssistant());
            String text;
            if (a != null)
                text = a.name() + " - " + a.getValue();
            else
                text = "";

            labels.get(i).setText(text);
        }

        Map<Label, Rectangle> temp = new HashMap<>(playerNumber);
        temp.put(username1, rectangle1);
        temp.put(username2, rectangle2);
        temp.put(username3, rectangle3);
        temp.put(username4, rectangle4);

        glowEffect(temp);
        mateEffect(names);
    }

    private void mateEffect(List<Label> names) {
        ModelMessage model = this.dataCollector.getModel();

        if (model.getPlayerNumber() != 4)
            return;

        Set<Integer> ids = this.dataCollector.getUsernames().keySet();

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
            System.out.print(names.get(id).getText() + " ");
            names.get(id).setTextFill(Color.BLUE);
        }
        System.out.println();
        System.out.println("Team Red");
        for (Integer id : teamRed){
            System.out.print(names.get(id).getText() + " ");
            names.get(id).setTextFill(Color.RED);
        }
        System.out.println();
    }

    public void glowEffect(Map<Label, Rectangle> temp){

        int curr = this.dataCollector.getIdOfCurrentPlayer();
        System.out.println("Current is " + curr);

        for(Label label : temp.keySet()){

            //setting glow for actual player
            if (label.getText().equals(dataCollector.getUsernames().get(dataCollector.getId()))){
                label.setEffect(new DropShadow(10, Color.GOLD));
            }
            else label.setEffect(null);

            //setting glow for current player
            if (label.getText().equals(dataCollector.getUsernameOfCurrentPlayer())){
                temp.get(label).setEffect(new DropShadow(10, Color.GOLD));
            }
            else temp.get(label).setEffect(null);
        }
    }


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
    private Label messageLabel;

    private Tab actualTab = null;
    private GridPane actualRoom = null;
    private List<List<ImageView>> room = null;
    private GridPane actualEntrance = null;
    private List<ImageView> entrance = null;

    public void setSchools(){

        int playerNumber = dataCollector.getModel().getPlayerNumber();
        Map<Integer, String> usernames = dataCollector.getUsernames();

        List<Tab> tabs = new ArrayList<>(4);
        tabs.add(tab1);
        tabs.add(tab2);
        tabs.add(tab3);
        tabs.add(tab4);

        for (Integer i : usernames.keySet()){
            tabs.get(i).setText(usernames.get(i));
            if (i.equals(this.dataCollector.getId())) {
                this.actualTab = tabs.get(i);
                this.tabPaneSchools.getSelectionModel().select(tabs.get(i));
            }
        }

        //todo consider remove completely the tab from his ancestor
        if (playerNumber < 4){
            this.tab4.setDisable(true);
            tabs.remove(tab4);
        }
        if (playerNumber < 3){
            this.tab3.setDisable(true);
            tabs.remove(tab3);
        }

        setProfessors();
        setRooms();
        setEntrance();
        setTowers();

        System.out.println("Schools Set");
    }

    public void setProfessors() {
        int[] professors = dataCollector.getModel().getProfessorsList();

        ArrayList<GridPane> professorsGrids = new ArrayList<>(4);
        professorsGrids.add(gridProfessors1);
        professorsGrids.add(gridProfessors2);
        professorsGrids.add(gridProfessors3);
        professorsGrids.add(gridProfessors4);

        double height = 11;

        //cannot place properly
        /*Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(
                height / 2, 0.0,
                height / 2 * (1 + cos(Math.PI / 6)), height / 2 * sin(Math.PI / 6),
                height / 2 * (1 + cos(Math.PI / 6)), height / 2 * (1 + sin(Math.PI / 6)),
                height / 2, height,
                height / 2 * (1 - cos(Math.PI / 6)), height / 2 * (1 + sin(Math.PI / 6)),
                height / 2 * (1 - cos(Math.PI / 6)), height / 2 * sin(Math.PI / 6)
        );*/

        for(int i = 0; i < professors.length; i++){
            ImageView tokenView = new ImageView();
            tokenView.setFitHeight(20);
            tokenView.setFitWidth(20);
            Image token = convertTo3DHexagon(i, 20);
            tokenView.setImage(token);
            if(professors[i] != -1)
                professorsGrids.get(professors[i]).add(tokenView, i, 0);
        }
    }

    public void setRooms(){

        Map<Integer, String> names = this.dataCollector.getUsernames();

        ArrayList<GridPane> roomGrids = new ArrayList<>();
        roomGrids.add(gridRoom1);
        roomGrids.add(gridRoom2);
        roomGrids.add(gridRoom3);
        roomGrids.add(gridRoom4);

        //double height = 9;

        for(Integer id: names.keySet()){

            int[] room = this.dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfRoom();

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
                        if (this.room == null) {
                            this.room = new ArrayList<>(5);
                            for (int i = 0; i < it.polimi.ingsw.Enum.Color.getNumberOfColors(); i++)
                                this.room.add(new ArrayList<>(10));
                        }
                        this.room.get(color.getIndex()).add(students, tokenView);
                    }
                    roomGrids.get(id).add(tokenView, color.getIndex(), students);
                }
            }

            if (id.equals(this.dataCollector.getId())) {
                this.actualRoom = roomGrids.get(id);
            }
        }
        disableRoom();
    }

    public void setEntrance(){

        Map<Integer, String> names = this.dataCollector.getUsernames();

        ArrayList<GridPane> entranceGrids = new ArrayList<>();
        entranceGrids.add(gridEntrance1);
        entranceGrids.add(gridEntrance2);
        entranceGrids.add(gridEntrance3);
        entranceGrids.add(gridEntrance4);

        //double height = 11;
        for(Integer id: names.keySet()){

            int[] entrance = dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfEntrance();
            //System.out.println("Entrance of player: " + names.get(id) + " " + Arrays.toString(entrance));
            GridPane entranceGrid = entranceGrids.get(id);
            int added = 0;
            for(it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){
                for(int student = 0; student < entrance[color.getIndex()]; student++){

                    ImageView tokenView = new ImageView();
                    tokenView.setFitHeight(20);
                    tokenView.setFitWidth(20);
                    Image token = convertTo3DCircle(color.getIndex(), 20);
                    tokenView.setImage(token);

                    // Circle c = new Circle(height);
                    // c.setFill(convertColor(color.getIndex()));

                    if (added == 4) //position (4, 0) not allowed
                        added++;

                    int column = added % entranceGrid.getColumnCount();
                    int row = added / entranceGrid.getColumnCount();

                    //System.out.println("Color " + color.name() + " added " + added + " [ " + column + ", " + row + "]");
                    if(row > 1 || column > 4)
                        System.out.println("Entrance space exceeded!");

                    entranceGrid.add(tokenView, column, row);

                    if (id.equals(this.dataCollector.getId())){
                        tokenView.setUserData(color);
                        if (this.entrance == null) {
                            this.entrance = new ArrayList<>(9);
                        }
                        this.entrance.add(tokenView);
                    }
                    added++;
                }
            }

            if (id.equals(this.dataCollector.getId())) {
                this.actualEntrance = entranceGrid;
            }
        }
        disableEntrance();
    }

    public void setTowers(){

        Map<Integer, String> names = this.dataCollector.getUsernames();
        ModelMessage model = this.dataCollector.getModel();
        ArrayList<GridPane> towerGrids = new ArrayList<>();

        towerGrids.add(gridTowers1);
        towerGrids.add(gridTowers2);
        towerGrids.add(gridTowers3);
        towerGrids.add(gridTowers4);

        //double height = 13;

        for(Integer id: names.keySet()){

            PlayerSerializable p = model.getPlayerById(id);
            //Color color = convertTowerColor(p.getTowerColor());
            int towers = p.getSchool().getTowers();
            GridPane grid = towerGrids.get(id);

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

    public void displayMessage(String s){
        this.messageLabel.setText(s);
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

    private void enableEntrance (EventHandler<MouseEvent> handler){

        if (this.entrance == null)
            return;

        for (ImageView c : this.entrance){
            c.setDisable(false);
            c.setOnMouseClicked(handler);
            enabledEffect(c);
        }
    }

    private void disableEntrance() {
        if (this.entrance == null)
            return;

        for (ImageView c : this.entrance){
            c.setDisable(true);
            c.setOnMouseClicked(null);
            disabledEffect(c);
        }
    }

    public void enableStudentsDestination() {
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

    private void disableStudentsDestination() {
        if (this.actualRoom != null) {
            this.actualRoom.setOnMouseClicked(null);
            disabledEffect(this.actualRoom);
            disabledEffect(this.actualRoom.getParent());
        }

        disableIslands();
    }

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







    List<GridPane> usedClouds = null;

    @FXML
    private AnchorPane cloudAnchor1;
    @FXML
    private GridPane cloudGrid1;
    @FXML
    private AnchorPane cloudAnchor2;
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

    public void setClouds(){

        ModelMessage model = dataCollector.getModel();
        int num = model.getCurrentPlayerId();

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

        List<CloudSerializable> clouds =  model.getCloudList();
        List<GridPane> grids = new ArrayList<>(4);
        grids.add(this.cloudGrid1);
        grids.add(this.cloudGrid2);
        grids.add(this.cloudGrid3);
        grids.add(this.cloudGrid4);

        //System.out.println("Height = " + height);

        int c = 0;
        for(CloudSerializable cloud : clouds){

            GridPane grid = grids.get(c);
            grid.setUserData(cloud);

            int[] students = cloud.getDrawnStudents();
            //double height = 16;
            int added = 0;
            for (int i = 0; i < students.length; i++){

                for (int j = 0; j < students[i]; j++){
                    int col = (grid.getColumnCount() - 1);
                    //double height = this.cloudGrid1.getPrefHeight()/this.cloudGrid1.getRowCount();
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

            if (this.usedClouds == null)
                this.usedClouds = new ArrayList<>(num);

            this.usedClouds.add(grid);

            c++;
        }

        disableClouds();

        System.out.println("Clouds Set");
    }

    private void disableClouds() {
        if (this.usedClouds == null)
            return;

        for (GridPane g: this.usedClouds) {
            g.setOnMouseClicked(null);
            g.setDisable(true);
            disabledEffect(g);
            disabledEffect(g.getParent());
        }
    }

    private void activateCloud() {
        if (this.usedClouds == null)
            return;

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
            enabledEffectCard(g.getParent());
        }

        displayMessage("It is your turn, please click on the cloud you want to choose");
    }





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

    private List<GridPane> activeIslands = null;

    public void setIslands(){

        ModelMessage model = dataCollector.getModel();

        List<Island> islands = model.getIslandList();

        List<GridPane> grids = new ArrayList<>(12);
        grids.add(this.islandGrid1);
        grids.add(this.islandGrid2);
        grids.add(this.islandGrid3);
        grids.add(this.islandGrid4);
        grids.add(this.islandGrid5);
        grids.add(this.islandGrid6);
        grids.add(this.islandGrid7);
        grids.add(this.islandGrid8);
        grids.add(this.islandGrid9);
        grids.add(this.islandGrid10);
        grids.add(this.islandGrid11);
        grids.add(this.islandGrid12);

        List<AnchorPane> panes = new ArrayList<>(12);
        panes.add(this.islandAnchor1);
        panes.add(this.islandAnchor2);
        panes.add(this.islandAnchor3);
        panes.add(this.islandAnchor4);
        panes.add(this.islandAnchor5);
        panes.add(this.islandAnchor6);
        panes.add(this.islandAnchor7);
        panes.add(this.islandAnchor8);
        panes.add(this.islandAnchor9);
        panes.add(this.islandAnchor10);
        panes.add(this.islandAnchor11);
        panes.add(this.islandAnchor12);

        for (int i = 0; i < 12; i++){
            panes.get(i).setEffect(new DropShadow(10, Color.YELLOWGREEN));
            if (!model.isIslandIdValid(i)){
                grids.get(i).setDisable(true);
                grids.get(i).setVisible(false);
                panes.get(i).setDisable(true);
                panes.get(i).setVisible(false);
            }
            else if(dataCollector.getModel().getIslandFromId(i).isMerged()){
                resizeIsland((ImageView) panes.get(i).getChildren().get(0));
            }
        }

        Image image = new Image("/token/mothernature.png",16,16,false,false);
        ImageView imageView = new ImageView();
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        imageView.setImage(image);
        grids.get(model.getMotherNatureIslandId()).add(imageView, 0, 0);
        grids.get(model.getMotherNatureIslandId()).setEffect(new DropShadow(10, Color.ORANGERED));

        System.out.println("MotherNature Set");

        for(Island island : islands) {

            GridPane grid = grids.get(island.getId());
            grid.setUserData(island);

            /*      0                   1               2
            0   [ Mother Nature     Ban Cards       Towers]
                [                                         ]
            1   [ Blue              Purple          Yellow]
                [                                         ]
            2   [ Red               Green                 ]
            */

            if (model.getGameMode() == 1 && island.getBanCard() > 0){
                ImageView banCard = new ImageView();
                banCard.setFitHeight(16);
                banCard.setFitWidth(16);
                banCard.setImage(new Image("/token/token_apothecary.png", 16, 16, false, false));
                Label banCardLabel = new Label(String.valueOf(island.getBanCard()));
                banCardLabel.setTranslateY(-11);
                banCardLabel.setTranslateX(4);
                banCardLabel.setOpacity(1);
                banCardLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
                banCardLabel.setTextFill(Color.WHITE);
                grid.add(banCard, 1, 0);
                grid.add(banCardLabel, 1, 0);
            }

            if (island.getTowerCount() > 0){
                /*ImageView tower = new ImageView();
                tower.setFitHeight(16);
                tower.setFitWidth(16);
                tower.setImage(new Image("/token/token_apothecary.png", 16, 16, false, false));*/
                ImageView tower = new ImageView();
                tower.setFitWidth(16);
                Image token = convertTo3DTowerColor(island.getTowerColor(), 16);
                tower.setImage(token);
                Label towerLabel = new Label(String.valueOf(island.getTowerCount()));
                towerLabel.setTranslateY(-30);
                towerLabel.setTranslateX(4);
                towerLabel.setOpacity(1);
                towerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
                towerLabel.setTextFill(Color.WHITE);
                grid.add(tower, 2, 0);
                grid.add(towerLabel, 2, 0);
            }



            int[] students = island.getStudents();
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

            if (this.activeIslands == null)
                this.activeIslands = new ArrayList<>(islands.size());

            this.activeIslands.add(grid);
        }
        disableIslands();
    }

    public void resizeIsland(ImageView img){
        img.setEffect(new DropShadow(15, Color.GOLD));
        img.setScaleX(1.5);
        img.setScaleY(1.5);
    }

    public void enableIslands(EventHandler<MouseEvent> handler){
        if (this.activeIslands == null)
            return;

        for (GridPane g : this.activeIslands) {
            g.setOnMouseClicked(handler);
            g.setDisable(false);
            enabledEffect(g);
            enabledEffectCard(g.getParent());
        }
    }

    public void disableIslands(){
        if (this.activeIslands == null)
            return;

        for (GridPane g : this.activeIslands) {
            g.setOnMouseClicked(null);
            g.setDisable(true);
            disabledEffect(g);
            disabledEffect(g.getParent());
        }
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


    @FXML
    private Tab charactersTab;
    @FXML
    private AnchorPane charactersPane;
    @FXML
    private ScrollPane scrollPaneCharacter;
    @FXML
    private AnchorPane anchorPaneScrollPaneCharacter;

    private List<AnchorPane> characterPlayable = null;


    @FXML
    private AnchorPane cleric;
    @FXML
    private GridPane clericGrid;
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
    private Label labelApothecary;
    @FXML
    private ImageView banCardToken;
    @FXML
    private ImageView apothecaryCoin;

    @FXML
    private AnchorPane minotaur;
    @FXML
    private ImageView minotaurCoin;

    @FXML
    private AnchorPane jester;
    @FXML
    private GridPane jesterGrid;
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
    private ImageView cookActiveColor;

    @FXML
    private AnchorPane bard;
    @FXML
    private ImageView bardCoin;

    @FXML
    private AnchorPane princess;
    @FXML
    private GridPane princessGrid;
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


    private void setCharacters() {

        ModelMessage model = dataCollector.getModel();
        int gM = model.getGameMode();

        if (gM == 0) {
            this.charactersTab.setDisable(true);
            return;
        }

        List<CharacterSerializable> characters = model.getCharacterList();

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

        for (int i = 0; i < 12; i++){

            AnchorPane c = list.get(i);

            if (model.isCharacterIdValid(i)){
                if (this.characterPlayable == null)
                    this.characterPlayable = new ArrayList<>(characters.size());

                this.characterPlayable.add(c);
                CharacterSerializable characterSerializable = model.getCharacterById(i);
                c.setUserData(characterSerializable);

                setCharacter(characterSerializable, coins, list);
            }
            else {
                c.setVisible(false);
                c.setDisable(true);
            }
        }

        disableCharacters();
        System.out.println("Set Characters");
    }

    private List<ImageView> clericStudents = null;
    private List<ImageView> jesterStudents = null;
    private List<ImageView> princessStudents = null;

    private void setCharacter(CharacterSerializable c, List<ImageView> coins, List<AnchorPane> containers) {

        ModelMessage model = this.dataCollector.getModel();
        int id = c.getId();

        if (!model.isCharacterIdValid(id))
            return;

        if (!c.isUsed()){
            containers.get(id).getChildren().remove(coins.get(id));
        }

        switch (id){
            case 0 -> {
                int banCard = c.getBanCard();
                this.labelApothecary.setText("x" + banCard);
                this.labelApothecary.setDisable(false);
            }
            case 2 -> {
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
                }
                else {
                    containers.get(3).getChildren().remove(this.cookActiveColor);
                }
            }
            case 6 -> {
                // jester
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

    private void disableCharacters() {
        if (this.characterPlayable == null)
            return;

        for (Node c : this.characterPlayable){
            c.setOnMouseClicked(null);
            //c.setDisable(false);
            disabledEffect(c);
        }
    }

    private void setCharacterMove (CharacterSerializable characterSerializable, int[] obj){
        disableCharacters();
        this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played " + characterSerializable.getName(), characterSerializable.getId(), obj));
    }

    private void activateCharacter() {
        if (this.characterPlayable == null) {
            System.out.println("list of playable character null");
            return;
        }

        for (AnchorPane c : this.characterPlayable){

            //c.setDisable(false);
            c.setOnMouseClicked(mouseEvent -> {
                //((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                CharacterSerializable character = (CharacterSerializable) ((Node)mouseEvent.getSource()).getUserData();
                System.out.println("Selected character " + character.getName());

                askCharacterAttributes(character);
            });
            enabledEffectCard(c);
        }
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








    @FXML
    private Tab assistantTab;

    @FXML
    private AnchorPane assistantPane;

    private List<ImageView> assistantList = null;

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

    private void setAssistant() {

        this.lion.setUserData(Assistant.Lion);
        this.goose.setUserData(Assistant.Goose);
        this.cat.setUserData(Assistant.Cat);
        this.eagle.setUserData(Assistant.Eagle);
        this.fox.setUserData(Assistant.Fox);
        this.snake.setUserData(Assistant.Snake);
        this.octopus.setUserData(Assistant.Octopus);
        this.dog.setUserData(Assistant.Dog);
        this.elephant.setUserData(Assistant.Elephant);
        this.turtle.setUserData(Assistant.Turtle);


        int[] assistant = this.dataCollector.getModel().getPlayerById(this.dataCollector.getId()).getAssistantDeck();
        List<ImageView> list = new ArrayList<>(Assistant.getNumberOfAssistants());
        list.add(lion);
        list.add(goose);
        list.add(cat);
        list.add(eagle);
        list.add(fox);
        list.add(snake);
        list.add(octopus);
        list.add(dog);
        list.add(elephant);
        list.add(turtle);

        for (Assistant a : Assistant.values()){

            boolean find = false;

            for (int i : assistant){
                if (i == a.getValue()){
                    find = true;
                    break;
                }
            }

            if (!find){
                assistantPane.getChildren().remove(list.get(a.getValue() - 1));
                //list.get(a.getValue() -1).setVisible(false);
            }
            else {
                if (this.assistantList == null)
                    this.assistantList = new ArrayList<>(assistant.length);

                this.assistantList.add(list.get(a.getValue() - 1));
            }
        }

        disableAssistants();
        System.out.println("Set Assistant");
    }

    private void disableAssistants () {
        if (this.assistantList == null)
            return;

        for (ImageView a : this.assistantList){
            a.setOnMouseClicked(null);
            a.setDisable(true);
            disabledEffect(a);
        }
    }

    private void activateAssistant() {

        if (this.assistantList == null) {
            return;
        }

        DataCollector dC = this.dataCollector;
        EventHandler<MouseEvent> handler = mouseEvent -> {
            ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
            Assistant a = (Assistant) ((Node) mouseEvent.getSource()).getUserData();
            System.out.println("Selected assistant " + a.name());
            dC.setNextMove(new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", a.getValue()));
            disableAssistants();
        };

        for (ImageView a : this.assistantList){
            a.setDisable(false);
            a.setOnMouseClicked(handler);
            enabledEffectCard(a);
        }

        displayMessage("It is your turn, please select an assistant. Just click on It");
    }

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

    private void notYourTurn() {

        //todo do it better
        //could use a sound for it

        String musicFile = "sound/mixkit-correct-answer-tone-2870.wav";

        Media sound = new Media(getClass().getClassLoader().getResource(musicFile).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        displayMessage("It is not your turn, please wait");
    }

    private void gameOver() {
        String message = this.dataCollector.getStandardWinMessage(); //todo use a better one

        super.main.gameOver(message);
    }
}
