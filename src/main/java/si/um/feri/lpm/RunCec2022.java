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

public class RunCec2022 {
    public static void main(String[] args) {

        Benchmark.printInfo = false;
        DummyAlgorithm.readFromJson = false;
        boolean displayRatingCharts = false;

        BenchmarkInfo cec2022 = BenchmarkManager.get(BenchmarkId.CEC2022);

        String projectDirectory = System.getProperty("user.dir");
        File projectDirFile = new File(projectDirectory);
        String algorithmResultsDir = projectDirFile + File.separator + "CEC_results" + File.separator + cec2022.name + File.separator + "EARS";
        String experimentalResultsDir = algorithmResultsDir + File.separator + "experimental_results";

        ArrayList<NumberAlgorithm> players = new ArrayList<>();

        //The _ in the name of the algorithm is replaced with - in the file name
        players.add(new DummyAlgorithm("Co-PPSO", algorithmResultsDir));
        players.add(new DummyAlgorithm("EA4eigN100-10", algorithmResultsDir));
        players.add(new DummyAlgorithm("IUMOEAII", algorithmResultsDir));
        players.add(new DummyAlgorithm("jSObinexpEig", algorithmResultsDir));
        players.add(new DummyAlgorithm("MTT-SHADE", algorithmResultsDir));
        players.add(new DummyAlgorithm("NL-SHADE-LBC", algorithmResultsDir));
        players.add(new DummyAlgorithm("NL-SHADE-RSP-MID", algorithmResultsDir));
        players.add(new DummyAlgorithm("NLSOMACLP", algorithmResultsDir));
        players.add(new DummyAlgorithm("OMCSOMA", algorithmResultsDir));
        players.add(new DummyAlgorithm("S-LSHADE-DP", algorithmResultsDir));
        players.add(new DummyAlgorithm("ZOCMAES", algorithmResultsDir));
        players.add(new DummyAlgorithm("SPHH-Ensemble", algorithmResultsDir));
        players.add(new DummyAlgorithm("IMPML-SHADE", algorithmResultsDir));

        HashMap<String, ArrayList<String>> playerRatings;
        playerRatings = new HashMap<>();
        for (int k = 0; k < cec2022.k; k++) {
            Cec2022StoredBenchmark cec2022Benchmark = new Cec2022StoredBenchmark(k);
            cec2022Benchmark.setDisplayRatingCharts(false);
            cec2022Benchmark.addAlgorithms(players);
            cec2022Benchmark.run(cec2022.runs);
            TournamentResults tournamentResults = cec2022Benchmark.getTournamentResults();
            ArrayList<Player> playerResults = tournamentResults.getPlayers();
            for (Player player : playerResults) {
                if (!playerRatings.containsKey(player.getId())) {
                    playerRatings.put(player.getId(), new ArrayList<>());
                }
                playerRatings.get(player.getId()).add(player.getGlicko2Rating().getRating() + " " + player.getGlicko2Rating().getRatingDeviation());
            }
            //save final results
            if (k + 1 == cec2022.k) {
                tournamentResults.saveToFile(experimentalResultsDir + File.separator + cec2022.name + "_final_results");
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
        }

        //save whole convergence graphs
        Cec2022StoredBenchmark cec2022StoredBenchmark = new Cec2022StoredBenchmark();
        cec2022StoredBenchmark.setDisplayRatingCharts(false);
        cec2022StoredBenchmark.addAlgorithms(players);
        cec2022StoredBenchmark.wholeConvergenceGraph = true;
        cec2022StoredBenchmark.run(cec2022.runs);
        TournamentResults tournamentResults = cec2022StoredBenchmark.getTournamentResults();
        tournamentResults.saveToFile(experimentalResultsDir + File.separator + cec2022.name +"_whole_convergence_graph");

    }
}
