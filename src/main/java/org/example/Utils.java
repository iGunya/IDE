package org.example;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.example.state.params.HairLong;
import org.example.state.params.TypeHaircut;

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
}
