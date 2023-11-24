package org.example.behaviour;

import com.google.common.collect.ImmutableList;
import org.example.state.Step;

import java.util.List;

public class StepHolder {

    private List<Step> steps;

    public void addStep( Step step ) {
        steps.add( step );
    }

    public List<Step> getSteps() {
        return ImmutableList.copyOf( steps );
    }

    public Step getLast() {
        return steps.get( steps.size() - 1 );
    }
}
