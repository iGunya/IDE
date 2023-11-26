package org.example.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.commons.lang3.Validate;
import org.controlsfx.control.CheckComboBox;
import org.example.behaviour.CommandBihaviour;
import org.example.behaviour.StepHolder;
import org.example.di.Containers;
import org.example.state.CommandConstants;
import org.example.state.HaircutBeforeState;
import org.example.state.HaircutDesiredState;
import org.example.state.params.*;

import java.util.*;

import static org.example.state.params.TypeHaircut.*;

public class FxController {

    private final CommandBihaviour commandBihaviour = Containers.getCommandBihaviour();
    private final StepHolder stepHolder = Containers.getStepHolder();
    private final HaircutDesiredState stateDisired = Containers.getStateDisired();
    private final HaircutBeforeState stateBefore = Containers.getStateBefore();
    private final Stack<Button> stackButton = new Stack<>();

    private final Set<String> colorStyilingDrying = ImmutableSet.of( "color", "styiling", "drying" );

    @FXML
    private VBox vBoxButton;
    @FXML
    private VBox vBoxInput;
    @FXML
    private Button typeHaircut;
    @FXML
    private Button confirm;

    @FXML
    public void typeHaircutHandler() {
        CheckComboBox<String> comboBox = new CheckComboBox<>();
        comboBox.getItems().add( HEAD.aliases.get( 0 ) );
        comboBox.getItems().add( BRARD.aliases.get( 0 ) );
        confirm.setOnAction( event -> inputTypeHaircut( event, comboBox ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        Label command = new Label( CommandConstants.CHOSE_HAIRCUT.stingCommand.split( "%.*" )[0] );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row );
        vBoxButton.getChildren().clear();
    }

    private void inputTypeHaircut( ActionEvent event, CheckComboBox<String> comboBox ) {
        ObservableList<String> checkedIndices = comboBox.getCheckModel().getCheckedItems();
        Validate.isTrue( !checkedIndices.isEmpty(), "Не выбран тип стрижки" );
        List<TypeHaircut> typeHaircuts = new ArrayList<>();
        for ( String aliase : checkedIndices ) {
           typeHaircuts.add( TypeHaircut.typeFrom( aliase ) );
        }
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        commandBihaviour.choseHaircut( typeHaircuts );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
        for ( TypeHaircut typeHaircut : stateDisired.haircuts) {
            createForButton( typeHaircut );
        }
    }

    private void createForButton( TypeHaircut typeHaircut ) {
        Button button = new Button( String.format( CommandConstants.FOR.stingCommand, typeHaircut.aliases.get( 0 ) ) );
        button.idProperty().set( typeHaircut.name() );
        button.setOnAction( this::forCommandHandler );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    @FXML
    public void forCommandHandler( ActionEvent event ) {
        Button forButton = ( Button )event.getTarget();
        TypeHaircut typeHaircut = typeFrom( forButton );
        commandBihaviour.forHaircut( typeHaircut );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        for ( Node node : vBoxButton.getChildren() )
            if ( !node.equals( forButton ) )
                stackButton.add( ( Button )node );
        vBoxButton.getChildren().clear();
        createButtonsAfterFor( typeHaircut );
    }

    private void createButtonsAfterFor( TypeHaircut typeHaircut ) {
        if ( HEAD == typeHaircut ) {
            createCurrentLongButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 1 ) );
            createCurrentLongButton( HEAD.aliases.get( 0 ), HeadSector.BACK.aliases.get( 1 ) );
            createCurrentLongButton( HEAD.aliases.get( 0 ), HeadSector.TOP.aliases.get( 1 ) );
            createDisiredLongButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 1 ) );
            createDisiredLongButton( HEAD.aliases.get( 0 ), HeadSector.BACK.aliases.get( 1 ) );
            createDisiredLongButton( HEAD.aliases.get( 0 ), HeadSector.TOP.aliases.get( 1 ) );
            createColorButton( HEAD.aliases.get( 0 ) );
            createStyilingButton( HEAD.aliases.get( 0 ) );
        } else {
            createCurrentLongButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            createCurrentLongButton( BRARD.aliases.get( 0 ), BeardSector.CHIN.aliases.get( 0 ) );
            createCurrentLongButton( BRARD.aliases.get( 0 ), BeardSector.MUSTACHE.aliases.get( 0 ) );
            createDisiredLongButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            createDisiredLongButton( BRARD.aliases.get( 0 ), BeardSector.CHIN.aliases.get( 0 ) );
            createDisiredLongButton( BRARD.aliases.get( 0 ), BeardSector.MUSTACHE.aliases.get( 0 ) );
            createColorButton( BRARD.aliases.get( 0 ) );
            createStyilingButton( BRARD.aliases.get( 0 ) );
        }
    }

    private void createCurrentLongButton( String type, String sector ) {
        Button button = new Button( String.format( "Текущая длина %s", sector.toLowerCase() ) );
        button.idProperty().set( "current_" + type + "_" + sector );
        button.setOnAction( this::currentLongHandler );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void currentLongHandler( ActionEvent event ) {
        Spinner<Integer> spinner = createSpinner( 0, 500 );
        confirm.setOnAction( e -> inputCurentLong( event, spinner ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        String[] currentLong = CommandConstants.CURRENT_LONG.stingCommand.split( "%." );
        Label commandStart = new Label( currentLong[0] );
        commandStart.setFont( new Font( 18 ) );
        Label commandEnd = new Label( currentLong[1] );
        commandEnd.setFont( new Font( 18 ) );
        row.getChildren().addAll( commandStart, spinner, commandEnd );
        vBoxInput.getChildren().add( row );
    }

    private Spinner<Integer> createSpinner(int min, int max) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, 0);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }

    private void inputCurentLong( ActionEvent event, Spinner<Integer> spinner ) {
        Integer value = spinner.getValue();
        Validate.isTrue( value > 0, "Длинна волос не может быть отрицательной" );

        Button currentLongButton = ( Button )event.getTarget();
        currentLongButton.setDisable( true );
        String[] id = currentLongButton.idProperty().get().split( "_" );
        for ( Node node : vBoxButton.getChildren() ) {
            if ( node.idProperty().get().equals( "disired_" + id[1] + "_" + id[2] ) ) {
                node.setDisable( false );
            }
        }
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );

        if ( TypeHaircut.typeFrom( id[1] ) == HEAD )
            commandBihaviour.currentLongHead( HeadSector.sectorFrom( id[2] ), value );
        else
            commandBihaviour.currentLongBeard( BeardSector.sectorFrom( id[2] ), value );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
    }

    private void createDisiredLongButton( String type, String sector ) {
        Button button = new Button( String.format( "Желаемая длина %s", sector.toLowerCase() ) );
        button.idProperty().set( "disired_" + type + "_" + sector  );
        button.setOnAction( this::disiredLongHandler );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        button.setDisable( true );
        vBoxButton.getChildren().add( button );
    }

    private void disiredLongHandler( ActionEvent event ) {
        Spinner<Integer> spinner = createSpinner( 0, 500 );
        confirm.setOnAction( e -> inputDesiredLong( event, spinner ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        String[] currentLong = CommandConstants.CURRENT_LONG.stingCommand.split( "%." );
        Label commandStart = new Label( currentLong[0] );
        commandStart.setFont( new Font( 18 ) );
        Label commandEnd = new Label( currentLong[1] );
        commandEnd.setFont( new Font( 18 ) );
        row.getChildren().addAll( commandStart, spinner, commandEnd );
        vBoxInput.getChildren().add( row );
    }

    private void inputDesiredLong( ActionEvent event, Spinner<Integer> spinner ) {
        Integer value = spinner.getValue();
        Validate.isTrue( value > 0, "Длинна волос не может быть отрицательной" );

        Button diseredLongButton = ( Button )event.getTarget();
        diseredLongButton.setDisable( true );
        String[] id = diseredLongButton.idProperty().get().split( "_" );
        boolean allowedNext = true;
        for ( Node node : vBoxButton.getChildren() ) {
            if ( node.idProperty().get().startsWith( "disired_" ) && !node.isDisable() ) {
                allowedNext = false;
            }
        }
        if ( allowedNext ) {
            createNextButton( false );
            vBoxButton.getChildren().stream().filter( node -> colorStyilingDrying.contains( node.idProperty().get() ) ).forEach( node -> node.setDisable( false ) );
        }
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        commandBihaviour.desiredLong( TypeHaircut.typeFrom( id[1] ), id[2], value );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
    }

    private void createNextButton( boolean isHaircut ) {
        Button button = new Button( "Далее" );
        button.setOnAction( event -> nextHandler( event, isHaircut ) );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void nextHandler(  ActionEvent event, boolean isHaicut  ) {
        vBoxButton.getChildren().clear();
        if ( !stackButton.isEmpty() ) {
            vBoxButton.getChildren().add( stackButton.peek() );
            stackButton.pop();
            return;
        }
        if ( !isHaicut )
            for ( TypeHaircut typeHaircut : stateDisired.haircuts) {
                createHaircutButton( typeHaircut );
            }
    }

    private void createColorButton( String typeHaircut ) {
        Button button = new Button( String.format( CommandConstants.HAIR_COLOR.stingCommand, "" ) );
        button.setOnAction( this::colorHandler );
        button.idProperty().set( "color_" + typeHaircut );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void colorHandler( ActionEvent event ) {
        Button forButton = ( Button )event.getTarget();
        forButton.setDisable( true );

        ObservableList<HairColor> colors = FXCollections.observableList( Arrays.asList( HairColor.values() ) );
        ComboBox<HairColor> comboBox = new ComboBox<>( colors );
        confirm.setOnAction( e -> inputColor( event, comboBox ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        Label command = new Label( CommandConstants.HAIR_COLOR.stingCommand.split( "%." )[0] );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row );
    }

    private void inputColor( ActionEvent event, ComboBox<HairColor> comboBox ) {
        HairColor color = comboBox.getValue();
        Validate.notNull( color, "Не выбран цвет стрижки" );

        Button diseredLongButton = ( Button )event.getTarget();
        diseredLongButton.setDisable( true );
        String[] id = diseredLongButton.idProperty().get().split( "_" );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        commandBihaviour.hairColor( TypeHaircut.typeFrom( id[1] ), color );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
    }

    private void createStyilingButton( String typeHaircut ) {
        Button button = new Button( "Укладка" );
        button.setOnAction( this::styilingHandler );
        button.idProperty().set( "styiling_" + typeHaircut );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void styilingHandler( ActionEvent event ) {
        Button forButton = ( Button )event.getTarget();
        forButton.setDisable( true );

        ObservableList<Styling> colors = FXCollections.observableList( Arrays.asList( Styling.values() ) );
        CheckComboBox<Styling> comboBox = new CheckComboBox<>( colors );
        confirm.setOnAction( e -> stylingColor( event, comboBox ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        Label command = new Label( CommandConstants.HAIR_COLOR.stingCommand.split( "%." )[0] );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row );
    }

    private void stylingColor( ActionEvent event, CheckComboBox<Styling> comboBox ) {
        ObservableList<Styling> checkedIndices = comboBox.getCheckModel().getCheckedItems();
        Validate.isTrue( !checkedIndices.isEmpty(), "Не выбрана укладка" );

        Button stylingButton = ( Button )event.getTarget();
        stylingButton.setDisable( true );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        commandBihaviour.hairStyling( checkedIndices );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
    }

    private void createHaircutButton( TypeHaircut typeHaircut ) {
        Button button = new Button( String.format( CommandConstants.HAIRCUT.stingCommand, typeHaircut.aliases.get( 0 ) ) );
        button.idProperty().set( typeHaircut.name() );
        button.setOnAction( this::haircutHandler );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void haircutHandler( ActionEvent event ) {
        Button haircutButton = ( Button )event.getTarget();
        TypeHaircut typeHaircut = typeFrom( haircutButton );
        commandBihaviour.haircut( typeHaircut );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        for ( Node node : vBoxButton.getChildren() )
            if ( !node.equals( haircutButton ) )
                stackButton.add( ( Button )node );
        vBoxButton.getChildren().clear();
        createButtonsAfterHaircut( typeHaircut );
    }

    private void createButtonsAfterHaircut( TypeHaircut typeHaircut ) {
        if ( HEAD == typeHaircut ) {
            createWahingButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
            createHaircutSectorButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
            createHaircutSectorButton( HEAD.aliases.get( 0 ), HeadSector.BACK.aliases.get( 0 ) );
            createHaircutSectorButton( HEAD.aliases.get( 0 ), HeadSector.TOP.aliases.get( 0 ) );
            createDryingButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
            createEndStyilingButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
        } else {
            createWahingButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            createHaircutSectorButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            createHaircutSectorButton( BRARD.aliases.get( 0 ), BeardSector.CHIN.aliases.get( 0 ) );
            createHaircutSectorButton( BRARD.aliases.get( 0 ), BeardSector.MUSTACHE.aliases.get( 0 ) );
            createDryingButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            createEndStyilingButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
        }

    }

    private void createWahingButton( String type, String sector ) {
        Button button = new Button( CommandConstants.WASHING_HAIR.stingCommand );
        button.setOnAction( this::wahingHandler );
        button.idProperty().set( "wahing" + type + "_" + sector );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void wahingHandler( ActionEvent event ) {
        commandBihaviour.washingHair();
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
    }

    private void createHaircutSectorButton( String type, String sector ) {
        Button button = new Button( String.format( CommandConstants.HAIRCUT_SECTOR.stingCommand, sector ) );
        button.setOnAction( this::haircutSectorHandler );
        button.idProperty().set( "haircut_" + type + "_" + sector );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void haircutSectorHandler( ActionEvent event ) {
        Button haircutSectorButton = ( Button )event.getTarget();
        haircutSectorButton.setDisable( true );
        String[] id = haircutSectorButton.idProperty().get().split( "_" );
        boolean allowedNext = true;
        for ( Node node : vBoxButton.getChildren() ) {
            if ( node.idProperty().get().startsWith( "haircut_" ) && !node.isDisable() ) {
                allowedNext = false;
            }
        }
        if ( allowedNext ) {
            createNextButton( true );
            vBoxButton.getChildren().stream().filter( node -> colorStyilingDrying.contains( node.idProperty().get() ) ).forEach( node -> node.setDisable( false ) );
        }

        commandBihaviour.haircutSector( TypeHaircut.typeFrom( id[1] ), id[2] );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
    }

    private void createDryingButton( String type, String sector ) {
        Button button = new Button( CommandConstants.DRYING_HAIR.stingCommand );
        button.setOnAction( this::dryingHandler );
        button.idProperty().set( "drying_" + type + "_" + sector );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        button.setDisable( true );
        vBoxButton.getChildren().add( button );
    }

    private void dryingHandler( ActionEvent event ) {
        commandBihaviour.dryingHair();
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
    }

    private void createEndStyilingButton( String type, String sector ) {
        Button button = new Button( CommandConstants.STYLING_PROCESS.stingCommand );
        button.setOnAction( this::endStyilingHandler );
        button.idProperty().set( "styiling" + type + "_" + sector );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void endStyilingHandler( ActionEvent event ) {
        Button haircutSectorButton = ( Button )event.getTarget();
        String[] id = haircutSectorButton.idProperty().get().split( "_" );
        commandBihaviour.stylingProcess( TypeHaircut.typeFrom( id[1] ) );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
    }
}
