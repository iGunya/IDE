package org.example.state;

import org.example.state.params.HairColor;
import org.example.state.params.Styling;
import org.example.state.params.TypeHaircut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaircutDesiredState {

    public List<TypeHaircut> strings;

    public Map<String, Integer> sectorSize = new HashMap<>();
    public List<Styling> hairStylings = new ArrayList<>();
    public List<Styling> beardStylings = new ArrayList<>();
    public HairColor hairColor;

    public void setStylings( List<Styling> stylings ) {
        for ( Styling styiling: stylings) {
            if ( styiling == Styling.MUSTACHE ) {
                beardStylings.add( styiling );
                continue;
            }
            hairStylings.add( styiling );
        }
    }
}
