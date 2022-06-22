package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import it.polimi.ingsw.Enum.Assistant;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Enum.Phases.ActionPhase;
import it.polimi.ingsw.Enum.Phases.Phase;
import it.polimi.ingsw.Message.ModelMessage.CharacterSerializable;
import it.polimi.ingsw.Message.ModelMessage.CloudSerializable;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.PlayAssistantMessage;
import it.polimi.ingsw.Message.PlayCharacterMessage;
import it.polimi.ingsw.Server.Model.Island;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Initialize Started");

        try {
            setUsernames();
            disableIfDreamMode();
            setClouds();
            setSchools();
            setIslands();
            setGameStatus();
            setAssistant();
            setCharacter();
        } catch (InterruptedException e) {
            System.err.println("Interrupted while loading data - initialize main game controller");
            e.printStackTrace();
            Thread.currentThread().interrupt(); //reset flag
            Platform.exit();
        }

        System.out.println("Elaborate Model");
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
            }
            else{
                this.coinsNumberLabel.setText(String.valueOf(dataCollector.getModel().getPlayerById(dataCollector.getId()).getCoins()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setGameStatus(){

        ModelMessage model = dataCollector.getModel();

        if (model.getGameMode() == 0)
            playedCharacterLabel.setVisible(false);
        else {
            CharacterSerializable c = model.getCharacterById(model.getActiveCharacterId());
            if (c == null)
                playedCharacterLabel.setText("No active character");
            else
                playedCharacterLabel.setText(c.getName());
        }

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
    private Rectangle rectangle3;
    @FXML
    private Rectangle color3;
    @FXML
    private Rectangle rectangle4;
    @FXML
    private Rectangle color4;

    public void setUsernames() throws InterruptedException {

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

        switch (playerNumber){
            case 2 -> {
                disable3userName();
                disable4userName();
            }
            case 3 -> disable4userName();
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

        glowCurrent(names);
    }

    private void disable3userName(){
        this.color3.setVisible(false);
        this.color3.setDisable(true);
        this.rectangle3.setVisible(false);
        this.username3.setVisible(false);
        this.label3.setVisible(false);
    }

    private void disable4userName(){
        this.color4.setVisible(false);
        this.color4.setDisable(true);
        this.rectangle4.setVisible(false);
        this.username4.setVisible(false);
        this.label4.setVisible(false);
    }

    public void glowCurrent(List<Label> usernameLabels){

        int curr = this.dataCollector.getIdOfCurrentPlayer();
        System.out.println("Current is " + curr);

        for(Label label : usernameLabels){
            if (label.getText().equals(dataCollector.getUsernameOfCurrentPlayer())){
                label.setEffect(new DropShadow(10, Color.GOLD));
            }
            else label.setEffect(null);
        }
    }



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
        }

        if (playerNumber == 2){
            disable3School();
            disable4School();
        }
        if (playerNumber == 3)
            disable4School();

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
        int[] room;
        int numOfPlayers = this.dataCollector.getModel().getPlayerNumber();
        ArrayList<GridPane> roomGrids = new ArrayList<>();
        roomGrids.add(gridRoom1);
        roomGrids.add(gridRoom2);
        roomGrids.add(gridRoom3);
        roomGrids.add(gridRoom4);
        double height = this.gridRoom1.getPrefHeight()/40;
        for(int id=0; id<numOfPlayers; id++){
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
        int[] entrance;
        int numOfPlayers = this.dataCollector.getModel().getPlayerNumber();
        ArrayList<GridPane> entranceGrids = new ArrayList<>();
        entranceGrids.add(gridEntrance1);
        entranceGrids.add(gridEntrance2);
        entranceGrids.add(gridEntrance3);
        entranceGrids.add(gridEntrance4);
        double height = this.gridEntrance1.getPrefHeight()/8;
        for(int id = 0; id<numOfPlayers; id++){
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

    private void disable3School() {
        this.tab3.setDisable(true);
        //todo consider remove completely the tab from his ancestor
    }

    private void disable4School() {
        this.tab4.setDisable(true);
        //todo consider remove completely the tab from his ancestor
    }









    public void setTowers(){
        int numOfPlayers = this.dataCollector.getModel().getPlayerNumber();
        ArrayList<GridPane> towerGrids = new ArrayList<>();
        towerGrids.add(gridTowers1);
        towerGrids.add(gridTowers2);
        towerGrids.add(gridTowers3);
        towerGrids.add(gridTowers4);
        double height = this.gridTowers1.getPrefHeight()/8;
        for(int id=0; id<numOfPlayers; id++){
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
        return switch (id) {
            case 0 -> Color.BLACK;
            case 1 -> Color.WHITE;
            case 2 -> Color.GREY;
            default -> Color.GOLD;
        };
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
        ArrayList<CloudSerializable> clouds = (ArrayList<CloudSerializable>) this.dataCollector.getModel().getCloudList();
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
                    // ----- Add Image -----
                    Image image = new Image("/token/circle_yellow.png");
                    ImageView c = new ImageView(image);
                    // --- End Add Image ---
                    // Circle c = new Circle(height);
                    // c.setFill(convertColor(color));
                    grids.get(cloud).add(c, column, row);
                    System.out.println("Added color[" + color + "] student in cloud " + cloud + " at column " + column + ", row " + row);
                }
            }
        }

    public Color convertColor(int id){
        return switch (id) {
            case 0 -> Color.DEEPSKYBLUE;
            case 1 -> Color.GREENYELLOW;
            case 2 -> Color.PURPLE;
            case 3 -> Color.RED;
            case 4 -> Color.GOLD;
            default -> Color.PAPAYAWHIP;
        };
    }

    public void setIslands(){
        List<Island> islands = this.dataCollector.getModel().getIslandList();
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

    private List<ImageView> ownedAssistant = null;

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

        int[] assistant = this.dataCollector.getModel().getPlayerById(this.dataCollector.getId()).getAssistantDeck();

        List<ImageView> list = new ArrayList<>(Assistant.getNumberOfAssistants());
        list.add(0, lion);
        list.add(1, goose);
        list.add(2, cat);
        list.add(3, eagle);
        list.add(4, fox);
        list.add(5, snake);
        list.add(6, octopus);
        list.add(7, dog);
        list.add(8, elephant);
        list.add(9, turtle);

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
                if (this.ownedAssistant == null)
                    this.ownedAssistant = new ArrayList<>(assistant.length);

                this.ownedAssistant.add(list.get(a.getValue() - 1));
            }
        }

        disableAssistants();
        System.out.println("Set Assistant");
    }

    private void disableAssistants () {
        if (this.ownedAssistant == null)
            return;

        for (ImageView a : this.ownedAssistant){
            a.setOnMouseClicked(null);
            a.setDisable(true);
        }
    }
    private void activateAssistant() {

        if (this.ownedAssistant == null) {
            return;
        }


        DataCollector dC = this.dataCollector;

        for (ImageView a : this.ownedAssistant){

            a.setDisable(false);
            a.setOnMouseClicked(mouseEvent -> {
                String name = a.getId();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                System.out.println("Selected assistant " + name);
                Assistant c = Assistant.valueOf(name);
                dC.setNextMove(new PlayAssistantMessage(Errors.NO_ERROR, "Played Assistant", c.getValue()));

                disableAssistants();
            });
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

    private void notYourTurn() {

        //todo do it better

        super.main.displayMessage("It is not your turn, please wait");
    }

    private void gameOver() {
        String message = this.dataCollector.getStandardWinMessage(); //todo use a better one

        super.main.gameOver(message);
    }
}
