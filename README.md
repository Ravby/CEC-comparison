# CEC Competition Confidence Bands based on Rating

This project aims to provide tools for comparing evolutionary algorithms with the winners of the CEC (Congress on Evolutionary Computation) competitions held in 2017, 2021, and 2022. It utilizes confidence bands based on rating, as presented in the paper "Confidence Bands Based on Rating Demonstrated on the CEC 2021 Competition Results".

## Usage

### Dependencies

This project relies on the EARS (Evolutionary Algorithm Rating System) framework, available at [EARS GitHub Repository](https://github.com/UM-LPM/EARS).

### Project Structure

- **CEC_results**: Contains official result files obtained from the following sources:
    - [CEC2017](https://github.com/P-N-Suganthan/CEC2017-BoundContrained)
    - [CEC2021](https://github.com/P-N-Suganthan/2021-SO-BCO)
    - [CEC2022](https://github.com/P-N-Suganthan/2022-SO-BO)

- **TransformResultsFromCecToEars**: This class is responsible for converting CEC result files to the EARS format.

- **EARS**: This folder contains converted files (e.g., [CEC2017/EARS](CEC_results/CEC2017/EARS)) enabling algorithm comparison.

- **Benchmark Classes**: For each CEC competition, there is a benchmark class (e.g., [Cec2017StoredBenchmark](src/main/java/si/um/feri/lpm/Cec2017StoredBenchmark.java)) with specific information about the competition and selected problems.

- **Run Classes**: These classes (e.g., [RunCec2017](src/main/java/si/um/feri/lpm/RunCec2017.java)) execute the benchmark using a list of algorithms for comparison. By default, results are stored in the experimental_results folder (e.g., [CEC_results/CEC2017/EARS/experimental_results](CEC_results/CEC2017/EARS/experimental_results)).

### Usage Instructions

1. **Format Result Files**: Ensure that the result files follow the official guidelines provided by each competition's "Problem Definitions and Evaluation Criteria" PDFs located in the [CEC_results](CEC_results) directory.
2. **Add Result Files**: Add your algorithm's result files to the official results folder.
3. **Run TransformResultsFromCecToEars**: To convert the results files to EARS format.
4. **Run Benchmark**: Add your algorithm to the players list (`players.add(new DummyAlgorithm("AlgorithmName", algorithmResultsDir));`) in one of the run classes to execute the benchmark, comparing your algorithm with CEC winners.
   