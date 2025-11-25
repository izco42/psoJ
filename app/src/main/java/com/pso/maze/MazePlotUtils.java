package com.pso.maze;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.Circle;
import org.knowm.xchart.style.markers.Diamond;
import org.knowm.xchart.style.markers.Square;

import java.awt.*;
import java.util.List;

public class MazePlotUtils {

    public static void plotMazeAndPath(
            Maze maze,
            List<Node> path,
            Node start,
            Node goal,
            String title
    ) {
        int width = maze.getWidth();
        int height = maze.getHeight();

        // ==========================
        // OBSTÁCULOS
        // ==========================
        double[] obsX = new double[width * height];
        double[] obsY = new double[width * height];
        int count = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!maze.isFree(x, y)) {
                    obsX[count] = x;
                    obsY[count] = -y; // invertimos Y para que se vea "natural"
                    count++;
                }
            }
        }

        double[] obstaclesX = new double[count];
        double[] obstaclesY = new double[count];
        System.arraycopy(obsX, 0, obstaclesX, 0, count);
        System.arraycopy(obsY, 0, obstaclesY, 0, count);

        // ==========================
        // CHART
        // ==========================
        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle("X")
                .yAxisTitle("Y")
                .build();

        // ==========================
        // SERIES: OBSTÁCULOS (scatter)
        // ==========================
        XYSeries obstacleSeries = chart.addSeries(
                "Obstacles",
                obstaclesX,
                obstaclesY
        );
        obstacleSeries.setMarker(new Square());
        obstacleSeries.setMarkerColor(Color.BLACK);
        obstacleSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        // ==========================
        // SERIES: PATH (línea roja + puntos)
        // ==========================
        double[] px = new double[path.size()];
        double[] py = new double[path.size()];
        for (int i = 0; i < path.size(); i++) {
            px[i] = path.get(i).x();
            py[i] = -path.get(i).y();
        }

        XYSeries pathSeries = chart.addSeries("Path", px, py);
        pathSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        pathSeries.setLineColor(Color.RED);
        pathSeries.setMarker(new Circle());
        pathSeries.setMarkerColor(Color.RED);

        // ==========================
        // START (verde, scatter)
        // ==========================
        XYSeries startSeries = chart.addSeries(
                "Start",
                new double[]{start.x()},
                new double[]{-start.y()}
        );
        startSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        startSeries.setMarker(new Diamond());
        startSeries.setMarkerColor(Color.GREEN);

        // ==========================
        // GOAL (azul, scatter)
        // ==========================
        XYSeries goalSeries = chart.addSeries(
                "Goal",
                new double[]{goal.x()},
                new double[]{-goal.y()}
        );
        goalSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        goalSeries.setMarker(new Diamond());
        goalSeries.setMarkerColor(Color.BLUE);

        // Mostrar el gráfico
        new SwingWrapper<>(chart).displayChart();
    }
}

