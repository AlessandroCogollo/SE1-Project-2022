package it.polimi.ingsw.Server;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Client.GraphicInterface.TestingGraphicHandler;
import it.polimi.ingsw.Enum.Errors;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
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

        String example = "0user01user12user23user31.json";
        //                0user01user 1 2 u s e r 2 3 u s e r 3 1 . j s o n
        //                01234567891011121314151617181920212223242526272829
        //                     5      11          17          23

        for (Path p: fileList){
            String name = String.valueOf(p.getFileName());

            if (name.length() != example.length()) {
                continue;
            }

            String sub1 = (String) name.subSequence(0, 5);
            String sub2 = (String) name.subSequence(6, 11);
            String sub3 = (String) name.subSequence(12, 17);
            String sub4 = (String) name.subSequence(18, 23);
            String sub5 = (String) name.subSequence(24, 30);

            System.out.println(sub1 + sub2 + sub3 + sub4 + sub5);


            if (    !sub1.equals("0user") ||
                    !sub2.equals("1user") ||
                    !sub3.equals("2user") ||
                    !sub4.equals("3user") ||
                    !sub5.equals("1.json")){
                continue;
            }

            u = new HashMap<>(4);
            u.put(0, "user" + name.charAt(5));
            u.put(1, "user" + name.charAt(11));
            u.put(2, "user" + name.charAt(17));
            u.put(3, "user" + name.charAt(23));

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