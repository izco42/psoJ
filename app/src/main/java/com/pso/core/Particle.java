package com.pso.core;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;

import java.util.Random;

public class Particle {

    private RealVector position;
    private RealVector velocity;
    private RealVector bestPosition;
    private double bestValue = Double.POSITIVE_INFINITY;

    private static final Random rand = new Random();

    public Particle(RealVector position, RealVector velocity) {
        this.position = position;
        this.velocity = velocity;
        this.bestPosition = position.copy();
    }

    public void updateVelocity(RealVector globalBest, double w, double c1, double c2) {
        for (int i = 0; i < velocity.getDimension(); i++) {
            double r1 = rand.nextDouble();
            double r2 = rand.nextDouble();

            double newV =
                w * velocity.getEntry(i)
                + c1 * r1 * (bestPosition.getEntry(i) - position.getEntry(i))
                + c2 * r2 * (globalBest.getEntry(i) - position.getEntry(i));

            velocity.setEntry(i, newV);
        }
    }

    public void updatePosition() {
        this.position = this.position.add(this.velocity);
    }

    public void updateParticle(RealVector globalBest, double w, double c1, double c2) {
        updateVelocity(globalBest, w, c1, c2);
        updatePosition();
    }

    public double evaluate(AptitudeFunction fn, ConstraintFunction constraint) {
        this.position = constraint.apply(this.position);
        return fn.evaluate(this.position);
    }

    // getters y setters
    public RealVector getPosition() { return position; }
    public RealVector getVelocity() { return velocity; }
    public RealVector getBestPosition() { return bestPosition; }
    public void setBestPosition(RealVector bestPosition) { this.bestPosition = bestPosition; }
    public double getBestValue() { return bestValue; }
    public void setBestValue(double bestValue) { this.bestValue = bestValue; }
}

