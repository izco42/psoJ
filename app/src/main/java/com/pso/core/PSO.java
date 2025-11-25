package com.pso.core;

import org.apache.commons.math3.linear.RealVector;
import java.util.ArrayList;
import java.util.List;

public class PSO {

    private final AptitudeFunction fn;
    private final List<Particle> swarm;
    private final ConstraintFunction constraint;

    private RealVector globalBestPosition;
    private double globalBestValue = Double.POSITIVE_INFINITY;

    public PSO(
        AptitudeFunction fn,
        List<Particle> swarm,
        ConstraintFunction constraint
    ) {
        this.fn = fn;
        this.swarm = swarm;
        this.constraint = constraint != null ? constraint : (v -> v);

        this.globalBestPosition = swarm.get(0).getPosition().copy();
    }

    public void showSwarm() {
        for (Particle p : swarm) {
            System.out.println("pos=" + p.getPosition() +
                               ", vel=" + p.getVelocity());
        }
    }

    public void evolve(double w, double c1, double c2, int epochs) {
        for (int e = 0; e < epochs; e++) {
            for (Particle p : swarm) {

                double current = p.evaluate(fn, constraint);

                if (current < p.getBestValue()) {
                    p.setBestValue(current);
                    p.setBestPosition(p.getPosition().copy());
                }

                if (current < globalBestValue) {
                    globalBestValue = current;
                    globalBestPosition = p.getPosition().copy();
                }

                p.updateParticle(globalBestPosition, w, c1, c2);
            }
        }
    }

    public RealVector getGlobalBestPosition() {
        return globalBestPosition;
    }

    public double getGlobalBestValue() {
        return globalBestValue;
    }


public List<Double> evolveWithHistory(double w, double c1, double c2, int epochs) {
    List<Double> history = new ArrayList<>();

    for (int e = 0; e < epochs; e++) {
        for (Particle p : swarm) {
            double current = p.evaluate(fn, constraint);

            if (current < p.getBestValue()) {
                p.setBestValue(current);
                p.setBestPosition(p.getPosition().copy());
            }

            if (current < globalBestValue) {
                globalBestValue = current;
                globalBestPosition = p.getPosition().copy();
            }

            p.updateParticle(globalBestPosition, w, c1, c2);
        }

        history.add(globalBestValue);
    }

    return history;
}


}

