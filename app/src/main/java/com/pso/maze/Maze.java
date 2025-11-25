package com.pso.maze;

public class Maze {

    private final int width;
    private final int height;
    private final int[][] grid;

    public Maze(int[][] grid) {
        this.grid = grid;
        this.height = grid.length;
        this.width = grid[0].length;
    }

    public int get(int x, int y) {
        return grid[y][x];
    }

    public boolean isFree(int x, int y) {
        return grid[y][x] == 0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
