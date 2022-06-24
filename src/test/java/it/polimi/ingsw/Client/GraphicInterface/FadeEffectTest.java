package it.polimi.ingsw.Client.GraphicInterface;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.stream.Stream;

public class FadeEffectTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Node Overlay Demo");
        primaryStage.show();


        Label faded = new Label("Fading");
        faded.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(faded);
        hBox.setPadding(new Insets(50));
        StackPane hPane = new StackPane(hBox);
        hPane.setMaxHeight(100);
        hPane.setVisible(false);
        //hPane.setStyle("-fx-background-color:transparent");
        hPane.setStyle("-fx-background-color:#55555550");


        Button top = new Button("Top");
        VBox buttons = new VBox(top);
        buttons.setStyle("-fx-border-width:2px;-fx-border-color:black;");
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        StackPane.setMargin(buttons, new Insets(15));

        StackPane content = new StackPane(buttons);

        root.getChildren().addAll(content, hPane);

        top.setOnAction(e -> {
            StackPane.setAlignment(hPane, Pos.TOP_CENTER);
            hPane.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(3000), hPane);
            ft.setOnFinished(actionEvent -> hPane.setVisible(false));
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();

        });
    }
}
