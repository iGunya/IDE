package org.example.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.commons.lang3.Validate;
import org.controlsfx.control.CheckComboBox;
import org.example.Utils;
import org.example.behaviour.CommandBihaviour;
import org.example.behaviour.StepHolder;
import org.example.di.Containers;
import org.example.state.CommandConstants;
import org.example.state.HaircutBeforeState;
import org.example.state.HaircutDesiredState;
import org.example.state.Step;
import org.example.state.params.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    private Button run;

    @FXML
    private Group mirrowImage;
    @FXML
    private Label mirrowLabel;
    @FXML
    private Group disiredImage;
    @FXML
    private Label disiredLabel;

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
        commandBihaviour.choseHaircut( typeHaircuts );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
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
        button.setOnAction( e -> currentLongHandler( e, sector ) );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void currentLongHandler( ActionEvent event, String sector ) {
        Spinner<Integer> spinner = createSpinner( 0, 500 );
        confirm.setOnAction( e -> inputCurentLong( event, spinner ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        String[] currentLong = CommandConstants.CURRENT_LONG.stingCommand.split( "%." );
        Label commandStart = new Label( currentLong[0] + " " + sector );
        commandStart.setFont( new Font( 18 ) );
        Label commandEnd = new Label( currentLong[1] );
        commandEnd.setFont( new Font( 18 ) );
        row.getChildren().addAll( commandStart, spinner, commandEnd );
        vBoxInput.getChildren().add( row );
    }

    private Spinner<Integer> createSpinner(int min, int max) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, 200);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable( true );
        return spinner;
    }

    private void inputCurentLong( ActionEvent event, Spinner<Integer> spinner ) {
        Integer value = spinner.getValue();
        Validate.isTrue( value > 0, "Длинна волос не может быть отрицательной" );

        Button currentLongButton = ( Button )event.getTarget();
        String[] id = currentLongButton.idProperty().get().split( "_" );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        if ( TypeHaircut.typeFrom( id[1] ) == HEAD )
            commandBihaviour.currentLongHead( HeadSector.sectorFrom( id[2] ), value );
        else
            commandBihaviour.currentLongBeard( BeardSector.sectorFrom( id[2] ), value );

        currentLongButton.setDisable( true );
        for ( Node node : vBoxButton.getChildren() )
            if ( node.idProperty().get().equals( "disired_" + id[1] + "_" + id[2] ) )
                node.setDisable( false );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
    }

    private void createDisiredLongButton( String type, String sector ) {
        Button button = new Button( String.format( "Желаемая длина %s", sector.toLowerCase() ) );
        button.idProperty().set( "disired_" + type + "_" + sector  );
        button.setOnAction( e -> disiredLongHandler( e, sector ) );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        button.setDisable( true );
        vBoxButton.getChildren().add( button );
    }

    private void disiredLongHandler( ActionEvent event, String sector ) {
        Spinner<Integer> spinner = createSpinner( 0, 500 );
        confirm.setOnAction( e -> inputDesiredLong( event, spinner ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        String[] currentLong = CommandConstants.DESIRED_LONG.stingCommand.split( "%." );
        Label commandStart = new Label( currentLong[0] + " " + sector );
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
        String[] id = diseredLongButton.idProperty().get().split( "_" );
        commandBihaviour.desiredLong( TypeHaircut.typeFrom( id[1] ), id[2], value );

        diseredLongButton.setDisable( true );
        boolean allowedNext = true;
        for ( Node node : vBoxButton.getChildren() ) {
            if ( node.idProperty().get().startsWith( "disired_" ) && !node.isDisable() ) {
                allowedNext = false;
            }
        }
        if ( allowedNext ) {
            createNextButton( false );
            vBoxButton.getChildren().stream().filter( node -> colorStyilingDrying.contains( node.idProperty().get().split( "_" )[0] ) )
                    .forEach( node -> node.setDisable( false ) );
        }
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );

    }

    private void createNextButton( boolean isHaircut ) {
        if ( vBoxButton.getChildren().stream().anyMatch( e -> e.idProperty().get().equals( "next" ) ) )
            return;

        Button button = new Button( "Далее" );
        button.setId( "next" );
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
        else
            run.setVisible( true );
    }

    private void createColorButton( String typeHaircut ) {
        Button button = new Button( "Цвет волос" );
        button.setOnAction( this::colorHandler );
        button.idProperty().set( "color_" + typeHaircut );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void colorHandler( ActionEvent event ) {
        Button forButton = ( Button )event.getTarget();
        forButton.setDisable( true );

        ObservableList<String> colors = FXCollections.observableList( Arrays.stream( HairColor.values() ).map( e -> e.name ).collect( Collectors.toList() )  );
        ComboBox<String> comboBox = new ComboBox<>( colors );
        confirm.setOnAction( e -> inputColor( event, comboBox ) );
        confirm.setVisible( true );
        HBox row = new HBox();
        Label command = new Label( "Цвет волос" );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row );
    }

    private void inputColor( ActionEvent event, ComboBox<String> comboBox ) {
        HairColor color = HairColor.colorFrom( comboBox.getValue() );
        Validate.notNull( color, "Не выбран цвет волос" );

        Button diseredLongButton = ( Button )event.getTarget();
        String[] id = diseredLongButton.idProperty().get().split( "_" );
        commandBihaviour.hairColor( TypeHaircut.typeFrom( id[1] ), color );
        diseredLongButton.setDisable( true );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
    }

    private void createStyilingButton( String typeHaircut ) {
        Button button = new Button( "Укладка" );
        button.setOnAction( e -> styilingHandler(e, typeHaircut) );
        button.idProperty().set( "styiling_" + typeHaircut );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void styilingHandler( ActionEvent event, String typeHaircut ) {
        Button forButton = ( Button )event.getTarget();
        forButton.setDisable( true );
        ObservableList<String> colors = FXCollections.observableList( Arrays.stream( Styling.values() ).map( e -> e.name ).collect( Collectors.toList() ) );

        CheckComboBox<String> comboBox = new CheckComboBox<>( colors );
        confirm.setOnAction( e -> stylingColor( event, comboBox ) );
        confirm.setVisible( true );
        FlowPane row = new FlowPane();
        Label command = new Label( "Укладка включает" );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row );
    }

    private void stylingColor( ActionEvent event, CheckComboBox<String> comboBox ) {
        ObservableList<String> checkedIndices = comboBox.getCheckModel().getCheckedItems();
        Validate.isTrue( !checkedIndices.isEmpty(), "Не выбрана укладка" );
        commandBihaviour.hairStyling( checkedIndices.stream().map( Styling::styingFrom ).collect( Collectors.toList()) );

        Button stylingButton = ( Button )event.getTarget();
        stylingButton.setDisable( true );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
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
        Button button = new Button( "Моем волосы" );
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
        Button button = new Button( String.format( "Стрижем %s", sector ) );
        button.setOnAction( this::haircutSectorHandler );
        button.idProperty().set( "haircut_" + type + "_" + sector );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void haircutSectorHandler( ActionEvent event ) {
        Button haircutSectorButton = ( Button )event.getTarget();
        String[] id = haircutSectorButton.idProperty().get().split( "_" );
        commandBihaviour.haircutSector( TypeHaircut.typeFrom( id[1] ), id[2] );
        haircutSectorButton.setDisable( true );
        boolean allowedNext = true;
        for ( Node node : vBoxButton.getChildren() ) {
            if ( node.idProperty().get().startsWith( "haircut_" ) && !node.isDisable() ) {
                allowedNext = false;
            }
        }
        if ( allowedNext ) {
            createNextButton( true );
            vBoxButton.getChildren().stream().filter( node -> colorStyilingDrying.contains( node.idProperty().get().split( "_" )[0] ) ).forEach( node -> node.setDisable( false ) );
        }


        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
    }

    private void createDryingButton( String type, String sector ) {
        Button button = new Button( "Сушим волосы" );
        button.setOnAction( this::dryingHandler );
        button.idProperty().set( "drying_" + type + "_" + sector );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        button.setDisable( true );
        vBoxButton.getChildren().add( button );
    }

    private void dryingHandler( ActionEvent event ) {
        Button drying = ( Button )event.getTarget();
        commandBihaviour.dryingHair();
        drying.setDisable( true );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
    }

    private void createEndStyilingButton( String type, String sector ) {
        Button button = new Button( "Укладываем волосы" );
        button.setOnAction( this::endStyilingHandler );
        button.idProperty().set( "styiling_" + type + "_" + sector );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void endStyilingHandler( ActionEvent event ) {
        Button endStyilingButton = ( Button )event.getTarget();
        String[] id = endStyilingButton.idProperty().get().split( "_" );
        commandBihaviour.stylingProcess( TypeHaircut.typeFrom( id[1] ) );
        endStyilingButton.setDisable( true );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        if ( stackButton.isEmpty() )
            run.setVisible( true );
    }

    private Integer stepIterator = 0;

    @FXML
    private void runProgrumHandler() {
        if ( run.getText().equals( "Запустить" ) )
            run.setText( "Далее" );
        if ( stepIterator != 0 )
            stepHolder.getSteps().get( stepIterator - 1 ).command.swapLight();
        if ( stepIterator >= stepHolder.getSteps().size() ) {
            mirrowLabel.setVisible( false );
            run.setVisible( false );
            return;
        }

        Step step = stepHolder.getSteps().get( stepIterator++ );
        step.command.swapLight();
        if ( !step.head.isInit && !step.beard.isInit )
            return;

        ImmutableList.Builder<ImageView> builder = ImmutableList.builder();

        builder.add( createBaseFaceViews() );
        String fileImageForHead = step.head.createFileImageName();
        if ( fileImageForHead != null )
            builder.addAll( createFaceImage( ImmutableList.of( fileImageForHead ), step.head.color.color ) );
        List<String> fileImageForBeard = step.beard.createFileImageName();
        if ( fileImageForBeard != null )
            builder.addAll( createFaceImage( fileImageForBeard, step.beard.color.color ) );
        mirrowLabel.setVisible( true );
        mirrowImage.getChildren().clear();
        mirrowImage.getChildren().addAll( builder.build() );

        ImmutableList.Builder<ImageView> disireBuilder = ImmutableList.builder();
        disireBuilder.add( createBaseFaceViews() );
        String disiredFileImageForHead = stateDisired.createFileImageNamesForHead();
        if ( disiredFileImageForHead != null )
            disireBuilder.addAll( createFaceImage( ImmutableList.of( disiredFileImageForHead ), stateDisired.hairColor.color ) );
        List<String> disiredFileImageForBeard = stateDisired.createFileImageNamesForBeard();
        if ( disiredFileImageForBeard != null )
            disireBuilder.addAll( createFaceImage( disiredFileImageForBeard, stateDisired.beardColor.color ) );
        disiredLabel.setVisible( true );
        disiredImage.getChildren().clear();
        disiredImage.getChildren().addAll( disireBuilder.build() );
    }



    private List<ImageView> createFaceImage( List<String> hairPaths, Color color ) {
        System.out.println( "Файлы для отображения: " + hairPaths );
        List<ImageView> face = new ArrayList<>();
        for ( String hairPath: hairPaths ) {

            ImageView hairImage = new ImageView( new Image(
                    getClass().getResourceAsStream( "/image/" + hairPath )
            ) );

            Utils.setColorFor( hairImage, color );
            hairImage.setFitHeight( 275 );
            hairImage.setFitWidth( 270 );
            face.add( hairImage );
        }

        return face;
    }

    private ImageView createBaseFaceViews() {
        ImageView baseImage = new ImageView ( new Image(
                getClass().getResourceAsStream("/image/base.png")
        ) );
        baseImage.setFitHeight( 275 );
        baseImage.setFitWidth( 270 );
        return baseImage;
    }
}
