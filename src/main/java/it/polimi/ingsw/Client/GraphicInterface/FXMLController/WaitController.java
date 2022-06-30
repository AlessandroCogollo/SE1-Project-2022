package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Controller of the wait scene.
 * It is changed automatically from the gui main class.
 */
public class WaitController extends Controller{



    public WaitController(Gui main, String resource) {
        super(main, resource);
    }

    @FXML
    public ImageView waitingImg;

    public void initialize() {
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(waitingImg);
        rotate.setDuration(Duration.millis(5000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setByAngle(360);
        rotate.play();
    }
}
