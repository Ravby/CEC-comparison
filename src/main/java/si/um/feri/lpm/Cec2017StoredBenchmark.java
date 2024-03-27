package si.um.feri.lpm;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.problems.*;

public class Cec2017StoredBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    static final BenchmarkInfo cec2017 = BenchmarkManager.get(BenchmarkId.CEC2017);

    int k; // represents the kth number of evaluations in the results file
    public boolean wholeConvergenceGraph = false;

    public Cec2017StoredBenchmark() {
        this(cec2017.k - 1);
    }

    public Cec2017StoredBenchmark(int k) {
        this.k = k;
        this.drawLimit = 1e-8; //minimum error
        name = cec2017.name + " Stored benchmark";
        maxEvaluations = 200000;
        maxIterations = 0;
    }

    @Override
    protected void addTask(DoubleProblem p, StopCriterion stopCriterion, int eval, long time, int maxIterations) {
        tasks.add(new Task<>(p, stopCriterion, eval, time, maxIterations));
    }

    @Override
    public void initAllProblems() {
        for (int p = 0; p < cec2017.numberOfProblems; p++) {
            // F2 has been excluded because it shows unstable behavior especially for higher dimensions
            if (p == 1)
                continue;

            for (int d = 0; d < cec2017.dimensions.length; d++) {
                if (wholeConvergenceGraph) {
                    for (int k = 0; k < cec2017.k; k++) {
                        addTask(new DummyProblem(cec2017.name + "F" + (p + 1) + "D" + cec2017.dimensions[d] + "k" + (k), false), stopCriterion, cec2017.evaluations[d], 0, maxIterations);
                    }
                } else {
                    addTask(new DummyProblem(cec2017.name + "F" + (p + 1) + "D" + cec2017.dimensions[d] + "k" + (k), false), stopCriterion, cec2017.evaluations[d], 0, maxIterations);
                }
            }
        }
    }
}
