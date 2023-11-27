package org.example;

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
}
