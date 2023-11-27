package org.example.state.params;

import javafx.scene.paint.Color;

import java.util.Arrays;

public enum HairColor {

    NO_CHANGE( "без изменений", null ),
    BLACK( "черный", null ),
    WHITE( "белый", Color.WHITE ),
    GREEN( "зеленый", Color.GREEN ),
    BLUE( "синий", Color.BLUE ),
    FIRE_HIRED( "рыжий", Color.DARKOLIVEGREEN ),
    REDHEAD( "красный", Color.RED );

    public final String name;
    public final Color color;

    HairColor( String name, Color color ) {
        this.name = name;
        this.color = color;
    }

    public static HairColor colorFrom( String color ) {
        return Arrays.stream( HairColor.values() ).filter( e -> e.name.equals( color ) ).findFirst().get();
    }
}
