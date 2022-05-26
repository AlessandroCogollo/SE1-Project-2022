package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import javafx.scene.control.TextField;
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

    @FXML
    TextField usernameInput;

    @Override
    public void displayMessage(String message) { }

    @Override
    public Wizard getWizard() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public int getNumOfPLayer() {
        return 0;
    }

    @Override
    public int getGameMode() {
        return 0;
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

    /*

    public ArrayList<URL> getRoots() {
        return roots;
    }

    public void Next(ActionEvent event){
        try {
            System.out.println("Loading " + this.getRoots().get(currentRoot + 1));
            this.parent = FXMLLoader.load(roots.get(currentRoot + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentRoot++;
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();

    }
     */

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        roots = new ArrayList<>();
        currentRoot = 0;
        roots.add(getClass().getClassLoader().getResource("scenes/launch.fxml"));
        roots.add(getClass().getClassLoader().getResource("scenes/username.fxml"));
        roots.add(getClass().getClassLoader().getResource("scenes/wizard.fxml"));
        roots.add(getClass().getClassLoader().getResource("scenes/gamemode.fxml"));
        roots.add(getClass().getClassLoader().getResource("scenes/maingame.fxml"));

        System.out.println("Setup completed!");

        try {
            this.parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("scenes/launch.fxml")));
            Scene scene = new Scene(parent);
            scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            if(currentRoot == 4){
                stage.setFullScreen(true);
                stage.setResizable(false);
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            }
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void connectClient (ActionEvent event) throws IOException {

        // TODO: add connect to client
        // move to username page

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/username.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void chooseUsername (ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/wizard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void chooseWizard (ActionEvent event) throws IOException {

        // TODO: actual wizard choose
        // move to username page

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/gamemode.fxml"));
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
