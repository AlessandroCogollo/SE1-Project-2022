package it.polimi.ingsw.Server;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceAssistantTest {

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
}