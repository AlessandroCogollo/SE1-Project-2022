package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the launch scene.
 * When the button on the scene is pushed, call a method in the main gui class for update the scenes.
 */
public class StartController extends Controller{

    public StartController(Gui main, String resource) {
        super(main, resource);
    }

    @FXML
    public Button StartGameButton;



    public void start(ActionEvent actionEvent) {
        this.main.startButtonPressed(actionEvent);
    }
}
