package si.um.feri.lpm;

import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.RatingType;
import org.um.feri.ears.statistic.rating_system.glicko2.TournamentResults;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.visualization.rating.RatingIntervalPlot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RunCec2024 {

    public static void main(String[] args) {

        Benchmark.printInfo = false;
        DummyAlgorithm.readFromJson = false;
        boolean displayRatingCharts = false;

        BenchmarkInfo cec2024 = BenchmarkManager.get(BenchmarkId.CEC2024);

        String projectDirectory = System.getProperty("user.dir");
        File projectDirFile = new File(projectDirectory);
        String algorithmResultsDir = projectDirFile + File.separator + "CEC_results" + File.separator + cec2024.name + File.separator + "EARS";
        String experimentalResultsDir = algorithmResultsDir + File.separator + "experimental_results";

        ArrayList<NumberAlgorithm> players = new ArrayList<>();

        players.add(new DummyAlgorithm("jSOa", algorithmResultsDir));
        players.add(new DummyAlgorithm("L-SRTDE", algorithmResultsDir));
        players.add(new DummyAlgorithm("BlockEA", algorithmResultsDir));
        players.add(new DummyAlgorithm("IEACOP", algorithmResultsDir));
        players.add(new DummyAlgorithm("mLSAHDE-RL", algorithmResultsDir));
        players.add(new DummyAlgorithm("RDE", algorithmResultsDir));

        /*HashMap<String, ArrayList<String>> playerRatings;
        playerRatings = new HashMap<>();
        for (int k = 0; k < cec2024.k; k++) {
            Cec2024StoredBenchmark cec2024Benchmark = new Cec2024StoredBenchmark(k);
            cec2024Benchmark.setDisplayRatingCharts(false);
            cec2024Benchmark.addAlgorithms(players);
            cec2024Benchmark.run(cec2024.runs);
            TournamentResults tournamentResults = cec2024Benchmark.getTournamentResults();
            ArrayList<Player> playerResults = tournamentResults.getPlayers();
            for (Player player : playerResults) {
                if (!playerRatings.containsKey(player.getId())) {
                    playerRatings.put(player.getId(), new ArrayList<>());
                }
                playerRatings.get(player.getId()).add(player.getGlicko2Rating().getRating() + " " + player.getGlicko2Rating().getRatingDeviation());
            }
            //save final results
            if (k + 1 == cec2024.k) {
                tournamentResults.saveToFile(experimentalResultsDir + File.separator + cec2024.name + "_final_results");
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
            Util.writeToFile(experimentalResultsDir + File.separator + playerId + "_rating_interval_band.txt", sb.toString());
            sb.setLength(0);
        }

        for(NumberAlgorithm player : players) {
            ((DummyAlgorithm)player).resetRunNumbers();
        }*/

        //save whole convergence graphs
        Cec2024StoredBenchmark cec2024StoredBenchmark = new Cec2024StoredBenchmark();
        cec2024StoredBenchmark.setDisplayRatingCharts(false);
        cec2024StoredBenchmark.addAlgorithms(players);
        cec2024StoredBenchmark.wholeConvergenceGraph = true;
        cec2024StoredBenchmark.run(cec2024.runs);
        TournamentResults tournamentResults = cec2024StoredBenchmark.getTournamentResults();
        tournamentResults.saveToFile(experimentalResultsDir + File.separator + cec2024.name +"_whole_convergence_graph");

    }
}
