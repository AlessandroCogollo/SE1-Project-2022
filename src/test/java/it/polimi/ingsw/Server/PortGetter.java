package it.polimi.ingsw.Server;

import java.util.ArrayList;
import java.util.Random;

public class PortGetter {

    static private ArrayList<Integer> lastPortsUsed = new ArrayList<>(10);
    static private Random rand = new Random(System.currentTimeMillis());
    public static int getPort(){

        int port;
        do {
            port = rand.nextInt(2000, 49151);
        }
        while (lastPortsUsed.contains(port));

        if (lastPortsUsed.size() > 9){
            lastPortsUsed.remove(0);
        }

        lastPortsUsed.add(port);
        return port;
    }
}
