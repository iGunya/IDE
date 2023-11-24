package org.example.state;

import org.example.state.params.TypeHaircut;

import java.util.List;

public class Command {

    public final CommandConstants command;
    public List<String> params;
    public boolean isLight;

    public Command( CommandConstants command ) {
        this.command = command;
        this.isLight = false;
    }

    public void setParams( List<String> params ) {
        this.params = params;
    }

    public void swapLight() {
        this.isLight = !isLight;
    }
}
