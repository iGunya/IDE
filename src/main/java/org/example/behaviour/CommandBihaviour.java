package org.example.behaviour;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.Validate;
import org.example.Utils;
import org.example.state.*;
import org.example.state.params.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.state.params.BeardSector.*;
import static org.example.state.params.HeadSector.*;
import static org.example.state.params.TypeHaircut.*;

public class CommandBihaviour {

    private final StepHolder stepHolder = new StepHolder();
    private final HaircutDesiredState stateDisired = new HaircutDesiredState();
    private final HaircutBeforeState stateBefore = new HaircutBeforeState();

    public void choseHaircut( List<TypeHaircut> haircuts ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.CHOSE_HAIRCUT );
        command.params = haircuts.stream().map( TypeHaircut::toString ).collect( Collectors.toList() );
        step.command = command;
        stepHolder.addStep( step );
        stateDisired.strings = haircuts;
    }

    public void forHaircut( TypeHaircut haircut ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.FOR );
        command.params = ImmutableList.of( haircut.toString() );
        step.command = command;
        stepHolder.addStep( step );
    }

    public void currentLong( TypeHaircut haircut, int x, int y, int z ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.CURRENT_LONG );
        command.params = ImmutableList.of( haircut.toString(), String.valueOf( x ),
                String.valueOf( y ), String.valueOf( z ) );
        step.command = command;
        stepHolder.addStep( step );
        switch ( haircut ) {
            case HEAD: {
                stateBefore.sectorSize.put( WHISKY.name(), x );
                stateBefore.sectorSize.put( BACK.name(), y );
                stateBefore.sectorSize.put( TOP.name(), z );
            }
            default: {
                stateBefore.sectorSize.put( CHIN.name(), x );
                stateBefore.sectorSize.put( CHEEKS.name(), y );
                stateBefore.sectorSize.put( MUSTACHE.name(), z );
            }

        }
    }

    public void desiredLong( TypeHaircut haircut, int x, int y, int z  ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.DESIRED_LONG );
        command.params = ImmutableList.of( haircut.toString(), String.valueOf( x ),
                String.valueOf( y ), String.valueOf( z ) );
        step.command = command;
        validateDisiredLessThenCurrent( haircut, x, y, z );
        switch ( haircut ) {
            case HEAD: {
                step.head = new Head( x, y, z );
                stateDisired.sectorSize.put( WHISKY.name(), Utils.getLong( HEAD, x ) );
                stateDisired.sectorSize.put( BACK.name(), Utils.getLong( HEAD, y ) );
                stateDisired.sectorSize.put( TOP.name(), Utils.getLong( HEAD, z ) );
            }
            default: {
                step.beard = new Beard( x, y, z );
                stateDisired.sectorSize.put( CHIN.name(), Utils.getLong( BRARD, x ) );
                stateDisired.sectorSize.put( CHEEKS.name(), Utils.getLong( BRARD, y ) );
                stateDisired.sectorSize.put( MUSTACHE.name(), Utils.getLong( BRARD, z ) );
            }

        }
        stepHolder.addStep( step );
    }

    private void validateDisiredLessThenCurrent( TypeHaircut haircut, int x, int y, int z ) {
        Map<String, Integer> sectorSize = stateBefore.sectorSize;
        switch ( haircut ) {
            case HEAD: {
                assertDisiredLessThenCurrent( sectorSize.get( WHISKY.name() ) > x );
                assertDisiredLessThenCurrent( sectorSize.get( BACK.name() ) > y );
                assertDisiredLessThenCurrent( sectorSize.get( TOP.name() ) > z );
            }
            default: {
                assertDisiredLessThenCurrent( sectorSize.get( CHIN.name() ) > x );
                assertDisiredLessThenCurrent( sectorSize.get( CHEEKS.name() ) > y );
                assertDisiredLessThenCurrent( sectorSize.get( MUSTACHE.name() ) > z );
            }

        }
    }

    private void assertDisiredLessThenCurrent( boolean isLess ) {
        Validate.isTrue( isLess,
                "Длина волос до стрижки, должна быть больше чем после стрижки" );
    }

    public void hairColor( TypeHaircut haircut, HairColor color ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.HAIR_COLOR );
        command.params = ImmutableList.of( color.toString() );
        step.command = command;
        switch ( haircut ) {
            case HEAD: {
                step.head.color = color;
            }
            default: {
                step.beard.color = color;
            }

        }
        stateDisired.hairColor = color;
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    private void copyPrevState( Step step ) {
        Step last = stepHolder.getLast();
        step.beard = last.beard;
        step.head = last.head;
    }

    public void hairStyling( List<Styling> styling ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.HAIR_STYLING );
        command.params = styling.stream().map( Styling::toString ).collect( Collectors.toList() );
        step.command = command;
        stateDisired.setStylings( styling );
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    public void satisfied() {
        Step step = new Step();
        step.command = new Command( CommandConstants.SATISFIED );
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    public void haircut( TypeHaircut typeHaircut ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.HAIRCUT );
        command.params = ImmutableList.of( typeHaircut.toString() );
        step.command = command;
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    public void washingHair( TypeHaircut typeHaircut ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.WASHING_HAIR );
        command.params = ImmutableList.of( typeHaircut.toString() );
        step.command = command;
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    public void haircutSector( TypeHaircut typeHaircut, String sector ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.HAIRCUT_SECTOR );
        command.params = ImmutableList.of( sector );
        step.command = command;
        copyPrevState( step );
        if ( typeHaircut == HEAD ) {
            HeadSector headSector = Arrays.stream( HeadSector.values() ).filter( e -> e.aliases.contains( sector ) ).findFirst().get();
            HairLong hairLong = stateDisired.sectorSize.get( headSector.name() );
            step.head.setSector( headSector, hairLong );
        } else {
            BeardSector beardSector = Arrays.stream( BeardSector.values() ).filter( e -> e.aliases.contains( sector ) ).findFirst().get();
            HairLong hairLong = stateDisired.sectorSize.get( beardSector.name() );
            step.beard.setSector( beardSector, hairLong );
        }
        stepHolder.addStep( step );
    }

    public void stylingProcess( TypeHaircut typeHaircut ) {
        Step step = new Step();
        step.command = new Command( CommandConstants.STYLING_PROCESS );
        copyPrevState( step );
        if ( typeHaircut == BRARD )
            step.beard.stylings = stateDisired.beardStylings;
        else
            step.head.stylings = stateDisired.hairStylings;
        stepHolder.addStep( step );
    }

    public void dryingHair() {
        Step step = new Step();
        step.command = new Command( CommandConstants.DRYING_HAIR );
        copyPrevState( step );
        stepHolder.addStep( step );
    }
}
