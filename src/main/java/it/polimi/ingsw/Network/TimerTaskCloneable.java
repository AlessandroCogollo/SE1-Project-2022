package it.polimi.ingsw.Network;

import java.util.TimerTask;
import java.util.concurrent.Callable;

public class TimerTaskCloneable extends TimerTask implements Cloneable {

    private final Callable call;

    public TimerTaskCloneable(Callable call) {
        super();
        this.call = call;
    }

    public Callable getCall() {
        return call;
    }

    @Override
    public void run() {
        try {
            this.call.call();
        } catch (Exception e) {
            System.err.println("TimerTaskCloneable: Error while executing the task");
            throw new RuntimeException(e);
        }
    }

    @Override
    public TimerTaskCloneable clone() {
        return new TimerTaskCloneable(this.call);
    }
}
