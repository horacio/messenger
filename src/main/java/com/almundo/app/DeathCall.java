package com.almundo.app;

class InstantaneousDeathCall extends Call {
    public InstantaneousDeathCall(String message) {
        super(message);
    }

    public String getMessage() {
        return this.message;
    }
}
