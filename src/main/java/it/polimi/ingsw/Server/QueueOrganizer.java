package it.polimi.ingsw.Server;

import it.polimi.ingsw.Message.ClientMessageDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Create all the thread-safe queues for communication between client and model
 */
public class QueueOrganizer {

    /**
     * The model Queue, use messages yet parsed
     */
    private final BlockingQueue<ClientMessageDecorator> modelQueue;

    /**
     * Map for maintaining all the queue from the model to the client
     */
    private final Map<Integer, BlockingQueue<String>> playersQueue;

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
     * @return the players thread-safe queue
     */
    public BlockingQueue<String> getPlayerQueue(int id){
        return this.playersQueue.get(id);
    }
}
