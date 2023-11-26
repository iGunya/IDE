package org.example.state.params;

import com.google.common.collect.ImmutableList;
import javafx.scene.control.Button;

import java.util.List;

public enum TypeHaircut {
    HEAD( ImmutableList.of( "головы", "голову" ) ),
    BRARD( ImmutableList.of( "бороды", "бороду" ) );

    public final List<String> aliases;

    TypeHaircut( List<String> aliases ) {
        this.aliases = aliases;
    }

    public static TypeHaircut typeFrom( Button button ) {
        return button.idProperty().get().equals( HEAD.name() ) ? HEAD : BRARD;
    }

    public static TypeHaircut typeFrom( String aliase ) {
        return TypeHaircut.HEAD.aliases.contains( aliase ) ? HEAD : BRARD;
    }
}
