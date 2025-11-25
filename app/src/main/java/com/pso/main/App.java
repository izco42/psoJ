package com.pso.main;

import com.pso.core.ConstraintFunction;
import com.pso.core.Particle;
import com.pso.experiment.ExperimentRunner;
import com.pso.experiment.RunResult;
import com.pso.experiment.PlotUtils;
import com.pso.functions.RastriginFunction;
import com.pso.functions.SphereFunction;
import com.pso.core.SwarmInitializer;

import org.apache.commons.math3.linear.ArrayRealVector;

import java.util.List;

public class App {

    public static void main(String[] args) {

        int n = 50;
        int d = 2;
        double xMin = -10.0;
        double xMax = 10.0;

        int runs = 20;
        int epochs = 150;

        double w = 0.5;
        double c1 = 2.0;
        double c2 = 2.0;

        // Restricción opcional para Rastrigin
        ConstraintFunction clamp = v -> {
            double[] arr = v.toArray();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = Math.max(-5.12, Math.min(arr[i], 5.12));
            }
            return new ArrayRealVector(arr);
        };

        ConstraintFunction identity = v -> v;

        //SPHERE
        System.out.println("\n===== EXPERIMENTO: SPHERE =====");

        List<RunResult> sphereResults = ExperimentRunner.runMultiple(
                new SphereFunction(),
                identity,
                runs,
                n, d, xMin, xMax,
                w, c1, c2,
                epochs
        );

        double[] sphereAvgEvolution = ExperimentRunner.averageEvolution(sphereResults, epochs);
        double sphereAvgFinal = ExperimentRunner.averageFinalValue(sphereResults);

        System.out.println("Promedio final (Sphere, 20 ejecuciones): " + sphereAvgFinal);

        PlotUtils.plotAverageEvolution(sphereAvgEvolution, "Sphere - Promedio de 20 ejecuciones");


        //RASTRIGIN
        System.out.println("\n===== EXPERIMENTO: RASTRIGIN =====");

        List<RunResult> rastriginResults = ExperimentRunner.runMultiple(
                new RastriginFunction(),
                clamp,
                runs,
                n, d, xMin, xMax,
                w, c1, c2,
                epochs
        );

        double[] rastriginAvgEvolution = ExperimentRunner.averageEvolution(rastriginResults, epochs);
        double rastriginAvgFinal = ExperimentRunner.averageFinalValue(rastriginResults);

        System.out.println("Promedio final (Rastrigin, 20 ejecuciones): " + rastriginAvgFinal);

        PlotUtils.plotAverageEvolution(rastriginAvgEvolution, "Rastrigin - Promedio de 20 ejecuciones");
    }
}


