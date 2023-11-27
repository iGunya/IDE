package org.example.state;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.example.Utils;
import org.example.di.Containers;
import org.example.state.params.*;

import java.util.List;

import static org.example.state.params.BeardSector.*;
import static org.example.state.params.TypeHaircut.BRARD;
import static org.example.state.params.TypeHaircut.HEAD;

public class Beard {

    public HairLong cheeks;
    public HairLong chin;
    public HairLong mustache;
    public HairColor color = HairColor.NO_CHANGE;
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
                cheeks = Utils.getLong( BRARD, size );;
                break;
            }
            case CHIN: {
                chin = Utils.getLong( BRARD, size );;
                break;
            }
            default: {
                mustache = Utils.getLong( BRARD, size );;
                break;
            }
        }
    }

    public List<String> createFileImageName() {
        if ( !isInit || !Containers.getStateDisired().haircuts.contains( BRARD ) )
            return null;

        String colorName = "black";
        StringBuilder fileNameCheeks = new StringBuilder( colorName );
        StringBuilder fileNameChin = new StringBuilder( colorName );
        StringBuilder fileNameMustache = new StringBuilder( colorName );

        if ( cheeks != null )
            fileNameCheeks.append( "_" ).append( cheeks.name().toLowerCase() ).append( "_cheeks" );
        if ( chin != null )
            fileNameChin.append( "_" ).append( chin.name().toLowerCase() ).append( "_chin" );
        if ( mustache != null )
            fileNameMustache.append( "_" ).append( mustache.name().toLowerCase() ).append( "_mustache" );
        if ( stylings != null )
            for ( Styling styling: stylings)
                fileNameMustache.append( "_" ).append( styling.name().toLowerCase() );
        else
            fileNameMustache.append( "_base" );

        return ImmutableList.of( fileNameCheeks.append( ".png" ).toString(),
                fileNameChin.append( ".png" ).toString(),
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
