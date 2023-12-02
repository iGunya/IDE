package org.example.state;

import org.apache.commons.lang3.tuple.Pair;
import org.example.Utils;
import org.example.di.Containers;
import org.example.state.params.*;

import java.util.List;

import static org.example.state.params.HeadSector.WHISKY;
import static org.example.state.params.TypeHaircut.*;

public class Head {

    public HairLong whisky;
    public HairLong back;
    public HairLong top;
    public HairColor color = HairColor.NO_CHANGE;
    public List<Styling> stylings;
    public boolean isBase;
    public boolean isInit;
    public boolean isWashing;

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
                whisky = size == HairLong.NON ? HairLong.SHORT : size;
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
        if ( !isInit || !Containers.getStateDisired().haircuts.contains( HEAD ))
            return null;

        StringBuilder fileName = new StringBuilder( "black" );
        if ( isWashing ) {
            isWashing = false;
            return fileName.append( "_" ).append( top.name().toLowerCase() ).append( "_washing" ).append( ".png" ).toString();
        }

        fileName.append( "_" ).append( top.name().toLowerCase() ).append( "_top" );
        fileName.append( "_" ).append( whisky.name().toLowerCase() ).append( "_wisky" );
        if ( stylings != null )
            for ( Styling styling: stylings)
                fileName.append( "_" ).append( styling.name().toLowerCase() );
        else
            fileName.append( "_base" );


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
