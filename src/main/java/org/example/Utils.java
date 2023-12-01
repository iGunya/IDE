package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.apache.commons.lang3.tuple.Pair;
import org.example.behaviour.StepHolder;
import org.example.di.Containers;
import org.example.state.CommandConstants;
import org.example.state.Step;
import org.example.state.params.HairLong;
import org.example.state.params.TypeHaircut;

import static org.example.state.params.TypeHaircut.HEAD;

public class Utils {

    public static HairLong getLong( TypeHaircut type, int x ) {
        for ( HairLong hairParams : HairLong.values() ) {
            HairLong.LongParams hairLong = hairParams.hairSectorParams.get( type );
            if ( x >= hairLong.min && x <= hairLong.max ) {
                return hairParams;
            }
        }
        throw new IllegalArgumentException( "Не должна вылететь " + type );
    }

    public static void setColorFor( ImageView image, Color color ) {
        if ( color == null )
            return;

        Lighting lighting = new Lighting( new Light.Distant(45, 90, color) );
        ColorAdjust bright = new ColorAdjust( 0, 1.0, 0.5, 1 );
        lighting.setContentInput( bright );
        lighting.setSurfaceScale( 1.0 );

        image.setEffect( lighting );
    }

    public static Pair<Spinner<Integer>, HBox> createInputBoxWithSpinner( String type, String sector, CommandConstants command, int def ) {
        Spinner<Integer> spinner;
        if ( TypeHaircut.typeFrom( type ) == HEAD )
            spinner = createSpinner( 0, 500, def );
        else
            spinner = createSpinner( 0, 150, def );
        spinner.idProperty().set( type + "_" + sector );
        HBox row = new HBox();
        String[] currentLong = command.stingCommand.split( "%." );
        Label commandStart = new Label( currentLong[0] + " " + sector );
        commandStart.setFont( new Font( 18 ) );
        Label commandEnd = new Label( currentLong[1] );
        commandEnd.setFont( new Font( 18 ) );
        row.getChildren().addAll( commandStart, spinner, commandEnd );
        return Pair.of( spinner, row );
    }


    private static Spinner<Integer> createSpinner(int min, int max, int def) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, def );
        spinner.setValueFactory(valueFactory);
        spinner.setEditable( true );
        return spinner;
    }

    public static void copyPrevStateLong( int iterator ) {
        StepHolder stepHolder = Containers.getStepHolder();
        Step cur = stepHolder.getSteps().get( iterator );
        Step prev = stepHolder.getSteps().get( iterator - 1 );
        cur.beard.chin = prev.beard.chin;
        cur.beard.cheeks = prev.beard.cheeks;
        cur.beard.mustache = prev.beard.mustache;
        cur.head.top = prev.head.top;
        cur.head.whisky = prev.head.whisky;
        cur.head.back = prev.head.back;
    }
}
