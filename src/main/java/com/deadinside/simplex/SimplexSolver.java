package com.deadinside.simplex;

import com.deadinside.simplex.types.ConstraintType;
import com.deadinside.simplex.types.OptimisationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 22/11/15.
 */
public class SimplexSolver {

    List<String> solve = new ArrayList<>();

    private double[][] schema;
    private int aimIndex;
    private int cIndex;

    //equation identifier
    private String[] head;
    private String[] side;

    public List<String> getSolve() {
        return solve;
    }

    public double[] solveExtended(SimplexProblem aSimplexProblem) {
        SimplexSolverDataHolder problem = SimplexSolverDataHolder.create(aSimplexProblem);
        //equals
        problem.convertEqualsConstraints();

        //switch ineqations to pattern: <=
        for (SimplexConstraint c : problem.getConstraints()) {
            if (c.getConstraintType() == ConstraintType.GreaterThanEquals)
                c.convertInequation();
        }

        //check if max or min
        if (problem.getOptimisationType() == OptimisationType.Min)
            problem.convertInequation();

        //create head & side variables
        head = new String[problem.getCoefficients().length - 1];

        for (int i = 0; i < head.length; i++)
            head[i] = "x" + (i + 1);

        side = new String[problem.getConstraints().length];

        for (int i = 0; i < side.length; i++)
            side[i] = "y" + (i + 1);

        //create schema
        schema = new double[problem.getConstraints().length + 1][problem.getCoefficients().length];

        aimIndex = schema.length - 1;
        cIndex = schema[aimIndex].length - 1;

        //fill initial schema
        for (int y = 0; y < aimIndex; y++)
            schema[y] = problem.getConstraints()[y].getSlackVariables();

        schema[aimIndex] = problem.getSlackVariables();

        //calcualte cancle steps size for zyclic problems
        long maxSteps = getMaxIterations(problem.getCoefficients().length - 2, problem.getConstraints().length);

        printSchema("Initial Schema");

        //check for dual algorithm
        int negCCount = getNegativeCCount();
        if (negCCount > 0) {
            dualAlgorithm(negCCount);
        }

        //run algorithm
        int stepCount = 0;
        while (nextStep(stepCount++)) {
            if (stepCount > maxSteps) {
                //cancle because of zyclic problems
                solve.add("Cylcic Problem! Not solvable!");
                return null;
            }
        }

        if (negCCount > 0) {
            schema[aimIndex][cIndex] *= -1;
        }

        solve.add("\n");

        double[] result = new double[head.length + 1];
        result[0] = schema[aimIndex][cIndex];
        //solve head functions
        for (int i = 0; i < head.length; i++) {
            String xName = "x" + (i + 1);
            int index = getIndexOf(xName);
            double v = schema[index][cIndex];
            result[i + 1] = v;
            solve.add(xName + "\t= " + v);
        }


        //show result
        solve.add("Result: " + schema[aimIndex][cIndex]);
        solve.add("\n");

        return result;
    }

    public Double solve(SimplexProblem aSimplexProblem) {

        double[] values = solveExtended(aSimplexProblem);
        Double result = values != null ? values[0] : null;
        return result;
    }

    private int getNegativeCCount() {
        int c = 0;

        boolean[] negC = getNegativeC();
        for (int i = 0; i < negC.length; i++) {
            if (negC[i])
                c++;
        }

        return c;
    }


    private boolean[] getNegativeC() {
        boolean[] isNegative = new boolean[schema.length - 1];
        for (int i = 0; i < schema.length - 1; i++) {
            isNegative[i] = schema[i][cIndex] < 0;
        }

        return isNegative;
    }

    private boolean nextStep(int stepCount) {
        //get position of not null aim function component
        MatrixPos aimFunctionPos = getPositiveAimFunctionComponent();

        //finished if aimFunctionPos is Null
        if (aimFunctionPos == null)
            return false;

        solve.add("Aim Column (" + aimFunctionPos.x + "): " + schema[aimIndex][aimFunctionPos.x]);

        //get position of smallest quotient (ci / aiq)
        MatrixPos pivotIndex = getPositionOfSmallestQuotient(aimFunctionPos.x);

        solve.add("Pivot (" + pivotIndex.y + "|" + pivotIndex.x + "): " + schema[pivotIndex.y][pivotIndex.x]);

        swap(pivotIndex);

        printSchema("Step " + stepCount);
        return true;
    }

    private void swap(MatrixPos pivotIndex) {
        //shift with pivot
        schema[pivotIndex.y] = Equation.shift(schema[pivotIndex.y], pivotIndex.x);

        //swap variable name
        swapVariableName(pivotIndex.y, pivotIndex.x);

        //solve every function except the one with the pivot element
        double[] values = schema[pivotIndex.y];
        for (int y = 0; y < schema.length; y++) {
            if (y != pivotIndex.y) {
                double[] eq = schema[y];
                schema[y] = Equation.plugIn(eq, values, pivotIndex.x);
            }
        }
    }

    private MatrixPos getPositionOfSmallestQuotient(int x) {
        double min = Double.MAX_VALUE;
        int bestY = -1;

        for (int y = 0; y < aimIndex; y++) {
            double aiq = schema[y][x];

            //bq has to be bigger than or equal 0
            if (aiq >= 0)
                continue;

            double q = Math.abs(schema[y][cIndex] / aiq);
            if (q < min) {
                min = q;
                bestY = y;
            }
        }

        assert bestY != -1;

        //return pivot element
        return new MatrixPos(bestY, x);
    }

    private MatrixPos getPositiveAimFunctionComponent() {
        for (int x = 0; x < cIndex; x++) {
            if (schema[aimIndex][x] > 0)
                return new MatrixPos(aimIndex, x);
        }

        //nothing found
        solve.add("no new element found to switch!");
        return null;
    }

    private void swapVariableName(int s, int h) {
        String tmp = side[s];
        side[s] = head[h];
        head[h] = tmp;
    }

    private int getIndexOf(String varName) {
        for (int i = 0; i < aimIndex; i++)
            if (side[i].equals(varName))
                return i;

        return 0;
    }

    private void printSchema(String message) {
        StringBuilder stringBuilder = new StringBuilder();
        solve.add(message + ":");
        //print header
        solve.add("\t");


        for (String s : head) stringBuilder.append(String.format("%12s", s));
        solve.add(stringBuilder.toString());
        stringBuilder.setLength(0);

        for (int i = 0; i < head.length; i++)
            stringBuilder.append(String.format("%12s", "--"));

        solve.add(stringBuilder.toString());
        stringBuilder.setLength(0);

        solve.add("\n");

        //print data
        for (int y = 0; y < schema.length; y++) {
            if (y == schema.length - 1)
                stringBuilder.append("z  | ");
            else
                stringBuilder.append(side[y] + " | ");

            for (int x = 0; x < schema[y].length; x++) {
                stringBuilder.append(String.format("%12.2f", schema[y][x]));
            }
            solve.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }

        solve.add("\n");
        solve.add("\n");
    }

    private void dualAlgorithm(int negCCount) {
        solve.add("Problem can only be solved with dual algorithm!\n");

        //save z and add h and cIndex
        double[] saveZ = schema[aimIndex].clone();
        int saveCIndex = cIndex;

        schema[aimIndex] = new double[schema[aimIndex].length];

        //for each minus add a helper var
        for (int n = 0; n < negCCount; n++) {

            //add q0 as helper
            for (int i = 0; i < schema.length; i++) {
                schema[i] = SimplexUtil.insertAt((schema[i]), 0, 1);

                if (i == aimIndex) {
                    //add name
                    head = SimplexUtil.insertAt(head, 0, "q" + n);
                    cIndex++;
                }
            }
        }

        //set h = -q0
        //todo: is it really -1?
        schema[aimIndex][0] = -1;

        //print
        printSchema("Dual Schema");

        //switch negative c's
        boolean[] negativeC = getNegativeC();
        int ci = 0;
        for (int i = negativeC.length - 1; i >= 0; i--) {
            if (negativeC[i]) {
                //todo: switch var
                swap(new MatrixPos(i, ci));
                ci++;
            }
        }

        printSchema("Pre Simplex Dual");

        //todo: do the simplex
        int stepCount = 0;
        while (nextStep(stepCount++)) {
        }

        solve.add("Dual algorithm stopped!");

        //todo: cleanup
        //remove columns
        for (int i = 0; i < head.length; i++) {
            if (head[i].startsWith("q")) {
                //remove entry
                for (int j = 0; j < schema.length; j++) {
                    schema[j] = SimplexUtil.removeAt(schema[j], i);
                }

                //remove head
                head = SimplexUtil.removeAt(head, i);
                i--;
            }
        }

        //change vars
        int indexX1 = getIndexOf("x1");
        schema[aimIndex] = Equation.plugIn(saveZ, schema[indexX1], 0);

        cIndex = saveCIndex;

        printSchema("Dual-Fixed Simplex Schema");
    }

    private long getMaxIterations(int varCount, int inequationCount) {
        int n = varCount + inequationCount;
        int k = inequationCount;

        return faculty(n) / (faculty(n - k) * faculty(k));
    }

    private long faculty(int n) {
        long m = 1;
        for (int i = 1; i <= n; i++) {
            m *= i;
        }
        return m;
    }
}
