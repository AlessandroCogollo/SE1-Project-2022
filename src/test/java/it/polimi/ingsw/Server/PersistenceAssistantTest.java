package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GraphicInterface.TestingGraphicHandler;
import it.polimi.ingsw.Enum.Errors;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersistenceAssistantTest {

    private final static Path persistenceFolder = Paths.get("", "persistence");
    private static Map<Integer, String> u = null;
    private static PersistenceAssistant a = null;

    @BeforeAll
    static void setPersistence () throws InterruptedException {
        int port = PortGetter.getPort();
        Server server = new Server(port);

        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(server::start);

        Thread.sleep(200);

        assertFalse(ex.isTerminated());

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            TestingGraphicHandler tgh = new TestingGraphicHandler("Cli");
            tgh.startGraphic();
            Client c = new Client(tgh, "127.0.0.1", port);
            executorService.execute(c::start);
        }

        Thread.sleep(2000);
        server.setCode(Errors.GAME_OVER);

        List<Path> fileList;
        try (Stream<Path> s = Files.list(persistenceFolder)){
            fileList = s.collect(Collectors.toList());
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            return;
        }


        String example = "41_user0_user1_user2_user3.json";
        //                41_user0_us e r 1 _ u s e r 2 _ u s e r 3 . j s o n
        //                0123456789101112131415161718192021222324252627282930
        //                  2    7        13          19          25

        for (Path p: fileList){
            String name = String.valueOf(p.getFileName());

            if (name.length() != example.length()) {
                continue;
            }

            String sub1 = (String) name.subSequence(0, 2);
            String sub2 = (String) name.subSequence(2, 7);
            String sub3 = (String) name.subSequence(8, 13);
            String sub4 = (String) name.subSequence(14, 19);
            String sub5 = (String) name.subSequence(20, 25);
            String sub6 = (String) name.subSequence(26, 31);

            System.out.println(sub1 + sub2 + sub3 + sub4 + sub5);


            if (    !sub1.equals("41") ||
                    !sub2.equals("_user") ||
                    !sub3.equals("_user") ||
                    !sub4.equals("_user") ||
                    !sub5.equals("_user") ||
                    !sub6.equals(".json")){
                continue;
            }

            u = new HashMap<>(4);
            u.put(0, "user" + name.charAt(7));
            u.put(1, "user" + name.charAt(13));
            u.put(2, "user" + name.charAt(19));
            u.put(3, "user" + name.charAt(25));

            a = new PersistenceAssistant(u, 1);

            System.out.println("####################Found right string " + name);

            break;
        }

        if (u == null){
            System.out.println("###################Not find");
        }

    }



    @Test
    void createFile() throws IOException {

        Path directory = Paths.get("", "testingfileIO");

        Files.createDirectory(directory);

        Random r = new Random(System.currentTimeMillis());

        int number = r.nextInt(0, 10);

        for (int i = 0; i < number; i++){
            Files.createFile(Paths.get(directory.toString(), "text" + i + ".txt"));
        }

        for (int i = 0; i < number; i++){
            assertTrue(Files.deleteIfExists(Paths.get(directory.toString(), "text" + i + ".txt")));
        }

        assertTrue(Files.deleteIfExists(directory));
    }
    @Order(3)
    @Test
    void getResumedModelMessage() {
        assertNotNull(a.getResumedModelMessage());
    }

    @Order(1)
    @Test
    void modelAvailable() {
        assertTrue(a.modelAvailable());
    }

    @Order(2)
    @Test
    void getResumedModel() {
        assertNotNull(a.getResumedModel());
    }

    @Test
    void updateModelFile() {
        //done during server test
    }


    @Test
    void gameOver() {
        a.gameOver();
    }
}