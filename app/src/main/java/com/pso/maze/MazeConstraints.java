package com.pso.maze;

import com.pso.core.ConstraintFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class MazeConstraints {

    /** 
     * Forces particle vector components to valid maze moves {0,1,2,3}
     * This keeps PSO discrete without modifying the core algorithm.
     */
    public static final ConstraintFunction MOVE_CONSTRAINT = (RealVector v) -> {
        double[] arr = v.toArray();
        for (int i = 0; i < arr.length; i++) {
            // Round, convert to integer, keep in range 0..3
            int move = Math.floorMod((int)Math.round(arr[i]), Move.MOVE_COUNT);
            arr[i] = move;
        }
        return new ArrayRealVector(arr);
    };
}
