package org.example.state;

import org.apache.commons.lang3.Validate;
import org.example.state.params.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaircutBeforeState {

    public List<TypeHaircut> strings;
    public Map<String, Integer> sectorSize = new HashMap<>();
    public HairColor hairColor;
}
