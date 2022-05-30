package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Message.*;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class NewGui extends Application implements Graphic {

    private static Stage mainStage;

    private final static Object lock = new Object(); //only one lock because any sync block is only some statement without while or a while with a wait on this lock

    private static int first = -1; //-1 not done, 0 not first, 1 first
    private static int infoCorrected = -1; //-1 not done, 0 not corrected, 1 corrected

    private static String username = null;
    private static Wizard wizard = null;
    private static int gameMode = -1;
    private static int numOfPlayers = -1;

    private static SceneController sceneController = null;

    public static void setUsername(String username) {
        synchronized (lock){
            NewGui.username = username;
            lock.notifyAll();
        }
    }

    public static void setWizard(Wizard wizard) {
        synchronized (lock){
            NewGui.wizard = wizard;
            lock.notifyAll();
        }
    }

    public static void setGameMode(int gameMode) {
        synchronized (lock){
            NewGui.gameMode = gameMode;
            lock.notifyAll();
        }
    }

    public static void setNumOfPlayers(int numOfPlayers) {
        synchronized (lock){
            NewGui.numOfPlayers = numOfPlayers;
            lock.notifyAll();
        }
    }

    @Override
    public String getUsername() throws InterruptedException {
        synchronized (lock){
            while (NewGui.username == null){
                lock.wait();
            }
        }
        return username;
    }

    @Override
    public Wizard getWizard() throws InterruptedException {
        synchronized (lock){
            while (NewGui.wizard == null){
                lock.wait();
            }
        }
        return wizard;
    }

    @Override
    public int getGameMode() throws InterruptedException {
        synchronized (lock){
            while (NewGui.gameMode == -1){
                lock.wait();
            }
        }
        return gameMode;
    }

    @Override
    public int getNumOfPlayers() throws InterruptedException {
        synchronized (lock){
            while (NewGui.numOfPlayers == -1){
                lock.wait();
            }
        }
        return numOfPlayers;
    }





    @Override
    public synchronized void start(Stage primaryStage) throws Exception{

        //load Parent for the first scene
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("scenes/launch.fxml")));
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);

        mainStage = primaryStage;
        sceneController = new SceneController(primaryStage.getScene());

        //show the scene
        primaryStage.show();
    }




    public void usernamePage (ActionEvent event) {

        //todo add a tep page for waiting info from server

        /*if (first != 0 && first != 1){
            Text text = new Text();
            text.setText("Waiting for info from server");
            //Creating a Group object
            Group root = new Group(text);
            Scene tempScene = new Scene(root);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("##Sample##, Waiting for info from server");
            stage.setScene(tempScene);
            stage.show();
        }*/

        synchronized (lock) {
            while (first != 0 && first != 1){
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    System.out.println("Wait interrupted while waiting for first info");
                }
            }
        }

        guiSetup();
    }

    @Override
    public void setFirst(boolean isFirst){
        synchronized (lock){
            if (isFirst)
                first = 1;
            else
                first = 0;
            lock.notifyAll();
        }
    }

    public void waitForDone(Event event) {
        /*if (infoCorrected != 0 && infoCorrected != 1){
            Text text = new Text();
            text.setText("Waiting for response from server if data is correct");
            //Creating a Group object
            Group root = new Group(text);
            Scene tempScene = new Scene(root);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("##Sample##, Waiting for response from server if data is correct");
            stage.setScene(tempScene);
            stage.show();
        }*/

        synchronized (lock) {
            while (infoCorrected != 0 && infoCorrected != 1){
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    System.out.println("Wait interrupted while waiting for first info");
                }
            }
        }

        if (infoCorrected == 0) {
            // not correct
            infoCorrected = -1;
            username = null;
            wizard = null;
            System.out.println("Gui Info not corrected, retry");
            startSetup();
        }
        else {
            System.out.println("Gui Info corrected");
            waitForStartGame(event);
        }


    }

    @Override
    public void setDone(boolean done){
        synchronized (lock){
            if (done)
                infoCorrected = 1;
            else
                infoCorrected = 0;
            lock.notifyAll();
        }
    }

    @FXML
    private Label myUsername;

    public void waitForStartGame(Event event) {
        //todo add new wait page
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("scenes/maingame.fxml")));
        } catch (IOException e) {
            System.err.println("Error while loading " + "scenes/maingame.fxml" + " resource, exit");
            e.printStackTrace();
            System.exit(-1);
        }
        //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        mainStage.setFullScreen(true);
        mainStage.setResizable(false);
        mainStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        mainStage.show();
    }

    public void setUsernameLabel(){
        myUsername.setText(username);
        myUsername.setEffect(new DropShadow(30, Color.DEEPSKYBLUE));
    }




    //used only for test
    public static void main() {
        launch();
    }

    public void guiSetup () {

        ClassLoader cL = getClass().getClassLoader();

        if (first == 1)
            sceneController.addScreen("gamemode", getResource(cL, "scenes/gamemode.fxml"));


        sceneController.addScreen("username", getResource(cL, "scenes/username.fxml"));
        sceneController.addScreen("wizard", getResource(cL, "scenes/wizard.fxml"));

        startSetup();
    }

    private Parent getResource (ClassLoader cL, String resources) {

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(cL.getResource(resources)));
        } catch (IOException e) {
            System.err.println("Error while loading " + resources + " resource, exit");
            e.printStackTrace();
            System.exit(-1);
        }
        return root;
    }


    public void startSetup () {
        sceneController.activate("username");
    }




    @FXML
    private TextField usernameInput;

    @FXML
    private Button continueWizard;

    public void selectedUsername (ActionEvent event) {

        String username = usernameInput.getText();
        if (username == null)
            return; //todo display error
        System.out.println("Your username is " + username);
        NewGui.setUsername(username);
        System.out.println("username set");
        sceneController.activate("wizard");

        //this.continueWizard.setDisable(true);
    }



    @FXML
    private javafx.scene.image.ImageView sorcerer;

    @FXML
    private javafx.scene.image.ImageView king;

    @FXML
    private javafx.scene.image.ImageView witch;

    @FXML
    private javafx.scene.image.ImageView wise;

    @FXML
    private javafx.scene.image.ImageView flowersQueen;

    /*
    void applyEffect (){
        String sorcererId = this.Sorcerer.getId();
        String kingId = this.Sorcerer.getId();
        String witchId = this.Sorcerer.getId();
        String wiseId = this.Sorcerer.getId();
        String flowersQueenId = this.Sorcerer.getId();

        if (sorcererId.equals(this.selectedId) || sorcererId.equals(hoverId))
            this.Sorcerer.setEffect(new DropShadow(50, Color.GREENYELLOW));
        else
            this.Sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));

        if (kingId.equals(this.selectedId) || kingId.equals(hoverId))
            this.King.setEffect(new DropShadow(50, Color.GOLD));
        else
            this.King.setEffect(new DropShadow(10, Color.FLORALWHITE));

        if (witchId.equals(this.selectedId) || witchId.equals(hoverId))
            this.Witch.setEffect(new DropShadow(50, Color.VIOLET));
        else
            this.Witch.setEffect(new DropShadow(10, Color.FLORALWHITE));

        if (wiseId.equals(this.selectedId) || wiseId.equals(hoverId))
            this.Wise.setEffect(new DropShadow(50, Color.DEEPSKYBLUE));
        else
            this.Wise.setEffect(new DropShadow(10, Color.FLORALWHITE));

        if (flowersQueenId.equals(this.selectedId) || flowersQueenId.equals(hoverId))
            this.Flowers_Queen.setEffect(new DropShadow(50, Color.FORESTGREEN));
        else
            this.Flowers_Queen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    private String selectedId = null;
    private String hoverId = null;

    @FXML
    void wizardHover (MouseEvent event){
        this.hoverId = event.getPickResult().getIntersectedNode().getId();
        applyEffect();
    }

    @FXML
    void selectWizard (MouseEvent event){
        this.selectedId = event.getPickResult().getIntersectedNode().getId();
        applyEffect();
    }

    @FXML
    void wizardNotHover (MouseEvent ignoredEvent) {
        this.hoverId = null;
        applyEffect();
    }}*/


    private Wizard tempWizard = null;


    @FXML
    void flowersEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King))this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.flowersQueen.setEffect(new DropShadow(50, Color.FORESTGREEN));
    }

    @FXML
    void kingEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.king.setEffect(new DropShadow(50, Color.GOLD));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void sorcererEntered(MouseEvent event) {
        this.sorcerer.setEffect(new DropShadow(50, Color.GREENYELLOW));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wiseEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.wise.setEffect(new DropShadow(50, Color.DEEPSKYBLUE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void witchEntered(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        this.witch.setEffect(new DropShadow(50, Color.VIOLET));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void wizardExited(MouseEvent event) {
        if(tempWizard == null || !tempWizard.equals(Wizard.Sorcerer)) this.sorcerer.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.King)) this.king.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Wise))this.wise.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Witch))this.witch.setEffect(new DropShadow(10, Color.FLORALWHITE));
        if(tempWizard == null || !tempWizard.equals(Wizard.Flowers_Queen))this.flowersQueen.setEffect(new DropShadow(10, Color.FLORALWHITE));
    }

    @FXML
    void SelectFlowersQueen(MouseEvent event) {
        flowersEntered(event);
        tempWizard = Wizard.Flowers_Queen;
    }

    @FXML
    void SelectKing(MouseEvent event) {
        kingEntered(event);
        tempWizard = Wizard.King;
    }

    @FXML
    void SelectSorcerer(MouseEvent event) {
        sorcererEntered(event);
        tempWizard = Wizard.Sorcerer;
    }

    @FXML
    void SelectWise(MouseEvent event) {
        wiseEntered(event);
        tempWizard = Wizard.Wise;
    }

    @FXML
    void SelectWitch(MouseEvent event) {
        witchEntered(event);
        tempWizard = Wizard.Witch;
    }

    public void wizardSelected (ActionEvent event) {
        if (tempWizard != null) {

            System.out.println("Gui Selected wizard: " + tempWizard);
            NewGui.setWizard(tempWizard);

            if (first == 1){
                sceneController.activate("gamemode");
                NewGui.setGameMode(0);
            }
            else
                waitForDone(event);
        }
        // todo visualize error
    }


    //todo add a button to send information together


    @FXML
    private Slider SliderNOP;

    @FXML
    private ToggleButton ExpertModeButton;

    @FXML
    void selectExpertMode(ActionEvent ignoredEvent) {
        int gameMode = this.ExpertModeButton.isSelected() ? 1 : 0;
        NewGui.setGameMode(gameMode);
        System.out.println("Set Gamemode: " + gameMode);

        this.ExpertModeButton.setEffect(null);
        Glow glow = new Glow();
        glow.setLevel(0.5);
        MotionBlur mb = new MotionBlur(7, 7);
        if(this.ExpertModeButton.isSelected()){
            //System.out.println("Nightmare mode");
            this.ExpertModeButton.setEffect(mb);
            this.ExpertModeButton.setText("Nightmare");
        }
        else{
            DropShadow ds = new DropShadow(30, Color.LIGHTBLUE);
            //System.out.println("Dream mode");
            //this.ExpertModeButton.setEffect(glow);
            this.ExpertModeButton.setEffect(ds);
            //this.ExpertModeButton.setEffect(new Reflection());
            this.ExpertModeButton.setText("Dream");
        }
    }

    @FXML
    public void selectNumOfPlayer(ActionEvent event){
        int numOfPlayers = (int) SliderNOP.getValue();
        System.out.println("set num of players: " + numOfPlayers);
        setNumOfPlayers(numOfPlayers);
        if (gameMode == 1 || gameMode == 0)
            waitForDone(event);

        //todo display error
    }



    /*
    @Override
    public void displayMessage(String message) {
        while(NewGui.getStage() == null){
            System.out.println("Waiting for the stage");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(message);
            alert.initOwner(stage.getOwner());
            alert.showAndWait();
        });
    }*/


    @Override
    public String askString(String askMessage) {
        return null;
    }

    @Override
    public String displayError(String errorMessage) {
        return null;
    }

    @Override
    public MoveMotherNatureMessage askMNMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public PlayAssistantMessage askAssistant(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public MoveStudentMessage askStudentMovement(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public ChooseCloudMessage askCloud(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public PlayCharacterMessage askCharacter(ModelMessage model, int playerId) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public void stopInput() {

    }

    @Override
    public void displayModel(ModelMessage model) {

    }

    @Override
    public void displayMessage(String s) {

    }








    /*
    public void gameModePage(ActionEvent event) {

            // TODO: actual wizard choose
            // move to username page

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("scenes/gamemode.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

    }
    public void goToLobby(ActionEvent event){
        wizard = tempWizard;
        synchronized (lock){
            while(first == -1){
                try {
                    System.out.println("Waiting in the lobby");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(first == 1) {
            System.out.println("You are the first player, going to game mode page");
            gameModePage(event);
        }
        else if(first == 0) {
            System.out.println("You aren't the first player, going to main game page");
            moveToMainGame(event);
        }
    }
    public void moveToMainGame (ActionEvent event){


        // move to maingame

        Parent root = null;
        try {

        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }*/
}
