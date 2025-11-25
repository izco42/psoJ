package com.pso.maze;

import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;

public class PathSimulator {

    public record SimulationResult(
            List<Node> path,
            boolean reachedGoal,
            int collisions,
            Node finalPosition
    ) {}

    public static SimulationResult simulate(
            Maze maze,
            Node start,
            Node goal,
            RealVector movements
    ) {
        int x = start.x();
        int y = start.y();

        List<Node> path = new ArrayList<>();
        path.add(new Node(x, y));

        int collisions = 0;

        for (int i = 0; i < movements.getDimension(); i++) {

            int move = (int) Math.round(movements.getEntry(i)) % Move.MOVE_COUNT;
            if (move < 0) move += Move.MOVE_COUNT;

            int nx = x;
            int ny = y;

            switch (move) {
                case Move.UP -> ny--;
                case Move.RIGHT -> nx++;
                case Move.DOWN -> ny++;
                case Move.LEFT -> nx--;
            }

            // validar movimiento
            if (nx < 0 || nx >= maze.getWidth() ||
                ny < 0 || ny >= maze.getHeight() ||
                !maze.isFree(nx, ny)) {

                collisions++;
                continue; // no nos movemos
            }

            // movimiento válido
            x = nx;
            y = ny;

            path.add(new Node(x, y));

            if (x == goal.x() && y == goal.y()) {
                return new SimulationResult(path, true, collisions, new Node(x,y));
            }
        }

        return new SimulationResult(path, false, collisions, new Node(x,y));
    }
}
