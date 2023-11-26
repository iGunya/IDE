package org.example.di;

import org.example.behaviour.CommandBihaviour;
import org.example.behaviour.StepHolder;
import org.example.state.HaircutBeforeState;
import org.example.state.HaircutDesiredState;

public class Containers {

    private static CommandBihaviour commandBihaviour;
    private static StepHolder stepHolder;
    private static HaircutDesiredState stateDisired;
    private static HaircutBeforeState stateBefore;

    public static CommandBihaviour getCommandBihaviour() {
        if ( commandBihaviour == null )
            commandBihaviour = new CommandBihaviour();
        return commandBihaviour;
    }

    public static StepHolder getStepHolder() {
        if ( stepHolder == null )
            stepHolder = new StepHolder();
        return stepHolder;
    }

    public static HaircutDesiredState getStateDisired() {
        if ( stateDisired == null )
            stateDisired = new HaircutDesiredState();
        return stateDisired;
    }

    public static HaircutBeforeState getStateBefore() {
        if ( stateBefore == null )
            stateBefore = new HaircutBeforeState();
        return stateBefore;
    }
}
