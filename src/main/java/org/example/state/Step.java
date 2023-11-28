package org.example.state;

public class Step {

    public String idStep;
    public Command command;
    public Beard beard = new Beard();
    public Head head = new Head();

    public Runnable stateSetter;
}
