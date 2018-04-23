package com.almundo.app;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
    private final Operator operators;
    private final Supervisor supervisors;
    private final Director directors;
    private final DispatcherQueue queue;
    private final int QUEUE_SIZE = 25;


    public Dispatcher(int operators, int supervisors, int directors) {
        this.operators = new Operator(this, operators);
        this.supervisors = new Supervisor(this, supervisors);
        this.directors = new Director(this, directors);
        this.queue = new DispatcherQueue(this, QUEUE_SIZE);

        this.setPrecedence();
    }

    public synchronized void dispatchCall(Call call) {
        try {
            this.operators.pickUpCall(call);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void enqueueCall(Call call) {
        this.queue.enqueueCall(call);
    }

    public void shutdown() {
        List<ThreadPoolExecutor> executors = Arrays.asList(
            this.operators.pool,
            this.supervisors.pool,
            this.directors.pool
        );

        try {
            System.out.println("Attempting to shut down current executors!");

            for (ThreadPoolExecutor executor : executors) {
                executor.shutdown();
                executor.awaitTermination(10, TimeUnit.SECONDS);
            }

        }
        catch (InterruptedException e) {
            System.err.println("Some tasks were interrupted!");
        }
        finally {
            for (ThreadPoolExecutor executor : executors) {
                executor.shutdownNow();
            }

            this.queue.shutdown();

            System.out.println("Shutdown finished!");
        }
    }

    private void setPrecedence() {
        this.operators.setSuperior(this.supervisors);
        this.supervisors.setSuperior(this.directors);
    }
}
