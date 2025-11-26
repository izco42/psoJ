package com.pso.experiment;

import com.pso.core.*;
import java.util.ArrayList;
import java.util.List;

public class ExperimentRunner {

    public static RunResult runSingle(
            AptitudeFunction fn,
            ConstraintFunction constraint,
            int n, int d, double xMin, double xMax,
            double w, double c1, double c2, int epochs
    ) {
        List<Particle> swarm = SwarmInitializer.initPopulation(n, d, xMin, xMax);
        PSO pso = new PSO(fn, swarm, constraint);

        List<Double> evolution = pso.evolveWithHistory(w, c1, c2, epochs);

        return new RunResult(
                evolution,
                pso.getGlobalBestValue(),
                pso.getGlobalBestPosition().toArray()
        );
    }

    public static List<RunResult> runMultiple(
            AptitudeFunction fn,
            ConstraintFunction constraint,
            int runs,
            int n, int d, double xMin, double xMax,
            double w, double c1, double c2, int epochs
    ) {

        List<RunResult> results = new ArrayList<>();

        for (int r = 0; r < runs; r++) {
            results.add(runSingle(fn, constraint, n, d, xMin, xMax, w, c1, c2, epochs));
        }

        return results;
    }

    public static double[] averageEvolution(List<RunResult> results, int epochs) {

        double[] avg = new double[epochs];

        for (RunResult r : results) {
            for (int i = 0; i < epochs; i++) {
                avg[i] += r.getEvolution().get(i);
            }
        }

        for (int i = 0; i < epochs; i++) {
            avg[i] /= results.size();
        }

        return avg;
    }

    public static double averageFinalValue(List<RunResult> results) {
        return results.stream()
                .mapToDouble(RunResult::getFinalValue)
                .average()
                .orElse(Double.NaN);
    }

    public static RunStats summarizeBestValues(List<RunResult> results) {
        List<Double> bestValues = new ArrayList<>();
        for (RunResult r : results) {
            bestValues.add(r.getFinalValue());
        }

        double mean = bestValues.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);

        double variance = bestValues.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(Double.NaN);

        double stdDev = Double.isNaN(variance) ? Double.NaN : Math.sqrt(variance);

        return new RunStats(bestValues, mean, stdDev);
    }

    public static record RunStats(List<Double> bestValues, double mean, double stdDev) {}
}
    
