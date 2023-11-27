package org.example.state;

import com.google.common.collect.ImmutableList;
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
    public boolean isBase;
    public boolean isInit;

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

    public List<String> createFileImageName() {
        if ( isInit )
            return null;

        String colorName = color == null ? "black" : color.name;
        StringBuilder fileNameCheeks = new StringBuilder( colorName );
        StringBuilder fileNameChin = new StringBuilder( colorName );
        StringBuilder fileNameMustache = new StringBuilder( colorName );

        fileNameCheeks.append( "_" ).append( cheeks.name().toLowerCase() ).append( "_cheeks" );
        fileNameChin.append( "_" ).append( chin.name().toLowerCase() ).append( "_chin" );
        fileNameMustache.append( "_" ).append( mustache.name().toLowerCase() ).append( "_mustache" );
        if ( isBase )
            fileNameMustache.append( "_base" );
        else if ( stylings != null )
            for ( Styling styling: stylings)
                fileNameMustache.append( "_" ).append( styling.name );

        return ImmutableList.of( fileNameCheeks.append( ".png" ).toString(),
                fileNameCheeks.append( ".png" ).toString(),
                fileNameMustache.append( ".png" ).toString() );
    }

    public static Beard copyFrom( Beard copy ) {
        Beard newBeard = new Beard();
        newBeard.cheeks = copy.cheeks;
        newBeard.chin = copy.chin;
        newBeard.mustache = copy.mustache;
        newBeard.color = copy.color;
        newBeard.stylings = copy.stylings;
        newBeard.isBase = copy.isBase;
        newBeard.isInit = copy.isInit;
        return newBeard;
    }
}
