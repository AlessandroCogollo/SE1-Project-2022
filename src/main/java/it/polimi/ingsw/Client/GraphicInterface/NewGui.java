package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Enum.Wizard;
import javafx.application.Application;
import javafx.application.Platform;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NewGui extends Application implements Graphic {

    private Client client;
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private Gui graphic;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        try {
            this.parent = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/launch.fxml"));
            Scene scene = new Scene(parent);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
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
