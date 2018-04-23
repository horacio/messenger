package com.almundo.app;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class DispatcherQueue {
    private final BlockingQueue<Call> queue;
    private final Dispatcher dispatcher;
    private final ExecutorService service;

    public DispatcherQueue(Dispatcher dispatcher, int capacity) {
        this.dispatcher = dispatcher;

        this.queue = new ArrayBlockingQueue<Call>(capacity);
        this.service = Executors.newSingleThreadExecutor();

        this.listen();
    }

    public void enqueueCall(Call call) {
        try {
            this.queue.put(call);
        }
        catch (InterruptedException e) {
            System.err.println("Call couldn't be queued and got lost into oblivion");
        }
    }

    public void listen() {
        this.service.submit(() -> {
            try {
                while (true) {
                    Call call = this.queue.take();

                    if (call.getMessage() == "THE END") {
                        return;
                    }

                    this.dispatcher.dispatchCall(call);
                }
            }
            catch (InterruptedException e) {
                System.err.println("Queue was shutdown");
            }
        });
    }

    public void shutdown() {
        this.enqueueCall(new InstantaneousDeathCall("THE END"));
    }
}
