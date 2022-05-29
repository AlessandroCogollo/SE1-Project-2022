package it.polimi.ingsw.Client.GraphicInterface;

import javafx.application.Application;
import org.junit.jupiter.api.Test;

import static javafx.application.Application.launch;
import static org.junit.jupiter.api.Assertions.*;

class NewGuiTest {

    @Test
    void test () throws InterruptedException {
        Thread t = new Thread(() -> Application.launch(NewGui.class, null));
        t.start();
        Thread.sleep(1000);
        System.out.println("launch create a thread");
    }

}