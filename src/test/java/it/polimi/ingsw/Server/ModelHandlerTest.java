package it.polimi.ingsw.Server;

import com.google.gson.JsonElement;
import it.polimi.ingsw.Enum.Errors;
import it.polimi.ingsw.Message.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ModelHandlerTest {

    public static int[] getIds(int playerNumber){
        int[] ids = null;
        switch (playerNumber) {
            case 2 -> {
                ids = new int[2];
                ids[1] = 1;
            }
            case 3 -> {
                ids = new int[3];
                ids[1] = 1;
                ids[2] = 2;
            }
            case 4 -> {
                ids = new int[4];
                ids[0] = 0;
                ids[1] = 1;
                ids[2] = 2;
                ids[3] = 3;
            }
        }
        return ids;
    }

    public static Stream<Arguments> argsMethod() {
        Collection<ClientMessageDecorator> args = new ArrayList<>();
        ClientMessage m1 = new PlayAssistantMessage(Errors.NO_ERROR, "Play Assistant", 1);
        ClientMessage m2 = new MoveStudentMessage(Errors.NO_ERROR, "Move Student", 2, -1);
        ClientMessage m3 = new MoveMotherNatureMessage(Errors.NO_ERROR, "Move MotherNature", 3);
        ClientMessage m4 = new ChooseCloudMessage(Errors.NO_ERROR, "Choose Clouds", 2);
        ClientMessage m5 = new PlayCharacterMessage(Errors.NO_ERROR, "Play Character", 9, null);

        ClientMessageDecorator message1 = new ClientMessageDecorator(m1, 0);
        ClientMessageDecorator message2 = new ClientMessageDecorator(m2, 0);
        ClientMessageDecorator message3 = new ClientMessageDecorator(m3, 0);
        ClientMessageDecorator message4 = new ClientMessageDecorator(m4, 0);
        ClientMessageDecorator message5 = new ClientMessageDecorator(m5, 0);

        args.add (message1);
        args.add (message2);
        args.add (message3);
        args.add (message4);
        args.add (message5);

        return args.stream().map(Arguments::of);
    }

    //real test done in Server class test and in the method below
    @Test
    void stopModel() {
        int[] ids = getIds(3);
        QueueOrganizer q = new QueueOrganizer(ids);

        //set to null server so the game can't finish or it will trigger an error
        ModelHandler x = new ModelHandler(ids, 1, null, q, null);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            x.stopModel();
        }).start();

        x.run();

        assertTrue(q.getPlayerQueue(0).size() > 0);
        assertTrue(q.getPlayerQueue(1).size() > 0);
        assertTrue(q.getPlayerQueue(2).size() > 0);

        assertTrue(true);
    }

    static class Producer implements Runnable {
        private final ClientMessageDecorator message;
        private final BlockingQueue<ClientMessageDecorator> q;

        private boolean run;

        public Producer (BlockingQueue<ClientMessageDecorator> q, ClientMessageDecorator message) {
            this.message = message;
            this.q = q;
            this.run = false;
        }

        public void stop (){
            this.run = false;
        }

        @Override
        public void run() {
            this.run = true;
            while (this.run){
                try {
                    q.put(message);
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<JsonElement> q;

        private boolean run;

        public Consumer (BlockingQueue<JsonElement> q) {
            this.q = q;
            this.run = false;
        }

        public void stop (){
            this.run = false;
        }

        @Override
        public void run() {
            this.run = true;
            while (this.run){
                JsonElement s = null;
                try {
                    s = q.poll(100, TimeUnit.MILLISECONDS);
                    System.out.println(s);
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (s == null)
                    this.run = false;
            }
        }
    }

    @ParameterizedTest
    @MethodSource("argsMethod")
    void run(ClientMessageDecorator move){
        int[] ids = getIds(3);
        QueueOrganizer q = new QueueOrganizer(ids);

        //set to null server so the game can't finish or it will trigger an error
        ModelHandler x = new ModelHandler(ids, 1, null, q,null);

        Producer p = new Producer(q.getModelQueue(), move);

        Consumer c = new Consumer(q.getPlayerQueue(0));

        new Thread(p).start();

        //modelHandler
        new Thread(x).start();

        new Thread(c).start();



        //wait

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //stop producer
        p.stop();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //stop model
        x.stopModel();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //stop consumer
        c.stop();
    }
}