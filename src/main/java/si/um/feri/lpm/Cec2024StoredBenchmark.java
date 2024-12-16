package si.um.feri.lpm;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.problems.*;

public class Cec2024StoredBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    static final BenchmarkInfo cec2024 = BenchmarkManager.get(BenchmarkId.CEC2024);

    int k; // represents the kth number of evaluations in the results file

    public boolean wholeConvergenceGraph = false;

    public Cec2024StoredBenchmark() {
        this(cec2024.k - 1);
    }

    public Cec2024StoredBenchmark(int k) {
        this.k = k;
        this.drawLimit = 1e-8; //minimum error
        name = cec2024.name + " Stored benchmark";
        maxEvaluations = 200000;
        maxIterations = 0;
    }

    @Override
    protected void addTask(DoubleProblem p, StopCriterion stopCriterion, int eval, long time, int maxIterations) {
        tasks.add(new Task<>(p, stopCriterion, eval, time, maxIterations));
    }

    @Override
    public void initAllProblems() {

        for (int p = 0; p < cec2024.numberOfProblems; p++) {
            for (int d = 0; d < cec2024.dimensions.length; d++) {
                if (wholeConvergenceGraph) {
                    for (int k = 0; k < cec2024.k; k++) {
                        addTask(new DummyProblem(cec2024.name + "F" + (p + 1) + "D" + cec2024.dimensions[d] + "k" + (k), false), stopCriterion, cec2024.evaluations[d], 0, maxIterations);
                    }
                } else {
                    addTask(new DummyProblem(cec2024.name + "F" + (p + 1) + "D" + cec2024.dimensions[d] + "k" + (k), false), stopCriterion, cec2024.evaluations[d], 0, maxIterations);
                }
            }
        }
    }
}
