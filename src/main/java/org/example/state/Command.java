package org.example.state;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.example.state.params.TypeHaircut;

import java.util.List;

public class Command {

    public final CommandConstants command;
    private HBox view;
    public String params;
    public boolean isLight;

    public Command( CommandConstants command ) {
        this.command = command;
        this.isLight = false;
    }

    public void setView( HBox view ) {
        this.view = view;
        Label commandLable = new Label( String.format( command.stingCommand,  params.toLowerCase() ) );
        commandLable.setFont( new Font( 18 ) );
        view.getChildren().add( commandLable );
    }

    public void setParams( String params ) {
        this.params = params;
    }

    public void offLight() {
        this.isLight = false;
        light();
    }

    public void onLight() {
        this.isLight = true;
        light();
    }

    private void light() {
        if ( this.isLight )
            view.setStyle( "-fx-background-color: #2b4fff;" );
        else {
            view.getStyleClass().clear();
            view.setStyle( null );
        }
    }

    public String getTextCommand() {
        return ( ( Label )view.getChildren().get( 0 )).getText();
    }
}
