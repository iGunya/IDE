package org.example.state;

import org.apache.commons.lang3.tuple.Pair;
import org.example.Utils;
import org.example.state.params.*;

import java.util.List;

import static org.example.state.params.TypeHaircut.HEAD;

public class Beard {

    public HairLong cheeks;
    public HairLong chin;
    public HairLong mustache;
    public HairColor color;
    public List<Styling> stylings;

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

    public void setSector( BeardSector sector, Integer size ) {
        switch ( sector ) {
            case CHEEKS: {
                cheeks = Utils.getLong( HEAD, size );;
                break;
            }
            case CHIN: {
                chin = Utils.getLong( HEAD, size );;
                break;
            }
            default: {
                mustache = Utils.getLong( HEAD, size );;
                break;
            }
        }
    }
}
