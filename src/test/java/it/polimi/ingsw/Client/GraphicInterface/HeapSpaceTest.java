package it.polimi.ingsw.Client.GraphicInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HeapSpaceTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {

            final BorderPane root = new BorderPane();
            Scene s = new Scene(root, 800, 600);
            Button button = new Button();
            button.setText("Action");

            final ExecutorService service = Executors.newSingleThreadExecutor();

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    final Task<Void> task = new CreateNodeTask(root);
                    service.submit(task);
                }
            });

            root.setTop(button);

            final Label freeMemLabel = new Label();

            final Task<Void> memMonitor = new Task<Void>() {
                @Override
                public Void call() {
                    while (!isCancelled()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException exc) {
                            if (isCancelled()) {
                                break;
                            }
                        }
                        updateMessage(memInfo());
                    }
                    return null;
                }
            };
            freeMemLabel.textProperty().bind(memMonitor.messageProperty());
            root.setBottom(freeMemLabel);
            final ExecutorService memMonitorService = Executors.newSingleThreadExecutor();
            memMonitorService.submit(memMonitor);

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    memMonitor.cancel(true);
                    memMonitorService.shutdown();
                    service.shutdown();
                }
            });

            primaryStage.setScene(s);
            primaryStage.show();

        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private String memInfo() {
        Runtime rt = Runtime.getRuntime();
        long max = rt.maxMemory();
        long total = rt.totalMemory();
        long free = rt.freeMemory();
        long used = total - free;
        return String.format("Max: %d Total: %d Free: %d Used: %d", max, total, free, used);
    }

    private class CreateNodeTask extends Task<Void> {
        private final BorderPane container;

        CreateNodeTask(BorderPane container) {
            this.container = container;
        }

        @Override
        public Void call() {
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException exc) {
                    if (isCancelled()) {
                        break;
                    }
                }
                final VBox box = new VBox();
                box.setPrefWidth(600);
                box.setPrefHeight(800);
                box.getChildren().clear();
                for (int x = 0; x < 500; x++) {
                    final TextField textField = new TextField("Text " + x);
                    textField.setMinHeight(Control.USE_PREF_SIZE);
                    box.getChildren().add(textField);
                    // pane.getChildren().add(new Label("Text " + x));
                }
                System.out.println("i: " + i + " " + memInfo());
                final ScrollPane scroller = new ScrollPane();
                scroller.setContent(box);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        container.setCenter(scroller);
                    }
                });
            }
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
