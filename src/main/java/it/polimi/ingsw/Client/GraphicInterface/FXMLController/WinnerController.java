package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the win scene.
 * When it is closed stops the JavaFx application.
 */
public class WinnerController extends Controller implements Initializable {

    private final String winMessage;
    private final DataCollector dataCollector;

    public WinnerController(Gui main, String resource, String winMessage) {
        super(main, resource);
        this.winMessage = winMessage;
        this.dataCollector = Gui.getDataCollector();
    }

    @FXML
    private Label winLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        winLabel.setText(this.winMessage);

        List<Integer> winningIds = this.dataCollector.getWinnerIds();
        int myId = this.dataCollector.getId();

        if (!winningIds.contains(myId)){
            //not the winner
            System.out.println("Loser");
        }


    }

    public void close(ActionEvent actionEvent){
        main.getMainStage().setOnCloseRequest(event -> main.stopGraphic());
        main.stopGraphic();
    }
}
