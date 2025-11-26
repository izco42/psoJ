package com.pso.main;

import com.pso.core.ConstraintFunction;
import com.pso.experiment.ExperimentRunner;
import com.pso.experiment.RunResult;
import com.pso.experiment.PlotUtils;
import com.pso.experiment.ExperimentRunner.RunStats;
import com.pso.functions.RastriginFunction;
import com.pso.functions.SphereFunction;

import com.pso.maze.*;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

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

        // SPHERE
        System.out.println("\n===== EXPERIMENTO: SPHERE =====");
        printContinuousParams("Sphere", n, d, xMin, xMax, runs, epochs, w, c1, c2);

        RunResult sphereSingle = ExperimentRunner.runSingle(
                new SphereFunction(),
                identity,
                n, d, xMin, xMax,
                w, c1, c2,
                epochs
        );
        PlotUtils.plotSingleEvolution(sphereSingle.getEvolution(), "Sphere - Evolución (1 corrida)");

        List<RunResult> sphereResults = ExperimentRunner.runMultiple(
                new SphereFunction(),
                identity,
                runs,
                n, d, xMin, xMax,
                w, c1, c2,
                epochs
        );

        RunStats sphereStats = ExperimentRunner.summarizeBestValues(sphereResults);
        printStatsTable("Sphere", sphereStats);


        // RASTRIGIN
        System.out.println("\n===== EXPERIMENTO: RASTRIGIN =====");
        printContinuousParams("Rastrigin", n, d, xMin, xMax, runs, epochs, w, c1, c2);

        RunResult rastriginSingle = ExperimentRunner.runSingle(
                new RastriginFunction(),
                clamp,
                n, d, xMin, xMax,
                w, c1, c2,
                epochs
        );
        PlotUtils.plotSingleEvolution(rastriginSingle.getEvolution(), "Rastrigin - Evolución (1 corrida)");

        List<RunResult> rastriginResults = ExperimentRunner.runMultiple(
                new RastriginFunction(),
                clamp,
                runs,
                n, d, xMin, xMax,
                w, c1, c2,
                epochs
        );

        RunStats rastriginStats = ExperimentRunner.summarizeBestValues(rastriginResults);
        printStatsTable("Rastrigin", rastriginStats);


        //           MAZE EXPERIMENT
        System.out.println("\n===== EXPERIMENTO: MAZE =====");

        // Laberinto simple de ejemplo
        int[][] grid = {
                {0,0,0,1,0},
                {1,1,0,1,0},
                {0,0,0,0,0},
                {0,1,1,1,1},
                {0,0,0,0,0}
        };

        Maze maze = new Maze(grid);
        Node start = new Node(0, 0);
        Node goal = new Node(4, 4);

        int pathLength = 40; // vector de movimientos
        printMazeParams(n, pathLength, runs, epochs, w, c1, c2, 0, 3);

        RunResult mazeSingle = ExperimentRunner.runSingle(
                new MazeFitness(maze, start, goal),
                MazeConstraints.MOVE_CONSTRAINT,
                n,
                pathLength,
                0, 3,
                w, c1, c2,
                epochs
        );
        PlotUtils.plotSingleEvolution(mazeSingle.getEvolution(), "Maze - Evolución (1 corrida)");

        List<RunResult> mazeResults = ExperimentRunner.runMultiple(
                new MazeFitness(maze, start, goal),
                MazeConstraints.MOVE_CONSTRAINT,  // <--- constraint especial
                runs,
                n,
                pathLength,
                0, 3,   // valores válidos del movimiento
                w, c1, c2,
                epochs
        );

        RunStats mazeStats = ExperimentRunner.summarizeBestValues(mazeResults);
        printStatsTable("Maze", mazeStats);

        // Graficar solución final usando la mejor posición de la corrida individual
        RealVector mazeBest = new ArrayRealVector(mazeSingle.getFinalPosition());
        var simulation = PathSimulator.simulate(maze, start, goal, mazeBest);

        MazePlotUtils.plotMazeAndPath(maze, simulation.path(), start, goal, "Maze Solution (PSO)");
    }

    private static void printContinuousParams(
            String name,
            int n, int d, double xMin, double xMax,
            int runs, int epochs,
            double w, double c1, double c2
    ) {
        System.out.printf(
                "%s params -> n=%d, d=%d, xMin=%.2f, xMax=%.2f, runs=%d, epochs=%d, w=%.2f, c1=%.2f, c2=%.2f%n",
                name, n, d, xMin, xMax, runs, epochs, w, c1, c2
        );
    }

    private static void printMazeParams(
            int n, int pathLength, int runs, int epochs,
            double w, double c1, double c2,
            int moveMin, int moveMax
    ) {
        System.out.printf(
                "Maze params -> n=%d, pathLength=%d, moveRange=[%d,%d], runs=%d, epochs=%d, w=%.2f, c1=%.2f, c2=%.2f%n",
                n, pathLength, moveMin, moveMax, runs, epochs, w, c1, c2
        );
    }

    private static void printStatsTable(String title, RunStats stats) {
        System.out.println("\n" + title + " - mejores valores por ejecución (runs=20)");
        System.out.println("Ejecución\tMejor valor final");
        for (int i = 0; i < stats.bestValues().size(); i++) {
            System.out.printf("%2d\t\t%.6f%n", i + 1, stats.bestValues().get(i));
        }
        System.out.printf("Promedio: %.6f%n", stats.mean());
        System.out.printf("Desv. estándar: %.6f%n", stats.stdDev());
    }
}
