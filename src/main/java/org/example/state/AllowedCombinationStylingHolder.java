package org.example.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.example.state.params.Styling;

import java.util.List;
import java.util.Map;

import static org.example.state.params.Styling.*;

public class AllowedCombinationStylingHolder {

    private static final Map<Styling, List<Styling>> allowedCombination =
            ImmutableMap.of( PARTING, ImmutableList.of( LEFT, RIGHT ),
                             CURLS, ImmutableList.of(),
                             LEFT, ImmutableList.of( PARTING ),
                             RIGHT, ImmutableList.of( PARTING ),
                             DREADLOCKS, ImmutableList.of(),
                             MUSTACHE, ImmutableList.of() );

    public List<Styling> getCombination( Styling styling ) {
        return allowedCombination.get( styling );
    }
}
