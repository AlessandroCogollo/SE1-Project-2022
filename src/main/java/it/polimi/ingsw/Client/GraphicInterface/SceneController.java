package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.GraphicInterface.FXMLController.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;

public class SceneController {

    private final Scene main;

    private final HashMap<String, Controller> sceneMap = new HashMap<>();

    private String currentScene = null;
    private Controller currentController = null;

    public SceneController(Scene main) {
        this.main = main;
    }

    public String getCurrentScene() {
        return currentScene;
    }

    public Controller getCurrentController() {
        return currentController;
    }

    public void addScreen(String name, Controller controller){
        sceneMap.put(name, controller);
    }

    public void activate(String name){
        Controller c = this.sceneMap.get(name);

        if (c == null)
            throw new IllegalArgumentException("Not a valid name for a scene");

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

        this.main.setRoot(root);
        this.currentScene = name;
        this.currentController = c;
    }

    public Scene getMain() {
        return main;
    }
}
