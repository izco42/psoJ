package com.pso.main;

import com.pso.core.ConstraintFunction;
import com.pso.core.Particle;
import com.pso.experiment.ExperimentRunner;
import com.pso.experiment.RunResult;
import com.pso.experiment.PlotUtils;
import com.pso.functions.RastriginFunction;
import com.pso.functions.SphereFunction;
import com.pso.core.SwarmInitializer;

import com.pso.maze.*;

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

        // SPHERE
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


        // RASTRIGIN
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

        double[] mazeAvgEvolution = ExperimentRunner.averageEvolution(mazeResults, epochs);
        double mazeAvgFinal = ExperimentRunner.averageFinalValue(mazeResults);

        System.out.println("Promedio final (Maze, 20 ejecuciones): " + mazeAvgFinal);
        PlotUtils.plotAverageEvolution(mazeAvgEvolution, "Maze - Promedio de 20 ejecuciones");

        // Obtener solucion final y graficarla
        var bestParticles = SwarmInitializer.initPopulation(n, pathLength, 0, 3);
        var pso = new com.pso.core.PSO(new MazeFitness(maze, start, goal), bestParticles, MazeConstraints.MOVE_CONSTRAINT);
        pso.evolveWithHistory(w, c1, c2, epochs);

        var simulation = PathSimulator.simulate(maze, start, goal, pso.getGlobalBestPosition());

        MazePlotUtils.plotMazeAndPath(maze, simulation.path(), start, goal, "Maze Solution (PSO)");
    }
}

