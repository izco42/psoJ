package com.pso.maze;

import java.util.*;

public class GraphBuilder {

    public static Map<Node, List<Node>> buildGraph(Maze maze) {

        Map<Node, List<Node>> graph = new HashMap<>();

        int width = maze.getWidth();
        int height = maze.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (!maze.isFree(x, y)) continue;

                Node node = new Node(x, y);
                List<Node> neighbors = new ArrayList<>();

                // arriba
                if (y > 0 && maze.isFree(x, y - 1)) {
                    neighbors.add(new Node(x, y - 1));
                }
                // abajo
                if (y < height - 1 && maze.isFree(x, y + 1)) {
                    neighbors.add(new Node(x, y + 1));
                }
                // izquierda
                if (x > 0 && maze.isFree(x - 1, y)) {
                    neighbors.add(new Node(x - 1, y));
                }
                // derecha
                if (x < width - 1 && maze.isFree(x + 1, y)) {
                    neighbors.add(new Node(x + 1, y));
                }

                graph.put(node, neighbors);
            }
        }

        return graph;
    }
}
