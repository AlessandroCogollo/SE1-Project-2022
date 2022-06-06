package it.polimi.ingsw.Client.GraphicInterface;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CliTest {
    //could be used for test the output on terminal
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    void setUpStreams() {
        System.setOut(new PrintStream(outContent, true));
        System.setErr(new PrintStream(errContent, true));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    @Test
    void displayMessage() {
        Graphic g = new Cli();
        g.displayMessage("Test");
        assertTrue(true);
        g.displayMessage("Seen");
    }

    @Test
    void getWizard() {
    }

    @Test
    void getUsername() {
    }

    @Test
    void getNumOfPLayer() {
    }

    @Test
    void getGameMode() {
    }
}