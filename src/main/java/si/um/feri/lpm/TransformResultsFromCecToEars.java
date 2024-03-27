package si.um.feri.lpm;

import org.um.feri.ears.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TransformResultsFromCecToEars {
    public static void main(String[] args) {

        //User can play around with different combinations of k values and dimensions in the benchmark
        //Option to use different files (CEC or EARS format)
        //selling point: EARS is easier to use and understand, users can use their own algorithms

        //Each line contains 30 runs seperated by a tab, comma, space.
        //Each run represents the function error value (f(x) - f(x*)).
        //Each line represents a k value (k = 0, 1, 2, 3, ... , 15), which defines a MaxFES.
        //Only for CEC 2022: The last line (17) represents the number of function evaluations (FES) used to reach the solution.
        //10^-8 equals to zero = draw limit in EARS benchmark
        //file name format for CEC2022 and CEC2017: AlgorithmName_FunctionNo._D.txt
        //file name format for CEC2021: AlgorithmName_(Parametrized_Vector)_FunctionNo._D.txt

        //CEC2017 F2 has been excluded because it shows unstable behavior especially for higher dimensions
        //CEC2021 same weights for all dimensions

        //select benchmark
        BenchmarkInfo benchmarkInfo = BenchmarkManager.get(BenchmarkId.CEC2021);

        String projectDirectory = System.getProperty("user.dir");
        File projectDirFile = new File(projectDirectory);
        String algorithmResultsDir = projectDirFile + File.separator + "CEC_results" + File.separator + benchmarkInfo.name;

        //go through all files in the directory and convert them to EARS format
        File directory = new File(algorithmResultsDir);
        if (directory.exists() && directory.isDirectory()) {

            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = getFileNameWithoutExtension(file.getName());
                        String[] splitFileName = fileName.split("_");
                        int length = splitFileName.length;
                        String dim, functionNo, algorithmName, rotation = "";

                        if(benchmarkInfo.id == BenchmarkId.CEC2022 || benchmarkInfo.id == BenchmarkId.CEC2017) {
                            dim = splitFileName[length-1]; //dimension is at last index
                            functionNo = splitFileName[length-2]; //function number is at second last index
                            algorithmName = fileName.substring(0, fileName.length() - dim.length() - functionNo.length() - 2).replace("_", "-");
                        }
                        else if (benchmarkInfo.id == BenchmarkId.CEC2021) {
                            dim = splitFileName[length-1];
                            if(splitFileName[length-2].length() < splitFileName[length-3].length())
                            {
                                functionNo = splitFileName[length-2];
                                rotation = splitFileName[length-3];
                            }
                            else
                            {
                                functionNo = splitFileName[length-3];
                                rotation = splitFileName[length-2];
                            }
                            algorithmName = fileName.substring(0, fileName.length() - dim.length() - functionNo.length() - rotation.length() - 3).replace("_", "-");
                            rotation = rotation.replace("(", "").replace(")", "");
                        }
                        else {
                            System.out.println("Benchmark not supported.");
                            return;
                        }

                        Double[][] results = new Double[benchmarkInfo.k][benchmarkInfo.runs];

                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            int kIndex = 0;
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if(benchmarkInfo.k == kIndex)
                                    break;

                                String[] splitLine;
                                if(line.contains("\t")) {
                                    splitLine = line.split("\t");
                                }
                                else if(line.contains(" ")) {
                                    splitLine = line.split(" ");
                                }
                                else {
                                    splitLine = line.split(",");
                                }
                                int runIndex = 0;
                                for (String s : splitLine) {
                                    if (!s.isEmpty()) {
                                        try {
                                            if (s.contains("inf"))
                                                results[kIndex][runIndex++] = Double.MAX_VALUE;
                                            else
                                                results[kIndex][runIndex++] = Double.valueOf(s);
                                        } catch (NumberFormatException e) {
                                            System.out.println("Error while parsing double value: " + s + " in file: " + file.getName());
                                        }
                                    }
                                }
                                kIndex++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < benchmarkInfo.k; i++) {

                            for (int j = 0; j < benchmarkInfo.runs; j++) {
                                sb.append(results[i][j]).append("\n");
                            }
                            String newFileName;
                            if(benchmarkInfo.id == BenchmarkId.CEC2021)
                                newFileName = algorithmName + "_" + benchmarkInfo.name + "F" + functionNo + "(" + rotation + ")" + "D" + dim + "k" + (i);
                            else
                                newFileName = algorithmName + "_" + benchmarkInfo.name + "F" + functionNo + "D" + dim + "k" + (i);

                            String newFileLocation = algorithmResultsDir + File.separator + "EARS" + File.separator + newFileName + ".txt";
                            Util.writeToFile(newFileLocation, sb.toString());

                            sb.setLength(0);
                        }
                    }
                }
            } else {
                System.out.println("The directory is empty.");
            }
        } else {
            System.out.println("The specified folder does not exist or is not a directory.");
        }
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }
}