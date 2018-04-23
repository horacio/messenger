package com.almundo.app;

import java.util.Random;
import java.util.concurrent.TimeUnit;

class Call {
    protected String message;

    public Call(String message) {
        this.message = message;
    }

    public String getMessage() {
        try {
            TimeUnit.SECONDS.sleep(this.randomIntInRange(10, 5));
        }
        catch (InterruptedException e) {
            throw new IllegalStateException("Call interrupted", e);
        }

        return this.message;
    }

    private int randomIntInRange(int high, int low) {
        return new Random().nextInt(high - low) + low;
    }
}
