package org.example.state.params;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

import static org.example.state.params.Styling.*;
import static org.example.state.params.TypeHaircut.BRARD;
import static org.example.state.params.TypeHaircut.HEAD;

public enum HairLong {
    SHORT( ImmutableMap.of( HEAD, new LongParams( ImmutableSet.of( BANGS_LEFT, BANGS_RIGHT, CURLS ), 0, 50 ) ,
                            BRARD, new LongParams( ImmutableSet.of(), 0, 1 ) ) ),
    MIDLE( ImmutableMap.of( HEAD, new LongParams( ImmutableSet.of( BANGS_LEFT, BANGS_RIGHT, CURLS, PARTING, DREADLOCKS), 51, 150 ),
                            BRARD, new LongParams( ImmutableSet.of( MUSTACHE ) , 2, 5 ) ) ),
    LONG( ImmutableMap.of( HEAD, new LongParams( ImmutableSet.of( DREADLOCKS, PARTING ), 151, 500 ),
                           BRARD, new LongParams( ImmutableSet.of( MUSTACHE ) , 6, 15) ) );

    public final Map<TypeHaircut, LongParams> hairSectorParams;

    HairLong( Map<TypeHaircut, LongParams> hairSectorParams ) {
        this.hairSectorParams = hairSectorParams;
    }

    public static class LongParams {
        public final Set<Styling> stylings;
        public final int min;
        public final int max;

        public LongParams( Set<Styling> stylings, int min, int max ) {
            this.stylings = stylings;
            this.min = min;
            this.max = max;
        }
    }
}
