package si.um.feri.lpm;

enum BenchmarkId {
    CEC2017,
    CEC2021,
    CEC2022,
    CEC2024
}
public class BenchmarkInfo {
    public String name;
    public BenchmarkId id;
    public int numberOfProblems;
    public int k;
    public int runs;
    public int[] dimensions;
    public int[] evaluations;

    public BenchmarkInfo(String name, BenchmarkId id, int numberOfProblems, int runs, int k, int[] dimensions, int[] evaluations) {
        this.name = name;
        this.id = id;
        this.numberOfProblems = numberOfProblems;
        this.k = k;
        this.runs = runs;
        this.dimensions = dimensions;
        this.evaluations = evaluations;
    }
}
