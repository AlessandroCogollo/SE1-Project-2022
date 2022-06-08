package it.polimi.ingsw.Client.GraphicInterface.FXMLController;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;

public class MainGameController extends Controller{

    private final DataCollector dataCollector;
    public MainGameController(Gui main, String resource) {
        super(main, resource);
        this.dataCollector = Gui.getDataCollector();
    }

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

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    private Tab tab4;



    public void initialize(){
        try {
            setUsername();
            disableUnused();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); //reset flag
        }
    }

    public void setUsername() throws InterruptedException {
        String username = this.dataCollector.getUsername();
        myUsername.setText(username);

        tab1.setText(username);
    }

    public void disableUnused() throws InterruptedException {
        int numOfPlayers = this.dataCollector.getNumOfPlayers();
        if(numOfPlayers < 4){
            this.color4.setVisible(false);
            this.color4.setDisable(true);
            this.rectangle4.setVisible(false);
            this.username4.setVisible(false);
            this.description4.setVisible(false);
            this.tab4.setDisable(true);
        }
        if(numOfPlayers < 3){
            this.color3.setVisible(false);
            this.color3.setDisable(true);
            this.rectangle3.setVisible(false);
            this.username3.setVisible(false);
            this.description3.setVisible(false);
            this.tab3.setDisable(true);
        }

    }

}
