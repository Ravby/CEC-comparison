package si.um.feri.lpm;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.SOBenchmark;
import org.um.feri.ears.problems.*;

enum Cec2021BenchmarkType {
    NON_SHIFTED("Non shifted",new String[]{"000", "100", "001", "101"}),
    SHIFTED("Shifted", new String[]{"010", "011", "110", "111"}),
    NON_ROTATED("Non rotated shifted", new String[]{"010", "110"}),
    ROTATED_SHIFTED("Rotated shifted", new String[]{"011", "111"}),
    ALL("All configurations", new String[]{"000", "001", "010", "011", "100", "101", "110", "111"});

    public String[] configurations;
    public String name;

    Cec2021BenchmarkType(String name, String[] configurations) {
        this.name = name;
        this.configurations = configurations;
    }
}

public class Cec2021StoredBenchmark extends SOBenchmark<NumberSolution<Double>, NumberSolution<Double>, DoubleProblem, NumberAlgorithm> {

    static final BenchmarkInfo cec2021 = BenchmarkManager.get(BenchmarkId.CEC2021);
    Cec2021BenchmarkType benchmarkType;

    int k; // represents the kth number of evaluations in the results file

    public boolean wholeConvergenceGraph = false;

    public Cec2021StoredBenchmark(Cec2021BenchmarkType benchmarkType) {
        this(benchmarkType, cec2021.k -1);
    }

    public Cec2021StoredBenchmark(Cec2021BenchmarkType benchmarkType, int k) {
        this.benchmarkType = benchmarkType;
        this.k = k;
        this.drawLimit = 1e-8; //minimum error
        name = cec2021.name + " Stored benchmark";
        maxEvaluations = 200000;
        maxIterations = 0;
    }

    @Override
    protected void addTask(DoubleProblem p, StopCriterion stopCriterion, int eval, long time, int maxIterations) {
        tasks.add(new Task<>(p, stopCriterion, eval, time, maxIterations));
    }

    @Override
    public void initAllProblems() {

        String[] configurations = benchmarkType.configurations;

        for (String config : configurations) {
            for (int p = 0; p < cec2021.numberOfProblems; p++) {
                for (int d = 0; d < cec2021.dimensions.length; d++) {
                    if (wholeConvergenceGraph) {
                        for (int k = 0; k < cec2021.k; k++) {
                            addTask(new DummyProblem(cec2021.name + "F" + (p + 1) + "(" + config + ")" + "D" + cec2021.dimensions[d] + "k" + (k), false), stopCriterion, cec2021.evaluations[d], 0, maxIterations);
                        }
                    } else {
                        addTask(new DummyProblem(cec2021.name + "F" + (p + 1) + "(" + config + ")" + "D" + cec2021.dimensions[d] + "k" + (k), false), stopCriterion, cec2021.evaluations[d], 0, maxIterations);
                    }
                }
            }
        }
    }
}
