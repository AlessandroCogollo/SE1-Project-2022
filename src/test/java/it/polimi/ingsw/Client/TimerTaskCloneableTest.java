package it.polimi.ingsw.Client;

import it.polimi.ingsw.Network.TimerTaskCloneable;
import org.junit.jupiter.api.Test;

import java.util.Timer;

import static org.junit.jupiter.api.Assertions.*;

class TimerTaskCloneableTest {

    @Test
    void testClone() throws InterruptedException {

        TimerTaskCloneable t1 = new TimerTaskCloneable(() -> {
            System.out.println("I'm the original, i think...");
            return null;
        }) {

        };

        Timer t = new Timer();
        t.schedule(t1, 0, 20);

        Thread.sleep(200);

        t.cancel();
        t.purge();
        System.out.println("Not original");
        t = new Timer();
        t.schedule(t1.clone(), 0, 10);

        Thread.sleep(20);

        t.cancel();
        assertTrue(true);
    }
}