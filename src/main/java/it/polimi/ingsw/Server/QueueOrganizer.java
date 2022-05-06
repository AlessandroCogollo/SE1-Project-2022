package it.polimi.ingsw.Server;

import it.polimi.ingsw.Message.ClientMessageDecorator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueOrganizer {

    private final BlockingQueue<ClientMessageDecorator> modelQueue;

    private final Map<Integer, BlockingQueue<String>> playersQueue;

    public QueueOrganizer(int[] ids) {
        this.modelQueue = new LinkedBlockingQueue<>();
        this.playersQueue = new HashMap<>(ids.length);
        for (Integer id: ids){
            this.playersQueue.put(id, new LinkedBlockingQueue<>());
        }
    }

    public BlockingQueue<ClientMessageDecorator> getModelQueue() {
        return modelQueue;
    }

    public BlockingQueue<String> getPlayerQueue(int id){
        return this.playersQueue.get(id);
    }
}
