package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.example.Utils;
import org.example.behaviour.CommandBihaviour;
import org.example.behaviour.StepHolder;
import org.example.di.Containers;
import org.example.state.*;
import org.example.state.params.BeardSector;
import org.example.state.params.HeadSector;
import org.example.state.params.TypeHaircut;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import static org.example.Utils.createInputBoxWithSpinner;
import static org.example.state.params.TypeHaircut.BRARD;
import static org.example.state.params.TypeHaircut.HEAD;

public class ConfirmController {

    private final HaircutDesiredState stateDisired = Containers.getStateDisired();
    private final Map<String, Step> desiredStep = Containers.getDisiredSteps();

    @FXML
    private VBox resetVBox;
    @FXML
    private Button resetConfirm;
    private Stage dialog;

    public ConfirmController() {
        Containers.setController( this );
    }

    public void fillInputScene( Stage dialog, Map<String, Step> disiredSteps ) {
        this.dialog = dialog;
        List<Pair<Spinner<Integer>, HBox>> userInput = createInputField( disiredSteps );

        resetConfirm.setOnAction( e -> setNewValues( userInput ) );
        resetVBox.getChildren().addAll( userInput.stream().map( Pair::getValue ).collect( Collectors.toList() ) );
    }

    private List<Pair<Spinner<Integer>, HBox>> createInputField( Map<String, Step> disiredSteps ) {
        List<Pair<Spinner<Integer>, HBox>> userInput = new ArrayList<>();
        Map<String, Integer> sectorSizeNumber = stateDisired.sectorSizeNumber;
        if ( stateDisired.haircuts.contains( HEAD ) ) {
            if ( disiredSteps.containsKey( HEAD.aliases.get( 0 ) + "_" + HeadSector.WHISKY.aliases.get( 1 ) ) )
                userInput.add( createInputBoxWithSpinner( HEAD.aliases.get( 0 ),
                        HeadSector.WHISKY.aliases.get( 1 ), CommandConstants.DESIRED_LONG,
                        sectorSizeNumber.get( HeadSector.WHISKY.name() ) ) );
            if ( disiredSteps.containsKey( HEAD.aliases.get( 0 ) + "_" +HeadSector.BACK.aliases.get( 1 ) ) )
                userInput.add( createInputBoxWithSpinner( HEAD.aliases.get( 0 ),
                        HeadSector.BACK.aliases.get( 1 ), CommandConstants.DESIRED_LONG,
                        sectorSizeNumber.get( HeadSector.BACK.name() ) ) );
            if ( disiredSteps.containsKey( HEAD.aliases.get( 0 ) + "_" +HeadSector.TOP.aliases.get( 1 ) ) )
                userInput.add( createInputBoxWithSpinner( HEAD.aliases.get( 0 ),
                        HeadSector.TOP.aliases.get( 1 ), CommandConstants.DESIRED_LONG,
                        sectorSizeNumber.get( HeadSector.TOP.name() ) ) );
        }
        if ( stateDisired.haircuts.contains( BRARD ) ) {
            if ( disiredSteps.containsKey( BRARD.aliases.get( 0 ) + "_" + BeardSector.CHIN.aliases.get( 1 ) ) )
                userInput.add( createInputBoxWithSpinner( BRARD.aliases.get( 0 ),
                        BeardSector.CHIN.aliases.get( 0 ), CommandConstants.DESIRED_LONG,
                        sectorSizeNumber.get( BeardSector.CHIN.name() ) ) );
            if ( disiredSteps.containsKey( BRARD.aliases.get( 0 ) + "_" + BeardSector.CHEEKS.aliases.get( 1 ) ) )
                userInput.add( createInputBoxWithSpinner( BRARD.aliases.get( 0 ),
                        BeardSector.CHEEKS.aliases.get( 0 ), CommandConstants.DESIRED_LONG,
                        sectorSizeNumber.get( BeardSector.CHEEKS.name() ) ) );
            if ( disiredSteps.containsKey( BRARD.aliases.get( 0 ) + "_" + BeardSector.MUSTACHE.aliases.get( 1 ) ) )
                userInput.add( createInputBoxWithSpinner( BRARD.aliases.get( 0 ),
                        BeardSector.MUSTACHE.aliases.get( 0 ), CommandConstants.DESIRED_LONG,
                        sectorSizeNumber.get( BeardSector.MUSTACHE.name() )) );
        }

        return userInput;
    }

    private void setNewValues( List<Pair<Spinner<Integer>, HBox>> userInput ) {
        for ( Pair<Spinner<Integer>, HBox> input: userInput ) {
            Integer value = input.getKey().getValue();
            String spinnerId = input.getKey().idProperty().get();
            String[] id = spinnerId.split( "_" );
            if ( TypeHaircut.typeFrom( id[0] ) == HEAD ) {
                stateDisired.sectorSizeNumber.put( HeadSector.sectorFrom( id[1] ).name(), value );
                stateDisired.sectorSize.put( HeadSector.sectorFrom( id[1] ).name(), Utils.getLong( HEAD, value ) );
            } else {
                stateDisired.sectorSizeNumber.put( BeardSector.sectorFrom( id[1] ).name(), value );
                stateDisired.sectorSize.put( BeardSector.sectorFrom( id[1] ).name(), Utils.getLong( BRARD, value ) );
            }
            String papamsOld = desiredStep.get( spinnerId ).command.params.split( " " )[0];
            desiredStep.get( spinnerId ).command.params = papamsOld + "_" + value.toString();
        }

        dialog.close();
    }
}
