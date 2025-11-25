package com.pso.core;

import org.apache.commons.math3.linear.RealVector;

@FunctionalInterface
public interface AptitudeFunction {
    double evaluate(RealVector position);
}
