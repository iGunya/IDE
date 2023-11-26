package org.example.state.params;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;

public enum BeardSector {
    CHEEKS( ImmutableList.of( "Щеки", "Щек" ) ),
    CHIN( ImmutableList.of( "Подбородок", "Подбородка" ) ),
    MUSTACHE( ImmutableList.of( "Усы", "Усов" ) );

    public final List<String> aliases;

    BeardSector( List<String> aliases ) {
        this.aliases = aliases;
    }

    public static BeardSector sectorFrom( String aliase ) {
        return Arrays.stream( BeardSector.values() ).filter( e -> e.aliases.contains( aliase ) ).findFirst().get();
    }
}
