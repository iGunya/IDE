package org.example.state;

import org.apache.commons.lang3.tuple.Pair;
import org.example.state.params.HairColor;
import org.example.state.params.HeadSector;
import org.example.state.params.Styling;

import java.util.List;

public class Head {

    public final Pair<HeadSector, Integer> whisky;
    public final Pair<HeadSector, Integer> back;
    public final Pair<HeadSector, Integer> top;
    public HairColor color;
    public List<Styling> stylings;

    public Head( int whisky, int back, int top ) {
        this.whisky = Pair.of( HeadSector.WHISKY, whisky );
        this.back = Pair.of( HeadSector.BACK, back );
        this.top = Pair.of( HeadSector.TOP, top );
    }

    public void setSector( HeadSector sector, int size ) {
        switch ( sector ) {
            case TOP: {
                top.setValue( size );
                break;
            }
            case BACK: {
                back.setValue( size );
                break;
            }
            default: {
                whisky.setValue( size );
                break;
            }
        }
    }
}
