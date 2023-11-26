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

    public void setSector( HeadSector sector, Integer size ) {
        switch ( sector ) {
            case TOP: {
                top = Utils.getLong( HEAD, size );
                break;
            }
            case BACK: {
                back = Utils.getLong( HEAD, size );
                break;
            }
            default: {
                whisky = Utils.getLong( HEAD, size );
                break;
            }
        }
    }
}
