package it.polimi.ingsw.Network;

import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * TimerTask that can be cloned for rest the timer.
 */
public class TimerTaskCloneable extends TimerTask implements Cloneable {

    private final Callable<?> call;

    /**
     * Constructor
     * @param call the function that will be call
     */
    public TimerTaskCloneable(Callable<?> call) {
        super();
        this.call = call;
    }

    /**
     * Getter
     * @return call passed in constructor
     */
    public Callable<?> getCall() {
        return call;
    }

    /**
     * Only call the passed Callable
     */
    @Override
    public void run() {
        try {
            this.call.call();
        } catch (Exception e) {
            System.err.println("TimerTaskCloneable: Error while executing the task");
            throw new RuntimeException(e);
        }
    }

    /**
     * Clone method for get a new TimerTaskCloneable with the same task
     * @return the new TimerTaskCloneable with the same task
     */
    @Override
    public TimerTaskCloneable clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ignored) {}
        return new TimerTaskCloneable(this.call);
    }
}
