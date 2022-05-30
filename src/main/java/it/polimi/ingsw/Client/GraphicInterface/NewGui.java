package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class NewGui extends Application implements Graphic {


    private static Stage stage;
    private Scene scene;
    private Parent parent;
    private static String username = null;
    private static Wizard wizard;
    private static Wizard tempWizard;
    private static int gameMode = 0;
    private static int numOfPlayers = -1;
    private final static Object lock = new Object();
    private static int first = -1;

    public static Stage getStage(){
        return stage;
    }

    public TextField getUsernameInput() {
        return usernameInput;
    }

    public static Wizard getTempWizard() {
        return tempWizard;
    }

    public static int getNumOfPlayers() {
        return numOfPlayers;
    }


    @FXML
    private Button StartGameButton;

    @FXML
    private TextField usernameInput;

    @FXML
    private Slider SliderNOP;

    @FXML
    private ToggleButton ExpertModeButton;

    @Override
    public void displayMessage(String message) {
        while(NewGui.getStage() == null){
            System.out.println("Waiting for the stage");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.initOwner(stage.getOwner());
            alert.showAndWait();
            if (message.contains("another") && message.contains("username")){
                username = null;
                usernamePage();
            }

        });
    }

    @Override
    public Wizard getWizard() {
        while(wizard == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return wizard;
    }

    @Override
    public String getUsername() {
        while(username == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    @Override
    public int getNumOfPLayer() {
        while(numOfPlayers == -1){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return numOfPlayers;
    }

    @Override
    public int getGameMode() {
        while(gameMode == -1){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gameMode;
    }

    @FXML
    private javafx.scene.image.ImageView sorcerer;

    @FXML
    private javafx.scene.image.ImageView king;

    @FXML
    private javafx.scene.image.ImageView witch;

    @FXML
    private javafx.scene.image.ImageView wise;

    @FXML
    private javafx.scene.image.ImageView flowersQueen;


    @FXML
    void SelectFlowersQueen(MouseEvent event) {
        flowersEntered(event);
        tempWizard = Wizard.Flowers_Queen;
        System.out.println("Flowers Queen selected");
    }

    @FXML
    void SelectKing(MouseEvent event) {
        kingEntered(event);
        tempWizard = Wizard.King;
        System.out.println("King selected");
    }

    @FXML
    void SelectSorcerer(MouseEvent event) {
        sorcererEntered(event);
        tempWizard = Wizard.Sorcerer;
        System.out.println("Sorcerer selected");
    }

    @FXML
    void SelectWise(MouseEvent event) {
        wiseEntered(event);
        tempWizard = Wizard.Wise;
        System.out.println("Wise selected");
    }

    @FXML
    void SelectWitch(MouseEvent event) {
        witchEntered(event);
        tempWizard = Wizard.Witch;
        System.out.println("Witch selected");
    }

    @FXML
    void flowersEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King))this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.flowersQueen.setEffect(new DropShadow(50, Color.FORESTGREEN));
    }

    @FXML
    void kingEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.king.setEffect(new DropShadow(50, Color.GOLD));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void sorcererEntered(MouseEvent event) {
        this.sorcerer.setEffect(new DropShadow(50, Color.GREENYELLOW));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wiseEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.wise.setEffect(new DropShadow(50, Color.DEEPSKYBLUE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void witchEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.witch.setEffect(new DropShadow(50, Color.VIOLET));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wizardExited(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @Override
    public PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
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

    }

    @Override
    public void displayModel(ModelMessage model) {

    }



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public synchronized void start(Stage stage) throws Exception {

        try {
            NewGui.stage = stage;
            this.parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("scenes/launch.fxml")));
            Scene scene = new Scene(parent);
            scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void usernamePage () {

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("scenes/username.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void setUsername(){
        username = usernameInput.getText();
        if(wizard == null){
            wizardPage();
        }
        else goToLobby();
    }

    public void wizardPage () {

        username = usernameInput.getText();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/wizard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void gameModePage() {

            // TODO: actual wizard choose
            // move to username page

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/gamemode.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

    }

    public void goToLobby(){
        wizard = tempWizard;
        synchronized (lock){
            while(first == -1){
                try {
                    System.out.println("Waiting in the lobby");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(first == 1) {
            System.out.println("You are the first player, going to game mode page");
            gameModePage();
        }
        else if(first == 0) {
            System.out.println("You aren't the first player, going to main game page");
            moveToMainGame();
        }
    }

    public void SetFirst(boolean f){
        synchronized (lock){
            if(f) first = 1;
            else first = 0;
            lock.notifyAll();
        }
    }

    public void saveNumOfPlayers(){
        numOfPlayers = (int) SliderNOP.getValue();
        System.out.println("Num Of Players: " + numOfPlayers + "\nGamemode selected: " + gameMode);
        moveToMainGame();
    }

    public void moveToMainGame (){

        Platform.runLater(() -> {
            MainGameController mg = new MainGameController(this, null);
        });



    }



    @FXML
    void ExpertMode() {
        this.ExpertModeButton.setEffect(null);
        gameMode = this.ExpertModeButton.isSelected() ? 1 : 0;
        Glow glow = new Glow();
        glow.setLevel(0.5);
        InnerShadow is = new InnerShadow();
        MotionBlur mb = new MotionBlur(7, 7);
        if(this.ExpertModeButton.isSelected()){
            System.out.println("Nightmare mode");
            this.ExpertModeButton.setEffect(mb);
            this.ExpertModeButton.setText("Nightmare");
        }
        else{
            DropShadow ds = new DropShadow(30, Color.LIGHTBLUE);
            System.out.println("Dream mode");
            //this.ExpertModeButton.setEffect(glow);
            this.ExpertModeButton.setEffect(ds);
            //this.ExpertModeButton.setEffect(new Reflection());
            this.ExpertModeButton.setText("Dream");
        }

    }

}
