package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.FXMLController.*;
import it.polimi.ingsw.Enum.Wizard;

import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class Gui extends Application implements Graphic {

    private static DataCollector dC = null;
    private final static Object lock = new Object();

    private static Stage mainStage = null;
    private SceneController sceneController = null;


    @Override
    public void start(Stage stage) {
        Gui.mainStage = stage;
        //mainStage.initStyle(StageStyle.UNDECORATED);

        StartController c = new StartController(this, "scenes/launch.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(c.getResource()));
        fxmlLoader.setController(c);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("Error while loading " + c.getResource() + " resource, exit");
            e.printStackTrace();
            System.exit(-1);
        }

        Scene scene = new Scene(root);

        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

        mainStage.setScene(scene);

        this.sceneController = new SceneController(scene);
        this.sceneController.addScreen("wait", new WaitController(this, "scenes/wait.fxml"));


        mainStage.show();
    }

    @Override
    public void init(){
        synchronized (Gui.lock) {
            Gui.dC = new DataCollector(this);
            Gui.dC.setCallbackForModel(() -> Platform.runLater(this::displayMainGame));
            Gui.lock.notifyAll();
        }
    }

    public static void startGraphic() {
        System.out.println("Gui Started");
        new Thread(() -> Application.launch(Gui.class, (String[]) null), "Gui Thread").start();
    }

    public static DataCollector getDataCollector() {
        synchronized (Gui.lock) {
            while (Gui.dC == null) {
                try {
                    Gui.lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dC;
    }

    @Override
    public void displayMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.initOwner(Gui.mainStage.getOwner());
            alert.showAndWait();
        });
    }

    public void gameOver(String winMessage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(winMessage);
            alert.initOwner(Gui.mainStage.getOwner());
            alert.showAndWait();
            stopGraphic();
        });
    }

    @Override
    public void stopGraphic() {
        Platform.exit();
    }

    @Override
    public void stop() throws Exception {
        dC.graphicIsTerminated();
        super.stop();
    }

    private void askData (){
        int first = Gui.dC.getFirst(null); //in this case the value is always corrected

        //setting all the scene possible
        this.sceneController.addScreen("wait", new WaitController(this, "scenes/wait.fxml"));

        this.sceneController.addScreen("username", new UsernameController(this, "scenes/username.fxml"));
        this.sceneController.addScreen("wizard", new WizardController(this, "scenes/wizard.fxml"));

        if (first == 0) //first
            this.sceneController.addScreen("addData", new AddDataController(this, "scenes/gamemode.fxml"));

        this.sceneController.activate("username");
    }

    private void waitForDone() {

        int done = Gui.dC.getDone(() -> Platform.runLater(this::waitForDone));

        switch (done) {
            case -1 -> { //not yet set

                //technically only one time can entered in this case because the second time that this method is called the done value must be 0 or 1

                if (!"wait".equals(this.sceneController.getCurrentScene()))
                    this.sceneController.activate("wait");
            }
            case 0 -> { //incorrect
                String error = Gui.dC.getErrorData();
                if (error != null)
                    displayMessage(error);

                askData();
            }
            case 1 ->  waitForStartGame(); //correct
        }
    }

    private void waitForStartGame() {
        System.out.println("WaitForStartGame");
        this.sceneController.addScreen("mainGame", new MainGameController(this, "scenes/maingame.fxml"));

        if (Gui.dC.getModel() != null)
            displayMainGame();
        else
            this.sceneController.activate("wait");

    }

    private void displayMainGame (){
        mainStage.setFullScreen(false);
        mainStage.setResizable(true);
        mainStage.setTitle("Main Game");
        this.sceneController.activate("mainGame");
    }

    public void startButtonPressed(ActionEvent actionEvent) {

        int first = Gui.dC.getFirst(() -> Platform.runLater(this::askData));

        if (first == -1)
            this.sceneController.activate("wait");
        else
            askData();
    }

    public void selectedUsername(ActionEvent actionEvent, String username) {

        Gui.dC.setUsername(username);

        this.sceneController.activate("wizard");
    }

    public void selectedWizard(ActionEvent actionEvent, Wizard tempWizard) {

        Gui.dC.setWizard(tempWizard);

        int first = Gui.dC.getFirst(null); //in this case the value is always corrected

        if (first == 0) //first
            this.sceneController.activate("addData");
        else
            this.waitForDone();
    }

    public void additionalDataSelected(ActionEvent actionEvent, int gameMode, int numOfPlayers) {

        Gui.dC.setGameMode(gameMode);
        Gui.dC.setNumOfPlayers(numOfPlayers);

        this.waitForDone();
    }
}
