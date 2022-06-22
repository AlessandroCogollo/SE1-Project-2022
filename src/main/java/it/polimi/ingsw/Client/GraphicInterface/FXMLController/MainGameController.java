package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.*;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Message.ChooseCloudMessage;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.PlayerSerializable;
import it.polimi.ingsw.Message.PlayAssistantMessage;
import it.polimi.ingsw.Message.PlayCharacterMessage;
import it.polimi.ingsw.Server.Model.Island;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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
            case 1 -> Color.GREENYELLOW;
            case 2 -> Color.PURPLE;
            case 3 -> Color.RED;
            case 4 -> Color.GOLD;
            default -> Color.TRANSPARENT;
        };
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initialize Started");

        setGameStatus();
        setUsernames();
        setClouds();
        setSchools();
        setIslands();
        setAssistant();
        setCharacter();

        System.out.println("Elaborate Model");
        elaborateModel();
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
        if (a != null)
            this.stepsLabel.setText("Max " + a.getMaxMovement() + " steps");
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

        System.out.println("School Set");
    }

    public void setProfessors(){
        int[] professors = dataCollector.getModel().getProfessorsList();

        ArrayList<GridPane> professorsGrids = new ArrayList<>(4);
        professorsGrids.add(gridProfessors1);
        professorsGrids.add(gridProfessors2);
        professorsGrids.add(gridProfessors3);
        professorsGrids.add(gridProfessors4);

        double height = this.gridProfessors1.getPrefHeight()/4;

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

        double height = this.gridRoom1.getPrefHeight()/40;

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

    public void setEntrance(){

        Map<Integer, String> names = this.dataCollector.getUsernames();

        ArrayList<GridPane> entranceGrids = new ArrayList<>();
        entranceGrids.add(gridEntrance1);
        entranceGrids.add(gridEntrance2);
        entranceGrids.add(gridEntrance3);
        entranceGrids.add(gridEntrance4);

        double height = this.gridEntrance1.getPrefHeight()/8;
        for(Integer id: names.keySet()){

            int[] entrance = dataCollector.getModel().getPlayerById(id).getSchool().getCopyOfEntrance();
            System.out.println("Entrance of player: " + names.get(id) + " " + Arrays.toString(entrance));
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

                    System.out.println("Color " + color.name() + " added " + added + " [ " + column + ", " + row + "]");
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

    private void disableEntrance() {
        if (this.entrance == null)
            return;

        for (Circle c : this.entrance){
            c.setDisable(true);
            c.setOnMouseClicked(null);
        }
    }

    public void setTowers(){

        Map<Integer, String> names = this.dataCollector.getUsernames();
        ModelMessage model = this.dataCollector.getModel();
        ArrayList<GridPane> towerGrids = new ArrayList<>();

        towerGrids.add(gridTowers1);
        towerGrids.add(gridTowers2);
        towerGrids.add(gridTowers3);
        towerGrids.add(gridTowers4);

        double height = this.gridTowers1.getPrefHeight()/8;

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

            int added = 0;
            for (int i = 0; i < students.length; i++){

                for (int j = 0; j < students[i]; j++){
                    double height = (grid.getPrefHeight() + grid.getPrefWidth()) / 16;
                    //double height = this.cloudGrid1.getPrefHeight()/this.cloudGrid1.getRowCount();
                    int column = added % grid.getColumnCount();
                    int row = added / grid.getColumnCount();
                    Circle circle = new Circle(height);
                    circle.setFill(convertColor(i));
                    grid.add(circle, column, row);
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
                CloudSerializable c = (CloudSerializable) g.getUserData();
                dC.setNextMove(new ChooseCloudMessage(Errors.NO_ERROR, "Cloud choosed", c.getId()));
                disableClouds();
            });
        }

        super.main.displayMessage("It is your turn, please click on the cloud you want to choose");
    }








    public void setIslands(){
        List<Island> islands = this.dataCollector.getModel().getIslandList();
        //todo
    }





    @FXML
    private Tab charactersTab;

    @FXML
    private AnchorPane charactersPane;

    private List<ImageView> characterPlayable = null;

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


    private void setCharacter() {
        ModelMessage model = dataCollector.getModel();
        int gM = model.getGameMode();

        if (gM == 0) {
            this.charactersTab.setDisable(true);
            return;
        }



        List<CharacterSerializable> characters = model.getCharacterList();

        List<ImageView> list = new ArrayList<>(12);
        list.add(0, apothecary);
        list.add(1, bard);
        list.add(2, cleric);
        list.add(3, cook);
        list.add(4, drunkard);
        list.add(5, herald);
        list.add(6, jester);
        list.add(7, knight);
        list.add(8, minotaur);
        list.add(9, postman);
        list.add(10, princess);
        list.add(11, thief);

        for (int i = 0; i < 12; i++){

            boolean find = false;

            for (CharacterSerializable character : characters) {
                if (character.getId() == i) {
                    find = true;
                    break;
                }
            }

            if (!find || model.getActiveCharacterId() == i){
                this.charactersPane.getChildren().remove(list.get(i));
            }
            else {
                if (this.characterPlayable == null)
                    this.characterPlayable = new ArrayList<>(characters.size());

                this.characterPlayable.add(list.get(i));
            }
        }

        disableCharacters();
        System.out.println("Set Characters");
    }

    private void disableCharacters() {
        if (this.characterPlayable == null)
            return;

        for (ImageView c : this.characterPlayable){
            c.setOnMouseClicked(null);
            c.setDisable(true);
        }
    }
    private void activateCharacter() {
        if (this.characterPlayable == null)
            return;

        for (ImageView c : this.characterPlayable){

            c.setDisable(false);
            c.setOnMouseClicked(mouseEvent -> {

                String name = c.getId();
                System.out.println("Selected character " + name);

                askCharacterAttributes(name);
                disableCharacters();
            });
        }
    }

    private void askCharacterAttributes(String name) {

        ModelMessage model = dataCollector.getModel();

        int characterId = model.getCharacterIdFromName(name);
        CharacterSerializable character = model.getCharacterById(characterId);

        if (character == null){
            return;
        }


        //todo all character effects

        switch (characterId){
            case 0 -> {
                //apothecary - ban card
                return; //todo could only enable islands like for move mother nature
            }
            case 1 -> {
                //bard - swap between entrance and room
                return; //todo
            }
            case 2 -> {
                //cleric - move a students from the character to an island
                return; //todo
            }
            case 3 -> {
                //cook - color that not count as influence
                return; //todo possible to use the 5 professor on the left to pick the color
            }
            case 5 -> {
                //herald calc influence of an island
                return; //todo could only enable islands like for move mother nature
            }
            case 6 -> {
                //jester - swap between jester and entrance
                return; //todo
            }
            case 10 -> {
                //princess - add a students from this character to your room
                return; //todo
            }
            case 11 -> {
                //thief - choose a color
                return; //todo possible to use the 5 professor on the left to pick the color
            }
        }

        this.dataCollector.setNextMove(new PlayCharacterMessage(Errors.NO_ERROR, "Played character", characterId, null));
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
            Assistant a = (Assistant) ((Node) mouseEvent.getSource()).getUserData();
            System.out.println("Selected assistant " + a.name());
            dC.setNextMove(new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", a.getValue()));
            disableAssistants();
        };

        for (ImageView a : this.assistantList){
            a.setDisable(false);
            a.setOnMouseClicked(handler);
        }

        System.out.println("Gui Activated assistant");
        super.main.displayMessage("It is your turn, please select an assistant. Just click on It");
        System.out.println("Gui Activated assistant and printed message");
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

    private void activateMotherNatureMove() {
        //todo enable island click
        super.main.displayMessage("It is your turn, please click on the island where you want to move Mother Nature");
    }

    private void activateStudentsMove() {
        //todo enable only the entrance, then the other message will popup after the player has choose the students
        super.main.displayMessage("It is your turn, please select a students from your entrance. Just click on It");
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
