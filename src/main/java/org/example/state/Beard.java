package org.example.state;

import org.apache.commons.lang3.tuple.Pair;
import org.example.Utils;
import org.example.state.params.BeardSector;
import org.example.state.params.HairColor;
import org.example.state.params.HairLong;
import org.example.state.params.Styling;

import java.util.List;

import static org.example.state.params.TypeHaircut.HEAD;

public class Beard {

    public HairLong cheeks;
    public HairLong chin;
    public HairLong mustache;
    public HairColor color;
    public List<Styling> stylings;

    public Beard( int cheeks, int chin, int mustache ) {
        this.cheeks = Utils.getLong( HEAD, cheeks );
        this.chin = Utils.getLong( HEAD, chin );
        this.mustache = Utils.getLong( HEAD, mustache );
    }

    public void setSector( BeardSector sector, HairLong size ) {
        switch ( sector ) {
            case CHEEKS: {
                cheeks = size;
                break;
            }
            case CHIN: {
                chin = size;
                break;
            }
            default: {
                mustache = size;
                break;
            }
        }
    }
}
