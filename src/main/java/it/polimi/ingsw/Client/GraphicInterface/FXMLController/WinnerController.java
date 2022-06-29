package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WinnerController extends Controller implements Initializable {

    private final String winMessage;

    public WinnerController(Gui main, String resource, String winMessage) {
        super(main, resource);
        this.winMessage = winMessage;
    }

    @FXML
    private Label winLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        winLabel.setText(this.winMessage);
    }

    public void close(ActionEvent actionEvent){
        main.getMainStage().setOnCloseRequest(event -> main.stopGraphic());
        main.stopGraphic();
    }
}
