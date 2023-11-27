package org.example.state.params;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;

public enum BeardSector {
    CHEEKS( ImmutableList.of( "щеки", "щек" ) ),
    CHIN( ImmutableList.of( "подбородок", "подбородка" ) ),
    MUSTACHE( ImmutableList.of( "усы", "усов" ) );

    public final List<String> aliases;

    BeardSector( List<String> aliases ) {
        this.aliases = aliases;
    }

    public static BeardSector sectorFrom( String aliase ) {
        return Arrays.stream( BeardSector.values() ).filter( e -> e.aliases.contains( aliase ) ).findFirst().get();
    }
}
