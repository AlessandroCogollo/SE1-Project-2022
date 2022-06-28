package it.polimi.ingsw.Server;

import com.google.gson.Gson;
import it.polimi.ingsw.Message.ModelMessage.ModelMessage;
import it.polimi.ingsw.Server.Model.Game;
import org.apache.maven.model.Model;
import org.apache.velocity.tools.generic.MathTool;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for the persistence function.
 * All the game info are use for create the identifier of this game.
 * playerNumber + gameMode + _ + username1 + _ + username2 + _ + ... (the usernames insert by the client can't contain special characters)
 */
public class PersistenceAssistant {

    private final String name;
    private final int[] ids;

    private final Path persistenceFolder = Paths.get("", "persistence");

    private Path modelFile = null;
    private Gson gson = null;

    private ModelMessage resumedModel = null;

    /**
     * Constructor of the class
     * @param usernames map used for generate an identifier of the game, so if the players name are the same we could find the last file created
     * @param gameMode the game mode of the game used for add information to the identifier
     */
    public PersistenceAssistant(Map<Integer, String> usernames, int gameMode) {

        //test only
        if (usernames == null) {
            name = null;
            ids = null;
            return;
        }


        StringBuilder sb = new StringBuilder();
        sb.append(usernames.size()).append(gameMode);
        List<Integer> x = new ArrayList<>(usernames.size());
        for (Integer id: usernames.keySet()){
            sb.append('_').append(usernames.get(id));
            x.add(id);
        }
        sb.append(".json");

        this.ids = x.stream().mapToInt(l -> l).toArray();

        this.name = sb.toString();
    }

    /**
     * get the modelMessage loaded from storage, return a not null value only if the mehtod getResumedModel is called before this
     * @return the model message resumed or null
     */
    public ModelMessage getResumedModelMessage () {
        return this.resumedModel;
    }

    /**
     * Check if the model is available. It is mandatory to call this method before getResumed model, otherwise it will return null
     * @return true if it is available false otherwise
     */
    public boolean modelAvailable(){

        if (this.name == null)
            return false;

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
        try (Stream<Path> s = Files.list(this.persistenceFolder)){
            fileList = s.collect(Collectors.toList());
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            System.out.println("PersistenceAssistant: game file required [" + this.name + "] not available");
            return false;
        }

        Path model = null;

        for (Path p: fileList){
            Path fileName = p.getFileName();
            if (this.name.equals(fileName.toString())){
                model = p;
                break;
            }
        }

        if (model == null){
            System.out.println("PersistenceAssistant: game file required [" + this.name + "] not available");
            return false;
        }

        this.modelFile = model;

        System.out.println("PersistenceAssistant: game file required [" + this.name + "] available");
        return true;
    }

    private static String getFile (Path path){
        if (path == null)
            return null;

        String string;
        try (Stream<String> s = Files.lines(path)){
            string = s.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
            System.out.println("PersistenceAssistant: cannot read lines from " + path);
            return null;
        }
        return string;
    }

    /**
     * Get the Model class resumed from the modelMessage file in storage. It is mandatory to call modelAvailable for have the model, otherwise this function will return always null
     * @return The Model resumed if there is one, null otherwise
     */
    public Game getResumedModel() {

        if (this.modelFile == null){
            System.out.println("PersistenceAssistant: can't find model file");
            return null;
        }

        String json = getFile(this.modelFile);

        if (this.gson == null)
            this.gson = new Gson();

        this.resumedModel = this.gson.fromJson(json, ModelMessage.class);

        return Game.setGame(this.ids, this.resumedModel);
    }

    /**
     * Update or create the Model in the storage
     * @param model the modelMessage for update the file
     * @return true if the update was a success false otherwise
     */
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

    /**
     * The game is over so this method will delete the file saved
     * @return true if the file is deleted correctly, false otherwise
     */
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
