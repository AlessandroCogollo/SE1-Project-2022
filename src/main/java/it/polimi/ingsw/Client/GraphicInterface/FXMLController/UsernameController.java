package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * Controller for the username selection.
 * When the button on the scene is pushed, call a method in the main gui class for update the scenes.
 */
public class UsernameController extends Controller{

    public UsernameController(Gui main, String resource) {
        super(main, resource);
    }

    private final int maxLength = 3;

    @FXML
    public Pane usernameStage;
    @FXML
    public Button usernameContinue;
    @FXML
    public TextField usernameInput;

    public void initialize() {
        if (this.usernameInput != null) {
            this.usernameContinue.setDisable(true);
            this.usernameContinue.setDefaultButton(true);
        }
    }



    public void checkUsernameLength(KeyEvent keyEvent) {
        this.usernameContinue.setDisable(usernameInput.getText().length() < this.maxLength);
    }

    public void selectedUsername(ActionEvent actionEvent) {
        String username = usernameInput.getText();

        if (username == null || username.length() < this.maxLength) {
            System.out.println("Username null or too short " + username);
            return;
        }

        this.main.selectedUsername(actionEvent, username);
    }
}
