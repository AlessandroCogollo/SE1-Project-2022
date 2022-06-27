package it.polimi.ingsw.Client.GraphicInterface;

import com.google.gson.Gson;
import it.polimi.ingsw.Client.DataCollector;
import it.polimi.ingsw.Client.GraphicInterface.FXMLController.*;
import it.polimi.ingsw.Enum.Wizard;

import it.polimi.ingsw.Message.LobbyInfoMessage;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Message.ModelMessage.PlayerSerializable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Gui extends Application implements Graphic {

    protected static DataCollector dC = null;
    private final static Object lock = new Object();
    protected static Stage mainStage = null;
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
    public void init() {
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

    public Stage getMainStage() {
        return mainStage;
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

    @Override
    public void displayError(String message) {
        dC.setErrorData(message);
    }

    @Override
    public void gameOver(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
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
        mainStage.setResizable(false);
        mainStage.setTitle("Main Game");
        mainStage.setHeight(780);
        mainStage.setWidth(1000);
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

    public static class ScenesLoader extends Gui {

        private void modelSelector(DataCollector dC){

            final String fileName = "0bit1tib1.json";

            final String name1 = "player1"; // id: 0
            final String name2 = "player2"; // id: 1
            final String name3 = "player3"; // id: 2
            final String name4 = "player4"; // id: 3

            final Wizard w1 = Wizard.Sorcerer;      // id: 0
            final Wizard w2 = Wizard.King;      // id: 1
            final Wizard w3 = Wizard.Witch;  // id: 2
            final Wizard w4 = Wizard.Wise;     // id: 3








            Path persistenceFolder = Paths.get("", "persistence");
            Path file = Paths.get(persistenceFolder.toString(), fileName);

            Map<Integer, String> names = new HashMap<>(4);
            names.put(0, name1);
            names.put(1, name2);
            names.put(2, name3);
            names.put(3, name4);

            Map<Integer, Wizard> wzs = new HashMap<>(4);
            wzs.put(0, w1);
            wzs.put(1, w2);
            wzs.put(2, w3);
            wzs.put(3, w4);

            String json;
            try (Stream<String> s = Files.lines(file)){
                json = s.collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException | SecurityException e) {
                e.printStackTrace();
                System.out.println("Error: cannot read lines from " + file);
                return;
            }

            Gson g = new Gson();

            ModelMessage model = g.fromJson(json, ModelMessage.class);

            final int id = 1;  //player id, below could use the current player
            //final int id = model.getCurrentPlayerId();

            dC.setModel(model);

            Map<Integer, String> usernames = new HashMap<>(model.getPlayerNumber());
            for (PlayerSerializable p : model.getPlayerList()){
                usernames.put(p.getId(), names.get(p.getId()));
            }
            Map<Integer, Wizard> wizards = new HashMap<>(model.getPlayerNumber());
            for (PlayerSerializable p : model.getPlayerList()){
                wizards.put(p.getId(), wzs.get(p.getId()));
            }

            LobbyInfoMessage l = new LobbyInfoMessage("TEST", usernames, wizards, model.getGameMode(), model.getPlayerNumber());
            dC.setGameData(l, id);

            dC.setUsername(names.get(id));
            dC.setWizard(wzs.get(id));
        }

        @Override
        public void init() {
            dC = new DataCollector(this);
            modelSelector(dC);
        }

        @Override
        public void start(Stage stage) {

            mainStage = stage;

            MainGameController c = new MainGameController(this, "scenes/maingame.fxml");
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

            stage.setScene(scene);

            stage.setFullScreen(false);
            stage.show();
        }
    }

    public static void main(String[] args){
        Application.launch(ScenesLoader.class, (String[]) null);
    }
}
