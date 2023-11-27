package org.example.state.params;

import java.util.Arrays;

public enum Styling {

    BANGS_LEFT( "челка влево" ),
    BANGS_RIGHT( "челка вправо" ),
    CURLS( "кудри" ),
    PARTING( "пробор" ),
    DREADLOCKS( "дреды" ),
    MUSTACHE( "усы" );

    public final String name;

    Styling( String name ) {
        this.name = name;
    }

    public static Styling styingFrom( String styiling ) {
        return Arrays.stream( Styling.values() ).filter( e -> e.name.equals( styiling ) ).findFirst().get();
    }
}
