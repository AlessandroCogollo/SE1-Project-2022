package it.polimi.ingsw.Network;

import java.time.Duration;
import java.util.Timer;

public class PingTimer implements Runnable{

    private Timer sendTimer = new Timer("sendTimer");
    private final TimerTaskCloneable sendPingTask;
    private final Duration sendTimeout;
    private Timer receiveTimer = new Timer("receiveTimer");
    private final TimerTaskCloneable errorTask;
    private final Duration receiveTimeout;

    public PingTimer(Duration defaultTimeout, TimerTaskCloneable sendPingTask, TimerTaskCloneable errorTask){
        this(defaultTimeout, defaultTimeout.multipliedBy(2), sendPingTask, errorTask);
    }

    public PingTimer(Duration sendTimeout, Duration receiveTimeout, TimerTaskCloneable sendPingTask, TimerTaskCloneable errorTask) {
        this.sendTimeout = sendTimeout;
        this.receiveTimeout = receiveTimeout;
        this.sendPingTask = sendPingTask;
        this.errorTask = errorTask;
    }

    /**
     * Only start the two timer and stop his thread
     */
    @Override
    public void run() {
        this.sendTimer.schedule(this.sendPingTask, 0, this.sendTimeout.toMillis());
        this.receiveTimer.schedule(this.errorTask, this.receiveTimeout.toMillis(), this.receiveTimeout.toMillis());
    }

    public void triggerServerError(){
        new Thread(this.errorTask).start();
    }

    /**
     * Reset the send timer stopping it and rescheduling it whit the same arguments
     */
    public void resetSendTimer (){
        stopSendTimer();
        this.sendTimer = new Timer("sendTimer");
        this.sendTimer.schedule(this.sendPingTask.clone(), this.sendTimeout.toMillis(), this.sendTimeout.toMillis());
    }

    /**
     * Reset the receive timer stopping it and rescheduling it whit the same arguments
     */
    public void resetReceiveTimer (){
        stopReceiveTimer();
        this.receiveTimer = new Timer("receiveTimer");
        this.receiveTimer.schedule(this.errorTask.clone(), this.receiveTimeout.toMillis(), this.receiveTimeout.toMillis());
    }

    public void stopSendTimer(){
        this.sendTimer.cancel();
        this.sendTimer.purge();
    }

    public void  stopReceiveTimer (){
        this.receiveTimer.cancel();
        this.receiveTimer.purge();
    }

    public void stopPing (){
        stopReceiveTimer();
        stopSendTimer();
    }
}