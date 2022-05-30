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
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class MainGameController {

    private static Stage stage;
    private static Scene scene;
    private static String username;
    private static Wizard wizard;

    @FXML
    private Label myUsername;

    /*
    public MainGameController(NewGui gui, ActionEvent event){
        stage = NewGui.getStage();
        username = gui.getUsername();
        wizard = gui.getWizard();
        // move to maingame

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("scenes/maingame.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();

        this.myUsername.setText(username);
        this.myUsername.setEffect(new DropShadow(30, Color.DEEPSKYBLUE));

    }
    
     */

}
