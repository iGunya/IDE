package org.example.behaviour;

import org.apache.commons.lang3.Validate;
import org.example.Utils;
import org.example.di.Containers;
import org.example.state.*;
import org.example.state.params.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.state.params.TypeHaircut.*;

public class CommandBihaviour {

    private final StepHolder stepHolder = Containers.getStepHolder();
    private final HaircutDesiredState stateDisired = Containers.getStateDisired();
    private final HaircutBeforeState stateBefore = Containers.getStateBefore();

    public void choseHaircut( List<TypeHaircut> haircuts ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.CHOSE_HAIRCUT );
        command.params = haircuts.stream().map( e -> e.aliases.get( 0 ) ).collect( Collectors.joining(" и ") );
        step.command = command;
        stepHolder.addStep( step );
        stateDisired.haircuts = haircuts;
    }

    public void forHaircut( TypeHaircut haircut ) {
        Step step = new Step();
        copyPrevState( step );
        Command command = new Command( CommandConstants.FOR );
        command.params = haircut.aliases.get( 0 ) ;
        step.head.isBase = true;
        step.beard.isBase = true;
        step.command = command;
        stepHolder.addStep( step );
    }

    public void currentLongHead( HeadSector sector, int x ) {
        Step step = new Step();
        copyPrevState( step );
        Command command = new Command( CommandConstants.CURRENT_LONG );
        command.params =  sector.aliases.get( 1 ) + " " + x;
        step.command = command;
        step.head.setSector( sector, x );
        stepHolder.addStep( step );
        stateBefore.sectorSize.put( sector.name(), x);
    }

    public void currentLongBeard( BeardSector sector, int x) {
        Step step = new Step();
        copyPrevState( step );
        Command command = new Command( CommandConstants.CURRENT_LONG );
        command.params =  sector.aliases.get( 1 ) + " " + x;
        step.command = command;
        step.beard.setSector( sector, x );
        stepHolder.addStep( step );
        stateBefore.sectorSize.put( sector.name(), x);
    }

    public void desiredLong( TypeHaircut haircut, String sectorSting, int x  ) {

        Step step = new Step();
        copyPrevState( step );
        switch ( haircut ) {
            case HEAD: {
                HeadSector sector = HeadSector.sectorFrom( sectorSting );
                validateDisiredLessThenCurrent( sector, x);
                stateDisired.sectorSize.put( HeadSector.sectorFrom( sectorSting ).name(), Utils.getLong( HEAD, x ) );
                break;
            }
            default: {
                BeardSector sector = BeardSector.sectorFrom( sectorSting );
                validateDisiredLessThenCurrent( sector, x);
                stateDisired.sectorSize.put( BeardSector.sectorFrom( sectorSting ).name(), Utils.getLong( BRARD, x ) );
            }

        }
        Command command = new Command( CommandConstants.DESIRED_LONG );
        command.params =  sectorSting + " " + x;
        step.command = command;
        stepHolder.addStep( step );
    }

    private void validateDisiredLessThenCurrent( Enum<?> sector, int x ) {
        Map<String, Integer> sectorSize = stateBefore.sectorSize;
        assertDisiredLessThenCurrent( sectorSize.get( sector.name() ) > x );
    }

    private void assertDisiredLessThenCurrent( boolean isLess ) {
        Validate.isTrue( isLess,
                "Длина волос до стрижки, должна быть больше чем после стрижки" );
    }

    public void hairColor( TypeHaircut haircut, HairColor color ) {
        Step step = new Step();
        copyPrevState( step );
        Command command = new Command( CommandConstants.HAIR_COLOR );
        command.params = color.name;
        step.command = command;
        stateDisired.hairColor = color;
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    private void copyPrevState( Step step ) {
        Step last = stepHolder.getLast();
        step.beard = Beard.copyFrom( last.beard );
        step.head = Head.copyFrom( last.head );
    }

    public void hairStyling( List<Styling> styling ) {
        Step step = new Step();
        copyPrevState( step );
        Command command = new Command( CommandConstants.HAIR_STYLING );
        command.params = styling.stream().map( e -> e.name ).collect( Collectors.joining( ", " ) );
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
        command.params = typeHaircut.aliases.get( 0 );
        step.command = command;
        copyPrevState( step );
        step.head.isInit = true;
        stepHolder.addStep( step );
    }

    public void washingHair() {
        Step step = new Step();
        Command command = new Command( CommandConstants.WASHING_HAIR );
        command.params = "";
        step.command = command;
        copyPrevState( step );
        stepHolder.addStep( step );
    }

    public void haircutSector( TypeHaircut typeHaircut, String sector ) {
        Step step = new Step();
        Command command = new Command( CommandConstants.HAIRCUT_SECTOR );
        command.params =  sector;
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
        step.command.params = "";
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
        step.command.params = "";
        copyPrevState( step );
        stepHolder.addStep( step );
    }
}
