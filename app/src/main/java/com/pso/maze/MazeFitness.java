package com.pso.maze;

import com.pso.core.AptitudeFunction;
import org.apache.commons.math3.linear.RealVector;

public class MazeFitness implements AptitudeFunction {

    private final Maze maze;
    private final Node start;
    private final Node goal;

    public MazeFitness(Maze maze, Node start, Node goal) {
        this.maze = maze;
        this.start = start;
        this.goal = goal;
    }

    @Override
    public double evaluate(RealVector movements) {

        var result = PathSimulator.simulate(maze, start, goal, movements);

        if (result.reachedGoal()) {
            return result.path().size(); // menor = mejor
        }

        double dx = result.finalPosition().x() - goal.x();
        double dy = result.finalPosition().y() - goal.y();
        double dist = Math.sqrt(dx*dx + dy*dy);

        return dist + result.collisions() * 10.0;
    }
}
