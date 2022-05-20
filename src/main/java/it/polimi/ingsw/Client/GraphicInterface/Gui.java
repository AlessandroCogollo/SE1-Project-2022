package it.polimi.ingsw.Client.GraphicInterface;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Enum.Wizard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Gui extends Application implements Graphic {
    private Client client;
    private TextInputDialog dialog = new TextInputDialog("the nightmare");
    private Optional<Wizard> w = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<Integer> numOfPlayers = Optional.empty();
    private Optional<Integer> gameMode = Optional.empty();

    public Gui() {
    }

    @Override
    public void displayMessage(String message) {
        Platform.runLater(() -> {
                    Dialog d = new Dialog();
                    d.setTitle("Message");
                    d.setContentText(message);
                    d.show();
                }
                );
    }

    @Override
    public Wizard getWizard() {

        ArrayList<Wizard> choices = new ArrayList<>(Wizard.values().length);
        choices.add(Wizard.Wise);
        choices.add(Wizard.King);
        choices.add(Wizard.Witch);
        choices.add(Wizard.Sorcerer);
        choices.add(Wizard.Flowers_Queen);

        ArrayList<Wizard> queue = new ArrayList<>();
        Platform.runLater(() -> {

            ChoiceDialog<Wizard> dialog = new ChoiceDialog<>(Wizard.King, choices);
            dialog.setTitle("Wizard");
            dialog.setContentText("Choose your wizard: ");
            Optional<Wizard> result = Optional.empty();
            while(result.isEmpty()) result = dialog.showAndWait();
            result.ifPresent(t -> System.out.println("Your choice: " + t));
            this.w = result;
            queue.add(result.get());
        });
        while (this.w.isEmpty()){}
        System.out.println("OK");
        return this.w.get();

    }

    @Override
    public String getUsername() {
        ArrayList<String> queue = new ArrayList<>();
        Platform.runLater(() -> {
            dialog.setTitle("Username");
            dialog.setContentText("Please enter your username:");
            Optional<String> result = dialog.showAndWait();
            while (result.isEmpty()){
                dialog.setContentText("Please enter a valid username:");
            }
            queue.add(String.valueOf(result));
            this.username = result;
        });
        while(this.username.isEmpty()){}
        return this.username.get();

    }

    @Override
    public int getNumOfPLayer() {
        ArrayList<Integer> choices = new ArrayList<>();
        choices.add(2);
        choices.add(3);
        choices.add(4);

        ArrayList<Integer> queue = new ArrayList<>();

        Platform.runLater(() -> {
            Optional<Integer> result = Choice("Number of players", "Choose the number of participants", choices);
            queue.add(result.get());
        });
        while(queue.size()==0){}
        return queue.get(0);
    }

    @Override
    public int getGameMode() {
        ArrayList choices = new ArrayList<>();
        choices.add(0);
        choices.add(1);

        ArrayList<Integer> queue = new ArrayList<>();
        Platform.runLater(() -> {
            Optional<Integer> result = Choice("Game Mode", "Choose a game mode (0 normal, 1 advanced): ", choices);
            queue.add(result.get());
        });
        while(queue.size()==0){}
        return queue.get(0);
    }

    public <T> Optional<T> Choice(String title, String contentText, ArrayList<T> choices){
        ChoiceDialog<T> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(title);
        dialog.setContentText(contentText);
        Optional<T> result = dialog.showAndWait();
        result.ifPresent(t -> System.out.println("Your choice: " + t));
        return result;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Eriantys");
        Button btn = new Button();
        btn.setText("Play");


        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

        Executor e = Executors.newCachedThreadPool();

        btn.setOnAction(event -> {
            this.client = new Client (this, "127.0.0.1", 5088);
            e.execute(client::start);
        });
    }
}
