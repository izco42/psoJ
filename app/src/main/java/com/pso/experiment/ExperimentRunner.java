package com.pso.experiment;

import com.pso.core.*;
import java.util.ArrayList;
import java.util.List;

public class ExperimentRunner {

    public static List<RunResult> runMultiple(
        AptitudeFunction fn,
        ConstraintFunction constraint,
        int runs,
        int n, int d, double xMin, double xMax,
        double w, double c1, double c2, int epochs
    ) {

        List<RunResult> results = new ArrayList<>();

        for (int r = 0; r < runs; r++) {

            List<Particle> swarm =
                SwarmInitializer.initPopulation(n, d, xMin, xMax);

            PSO pso = new PSO(fn, swarm, constraint);

            List<Double> evolution =
                pso.evolveWithHistory(w, c1, c2, epochs);

            results.add(
                new RunResult(
                    evolution,
                    pso.getGlobalBestValue(),
                    pso.getGlobalBestPosition().toArray()
                )
            );
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
}
    
