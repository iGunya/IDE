package org.example.state.params;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public enum BeardSector {
    CHEEKS( ImmutableList.of( "Щек", "Щеки" ) ),
    CHIN( ImmutableList.of( "Подбородок", "Подбородка" ) ),
    MUSTACHE( ImmutableList.of( "Усов", "Усы" ) );

    public final List<String> aliases;

    BeardSector( List<String> aliases ) {
        this.aliases = aliases;
    }
}
