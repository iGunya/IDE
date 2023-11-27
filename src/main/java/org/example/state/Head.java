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
    public boolean isBase;
    public boolean isInit;

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

    public String createFileImageName() {
        if ( isInit )
            return null;

        StringBuilder fileName = new StringBuilder();
        fileName.append( color == null ? "black" : color.name );
        fileName.append( "_" ).append( top.name().toLowerCase() ).append( "_top" );
        fileName.append( "_" ).append( whisky.name().toLowerCase() ).append( "_wisky" );
        if ( isBase )
            fileName.append( "_base" );
        else if ( stylings != null )
            for ( Styling styling: stylings)
                fileName.append( "_" ).append( styling.name );

        return fileName.append( ".png" ).toString();
    }

    public static Head copyFrom( Head copy ) {
        Head newHead = new Head();
        newHead.back = copy.back;
        newHead.top = copy.top;
        newHead.whisky = copy.whisky;
        newHead.color = copy.color;
        newHead.stylings = copy.stylings;
        newHead.isBase = copy.isBase;
        newHead.isInit = copy.isInit;
        return newHead;
    }
}
