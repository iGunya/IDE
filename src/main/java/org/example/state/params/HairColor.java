package org.example.state.params;

import java.util.Arrays;

public enum HairColor {

    NO_CHANGE( "без изменений" ),
    BLACK( "черный" ),
    WHITE( "белый" ),
    GREEN( "зеленый" ),
    BLUE( "синий" ),
    FIRE_HIRED( "рыжий" ),
    REDHEAD( "красный" );

    public final String name;

    HairColor( String name ) {
        this.name = name;
    }

    public static HairColor colorFrom( String color ) {
        return Arrays.stream( HairColor.values() ).filter( e -> e.name.equals( color ) ).findFirst().get();
    }
}
