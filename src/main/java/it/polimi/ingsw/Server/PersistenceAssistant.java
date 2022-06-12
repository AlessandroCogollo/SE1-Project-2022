package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Server.Model.Game;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersistenceAssistant {

    private final String name;
    private final int[] ids;

    private final Path persistenceFolder = Paths.get("", "persistence");

    private Path modelFile = null;
    private Gson gson = null;

    public PersistenceAssistant(Map<Integer, String> usernames) {

        //test only
        if (usernames == null) {
            name = null;
            ids = null;
            return;
        }

        StringBuilder sb = new StringBuilder();
        List<Integer> x = new ArrayList<>(usernames.size());
        for (Integer id: usernames.keySet()){
            sb.append(id).append(usernames.get(id));
            x.add(id);
        }
        sb.append(".json");
        this.ids = x.stream().mapToInt(l -> l).toArray();

        this.name = sb.toString();
    }

    public boolean modelAvailable(){

        if (!Files.exists(this.persistenceFolder)){
            try {
                Files.createDirectory(this.persistenceFolder);
            } catch (FileAlreadyExistsException ignored){} //impossible
            catch (UnsupportedOperationException | IOException | SecurityException e) {
                System.out.println("PersistenceAssistant: cannot create the folder");
                e.printStackTrace();
            }
            System.out.println("PersistenceAssistant: game file required [" + this.name + "] not available");
            return false;
        }

        List<Path> fileList;
        try (Stream<Path> s = Files.list(persistenceFolder)){
            fileList = s.collect(Collectors.toList());
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            System.out.println("PersistenceAssistant: game file required [" + this.name + "] not available");
            return false;
        }

        boolean find = false;

        for (Path p: fileList){
            Path fileName = p.getFileName();
            if (this.name.equals(fileName.toString())){
                find = true;
                this.modelFile = p;
                break;
            }
        }

        System.out.println("PersistenceAssistant: game file required [" + this.name + "] " + (find ? "" : "not ") + "available");
        return find;
    }

    public Game getResumedModel() {

        if (!modelAvailable()){
            System.out.println("PersistenceAssistant: can't find model file");
            return null;
        }

        String json;
        try (Stream<String> s = Files.lines(this.modelFile)){
            json = s.collect(Collectors.joining(""));
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            System.out.println("PersistenceAssistant: cannot read lines from " + this.modelFile);
            return null;
        }

        if (this.gson == null)
            this.gson = new Gson();

        ModelMessage resumedModel = this.gson.fromJson(json, ModelMessage.class);

        return Game.setGame(this.ids, resumedModel);
    }

    public boolean updateModelFile(ModelMessage model) {

        if (this.gson == null)
            this.gson = new Gson();

        if (this.modelFile == null)
            this.modelFile = Paths.get(this.persistenceFolder.toString(), this.name);

        String json = this.gson.toJson(model);

        try ( OutputStream outputStream = Files.newOutputStream(this.modelFile); // truncate and overwrite an existing file, or create the file if it doesn't initially exist
              PrintWriter writer = new PrintWriter(outputStream) ){

            writer.println(json);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("PersistenceAssistant: cannot write lines from " + this.modelFile);
            return false;
        }

        return true;
    }

    public boolean gameOver() {

        if (!modelAvailable()){
            System.out.println("PersistenceAssistant: can't find model file");
            return false;
        }

        try {
            return Files.deleteIfExists(this.modelFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("PersistenceAssistant: cannot cannot delete " + this.modelFile);
            return false;
        }
    }
}
