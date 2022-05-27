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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

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
    private int gameMode = -1;
    private int numOfPlayers = -1;

    @FXML
    private Button StartGameButton;

    @FXML
    private TextField usernameInput;

    public Button getStartGameButton() {
        return StartGameButton;
    }

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
    void SelectFlowersQueen(MouseEvent event) {
        this.wizard = Wizard.Flowers_Queen;
        System.out.println("Flowers Queen selected");
    }

    @FXML
    void SelectKing(MouseEvent event) {
        this.wizard = Wizard.King;
        System.out.println("King selected");
    }

    @FXML
    void SelectSorcerer(MouseEvent event) {
        this.wizard = Wizard.Sorcerer;
        System.out.println("Sorcerer selected");
    }

    @FXML
    void SelectWise(MouseEvent event) {
        this.wizard = Wizard.Wise;
        System.out.println("Wise selected");
    }

    @FXML
    void SelectWitch(MouseEvent event) {
        this.wizard = Wizard.Witch;
        System.out.println("Witch selected");
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

        System.out.println("Chosen username: " + this.username);

    }

    public void chooseWizard (ActionEvent event) {

            System.out.println("Choose Wizard");



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
}
