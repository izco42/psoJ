package com.pso.experiment;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.List;

public class PlotUtils {

    public static void plotSingleEvolution(List<Double> evolution, String title) {

        int epochs = evolution.size();
        double[] x = new double[epochs];
        double[] y = new double[epochs];

        for (int i = 0; i < epochs; i++) {
            x[i] = i;
            y[i] = evolution.get(i);
        }

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle("Epoch")
                .yAxisTitle("Fitness")
                .build();

        chart.addSeries("Evolution", x, y);

        new SwingWrapper<>(chart).displayChart();
    }

    public static void plotAverageEvolution(double[] avgEvolution, String title) {

        int epochs = avgEvolution.length;
        double[] x = new double[epochs];

        for (int i = 0; i < epochs; i++) x[i] = i;

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle("Epoch")
                .yAxisTitle("Promedio Fitness")
                .build();

        chart.addSeries("Promedio", x, avgEvolution);

        new SwingWrapper<>(chart).displayChart();
    }
}
