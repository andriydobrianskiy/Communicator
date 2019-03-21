
package com.mainLogin.updateProgram.Model;


public class Instruction {

    private Action action;
    private String destination;
    private String filename;

    public Action getAction() {
        return action;
    }

    public String getDestination() {
        return destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setAction(String value) {
        if (value.equalsIgnoreCase("MOVE")) {
            this.action = Action.MOVE;
        } else if (value.equalsIgnoreCase("DELETE")) {
            this.action = Action.DELETE;
        } else if (value.equalsIgnoreCase("EXECUTE")) {
            this.action = Action.EXECUTE;
        }
    }
}
