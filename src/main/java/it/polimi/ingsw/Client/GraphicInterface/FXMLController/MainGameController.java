package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.PlayerSerializable;
import it.polimi.ingsw.Server.Model.Island;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;


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

    public Image convertColor(int id, double size) {
        return switch (id) {
            case 0 -> new Image("/token/circle_pawn_blue.png", size, size, false, false);
            case 1 -> new Image("/token/circle_pawn_pink.png", size, size, false, false);
            case 2 -> new Image("/token/circle_pawn_yellow.png", size, size, false, false);
            case 3 -> new Image("/token/circle_pawn_red.png", size, size, false, false);
            case 4 -> new Image("/token/circle_pawn_green.png", size, size, false, false);
            default -> null;
        };
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initialize Started");
        super.main.getMainStage().getScene().setCursor(Cursor.WAIT);

        setGameStatus();
        setUsernames();
        setClouds();
        setSchools();
        setIslands();
        setAssistant();
        setCharacters();

        System.out.println("Elaborate Model");
        elaborateModel();
        super.main.getMainStage().getScene().setCursor(Cursor.DEFAULT);
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

        Assistant a = Assistant.getAssistantByValue(model.getPlayerById(this.dataCollector.getId()).getActiveAssistant());
        if (a != null) {
            int max = a.getMaxMovement();
            if (model.getActiveCharacterId() == 9)
                max += 2;
            this.stepsLabel.setText("Max " + max + " steps");
        }
        else
            this.stepsLabel.setText("No active Assistant at the moment");

        int[] bag = dataCollector.getModel().getBag();
        leftBlue.setText(String.valueOf(bag[0]));
        leftGreen.setText(String.valueOf(bag[1]));
        leftPink.setText(String.valueOf(bag[2]));
        leftRed.setText(String.valueOf(bag[3]));
        leftYellow.setText(String.valueOf(bag[4]));
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

        if (playerNumber < 4){
            this.color4.setVisible(false);
            this.color4.setDisable(true);
            this.rectangle4.setVisible(false);
            this.username4.setVisible(false);
            this.label4.setVisible(false);
        }
        if (playerNumber < 3){
            this.color3.setVisible(false);
            this.color3.setDisable(true);
            this.rectangle3.setVisible(false);
            this.username3.setVisible(false);
            this.label3.setVisible(false);
        }


        for (Integer i : usernames.keySet()){

            names.get(i).setText(usernames.get(i));

            Assistant a = Assistant.getAssistantByValue(model.getPlayerById(i).getActiveAssistant());
            String text;
            if (a != null)
                text = "Active assistant: " + a.name() + " value: " + a.getValue();
            else
                text = "No active Assistant";

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

    private Tab actualTab = null;
    private GridPane actualRoom = null;
    private List<List<Circle>> room = null;
    private GridPane actualEntrance = null;
    private List<Circle> entrance = null;

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
            Circle c = new Circle(height);
            c.setFill(convertColor(i));
            if(professors[i] != -1)
                professorsGrids.get(professors[i]).add(c, i, 0);
        }
    }

    public void setRooms(){

        Map<Integer, String> names = this.dataCollector.getUsernames();

        ArrayList<GridPane> roomGrids = new ArrayList<>();
        roomGrids.add(gridRoom1);
        roomGrids.add(gridRoom2);
        roomGrids.add(gridRoom3);
        roomGrids.add(gridRoom4);

        double height = 9;

        for(Integer id: names.keySet()){

            int[] room = this.dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfRoom();

            for(it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){

                for(int students = 0; students < room[color.getIndex()]; students++){
                    Circle c = new Circle(height);
                    c.setFill(convertColor(color.getIndex()));


                    //if this in the player room set the color data to each circle and add all of them to a matrix
                    if (id.equals(this.dataCollector.getId())){
                        c.setUserData(color);
                        if (this.room == null) {
                            this.room = new ArrayList<>(5);
                            for (int i = 0; i < it.polimi.ingsw.Enum.Color.getNumberOfColors(); i++)
                                this.room.add(new ArrayList<>(10));
                        }
                        this.room.get(color.getIndex()).add(students, c);
                    }
                    roomGrids.get(id).add(c, color.getIndex(), students);
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

        double height = 11;
        for(Integer id: names.keySet()){

            int[] entrance = dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfEntrance();
            //System.out.println("Entrance of player: " + names.get(id) + " " + Arrays.toString(entrance));
            GridPane entranceGrid = entranceGrids.get(id);
            int added = 0;
            for(it.polimi.ingsw.Enum.Color color: it.polimi.ingsw.Enum.Color.values()){
                for(int student = 0; student < entrance[color.getIndex()]; student++){

                    Circle c = new Circle(height);
                    c.setFill(convertColor(color.getIndex()));

                    if (added == 4) //position (4, 0) not allowed
                        added++;

                    int column = added % entranceGrid.getColumnCount();
                    int row = added / entranceGrid.getColumnCount();

                    //System.out.println("Color " + color.name() + " added " + added + " [ " + column + ", " + row + "]");
                    if(row > 1 || column > 4)
                        System.out.println("Entrance space exceeded!");

                    entranceGrid.add(c, column, row);

                    if (id.equals(this.dataCollector.getId())){
                        c.setUserData(color);
                        if (this.entrance == null) {
                            this.entrance = new ArrayList<>(9);
                        }
                        this.entrance.add(c);
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

        double height = 13;

        for(Integer id: names.keySet()){

            PlayerSerializable p = model.getPlayerById(id);
            Color color = convertTowerColor(p.getTowerColor());
            int towers = p.getSchool().getTowers();
            GridPane grid = towerGrids.get(id);

            for(int i = 0; i < towers; i++){
                Circle c = new Circle(height);
                c.setFill(color);
                int column = i % grid.getColumnCount();
                int row = i / grid.getColumnCount();
                grid.add(c, column, row);

                if(column > 3 || row > 1)
                    System.out.println("Tower space exceeded!");
            }
        }

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

        super.main.displayMessage("It is your turn, please select a students from your entrance. Just click on It ( " + this.dataCollector.getModel().getStudentsToMove() + " students remaining)");
    }

    private void enableEntrance (EventHandler<MouseEvent> handler){

        if (this.entrance == null)
            return;

        for (Circle c : this.entrance){
            c.setDisable(false);
            c.setOnMouseClicked(handler);
        }
    }

    private void disableEntrance() {
        if (this.entrance == null)
            return;

        for (Circle c : this.entrance){
            c.setDisable(true);
            c.setOnMouseClicked(null);
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

        this.actualRoom.setOnMouseClicked(handler);

        handler = mouseEvent -> {
            ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
            Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
            dC.setNextMove(new MoveStudentMessage(Errors.NO_ERROR, "Moved Student", colorChoose.getIndex(), i.getId()));
            disableStudentsDestination();
        };

        enableIslands(handler);

        super.main.displayMessage("Please select where to put the students: on an island or on your room.");
    }

    private void disableStudentsDestination() {
        if (this.actualRoom != null)
            this.actualRoom.setOnMouseClicked(null);

        disableIslands();
    }

    private void enableRoom (EventHandler<MouseEvent> handler) {
        if (this.room == null)
            return;

        for (List<Circle> l : this.room){
            for (Circle c : l){
                c.setDisable(false);
                c.setOnMouseClicked(handler);
            }
        }
    }

    private void disableRoom() {
        if (this.room == null)
            return;

        for (List<Circle> list: this.room){
            for (Circle c : list){
                c.setDisable(true);
                c.setOnMouseClicked(null);
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
                    tokenView.setFitHeight(32);
                    tokenView.setFitWidth(32);
                    Image token = convertColor(i, 32);
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
        }

        super.main.displayMessage("It is your turn, please click on the cloud you want to choose");
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

    private List<GridPane> activeIsland = null;

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
            if (!model.isIslandIdValid(i)){
                grids.get(i).setDisable(true);
                grids.get(i).setVisible(false);
                panes.get(i).setDisable(true);
                panes.get(i).setVisible(false);
            }
        }

        Image image = new Image("/token/mothernature.png",32,32,false,false);
        ImageView imageView = new ImageView();
        imageView.setFitHeight(32);
        imageView.setFitWidth(32);
        imageView.setImage(image);
        grids.get(model.getMotherNatureIslandId()).add(imageView, 0, 0);

        System.out.println("MotherNature Set");

        for(Island island : islands) {

            GridPane grid = grids.get(island.getId());
            grid.setUserData(island);

            int[] students = island.getStudents();
            int added = 1; // posion 0, 0 is only for mother nature
            for (int i = 0; i < students.length; i++) {
                for (int j = 0; j < students[i]; j++) {

                    int column = added % grid.getColumnCount();
                    int row = added / grid.getColumnCount();
                    ImageView tokenView = new ImageView();
                    tokenView.setFitHeight(32);
                    tokenView.setFitWidth(32);
                    Image token = convertColor(i, 32);
                    tokenView.setImage(token);
                    // Circle circle = new Circle(32);
                    // circle.setFill(convertColor(i));
                    grid.add(tokenView, column, row);
                    added++;
                }
            }

            if (this.activeIsland == null)
                this.activeIsland = new ArrayList<>(islands.size());

            this.activeIsland.add(grid);
        }
        disableIslands();
    }

    public void enableIslands(EventHandler<MouseEvent> handler){
        if (this.activeIsland == null)
            return;

        for (GridPane g : this.activeIsland) {
            g.setOnMouseClicked(handler);
            g.setDisable(false);
        }
    }

    public void disableIslands(){
        if (this.activeIsland == null)
            return;

        for (GridPane g : this.activeIsland) {
            g.setOnMouseClicked(null);
            g.setDisable(true);
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

        super.main.displayMessage("It is your turn, please click on the island where you want to move Mother Nature");
    }


    @FXML
    private Tab charactersTab;

    @FXML
    private AnchorPane charactersPane;

    private List<Node> characterPlayable = null;

    @FXML
    private ImageView cook;

    @FXML
    private ImageView knight;

    @FXML
    private AnchorPane jesterPane;
    @FXML
    private ImageView jester;
    @FXML
    private GridPane jesterGrid;

    @FXML
    private ImageView minotaur;

    @FXML
    private AnchorPane apothecaryPane;
    @FXML
    private ImageView apothecary;
    @FXML
    private Label labelApothecary;
    @FXML
    private ImageView bancardToken;

    @FXML
    private ImageView postman;

    @FXML
    private ImageView herald;

    @FXML
    private AnchorPane clericPane;
    @FXML
    private ImageView cleric;
    @FXML
    private GridPane clericGrid;

    @FXML
    private ImageView drunkard;

    @FXML
    private ImageView thief;

    @FXML
    private AnchorPane princessPane;
    @FXML
    private ImageView princess;
    @FXML
    private GridPane princessGrid;

    @FXML
    private ImageView bard;


    private void setCharacters() {

        ModelMessage model = dataCollector.getModel();
        int gM = model.getGameMode();

        if (gM == 0) {
            this.charactersTab.setDisable(true);
            return;
        }

        List<CharacterSerializable> characters = model.getCharacterList();

        List<Node> list = new ArrayList<>(12);
        list.add(0, apothecaryPane);
        list.add(1, bard);
        list.add(2, clericPane);
        list.add(3, cook);
        list.add(4, drunkard);
        list.add(5, herald);
        list.add(6, jesterPane);
        list.add(7, knight);
        list.add(8, minotaur);
        list.add(9, postman);
        list.add(10, princessPane);
        list.add(11, thief);

        for (int i = 0; i < 12; i++){

            if (model.isCharacterIdValid(i)){
                if (this.characterPlayable == null)
                    this.characterPlayable = new ArrayList<>(characters.size());

                this.characterPlayable.add(list.get(i));
                list.get(i).setUserData(model.getCharacterById(i));

                setCharacter(i);
            }
            else
                this.charactersPane.getChildren().remove(list.get(i));
        }

        disableCharacters();
        System.out.println("Set Characters");
    }

    private List<ImageView> clericStudents = null;
    private List<ImageView> jesterStudents = null;
    private List<ImageView> princessStudents = null;

    private void setCharacter(int id) {

        ModelMessage model = this.dataCollector.getModel();
        CharacterSerializable c = model.getCharacterById(id);

        if (c == null)
            return;

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
                        Image token = convertColor(color.getIndex(), 32);
                        tokenView.setImage(token);
                        this.clericGrid.add(tokenView, column, row);
                        if (this.clericStudents == null)
                            this.clericStudents = new ArrayList<>(4);

                        this.clericStudents.add(tokenView);
                        added++;
                    }
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
                        Image token = convertColor(color.getIndex(), 32);
                        tokenView.setImage(token);
                        this.jesterGrid.add(tokenView, column, row);
                        if (this.jesterStudents == null)
                            this.jesterStudents = new ArrayList<>(6);

                        this.jesterStudents.add(tokenView);
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
                        Image token = convertColor(color.getIndex(), 32);
                        tokenView.setImage(token);
                        this.princessGrid.add(tokenView, column, row);
                        if (this.princessStudents == null)
                            this.princessStudents = new ArrayList<>(4);

                        this.princessStudents.add(tokenView);
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
            c.setDisable(true);
        }
    }

    private void activateCharacter() {
        if (this.characterPlayable == null)
            return;

        for (Node c : this.characterPlayable){

            c.setDisable(false);
            c.setOnMouseClicked(mouseEvent -> {
                ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                CharacterSerializable character = (CharacterSerializable) ((Node)mouseEvent.getSource()).getUserData();
                System.out.println("Selected character " + character.getName());

                askCharacterAttributes(character);
            });
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

        DataCollector dC = this.dataCollector;
        EventHandler<MouseEvent> handler;

        if (characterId != 6 && characterId != 1)
            disableCharacters();
        //todo all character effects

        switch (characterId){
            case 0 -> {
                //apothecary - ban card

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose Island for apothecary  " + i.getId());
                    int[] obj = new int[1];
                    obj[0] = i.getId();
                    dC.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played " + character.getName(), characterId, obj));
                    disableIslands();
                };

                enableIslands(handler);

                super.main.displayMessage("Please click on the island where put the ban card");

                return;
            }
            case 1 -> {
                //bard - swap between entrance and room

                int[] e = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfEntrance();
                int[] r = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfRoom();

                if (Arrays.stream(e).sum() < 1 || Arrays.stream(r).sum() < 1){
                    super.main.displayMessage("Cannot play Bard because you don't have enough students in your room");
                    return;
                }

                disableCharacters();

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " from entrance for bard Effect");
                    bardEffect(color, null);
                };

                enableEntrance(handler);

                super.main.displayMessage("Click on the students from your entrance (max 2), then click on the students in the room for swap them");

                return;
            }
            case 2 -> {
                //cleric - move a students from the character to an island

                if (this.clericStudents == null)
                    return;

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " from cleric for cleric Effect");
                    clericEffect(color, null);
                };

                for (ImageView c: this.clericStudents){
                    c.setOnMouseClicked(handler);
                }

                return;
            }
            case 3 -> {
                //cook - color that not count as influence
                return; //todo possible to use the 5 professor on the left to pick the color
            }
            case 5 -> {
                //herald calc influence of an island

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    Island i = (Island) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose Island for herald  " + i.getId());
                    int[] obj = new int[1];
                    obj[0] = i.getId();
                    dC.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played " + character.getName(), characterId, obj));
                    disableIslands();
                };

                enableIslands(handler);

                super.main.displayMessage("Please click on the island where calc the influence");

                return;
            }
            case 6 -> {
                //jester - swap between jester and entrance

                int[] e = model.getPlayerById(this.dataCollector.getId()).getSchool().getCopyOfEntrance();

                if (Arrays.stream(e).sum() < 1){
                    super.main.displayMessage("Cannot play Jester because you don't have enough students in your entrance");
                    return;
                }

                if (this.jesterStudents == null)
                    return;

                disableCharacters();

                handler = mouseEvent -> {
                    ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                    it.polimi.ingsw.Enum.Color color = (it.polimi.ingsw.Enum.Color) ((Node) mouseEvent.getSource()).getUserData();
                    System.out.println("Choose color " + color + " from jester for jester Effect");
                    jesterEffect(color, null);
                };

                for (ImageView c: this.jesterStudents){
                    c.setOnMouseClicked(handler);
                }


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
                    dC.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played " + character.getName(), characterId, obj));
                    disablePrincessEffect();
                };

                for (ImageView c: this.princessStudents){
                    c.setOnMouseClicked(handler);
                }

                return;
            }
            case 11 -> {
                //thief - choose a color
                return; //todo possible to use the 5 professor on the left to pick the color
            }
        }

        this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played " + character.getName(), characterId, null));
    }

    private boolean checkCoins(CharacterSerializable character) {

        int cost = character.getCost();
        if (character.isUsed())
            cost++;

        int coins = this.dataCollector.getModel().getPlayerById(this.dataCollector.getId()).getCoins();

        if (coins < cost) {
            super.main.displayMessage("Sorry you don't have enough coins to play the " + character.getName() + " its cost is " + cost + " and your coins are " + coins);
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
                this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Bard", 1, bardObject));
            }
            else { //choose 2 color
                if (bardObject[1] == -1){ // first setted
                    bardObject[1] = room.getIndex();
                }
                else { //last setted
                    bardObject[3] = room.getIndex();
                    disableRoom();
                    this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Bard", 1, bardObject));
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
            clericObject[0] = colorChoose.getIndex();
            disableClericEffect();

            EventHandler<MouseEvent> handler = mouseEvent -> {
                ((Node) mouseEvent.getSource()).setOnMouseClicked(null);
                Island is = (Island) ((Node) mouseEvent.getSource()).getUserData();
                System.out.println("Choose island " + is.getId() + " for cleric Effect");
                clericEffect(null, i);
            };

            enableIslands(handler);
        }
        else if (clericObject != null && color == null && i != null){
            clericObject[1] = i.getId();
            this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Cleric", 2, clericObject));
            disableIslands();
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
            }
            else if (length == 4){
                if (jesterObject[1] == -1)
                    jesterObject[1] = entrance.getIndex();
                else{
                    jesterObject[3] = entrance.getIndex();
                    this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played Jester", 6, jesterObject));
                    disableEntrance();
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
        }
    }

    private void disablePrincessEffect() {

        if (this.princessStudents == null)
            return;

        for (ImageView c: this.princessStudents){
            c.setOnMouseClicked(null);
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
        }

        super.main.displayMessage("It is your turn, please select an assistant. Just click on It");
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

    private void notYourTurn() {

        //todo do it better

        super.main.displayMessage("It is not your turn, please wait");
    }

    private void gameOver() {
        String message = this.dataCollector.getStandardWinMessage(); //todo use a better one

        super.main.gameOver(message);
    }
}
