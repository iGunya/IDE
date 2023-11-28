package org.example.state;

import org.example.state.params.HairColor;
import org.example.state.params.HairLong;
import org.example.state.params.Styling;
import org.example.state.params.TypeHaircut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaircutBeforeState {
    public List<TypeHaircut> haircuts;
    public Map<String, HairLong> sectorSize = new HashMap<>();
    public List<Styling> hairStylings = new ArrayList<>();
    public List<Styling> beardStylings = new ArrayList<>();
    public HairColor hairColor = HairColor.NO_CHANGE;
    public HairColor beardColor = HairColor.NO_CHANGE;

    public Map<String, Integer> sectorSizeNumber = new HashMap<>();
}
