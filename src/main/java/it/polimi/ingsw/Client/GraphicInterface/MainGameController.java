package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainGameController {

    private static Stage stage;
    private static Scene scene;
    private static String username;
    private static Wizard wizard;
    private static NewGui gui;
    private static int numOfPlayers;

    @FXML
    private Label myUsername;

    @FXML
    private Label username2;

    @FXML
    private Rectangle rectangle3;

    @FXML
    private Rectangle color3;

    @FXML
    private Label username3;

    @FXML
    private Label description3;

    @FXML
    private Rectangle rectangle4;

    @FXML
    private Rectangle color4;

    @FXML
    private Label username4;

    @FXML
    private Label description4;

    public void initialize(){
        gui = new NewGui();
        try {
            setUsername();
            disableUnused();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setUsername() throws InterruptedException {
        username = gui.getUsername();
        myUsername.setText(username);
    }

    public void disableUnused() throws InterruptedException {
        numOfPlayers = gui.getNumOfPlayers();
        if(numOfPlayers < 4){
            this.color4.setVisible(false);
            this.color4.setDisable(true);
            this.rectangle4.setVisible(false);
            this.username4.setVisible(false);
            this.description4.setVisible(false);
        }
        if(numOfPlayers < 3){
            this.color3.setVisible(false);
            this.color3.setDisable(true);
            this.rectangle3.setVisible(false);
            this.username3.setVisible(false);
            this.description3.setVisible(false);
        }

    }

}
