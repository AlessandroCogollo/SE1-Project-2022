package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Controller of the sceneS for the additional information.
 * When the button on the scene is pushed, call a method in the main gui class for update the scenes.
 */
public class AddDataController extends Controller{ //Additional Data Controller

    public AddDataController(Gui main, String resource) {
        super(main, resource);
    }

    @FXML
    public ToggleButton ExpertModeButton;
    @FXML
    public Slider SliderNOP;
    @FXML
    public Pane usernameStage;

    public void changedButton(ActionEvent actionEvent) {

        this.ExpertModeButton.setEffect(null);
        Glow glow = new Glow();
        glow.setLevel(0.5);
        MotionBlur mb = new MotionBlur(7, 7);
        if (this.ExpertModeButton.isSelected()) {
            this.ExpertModeButton.setEffect(mb);
            this.ExpertModeButton.setText("Nightmare");
        } else {
            DropShadow ds = new DropShadow(30, Color.LIGHTBLUE);
            this.ExpertModeButton.setEffect(ds);
            this.ExpertModeButton.setText("Dream");
        }
    }

    public void selectedAdditionalData(ActionEvent actionEvent) {

        int gameMode = this.ExpertModeButton.isSelected() ? 1 : 0;
        int numOfPlayers = (int) SliderNOP.getValue();

        if (numOfPlayers < 2 || numOfPlayers > 4)
            return;

        this.main.additionalDataSelected(actionEvent, gameMode, numOfPlayers);
    }


}
