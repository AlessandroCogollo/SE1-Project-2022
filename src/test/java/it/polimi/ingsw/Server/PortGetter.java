package it.polimi.ingsw.Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

public class PortGetter {

    static private final ArrayList<Integer> lastPortsUsed = new ArrayList<>(10);
    static private final Random rand = new Random(System.currentTimeMillis());
    public static int getPort(){

        int port;
        do {
            port = rand.nextInt(2000, 49151);
        }
        while (lastPortsUsed.contains(port) && !available(port));

        if (lastPortsUsed.size() > 9){
            lastPortsUsed.remove(0);
        }

        lastPortsUsed.add(port);
        return port;
    }

    public static boolean available(int port) {
        if (port < 0 || port > 65535) {
            return false;
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {}
        finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException ignored) {}
            }
        }
        return false;
    }
}
