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

public class RunCec2017 {
    public static void main(String[] args) {

        //only last k used for rating
        Benchmark.printInfo = false;
        DummyAlgorithm.readFromJson = false;
        boolean displayRatingCharts = true;

        BenchmarkInfo cec2017 = BenchmarkManager.get(BenchmarkId.CEC2017);

        String projectDirectory = System.getProperty("user.dir");
        File projectDirFile = new File(projectDirectory);
        String algorithmResultsDir = projectDirFile + File.separator + "CEC_results" + File.separator + cec2017.name + File.separator + "EARS";
        String experimentalResultsDir = algorithmResultsDir + File.separator + "experimental_results";

        ArrayList<NumberAlgorithm> players = new ArrayList<>();

        //The _ in the name of the algorithm is replaced with - in the file name
        players.add(new DummyAlgorithm("jSO", algorithmResultsDir));
        players.add(new DummyAlgorithm("MM-OED", algorithmResultsDir));
        players.add(new DummyAlgorithm("IDEbestNsize", algorithmResultsDir));
        players.add(new DummyAlgorithm("RB-IPOP-CMA-ES", algorithmResultsDir));
        players.add(new DummyAlgorithm("LSHADE-SPACMA", algorithmResultsDir));
        players.add(new DummyAlgorithm("DES", algorithmResultsDir));
        players.add(new DummyAlgorithm("DYYPO", algorithmResultsDir));
        players.add(new DummyAlgorithm("TLBO-FL", algorithmResultsDir));
        players.add(new DummyAlgorithm("EBOwithCMAR", algorithmResultsDir));
        players.add(new DummyAlgorithm("PPSO", algorithmResultsDir));

        //fixed file names for IDEbestNsize: IDEbestNsize_D_FunctionNo. instead of IDEbestNsize_FunctionNo._D
        //Paper ID 17106 (LSHADE-cnEpSin) strange format?
        //Paper ID E-17260 (MOS-SOCO2011) results for cec2017 missing only for 2010, 2012 and 2013?

        /*HashMap<String, ArrayList<String>> playerRatings;
        playerRatings = new HashMap<>();
        for (int k = 0; k < cec2017.k; k++) {
            Cec2017StoredBenchmark cec2017StoredBenchmark = new Cec2017StoredBenchmark(k);
            cec2017StoredBenchmark.setDisplayRatingCharts(false);
            cec2017StoredBenchmark.addAlgorithms(players);
            cec2017StoredBenchmark.run(cec2017.runs);
            TournamentResults tournamentResults = cec2017StoredBenchmark.getTournamentResults();
            ArrayList<Player> playerResults = tournamentResults.getPlayers();
            for (Player player : playerResults) {
                if (!playerRatings.containsKey(player.getId())) {
                    playerRatings.put(player.getId(), new ArrayList<>());
                }
                playerRatings.get(player.getId()).add(player.getGlicko2Rating().getRating() + " " + player.getGlicko2Rating().getRatingDeviation());
            }
            //save final results
            if (k + 1 == cec2017.k) {
                tournamentResults.saveToFile(experimentalResultsDir + File.separator + cec2017.name + "_final_results");
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
        }*/

        //reset run numbers to reuse the same algorithms
        for (NumberAlgorithm player : players) {
            ((DummyAlgorithm) player).resetRunNumbers();
        }

        //save whole convergence graphs
        Cec2017StoredBenchmark cec2017StoredBenchmark = new Cec2017StoredBenchmark();
        cec2017StoredBenchmark.setDisplayRatingCharts(false);
        cec2017StoredBenchmark.addAlgorithms(players);
        cec2017StoredBenchmark.wholeConvergenceGraph = true;
        cec2017StoredBenchmark.run(cec2017.runs);
        TournamentResults tournamentResults = cec2017StoredBenchmark.getTournamentResults();
        tournamentResults.saveToFile(experimentalResultsDir + File.separator + cec2017.name +"_whole_convergence_graph");
    }
}
