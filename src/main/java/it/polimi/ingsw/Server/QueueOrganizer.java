package it.polimi.ingsw.Server;

import com.google.gson.JsonElement;
import it.polimi.ingsw.Message.ClientMessageDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Create all the thread-safe queues for communication between client and model
 */
public class QueueOrganizer {

    private final BlockingQueue<ClientMessageDecorator> modelQueue;
    private final Map<Integer, BlockingQueue<JsonElement>> playersQueue;

    /**
     * Constructor of QueueOrganizer class
     * @param ids used for create the right number of queues and map them to the right ids of players
     */
    public QueueOrganizer(int[] ids) {
        this.modelQueue = new LinkedBlockingQueue<>();
        this.playersQueue = new HashMap<>(ids.length);
        for (Integer id: ids){
            this.playersQueue.put(id, new LinkedBlockingQueue<>());
        }
    }

    /**
     * getter for the model queue
     * @return the model thread-safe queue
     */
    public BlockingQueue<ClientMessageDecorator> getModelQueue() {
        return modelQueue;
    }

    /**
     * getter for the player queue
     * @param id request the queue to the player with this id
     * @return the players thread-safe queue, null if there isn't a queue for the requested id
     */
    public BlockingQueue<JsonElement> getPlayerQueue(int id){
        return this.playersQueue.get(id);
    }
}
