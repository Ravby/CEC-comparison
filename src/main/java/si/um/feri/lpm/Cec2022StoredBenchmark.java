package si.um.feri.lpm;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.problems.*;

public class Cec2022StoredBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    static final BenchmarkInfo cec2022 = BenchmarkManager.get(BenchmarkId.CEC2022);

    int k; // represents the kth number of evaluations in the results file

    public boolean wholeConvergenceGraph = false;

    public Cec2022StoredBenchmark() {
        this(cec2022.k - 1);
    }

    public Cec2022StoredBenchmark(int k) {
        this.k = k;
        this.drawLimit = 1e-8; //minimum error
        name = cec2022.name + " Stored benchmark";
        maxEvaluations = 200000;
        maxIterations = 0;
    }

    @Override
    protected void addTask(DoubleProblem p, StopCriterion stopCriterion, int eval, long time, int maxIterations) {
        tasks.add(new Task<>(p, stopCriterion, eval, time, maxIterations));
    }

    @Override
    public void initAllProblems() {

        for (int i = 0; i < 5; i++) { //Only Rotated Shifted cases were considered in the final ranking
            for (int d = 0; d < cec2022.dimensions.length; d++) {
                if (wholeConvergenceGraph) {
                    for (int k = 0; k < cec2022.k; k++) {
                        addTask(new DummyProblem(cec2022.name + "F" + (i + 1) + "D" + cec2022.dimensions[d] + "k" + (k), false), stopCriterion, cec2022.evaluations[d], 0, maxIterations);
                    }
                } else {
                    addTask(new DummyProblem(cec2022.name + "F" + (i + 1) + "D" + cec2022.dimensions[d] + "k" + (k), false), stopCriterion, cec2022.evaluations[d], 0, maxIterations);
                }
            }
        }
    }
}
