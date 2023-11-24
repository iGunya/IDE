package org.example.state;

import org.apache.commons.lang3.tuple.Pair;
import org.example.state.params.BeardSector;
import org.example.state.params.HairColor;
import org.example.state.params.HeadSector;
import org.example.state.params.Styling;

import java.util.List;

public class Beard {

    public final Pair<BeardSector, Integer> cheeks;
    public final Pair<BeardSector, Integer> chin;
    public final Pair<BeardSector, Integer> mustache;
    public HairColor color;
    public List<Styling> stylings;

    public Beard( int cheeks, int chin, int mustache ) {
        this.cheeks = Pair.of( BeardSector.CHEEKS, cheeks );
        this.chin = Pair.of( BeardSector.CHIN, chin );
        this.mustache = Pair.of( BeardSector.MUSTACHE, mustache );
    }

    public void setSector( BeardSector sector, int size ) {
        switch ( sector ) {
            case CHEEKS: {
                cheeks.setValue( size );
                break;
            }
            case CHIN: {
                chin.setValue( size );
                break;
            }
            default: {
                mustache.setValue( size );
                break;
            }
        }
    }
}
