package com.almundo.app;

import com.almundo.app.Call;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;

abstract class CallCenterEmployee {
    protected Dispatcher dispatcher;
    protected ThreadPoolExecutor pool;
    protected CallCenterEmployee superior;

    public CallCenterEmployee(Dispatcher dispatcher, int capacity) {
        this.dispatcher = dispatcher;
        this.pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(capacity);
    }

    protected boolean isAvailable() {
        return this.pool.getActiveCount() < this.pool.getCorePoolSize();
    }

    protected void setSuperior(CallCenterEmployee superior) {
        this.superior = superior;
    }

    protected void pickUpCall(Call call) {
        if (this.isAvailable()) {
            this.pool.submit(() -> {
                String message = call.getMessage();
                String whoPickedUp = this.getClass().getName();

                System.out.println(whoPickedUp + " picked the call. It was about: " + message);
            });
        } else if (superior != null) {
            System.out.println("Full capacity reached! Escalated to: " + superior.getClass().getName());

            this.superior.pickUpCall(call);
        } else {
            this.dispatcher.enqueueCall(call);
        }
    }
}
