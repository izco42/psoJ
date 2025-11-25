package com.pso.functions;

import com.pso.core.AptitudeFunction;
import org.apache.commons.math3.linear.RealVector;

public class RastriginFunction implements AptitudeFunction {

    @Override
    public double evaluate(RealVector v) {
        double A = 10.0;
        int d = v.getDimension();

        double sum = 0.0;

        for (int i = 0; i < d; i++) {
            double xi = v.getEntry(i);
            sum += (xi * xi) - A * Math.cos(2 * Math.PI * xi);
        }

        return A * d + sum;
    }
}
