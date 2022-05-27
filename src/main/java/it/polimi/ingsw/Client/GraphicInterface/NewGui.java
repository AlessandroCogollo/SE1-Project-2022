package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

import javafx.stage.WindowEvent;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class NewGui extends Application implements Graphic {


    private Client client;
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private Gui graphic;
    private ArrayList<URL> roots = new ArrayList<>();
    private int currentRoot;
    private String username;
    private Wizard wizard;
    private int gameMode = 0;
    private int numOfPlayers = -1;

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
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.showAndWait();
        });

    }

    @Override
    public Wizard getWizard() {
        while(this.wizard == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.wizard;
    }

    @Override
    public String getUsername() {
        while(this.username == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.username;
    }

    @Override
    public int getNumOfPLayer() {
        while(this.numOfPlayers == -1){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.numOfPlayers;
    }

    @Override
    public int getGameMode() {
        while(this.gameMode == -1){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.gameMode;
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
        this.wizard = Wizard.Flowers_Queen;
        System.out.println("Flowers Queen selected");
    }

    @FXML
    void SelectKing(MouseEvent event) {
        kingEntered(event);
        this.wizard = Wizard.King;
        System.out.println("King selected");
    }

    @FXML
    void SelectSorcerer(MouseEvent event) {
        sorcererEntered(event);
        this.wizard = Wizard.Sorcerer;
        System.out.println("Sorcerer selected");
    }

    @FXML
    void SelectWise(MouseEvent event) {
        wiseEntered(event);
        this.wizard = Wizard.Wise;
        System.out.println("Wise selected");
    }

    @FXML
    void SelectWitch(MouseEvent event) {
        witchEntered(event);
        this.wizard = Wizard.Witch;
        System.out.println("Witch selected");
    }

    @FXML
    void flowersEntered(MouseEvent event) {
        if(this.wizard == null || !this.wizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.King))this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.flowersQueen.setEffect(new DropShadow(50, Color.FORESTGREEN));
    }

    @FXML
    void kingEntered(MouseEvent event) {
        if(this.wizard == null || !this.wizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.king.setEffect(new DropShadow(50, Color.GOLD));
        if(this.wizard == null || !this.wizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void sorcererEntered(MouseEvent event) {
        this.sorcerer.setEffect(new DropShadow(50, Color.GREENYELLOW));
        if(this.wizard == null || !this.wizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wiseEntered(MouseEvent event) {
        if(this.wizard == null || !this.wizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.wise.setEffect(new DropShadow(50, Color.DEEPSKYBLUE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void witchEntered(MouseEvent event) {
        if(this.wizard == null || !this.wizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.witch.setEffect(new DropShadow(50, Color.VIOLET));
        if(this.wizard == null || !this.wizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wizardExited(MouseEvent event) {
        if(this.wizard == null || !this.wizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Wise)) this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Witch)) this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(this.wizard == null || !this.wizard.equals(Wizard.Flowers_Queen)) this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
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
    public void start(Stage stage) throws Exception {

        try {
            this.stage = stage;
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

    public void connectClient (ActionEvent event) {

        // TODO: add connect to client
        // move to username page

            System.out.println("Connect Client");

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/username.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


    }

    public void chooseUsername (ActionEvent event) {

        System.out.println("Your username is " + usernameInput.getText());


        this.username = usernameInput.getText();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/wizard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void chooseWizard (ActionEvent event) {

            // TODO: actual wizard choose
            // move to username page

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/gamemode.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

    }

    public void moveToMainGame (ActionEvent event) throws IOException {

        this.numOfPlayers = (int) SliderNOP.getValue();

        System.out.println("Num Of Players: " + this.numOfPlayers + "\nGamemode selected: " + this.gameMode);

        // move to maingame

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/maingame.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    @FXML
    void ExpertMode(ActionEvent event) {
        this.ExpertModeButton.setEffect(null);
        this.gameMode = this.ExpertModeButton.isSelected() ? 1 : 0;
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
