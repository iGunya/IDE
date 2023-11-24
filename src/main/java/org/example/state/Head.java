package org.example.state;

import org.apache.commons.lang3.tuple.Pair;
import org.example.Utils;
import org.example.state.params.*;

import java.util.List;

import static org.example.state.params.TypeHaircut.*;

public class Head {

    public HairLong whisky;
    public HairLong back;
    public HairLong top;
    public HairColor color;
    public List<Styling> stylings;

    public Head( int whisky, int back, int top ) {
        this.whisky = Utils.getLong( HEAD, whisky );
        this.back = Utils.getLong( HEAD, back );
        this.top = Utils.getLong( HEAD, top );
    }

    public void setSector( HeadSector sector, HairLong size ) {
        switch ( sector ) {
            case TOP: {
                top = size;
                break;
            }
            case BACK: {
                back = size;
                break;
            }
            default: {
                whisky = size;
                break;
            }
        }
    }
}
