package org.example.di;

import javafx.stage.Stage;
import org.example.behaviour.CommandBihaviour;
import org.example.behaviour.StepHolder;
import org.example.controller.ConfirmController;
import org.example.state.HaircutBeforeState;
import org.example.state.HaircutDesiredState;
import org.example.state.Step;

import java.util.HashMap;
import java.util.Map;

public class Containers {

    private static CommandBihaviour commandBihaviour;
    private static StepHolder stepHolder;
    private static HaircutDesiredState stateDisired;
    private static HaircutBeforeState stateBefore;
    private static Stage stage;
    private static ConfirmController controller;
    private static final Map<String, Step> disiredSteps = new HashMap<>();

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

    public static Stage getStage() {
        return stage;
    }

    public static void setStage( Stage stage ) {
        Containers.stage = stage;
    }

    public static ConfirmController getController() {
        return controller;
    }

    public static void setController( ConfirmController controller ) {
        Containers.controller = controller;
    }

    public static Map<String, Step> getDisiredSteps() {
        return disiredSteps;
    }
}
