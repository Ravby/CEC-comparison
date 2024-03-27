package si.um.feri.lpm;

import java.util.HashMap;
import java.util.Map;

public class BenchmarkManager {
    private static Map<BenchmarkId, BenchmarkInfo> benchmarks = new HashMap<>();
    private static BenchmarkManager instance = new BenchmarkManager();

    private BenchmarkManager() {

        BenchmarkInfo cec2022 = new BenchmarkInfo("CEC2022", BenchmarkId.CEC2022, 12, 30, 16, new int[]{10, 20}, new int[]{200000, 1000000}); //24 files for each algorithm
        BenchmarkInfo cec2021 = new BenchmarkInfo("CEC2021", BenchmarkId.CEC2021, 10, 30, 16, new int[]{10, 20}, new int[]{200000, 1000000}); //100 files for each algorithm
        BenchmarkInfo cec2017 = new BenchmarkInfo("CEC2017", BenchmarkId.CEC2017, 30, 51, 14, new int[]{10, 30, 50, 100}, new int[]{100000, 300000, 500000, 1000000}); //120 files for each algorithm (MaxFEs = 10000 * D)

        benchmarks.put(cec2022.id, cec2022);
        benchmarks.put(cec2021.id, cec2021);
        benchmarks.put(cec2017.id, cec2017);
    }

    public static BenchmarkManager getInstance() {
        return instance;
    }

    public static Map<BenchmarkId, BenchmarkInfo> getAll() {
        return benchmarks;
    }

    public static BenchmarkInfo get(BenchmarkId id) {
        return benchmarks.get(id);
    }
}