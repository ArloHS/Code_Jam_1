import java.util.Scanner;
import java.util.List;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static boolean DEBUG = false;
    private static Map<String, Boolean> memo;

    private static void debug(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    private static void debugln(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    public static void processCase(Scanner in, PrintStream out) {
        // Max num of deletes
        int K = in.nextInt();
        // Tolerance of doc
        int T = in.nextInt();
        // Num already existing docs
        int E = in.nextInt();
        debug("Starting case: K = " + K + ", T = " + ", E = " + E);
        List<List<Integer>> docsExist = new ArrayList<>();

        // Get all existing docs
        for (int i = 0; i < E; i++) {
            // Doc length
            int M = in.nextInt();
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < M; j++) {
                int next = in.nextInt();
                temp.add(next);
            }

            docsExist.add(temp);
            debugln("Existing doc: " + (i + 1) + ": " + temp);
        }

        // Num candidate docs
        int C = in.nextInt();
        debugln("Num Candidate Docs: " + C);
        List<List<Integer>> docsCand = new ArrayList<>();

        // Get all candidate docs
        for (int i = 0; i < C; i++) {
            // Doc length
            int M = in.nextInt();
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < M; j++) {
                int next = in.nextInt();
                temp.add(next);
            }

            docsCand.add(temp);
            debugln("Candidate doc: " + (i + 1) + ": " + temp);
        }

        // Check if mathes
        for (int i = 0; i < docsCand.size(); i++) {
            List<Integer> currCand = docsCand.get(i);
            debugln("CHECKING CANDIDATES: " + (i + 1) + ": " + currCand);
            boolean match = false;
            for (int j = 0; j < docsExist.size(); j++) {
                List<Integer> currCandExisting = docsExist.get(j);
                // Match! --> Break
                if (runTesti(currCandExisting, currCand, K, T)) {
                    out.println("match");
                    match = true;
                    debugln("MATCH FOUND (existing doc): " + (j + 1));
                    break;
                }
            }

            // No match --> uplaod
            if (!match) {
                out.println("upload");
                docsExist.add(currCand);
                debugln("Negative Match --> Uploading: " + (i + 1));
            }
        }
    }

    public static void process(Scanner in, PrintStream out) {
        int N = in.nextInt();
        debugln("Num cases: " + N);
        for (int i = 1; i <= N; i++) {
            out.println(i + ":");
            debugln("Starting case: " + i);
            processCase(in, out);
        }
    }

    public static void main(String[] argv) {
        process(new Scanner(System.in), System.out);
    }

    // Runs test iteration
    public static boolean runTesti(List<Integer> exist, List<Integer> cand, int K, int T) {
        memo = new HashMap<>();
        debugln("BEGIN match test FOR: Exists =" + exist + ", Cand= " + cand + ", K =" + K + ", T =" + T);
        return simulate(exist, cand, 0, 0, 0, K, T);
    }

    // Checks mathcing of two docs
    private static boolean simulate(List<Integer> exist, List<Integer> cand, int eI, int cI, int del, int K, int T) {
        String key = eI + "_" + cI + "_" + del;
        // Check if already calculated
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // Check if all candidate blocks matehd
        if (cI == cand.size()) {
            // Perfect match
            if (eI == exist.size()) {
                memo.put(key, true);
                debugln("Perfect match at eI =" + eI + ", cI =" + cI);
                return true;

                // No more to delete
            } else if (del >= K) {
                memo.put(key, false);
                debugln("No more deletions allowed at eI =" + eI + ", cI =" + cI);
                return false;

            } else {
                // Try delete sections
                int maxDel = Math.min(5, exist.size() - eI);
                debugln("Trying to delete up to " + maxDel + " blocks from eI =" + eI);
                for (int i = 1; i <= maxDel; i++) {
                    if (simulate(exist, cand, eI + i, cI, del + 1, K, T)) {
                        memo.put(key, true);
                        debugln("Successful deletion of " + i + " blocks at eI =" + eI);
                        return true;
                    }
                }

                memo.put(key, false);
                debugln("No valid deletions worked at eI =" + eI);
                return false;
            }
        }

        // Candidate still to process
        if (eI == exist.size()) {
            memo.put(key, false);
            debugln("Ran out of existing blocks!");
            return false;
        }

        boolean canMatch = false;
        int diff = Math.abs(exist.get(eI) - cand.get(cI));
        debugln("Comparing exist [" + eI + "] =" + exist.get(eI) + " with cand [" + cI + "] =" + cand.get(cI)
                + ", diff ="
                + diff);
        if (diff <= T) {
            // Match within tolerance
            if (simulate(exist, cand, eI + 1, cI + 1, del, K, T)) {
                canMatch = true;
            }
        }

        // Delete section
        if (del < K) {
            int maxDel = Math.min(5, exist.size() - eI);
            debugln("Try delete to " + maxDel + " blocks from eI =" + eI + " with del =" + del);
            for (int i = 1; i <= maxDel; i++) {
                if (simulate(exist, cand, eI + i, cI, del + 1, K, T)) {
                    canMatch = true;
                    debugln("DELETE SUCCESS");
                    break;
                }
            }
        }
        memo.put(key, canMatch);
        return canMatch;
    }
}
