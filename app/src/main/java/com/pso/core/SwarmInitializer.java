package com.pso.core;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwarmInitializer {

    private static final Random rand = new Random();

    public static List<Particle> initPopulation(int n, int d, double xMin, double xMax, double c) {
        List<Particle> swarm = new ArrayList<>();

        double vMax = c * (xMax - xMin);
        double vMin = -vMax;

        for (int i = 0; i < n; i++) {

            double[] pos = new double[d];
            double[] vel = new double[d];

            for (int j = 0; j < d; j++) {
                pos[j] = xMin + rand.nextDouble() * (xMax - xMin);
                vel[j] = vMin + rand.nextDouble() * (vMax - vMin);

                // opcional: clamping manual
                if (vel[j] > vMax) vel[j] = vMax;
                if (vel[j] < vMin) vel[j] = vMin;
            }

            RealVector position = new ArrayRealVector(pos);
            RealVector velocity = new ArrayRealVector(vel);

            swarm.add(new Particle(position, velocity));
        }

        return swarm;
    }

    public static List<Particle> initPopulation(int n, int d, double xMin, double xMax) {
        return initPopulation(n, d, xMin, xMax, 0.2);
    }
}
