package com.pso.experiment;

import java.util.List;

public class RunResult {

    private final List<Double> evolution;   // gbestf por epoch
    private final double finalValue;        // valor al terminar
    private final double[] finalPosition;   // posición final

    public RunResult(List<Double> evolution, double finalValue, double[] finalPosition) {
        this.evolution = evolution;
        this.finalValue = finalValue;
        this.finalPosition = finalPosition;
    }

    public List<Double> getEvolution() { return evolution; }
    public double getFinalValue() { return finalValue; }
    public double[] getFinalPosition() { return finalPosition; }
}
