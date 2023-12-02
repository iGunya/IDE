package org.example.state;

import org.apache.commons.lang3.Validate;
import org.example.state.params.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.state.params.BeardSector.*;
import static org.example.state.params.HeadSector.TOP;
import static org.example.state.params.HeadSector.WHISKY;

public class HaircutDesiredState {

    public List<TypeHaircut> haircuts;
    public Map<String, HairLong> sectorSize = new HashMap<>();
    public List<Styling> hairStylings = new ArrayList<>();
    public List<Styling> beardStylings = new ArrayList<>();
    public HairColor hairColor = HairColor.NO_CHANGE;
    public HairColor beardColor = HairColor.NO_CHANGE;
    public Map<String, Integer> sectorSizeNumber = new HashMap<>();

    public void setStylings( List<Styling> stylings ) {
        for ( Styling styiling: stylings) {
            if ( styiling == Styling.MUSTACHE ) {
                validatePosibleStyle( HairLong.MIDLE == sectorSize.get( BeardSector.MUSTACHE.name() ) );
                beardStylings.add( styiling );
                continue;
            }
            HairLong hairLong = sectorSize.get( TOP.name() );
            validatePosibleStyle( hairLong.hairSectorParams.get( TypeHaircut.HEAD ).stylings.contains( styiling ) );
            hairStylings.add( styiling );
        }
    }

    private void validatePosibleStyle( boolean isPossibvle ) {
        Validate.isTrue( isPossibvle,
                "Для текущей длинны невозможно выполнить выбранную укладку" );
    }

    public String createFileImageNamesForHead() {
        if ( !haircuts.contains( TypeHaircut.HEAD ) )
            return null;
        StringBuilder fileName = new StringBuilder( "black" );
        fileName.append( "_" ).append( sectorSize.get( TOP.name() ).name().toLowerCase() ).append( "_top" );
        HairLong whiskyLong = sectorSize.get( WHISKY.name() ) == HairLong.NON ? HairLong.SHORT : sectorSize.get( WHISKY.name() );
        fileName.append( "_" ).append( whiskyLong.name().toLowerCase() ).append( "_wisky" );
        if ( !hairStylings.isEmpty() )
            for ( Styling styling: hairStylings )
                fileName.append( "_" ).append( styling.name().toLowerCase() );
        else
            fileName.append( "_base" );
        return fileName.append( ".png" ).toString();
    }

    public List<String> createFileImageNamesForBeard() {
        if ( !haircuts.contains( TypeHaircut.BRARD ) )
            return null;

        List<String> fileNames = new ArrayList<>();
        String colorName = "black";
        StringBuilder fileNameCheeks = new StringBuilder( colorName );
        StringBuilder fileNameChin = new StringBuilder( colorName );
        StringBuilder fileNameMustache = new StringBuilder( colorName );

        HairLong cheeksLong = sectorSize.get( CHEEKS.name() );
        if ( cheeksLong != null )
            fileNameCheeks.append( "_" ).append( cheeksLong.name().toLowerCase() ).append( "_cheeks" );
        HairLong chinLong = sectorSize.get( CHIN.name() );
        if ( chinLong != null )
            fileNameChin.append( "_" ).append( chinLong.name().toLowerCase() ).append( "_chin" );
        HairLong mustacheLong = sectorSize.get( MUSTACHE.name() );
        if ( mustacheLong != null )
            fileNameMustache.append( "_" ).append( mustacheLong.name().toLowerCase() ).append( "_mustache" );

        if ( !beardStylings.isEmpty() )
            for ( Styling styling: beardStylings)
                fileNameMustache.append( "_" ).append( styling.name().toLowerCase() );
        else
            fileNameMustache.append( "_base" );

        fileNames.add( fileNameCheeks.append( ".png" ).toString() );
        fileNames.add( fileNameChin.append( ".png" ).toString() );
        fileNames.add( fileNameMustache.append( ".png" ).toString() );
        return fileNames;
    }
}
