package org.example.state.params;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;

public enum HeadSector {
    WHISKY( ImmutableList.of( "Виски", "Висков" ) ),
    BACK( ImmutableList.of( "Затылок", "Затылка" ) ),
    TOP( ImmutableList.of( "Верх", "Верха" ) );

    public final List<String> aliases;

    HeadSector( List<String> aliases ) {
        this.aliases = aliases;
    }

    public static HeadSector sectorFrom( String aliase ) {
        return Arrays.stream( HeadSector.values() ).filter( e -> e.aliases.contains( aliase ) ).findFirst().get();
    }
}
