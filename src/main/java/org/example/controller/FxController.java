package org.example.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.CheckComboBox;
import org.example.App;
import org.example.Utils;
import org.example.behaviour.CommandBihaviour;
import org.example.behaviour.StepHolder;
import org.example.di.Containers;
import org.example.state.*;
import org.example.state.params.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.example.Utils.createInputBoxWithSpinner;
import static org.example.state.params.TypeHaircut.*;

public class FxController {

    private final static Logger LOGGER = Logger.getGlobal();

    private final static String START_HENDEL = "Нажата кнопка для команды %s, id %s";
    private final static String END_HENDEL = "Команда %s обработана";

    private final CommandBihaviour commandBihaviour = Containers.getCommandBihaviour();
    private final StepHolder stepHolder = Containers.getStepHolder();
    private final HaircutDesiredState stateDisired = Containers.getStateDisired();
    private final HaircutBeforeState stateBefore = Containers.getStateBefore();
    private final Stack<Button> stackButton = new Stack<>();

    private final Set<String> colorStyilingDrying = ImmutableSet.of( "color", "styiling" );
    private final Map<String, Step> disiredSteps = Containers.getDisiredSteps();
    private List<Boolean> buttonDisableState = new ArrayList<>();

    @FXML
    private VBox vBoxButton;
    @FXML
    private VBox vBoxInput;
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
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.CHOSE_HAIRCUT.name(), "type_haircut" ) );
        CheckComboBox<String> comboBox = new CheckComboBox<>(); // создаем объект для ввода параметов
        comboBox.getItems().add( HEAD.aliases.get( 0 ) ); // параметры выбираются из заранее
        comboBox.getItems().add( BRARD.aliases.get( 0 ) ); // сформированного списка
        LOGGER.log( Level.INFO, String.format( "Для пользователя сформированны параметры: %s", comboBox.getItems() ) );
        confirm.setOnAction( event -> inputTypeHaircut( event, comboBox ) ); // заполнение поля с параметрами нужно подтвердить
        confirm.setVisible( true ); // отображаем кнопку для подтверждения введенных параметров
        HBox row = new HBox(); // создаем область в котором будем отображать команду и поле для ввода параметров
        Label command = new Label( CommandConstants.CHOSE_HAIRCUT.stingCommand.split( "%.*" )[0] );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row ); // отображаем команду и поле для ввода на экране
        offAllButton();
    }

    private void offAllButton() {
        buttonDisableState = vBoxButton.getChildren().stream().map( Node::isDisable ).collect( Collectors.toList());
        vBoxButton.getChildren().forEach( e -> e.setDisable( true ) );
    }

    private void inputTypeHaircut( ActionEvent event, CheckComboBox<String> comboBox ) {
        ObservableList<String> checkedIndices = comboBox.getCheckModel().getCheckedItems(); // получаем выбранные параметры
        Validate.isTrue( !checkedIndices.isEmpty(), "Не выбран тип стрижки" ); // валидируем введенные параметры
        LOGGER.log( Level.INFO, String.format( "Пользователь задал параметры: %s", comboBox.getItems() ) );
        List<TypeHaircut> typeHaircuts = new ArrayList<>();
        for ( String aliase : checkedIndices ) { // формируем из введенных пользователем параметры, внутренние обекты
           typeHaircuts.add( TypeHaircut.typeFrom( aliase ) );
        }
        commandBihaviour.choseHaircut( typeHaircuts ); // обрабатываем выражение
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 ); // удаляем выражение для ввода
        vBoxInput.getChildren().add( row ); // фиксируем на экране выражение
        confirm.setVisible( false );
        vBoxButton.getChildren().clear();
        for ( TypeHaircut typeHaircut : stateDisired.haircuts) { // создаем следующие кнопки
            createForButton( typeHaircut );
        }
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.CHOSE_HAIRCUT.name() ) );
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
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.FOR.name(), forButton.idProperty().get() ) );
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
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.FOR.name() + "_" + typeHaircut.name() ) );
    }

    private void createButtonsAfterFor( TypeHaircut typeHaircut ) {
        if ( HEAD == typeHaircut ) {
            createCurrentLongButton( HEAD.aliases.get( 0 ), HeadSector.TOP.aliases.get( 1 ) );
            createCurrentLongButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 1 ) );
            createCurrentLongButton( HEAD.aliases.get( 0 ), HeadSector.BACK.aliases.get( 1 ) );
            createDisiredLongButton( HEAD.aliases.get( 0 ), HeadSector.TOP.aliases.get( 1 ) );
            createDisiredLongButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 1 ) );
            createDisiredLongButton( HEAD.aliases.get( 0 ), HeadSector.BACK.aliases.get( 1 ) );
            createColorButton( HEAD.aliases.get( 0 ) );
            createStyilingButton( HEAD.aliases.get( 0 ) );
        } else {
            createCurrentLongButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 1 ) );
            createCurrentLongButton( BRARD.aliases.get( 0 ), BeardSector.CHIN.aliases.get( 1 ) );
            createCurrentLongButton( BRARD.aliases.get( 0 ), BeardSector.MUSTACHE.aliases.get( 1 ) );
            createDisiredLongButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 1 ) );
            createDisiredLongButton( BRARD.aliases.get( 0 ), BeardSector.CHIN.aliases.get( 1 ) );
            createDisiredLongButton( BRARD.aliases.get( 0 ), BeardSector.MUSTACHE.aliases.get( 1 ) );
            createColorButton( BRARD.aliases.get( 0 ) );
            if (stateDisired.sectorSize.get( BeardSector.MUSTACHE.name() ) != HairLong.NON)
                createStyilingButton( BRARD.aliases.get( 0 ) );
        }
    }

    private void createCurrentLongButton( String type, String sector ) {
        Button button = new Button( String.format( "Текущая длина %s", sector.toLowerCase() ) );
        button.idProperty().set( "current_" + type + "_" + sector );
        button.setOnAction( e -> currentLongHandler( e, type, sector ) );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void currentLongHandler( ActionEvent event, String type, String sector ) {
        Button currentLongButton = ( Button )event.getTarget();
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.CURRENT_LONG.name(), currentLongButton.idProperty().get() ) );
        Pair<Spinner<Integer>, HBox> inputBoxWithSpinner =
                createInputBoxWithSpinner( type, sector, CommandConstants.CURRENT_LONG, TypeHaircut.typeFrom( type ) == HEAD ? 200 : 60 );

        confirm.setOnAction( e -> inputCurentLong( event, inputBoxWithSpinner.getKey() ) );
        confirm.setVisible( true );

        vBoxInput.getChildren().add( inputBoxWithSpinner.getValue() );
        offAllButton();
    }


    private void inputCurentLong( ActionEvent event, Spinner<Integer> spinner ) {
        Integer value = spinner.getValue();
        LOGGER.log( Level.INFO, String.format( "Пользователь задал параметр: %s", value ) );

        Button currentLongButton = ( Button )event.getTarget();
        String[] id = currentLongButton.idProperty().get().split( "_" );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        if ( TypeHaircut.typeFrom( id[1] ) == HEAD )
            commandBihaviour.currentLongHead( HeadSector.sectorFrom( id[2] ), value );
        else
            commandBihaviour.currentLongBeard( BeardSector.sectorFrom( id[2] ), value );

        returnStateButton();
        currentLongButton.setDisable( true );
        for ( Node node : vBoxButton.getChildren() )
            if ( node.idProperty().get().equals( "disired_" + id[1] + "_" + id[2] ) )
                node.setDisable( false );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );

        boolean allowedNext = true;
        for ( Node node : vBoxButton.getChildren() ) {
            if ( node.idProperty().get().startsWith( "current_" ) && !node.isDisable() ) {
                allowedNext = false;
            }
        }
        if ( allowedNext ) {
            createNextButton( false );
            vBoxButton.getChildren().stream().filter( node -> colorStyilingDrying.contains( node.idProperty().get().split( "_" )[0] ) )
                    .forEach( node -> node.setDisable( false ) );
        }

        confirm.setVisible( false );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.CURRENT_LONG.name() + "_" + TypeHaircut.typeFrom( id[1] ).name() ) );
    }

    private void returnStateButton() {
        Validate.isTrue( vBoxButton.getChildren().size() == buttonDisableState.size(),
                "Упс... что-то пошло не так" );
        for ( int i = 0; i < buttonDisableState.size(); i++ )
            vBoxButton.getChildren().get( i ).setDisable( buttonDisableState.get( i ) );
    }

    private void createDisiredLongButton( String type, String sector ) {
        Button button = new Button( String.format( "Желаемая длина %s", sector.toLowerCase() ) );
        button.idProperty().set( "disired_" + type + "_" + sector  );
        button.setOnAction( e -> disiredLongHandler( e, type, sector ) );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        button.setDisable( true );
        vBoxButton.getChildren().add( button );
    }

    private void disiredLongHandler( ActionEvent event, String type, String sector ) {
        Button currentLongButton = ( Button )event.getTarget();
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.DESIRED_LONG.name(), currentLongButton.idProperty().get() ) );
        Pair<Spinner<Integer>, HBox> inputBoxWithSpinner =
                createInputBoxWithSpinner( type, sector, CommandConstants.DESIRED_LONG, TypeHaircut.typeFrom( type ) == HEAD ? 100 : 20 );
        confirm.setOnAction( e -> inputDesiredLong( event, inputBoxWithSpinner.getKey() , type, sector ) );
        confirm.setVisible( true );

        vBoxInput.getChildren().add( inputBoxWithSpinner.getValue() );
        offAllButton();
    }

    private void inputDesiredLong( ActionEvent event, Spinner<Integer> spinner, String type, String sector  ) {
        Integer value = spinner.getValue();
        LOGGER.log( Level.INFO, String.format( "Пользователь задал параметр: %s", value ) );

        Button diseredLongButton = ( Button )event.getTarget();
        String[] id = diseredLongButton.idProperty().get().split( "_" );
        commandBihaviour.desiredLong( TypeHaircut.typeFrom( id[1] ), id[2], value );

        returnStateButton();
        diseredLongButton.setDisable( true );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );

        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        disiredSteps.put( type + "_" + sector, stepHolder.getLast() );
        vBoxInput.getChildren().add( row );
        confirm.setVisible( false );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.DESIRED_LONG.name() ) );
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
        disabeDisiredButton();
        if ( !stackButton.isEmpty() ) {
            vBoxButton.getChildren().add( stackButton.peek() );
            stackButton.pop();
            return;
        }
        if ( !isHaicut )
            createSatisfiedButton();
        else
            run.setVisible( true );
    }

    private void createColorButton( String typeHaircut ) {
        Button button = new Button( "Цвет волос" ); // создаем кнопуку с названием команды
        button.setOnAction( e -> colorHandler( e, typeHaircut )  ); // добавляем обработчик нажатия
        button.idProperty().set( "color_" + typeHaircut ); // добавляем id кнопки для возможности отличить ее от других
        button.setDisable( true ); // сразу делаем ее активной
        button.setMinWidth( vBoxButton.getPrefWidth() ); // устанавливаем ширину по всему блоку отображкния
        vBoxButton.getChildren().add( button ); // добавляем кнопку на экран
    }

    private void colorHandler( ActionEvent event, String typeHaircut ) { // обработка нажатия на кнопку
        Button colorButton = ( Button )event.getTarget();
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.HAIR_COLOR.name(), colorButton.idProperty().get() ) );
        if ( HEAD == TypeHaircut.typeFrom( typeHaircut ) )
            disabeDisiredButton();
        colorButton.setDisable( true ); // делаем кнопку неактивной

        ObservableList<String> colors = // формируем список возможных параметров для выбора цвета
                FXCollections.observableList( Arrays.stream( HairColor.values() )
                        .map( e -> e.name )
                        .collect( Collectors.toList() )  );
        LOGGER.log( Level.INFO, String.format( "Для пользователя сформированны параметры: %s", colors ) );
        ComboBox<String> comboBox = new ComboBox<>( colors ); // создаем контейнер для ввода значений
        confirm.setOnAction( e -> inputColor( event, comboBox ) ); // весим обработчик подтвержения выбора цвета
        confirm.setVisible( true ); // делаем кнопку подтверждения активной
        HBox row = new HBox(); // создаем контейнер для отображения команды и подля для ввода параметров
        Label command = new Label( "Цвет волос" );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox ); // заполняем контейнер содежимым
        vBoxInput.getChildren().add( row ); // добавляем контейнер для отображения на экран
        offAllButton();
    }

    private void disabeDisiredButton() {
        vBoxButton.getChildren().stream().filter( node ->  node.idProperty().get().startsWith( "disired_" ) )
                .forEach( node -> node.setDisable( true ) );
        for ( Map.Entry<String, HairLong> sectorSize: stateBefore.sectorSize.entrySet() ) {
            stateDisired.sectorSize.computeIfAbsent( sectorSize.getKey(), v -> sectorSize.getValue() );
        }
    }

    private void inputColor( ActionEvent event, ComboBox<String> comboBox ) {
        HairColor color = HairColor.colorFrom( comboBox.getValue() ); // валидация введенного цвета
        Validate.notNull( color, "Не выбран цвет волос" );
        LOGGER.log( Level.INFO, String.format( "Пользователь задал параметры: %s", color.name ) );

        Button diseredLongButton = ( Button )event.getTarget();
        String[] id = diseredLongButton.idProperty().get().split( "_" );
        commandBihaviour.hairColor( TypeHaircut.typeFrom( id[1] ), color ); // обработка введенной команды
        returnStateButton();
        diseredLongButton.setDisable( true ); // деактивация нажатия кнопки

        HBox row = new HBox(); // создаем контейнер для фиксации команды с параметрами введёнными пользователем
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row ); // прихраниваем контейнер для дальнейших манипуляций
        // команду с полем для ввода параметров удаляем с экрана
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 );
        vBoxInput.getChildren().add( row ); // зафиксированную команду добавляем на экран
        confirm.setVisible( false ); // выключаем кнопку для подтверждения введенных парметров
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.HAIR_COLOR.name() ) );
    }

    private void createStyilingButton( String typeHaircut ) {
        Button button = new Button( "Укладка" );
        button.setOnAction( e -> styilingHandler(e, typeHaircut) );
        button.idProperty().set( "styiling_" + typeHaircut );
        button.setDisable( true );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void styilingHandler( ActionEvent event, String typeHaircutString ) {
        Button stylingButton = ( Button )event.getTarget();
        TypeHaircut typeHaircut = typeFrom( typeHaircutString );
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.HAIR_STYLING.name(), stylingButton.idProperty().get() ) );
        if ( HEAD == typeHaircut )
            disabeDisiredButton();
        stylingButton.setDisable( true );
        ObservableList<String> styilings = createListStylingFor( typeHaircut );
        LOGGER.log( Level.INFO, String.format( "Для пользователя сформированны параметры: %s", styilings ) );

        CheckComboBox<String> comboBox = new CheckComboBox<>( styilings );
        confirm.setOnAction( e -> stylingHandler( event, comboBox ) );
        confirm.setVisible( true );
        FlowPane row = new FlowPane();
        Label command = new Label( "Укладка включает" );
        command.setFont( new Font( 18 ) );
        row.getChildren().addAll( command, comboBox );
        vBoxInput.getChildren().add( row );
        offAllButton();
    }

    private ObservableList<String> createListStylingFor( TypeHaircut typeHaircut ) {
        if (  typeHaircut == HEAD ) {
            HairLong hairLong = stateDisired.sectorSize.get( HeadSector.TOP.name() );
            return FXCollections.observableArrayList( hairLong.hairSectorParams.get( HEAD )
                    .stylings.stream().map( e -> e.name ).collect( Collectors.toList() ) );
        } else {
            HairLong hairLong = stateDisired.sectorSize.get( BeardSector.MUSTACHE.name() );
            return hairLong == null ? FXCollections.observableArrayList() :
                    FXCollections.observableArrayList( hairLong.hairSectorParams.get( BRARD )
                    .stylings.stream().map( e -> e.name ).collect( Collectors.toList() ) );
        }
    }

    private void stylingHandler( ActionEvent event, CheckComboBox<String> comboBox ) {
        Button stylingButton = ( Button )event.getTarget();
        List<Styling> checkedIndices = getAndValidateStyilingsParams( comboBox );
        if ( checkedIndices.isEmpty() ) {
            vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 ); // удаляем выражение для ввода
            LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.HAIR_STYLING.name() ) );
            return;
        }

        commandBihaviour.hairStyling( checkedIndices );
        returnStateButton();
        stylingButton.setDisable( true );

        HBox row = new HBox(); // создаем контейнер для фиксации команды с параметрами введёнными пользователем
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().remove( vBoxInput.getChildren().size() - 1 ); // удаляем выражение для ввода
        vBoxInput.getChildren().add( row ); // фиксируем на экране выражение
        confirm.setVisible( false );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.HAIR_STYLING.name() ) );
    }

    private List<Styling> getAndValidateStyilingsParams( CheckComboBox<String> comboBox ) {
        LOGGER.log( Level.INFO, String.format( "Пользователь задал параметры: %S", comboBox.getCheckModel().getCheckedItems() ) );
        List<Styling> checkedIndices =
                comboBox.getCheckModel().getCheckedItems().stream().map( Styling::styingFrom ).collect( Collectors.toList() );
        Validate.isTrue( checkedIndices.size() < 3 && validateCombination( checkedIndices ),
                "Комбинация параметров укладки невозможна" );
        return checkedIndices;
    }

    private boolean validateCombination( List<Styling> stylings ) {
        if ( stylings.size() == 2 ) {
            return AllowedCombinationStylingHolder.getCombination( stylings.get( 0 ) ).contains( stylings.get( 1 ) );
        }
        return true;
    }

    private void createSatisfiedButton() {
        Button button = new Button( "Опрос" );
        button.setOnAction( this::satisfiedHandler );
        button.idProperty().set( "satisfied" );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void satisfiedHandler( ActionEvent event ) {
        Button satisfiedButton = ( Button )event.getTarget();
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.SATISFIED.name(), satisfiedButton.idProperty().get() ) );
        commandBihaviour.satisfied();
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );

        vBoxButton.getChildren().clear();
        for ( TypeHaircut typeHaircut : stateDisired.haircuts) {
            createHaircutButton( typeHaircut );
        }
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.SATISFIED.name() ) );
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
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.HAIRCUT.name(), haircutButton.idProperty().get() ) );
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
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.HAIRCUT.name() ) );
    }

    private void createButtonsAfterHaircut( TypeHaircut typeHaircut ) {
        if ( HEAD == typeHaircut ) {
            createHaircutSectorButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
            createHaircutSectorButton( HEAD.aliases.get( 0 ), HeadSector.BACK.aliases.get( 0 ) );
            createHaircutSectorButton( HEAD.aliases.get( 0 ), HeadSector.TOP.aliases.get( 0 ) );
            createWahingButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
            createDryingButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
            if ( stateDisired.hairColor != HairColor.NO_CHANGE )
                creatColorProcessButton( HEAD.aliases.get( 0 ) );
            if ( stateDisired.hairStylings != null && !stateDisired.hairStylings.isEmpty() )
                createEndStyilingButton( HEAD.aliases.get( 0 ), HeadSector.WHISKY.aliases.get( 0 ) );
        } else {
            createHaircutSectorButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            createHaircutSectorButton( BRARD.aliases.get( 0 ), BeardSector.CHIN.aliases.get( 0 ) );
            createHaircutSectorButton( BRARD.aliases.get( 0 ), BeardSector.MUSTACHE.aliases.get( 0 ) );
            createWahingButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            if ( stateDisired.beardColor != HairColor.NO_CHANGE )
                creatColorProcessButton( BRARD.aliases.get( 0 ) );
            createDryingButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
            if ( stateDisired.beardStylings != null && !stateDisired.beardStylings.isEmpty() )
                createEndStyilingButton( BRARD.aliases.get( 0 ), BeardSector.CHEEKS.aliases.get( 0 ) );
        }

    }

    private void creatColorProcessButton( String type ) {
        Button button = new Button( "Красим волосы" );
        button.setOnAction( e -> colorProcessHandler( e, type ) );
        button.idProperty().set( "color" + type + "_" );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void colorProcessHandler( ActionEvent event, String type  ) {
        Button colorProcess = ( Button )event.getTarget();
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.COLOR_PROCESS.name(), colorProcess.idProperty().get() ) );
        colorProcess.setDisable( true );
        commandBihaviour.colorProcess( TypeHaircut.typeFrom( type ) );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.COLOR_PROCESS.name() ) );
    }

    private void createWahingButton( String type, String sector ) {
        Button button = new Button( "Моем волосы" );
        button.setOnAction( e -> wahingHandler( e, type, sector ) );
        button.idProperty().set( "wahing" + type + "_" + sector );
        button.setMinWidth( vBoxButton.getPrefWidth() );
        vBoxButton.getChildren().add( button );
    }

    private void wahingHandler( ActionEvent event, String type, String sector  ) {
        Button wahingProcess = ( Button )event.getTarget();
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.WASHING_HAIR.name(), wahingProcess.idProperty().get() ) );
        commandBihaviour.washingHair( type );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        vBoxButton.getChildren().stream().filter( e -> e.idProperty().get().startsWith( "drying" ) )
                .forEach( e -> e.setDisable( false ) );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.WASHING_HAIR.name() ) );
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
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.HAIRCUT_SECTOR.name(), haircutSectorButton.idProperty().get() ) );
        String[] id = haircutSectorButton.idProperty().get().split( "_" );
        commandBihaviour.haircutSector( id[1], id[2] );
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
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.HAIRCUT_SECTOR.name() ) );
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
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.DRYING_HAIR.name(), drying.idProperty().get() ) );
        commandBihaviour.dryingHair();
        drying.setDisable( true );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.DRYING_HAIR.name() ) );
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
        LOGGER.log( Level.INFO, String.format( START_HENDEL, CommandConstants.STYLING_PROCESS.name(), endStyilingButton.idProperty().get() ) );
        String[] id = endStyilingButton.idProperty().get().split( "_" );
        commandBihaviour.stylingProcess( TypeHaircut.typeFrom( id[1] ) );
        endStyilingButton.setDisable( true );
        HBox row = new HBox();
        row.setMinWidth( vBoxButton.getPrefWidth() );
        stepHolder.getLast().command.setView( row );
        vBoxInput.getChildren().add( row );
        if ( stackButton.isEmpty() )
            run.setVisible( true );
        LOGGER.log( Level.INFO, String.format( END_HENDEL, CommandConstants.STYLING_PROCESS.name() ) );
    }

    private Integer stepIterator = 0;

    @FXML
    private void runProgrumHandler() {
        if ( run.getText().equals( "Запустить" ) )
            run.setText( "Далее" );
        if ( stepIterator != 0 )
            stepHolder.getSteps().get( stepIterator - 1 ).command.offLight();
        if ( stepIterator >= stepHolder.getSteps().size() ) {
            run.setVisible( false );
            endHaircut();
            return;
        }

        Step step = stepHolder.getSteps().get( stepIterator++ );
        step.command.onLight();
        if ( step.command.command == CommandConstants.HAIRCUT_SECTOR ||
             step.command.command == CommandConstants.COLOR_PROCESS ||
             step.command.command == CommandConstants.STYLING_PROCESS ||
             step.command.command == CommandConstants.WASHING_HAIR ||
             step.command.command == CommandConstants.DRYING_HAIR ||
             step.command.command == CommandConstants.HAIRCUT ) {
            String idStep = step.idStep;
            Utils.copyPrevStateLong( stepIterator - 1 );
            if ( idStep != null ) {
                String[] id = step.idStep.split( "_" );
                TypeHaircut typeHaircut = typeFrom( id[0] );
                if ( typeHaircut == HEAD ) {
                    HeadSector headSector = HeadSector.sectorFrom( id[1] );
                    HairLong hairLong = stateDisired.sectorSize.get( headSector.name() );
                    step.head.setSector( headSector, hairLong );
                } else {
                    BeardSector headSector = BeardSector.sectorFrom( id[1] );
                    HairLong hairLong = stateDisired.sectorSize.get( headSector.name() );
                    step.beard.setSector( headSector, hairLong );
                }
            }
        }
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
        if ( step.command.command == CommandConstants.SATISFIED ) {
            satisfiedBehaviour();
        }
    }



    private List<ImageView> createFaceImage( List<String> hairPaths, Color color ) {
        LOGGER.log( Level.INFO, "Файлы для отображения: " + hairPaths );
        List<ImageView> face = new ArrayList<>();
        for ( String hairPath: hairPaths ) {

            ImageView hairImage;
            try {
                hairImage = new ImageView( new Image(
                        getClass().getResourceAsStream( "/image/" + hairPath )
                ) );
            } catch ( NullPointerException e ) {
                LOGGER.log( Level.WARNING, "Отсутсвует файл: " + hairPath );
                continue;
            }

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

    private void endHaircut() {
        Alert alert = new Alert( Alert.AlertType.INFORMATION);
        alert.setContentText( "Стрижка закончена" );
        alert.showAndWait();
    }

    private void satisfiedBehaviour() {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Вас устравивает ожидаемый результат");
        alert.setTitle("Опрос");
        alert.setContentText("Выберите Да или Нет.");

        ButtonType buttonTypeYes = new ButtonType("Да");
        ButtonType buttonTypeNo = new ButtonType("Нет");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeNo) {
            stepHolder.getSteps().get( stepIterator ).command.offLight();
            stepIterator--;
            createInputScene();
        }
    }

    private void createInputScene() {

        Stage dialog = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader( App.class.getResource( "confirm.fxml" ) );
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 420);
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        dialog.setScene(scene);
        dialog.setTitle("Введите данные");
        dialog.show();
        Containers.getController().fillInputScene( dialog, disiredSteps );
    }

    @FXML
    private void saveCode() throws FileNotFoundException, UnsupportedEncodingException {
        FileChooser fileChooser = new FileChooser();
        File file = new File( App.class.getResource( "haircut.fxml" ).getFile() );
        fileChooser.setInitialDirectory( file.getParentFile().getAbsoluteFile() );
        File file1 = fileChooser.showSaveDialog( Containers.getStage() );
        PrintWriter writer = new PrintWriter(file1, "UTF-8");

        for ( Step step : stepHolder.getSteps() )
            writer.print( step.command.getTextCommand() + "\n" );


        writer.close();
    }

    @FXML
    private void saveLogs() throws FileNotFoundException, UnsupportedEncodingException {
        FileChooser fileChooser = new FileChooser();
        File file = new File( App.class.getResource( "haircut.fxml" ).getFile() );
        fileChooser.setInitialDirectory( file.getParentFile().getAbsoluteFile() );
        File file1 = fileChooser.showSaveDialog( Containers.getStage() );
        Scanner scanner = new Scanner( new File( "D:/javaProject/haircut2/src/main/resources/logs/haircut.log" ) );
        PrintWriter writer = new PrintWriter(file1, "UTF-8");
        while ( scanner.hasNextLine() )
            writer.print( scanner.nextLine() + "\n" );
        writer.close();
    }
}
