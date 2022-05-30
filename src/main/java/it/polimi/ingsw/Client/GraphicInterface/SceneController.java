package it.polimi.ingsw.Client.GraphicInterface;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class SceneController {
    private HashMap<String, Parent> screenMap = new HashMap<>();
    private final Scene main;

    public SceneController(Scene main) {
        this.main = main;
    }

    public void addScreen(String name, Parent pane){
        screenMap.put(name, pane);
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        main.setRoot( screenMap.get(name) );
    }
}
