package org.example.state;

import org.apache.commons.lang3.Validate;
import org.example.state.params.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaircutDesiredState {

    public List<TypeHaircut> strings;
    public Map<String, HairLong> sectorSize = new HashMap<>();
    public List<Styling> hairStylings = new ArrayList<>();
    public List<Styling> beardStylings = new ArrayList<>();
    public HairColor hairColor;

    public void setStylings( List<Styling> stylings ) {
        for ( Styling styiling: stylings) {
            if ( styiling == Styling.MUSTACHE ) {
                validatePosibleStyle( HairLong.MIDLE == sectorSize.get( BeardSector.MUSTACHE.name() ) );
                beardStylings.add( styiling );
                continue;
            }
            HairLong hairLong = sectorSize.get( HeadSector.TOP.name() );
            validatePosibleStyle( hairLong.hairSectorParams.get( TypeHaircut.HEAD ).stylings.contains( styiling ) );
            hairStylings.add( styiling );
        }
    }

    private void validatePosibleStyle( boolean isPossibvle ) {
        Validate.isTrue( isPossibvle,
                "Для текущей длинны невозможно выполнить выбранную укладку" );
    }
}
