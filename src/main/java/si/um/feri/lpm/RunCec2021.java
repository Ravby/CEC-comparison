package si.um.feri.lpm;

import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.RatingType;
import org.um.feri.ears.statistic.rating_system.glicko2.Glicko2Rating;
import org.um.feri.ears.statistic.rating_system.glicko2.TournamentResults;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.visualization.rating.RatingIntervalPlot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RunCec2021 {
    public static void main(String[] args) {

        Benchmark.printInfo = false;
        DummyAlgorithm.readFromJson = false;
        boolean displayRatingCharts = false;

        BenchmarkInfo cec2021 = BenchmarkManager.get(BenchmarkId.CEC2021);

        String projectDirectory = System.getProperty("user.dir");
        File projectDirFile = new File(projectDirectory);
        String algorithmResultsDir = projectDirFile + File.separator + "CEC_results" + File.separator + cec2021.name + File.separator + "EARS";
        String experimentalResultsDir = algorithmResultsDir + File.separator + "experimental_results";

        ArrayList<NumberAlgorithm> players = new ArrayList<>();

        //The _ in the name of the algorithm is replaced with - in the file name
        players.add(new DummyAlgorithm("APGSK-IMODE", algorithmResultsDir));
        players.add(new DummyAlgorithm("DEDMNA", algorithmResultsDir));
        players.add(new DummyAlgorithm("j21", algorithmResultsDir));
        players.add(new DummyAlgorithm("LSHADE", algorithmResultsDir));
        players.add(new DummyAlgorithm("MadDE", algorithmResultsDir));
        players.add(new DummyAlgorithm("MLS-LSHADE", algorithmResultsDir));
        players.add(new DummyAlgorithm("NL-SHADE-RSP", algorithmResultsDir));
        players.add(new DummyAlgorithm("RB-IPOP-CMAES-PPMF", algorithmResultsDir));
        players.add(new DummyAlgorithm("SOMA-CLP", algorithmResultsDir));


        for (Cec2021BenchmarkType type : Cec2021BenchmarkType.values()) {
            if(type != Cec2021BenchmarkType.ALL)
                continue;

            HashMap<String, ArrayList<String>> playerRatings;
            String resultSpecificDir = experimentalResultsDir + File.separator + type.name;
            playerRatings = new HashMap<>();
            for (int k = 0; k < cec2021.k; k++) {
                Cec2021StoredBenchmark cec2021StoredBenchmark = new Cec2021StoredBenchmark(type, k);
                cec2021StoredBenchmark.setDisplayRatingCharts(false);
                cec2021StoredBenchmark.addAlgorithms(players);
                cec2021StoredBenchmark.run(cec2021.runs);
                TournamentResults tournamentResults = cec2021StoredBenchmark.getTournamentResults();
                ArrayList<Player> playerResults = tournamentResults.getPlayers();
                for (Player player : playerResults) {
                    if (!playerRatings.containsKey(player.getId())) {
                        playerRatings.put(player.getId(), new ArrayList<>());
                    }
                    playerRatings.get(player.getId()).add(player.getGlicko2Rating().getRating() + " " + player.getGlicko2Rating().getRatingDeviation());
                }
                //save final results
                if (k + 1 == cec2021.k) {
                    tournamentResults.saveToFile(resultSpecificDir + File.separator + cec2021.name + "_" + type.name.replace(" ","_") +"_final_results");
                    if (displayRatingCharts) {
                        RatingIntervalPlot.displayChart(tournamentResults.getPlayers(), RatingType.GLICKO2, "Rating Interval");
                    }
                }
            }
            //save rating interval bands
            StringBuilder sb = new StringBuilder();
            for (String playerId : playerRatings.keySet()) {
                ArrayList<String> rating = playerRatings.get(playerId);

                sb.append(playerId).append("\n");
                for (String r : rating) {
                    sb.append(r).append("\n");
                }
                Util.writeToFile(resultSpecificDir + File.separator + playerId + "_rating_interval_band.txt", sb.toString());
                sb.setLength(0);
            }
            //reset run numbers to reuse the same algorithms
            for (NumberAlgorithm player : players) {
                ((DummyAlgorithm) player).resetRunNumbers();
            }

            //save whole convergence graphs
            Cec2021StoredBenchmark cec2021StoredBenchmark = new Cec2021StoredBenchmark(type);
            cec2021StoredBenchmark.setDisplayRatingCharts(false);
            cec2021StoredBenchmark.addAlgorithms(players);
            cec2021StoredBenchmark.wholeConvergenceGraph = true;
            cec2021StoredBenchmark.run(cec2021.runs);
            TournamentResults tournamentResults = cec2021StoredBenchmark.getTournamentResults();
            tournamentResults.saveToFile(resultSpecificDir + File.separator + cec2021.name + "_" + type.name.replace(" ","_") + "_whole_convergence_graph");

            for(NumberAlgorithm player : players) {
                ((DummyAlgorithm)player).resetRunNumbers();
            }
        }


        players.clear();
    }
}
