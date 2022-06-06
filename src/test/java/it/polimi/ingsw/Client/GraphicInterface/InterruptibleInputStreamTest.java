package it.polimi.ingsw.Client.GraphicInterface;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class InterruptibleInputStreamTest {
    //testing only the interrupt

    private Thread t = null;

    void r () {
        InterruptibleInputStream iS = new InterruptibleInputStream(System.in);
        try {
            iS.read();
        } catch (IOException ignored) {}
    }

    void rM () {
        InterruptibleInputStream iS = new InterruptibleInputStream(System.in);
        try {
            iS.read(new byte[3], 0, 3);
        } catch (IOException ignored) {}
    }

    @Test
    void read() throws InterruptedException {
        t = new Thread(this::r);
        t.start();
        Thread.sleep(200);
        t.interrupt();
        assertTrue(t.isInterrupted());

        Thread.sleep(200);

        t = new Thread(this::rM);
        t.start();
        Thread.sleep(200);
        t.interrupt();
        assertTrue(true);
    }
}