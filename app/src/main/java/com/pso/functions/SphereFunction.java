package com.pso.functions;

import com.pso.core.AptitudeFunction;
import org.apache.commons.math3.linear.RealVector;

public class SphereFunction implements AptitudeFunction {

    @Override
    public double evaluate(RealVector v) {
        double sum = 0.0;

        for (int i = 0; i < v.getDimension(); i++) {
            double x = v.getEntry(i);
            sum += x * x;
        }

        return sum;
    }
}
