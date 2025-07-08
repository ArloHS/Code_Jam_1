package SF;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SurnamesFirst {
    // Minimum 1 for each
    private static final int maxTests = 100;
    private static final int maxNumNames = 13;

    // Cost per test case
    private static int currCost = 0;

    static boolean DEBUG = true;

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "SF/input.txt";
        String arr[][] = readData(filePath);
        int moveCountPerTest[] = new int[arr.length];

        // moveCountPerTest[0] = 3;
        // moveCountPerTest[1] = 6;

        // displayInfo(arr);
        // testStringMethods();
        // System.out.println();

        // displayTest(arr[0]);

        // String cost = returnCost(moveCountPerTest);
        // System.out.println(cost);
        // System.out.println();
        // System.out.println("Before Swap:\n" + viewArray(arr[0]));
        // arr[0][1] = rename(arr[0][1]);
        // sort(arr[0]);
        // System.out.println("\nAfter Swap:\n" + viewArray(arr[0]));

        // Main loop logic, each run is 1 test
        for (int i = 0; i < arr.length; i++) {
            System.out.println("Running test: " + (i + 1));
            System.out.println("\nTest Input:");
            displayTest(arr[i]);
            moveCountPerTest[i] = runTesti(arr[i]);
        }

        System.out.println("\nCOSTS PER TEST CASE:\n" + returnCost(moveCountPerTest));

    }

    // public static int runTesti(String[] arr) {
    // // Cursor locations
    // int cursorIndex = 0;

    // // Set min and cost to 0
    // currCost = 1000;

    // int numStilltoBeRenamed = arr.length;

    // int c = 0;
    // while (numStilltoBeRenamed != 1) {
    // debugln("Entries that still need to be processed: " + numStilltoBeRenamed);

    // // Store old cursor
    // String ext = getExtension(arr[cursorIndex]);
    // debugln("Current Cursor: " + arr[cursorIndex]);
    // if (ext.equals("rec")) {

    // String surname = getSurname(arr[cursorIndex]);

    // // Press R
    // debugln("Pressed R on index: " + cursorIndex + " --> " + arr[cursorIndex]);
    // arr[cursorIndex] = rename(arr[cursorIndex]);
    // debugln("Changed to: " + arr[cursorIndex]);
    // c++;
    // debugln("Increased Cost R: " + c);
    // numStilltoBeRenamed--;

    // // Find new cursor index
    // arr = sort(arr);
    // debugln("Array Now looks like this: ");
    // debugln(viewArray(arr));
    // int nextIndex = findCursor(surname, arr);

    // debugln("Cursor found at: " + nextIndex);
    // cursorIndex = closestAvailableCursor(arr, nextIndex);
    // int cc = Math.abs(cursorIndex - nextIndex);
    // c += cc;
    // debugln("Next available Cursor: " + cursorIndex);
    // debugln("Increased Cost UP or DOWN: " + c);

    // }
    // debugln("");
    // }
    // debugln("AFTER WHILE");
    // debugln("One Last CASE");
    // // Press R
    // debugln("Pressed R on index: " + cursorIndex + " --> " + arr[cursorIndex]);
    // arr[cursorIndex] = rename(arr[cursorIndex]);
    // debugln("Changed to: " + arr[cursorIndex]);
    // c++;
    // arr = sort(arr);
    // debugln("Array Now looks like this: ");
    // debugln(viewArray(arr));

    // // Update cost if better
    // if (currCost > c) {
    // currCost = c;
    // }

    // return currCost;
    // }

    // public static int runTesti(String[] arr) {
    // int K = arr.length;
    // int INF = Integer.MAX_VALUE;

    // // Precompute file names and positions for all subsets
    // String[][] fileNames = new String[1 << K][K];
    // int[][] pos = new int[1 << K][K];
    // for (int S = 0; S < (1 << K); S++) {
    // List<String> list = new ArrayList<>();
    // for (int i = 0; i < K; i++) {
    // if ((S & (1 << i)) != 0) {
    // String name = getName(arr[i]);
    // String surname = getSurname(arr[i]);
    // fileNames[S][i] = surname + "," + name + ".dat";
    // } else {
    // fileNames[S][i] = arr[i];
    // }
    // list.add(fileNames[S][i]);
    // }
    // Collections.sort(list); // Using built-in sort for efficiency
    // for (int p = 0; p < K; p++) {
    // String fname = list.get(p);
    // for (int i = 0; i < K; i++) {
    // if (fileNames[S][i].equals(fname)) {
    // pos[S][i] = p;
    // break;
    // }
    // }
    // }
    // }

    // // DP[S][k] = min cost to rename set S, last renamed is k
    // int[][] dp = new int[1 << K][K];
    // for (int S = 0; S < (1 << K); S++)
    // Arrays.fill(dp[S], INF);

    // // Base case: rename one file from initial position 0
    // for (int j = 0; j < K; j++) {
    // int S = 1 << j;
    // dp[S][j] = pos[0][j] + 1; // Move from 0 to file j, press R
    // }

    // // Fill DP table
    // for (int S = 1; S < (1 << K); S++) {
    // for (int k = 0; k < K; k++) {
    // if ((S & (1 << k)) == 0 || dp[S][k] == INF)
    // continue;
    // for (int j = 0; j < K; j++) {
    // if ((S & (1 << j)) != 0)
    // continue;
    // int nextS = S | (1 << j);
    // int cost = dp[S][k] + Math.abs(pos[S][k] - pos[S][j]) + 1;
    // dp[nextS][j] = Math.min(dp[nextS][j], cost);
    // }
    // }
    // }

    // // Find minimum cost to rename all files
    // int finalS = (1 << K) - 1;
    // int ans = INF;
    // for (int k = 0; k < K; k++) {
    // ans = Math.min(ans, dp[finalS][k]);
    // }
    // return ans;
    // }

    public static int runTesti(String[] arr) {
        // Initialize minimum cost
        currCost = Integer.MAX_VALUE;

        // Clone array to avoid modifying original
        String[] workingArr = arr.clone();

        // Start simulation with cursor at 0, cost 0, and all files to rename
        simulate(workingArr, 0, 0, arr.length);

        return currCost;
    }

    // private static void simulate(String[] arr, int cursorIndex, int cost, int
    // numStilltoBeRenamed) {
    // // Base case: all files renamed
    // if (numStilltoBeRenamed == 0) {
    // if (cost < currCost) {
    // currCost = cost;
    // debugln("Updated currCost to: " + currCost);
    // }
    // return;
    // }

    // debugln("Entries that still need to be processed: " + numStilltoBeRenamed);

    // // Try renaming each .rec file
    // for (int i = 0; i < arr.length; i++) {
    // if (getExtension(arr[i]).equals("rec")) {
    // // Clone array for this simulation
    // String[] tempArr = arr.clone();

    // // Move to position i
    // int moveCost = Math.abs(cursorIndex - i);
    // debugln("Trying to rename at index: " + i + " (" + tempArr[i] + "), move
    // cost: " + moveCost);

    // // Rename
    // String surname = getSurname(tempArr[i]);
    // debugln("Pressed R on index: " + i + " --> " + tempArr[i]);
    // tempArr[i] = rename(tempArr[i]);
    // debugln("Changed to: " + tempArr[i]);

    // // Sort array
    // tempArr = sort(tempArr);
    // debugln("Array now looks like this: ");
    // debugln(viewArray(tempArr));

    // // Find new cursor position
    // int nextIndex = findCursor(surname, tempArr);
    // debugln("Cursor found at: " + nextIndex);

    // // Continue simulation
    // simulate(tempArr, nextIndex, cost + moveCost + 1, numStilltoBeRenamed - 1);
    // }
    // }
    // }

    private static void simulate(String[] arr, int cursorIndex, int cost, int numStilltoBeRenamed) {
        if (numStilltoBeRenamed == 0) {
            if (cost < currCost) {
                currCost = cost;
                debugln("Updated currCost to: " + currCost);
            }
            return;
        }

        for (int i = 0; i < arr.length; i++) {
            if (getExtension(arr[i]).equals("rec")) {
                String[] tempArr = arr.clone();
                int moveCost = Math.abs(cursorIndex - i);
                String surname = getSurname(tempArr[i]);
                tempArr[i] = rename(tempArr[i]);
                tempArr = sort(tempArr);
                int nextIndex = findCursor(surname, tempArr);
                if (nextIndex == -1) {
                    continue;
                }
                simulate(tempArr, nextIndex, cost + moveCost + 1, numStilltoBeRenamed - 1);
            }
        }
    }

    // public static int findCursor(String surname, String arr[]) {
    // for (int i = 0; i < arr.length; i++) {
    // if (getExtension(arr[i]).equals("dat") && getSurname(arr[i]).equals(surname))
    // {
    // return i;
    // }
    // }

    // debugln("CURSOR NOT FOUND");
    // return 0;
    // }

    public static int findCursor(String surname, String arr[]) {
        for (int i = 0; i < arr.length; i++) {
            if (getExtension(arr[i]).equals("dat") && getSurname(arr[i]).equals(surname)) {
                return i;
            }
        }
        debugln("CURSOR NOT FOUND for surname: " + surname);
        return -1;
    }

    // public static int closestAvailableCursor(String arr[], int cursor) {
    // int nextCursor = 0;
    // int stepSize = 0;

    // while (true) {
    // // Down
    // if ((cursor + stepSize) < arr.length) {
    // if (getExtension(arr[cursor + stepSize]).equals("rec")) {
    // debugln("DOWN!, Step: " + stepSize);
    // nextCursor = cursor + stepSize;
    // return nextCursor;
    // }
    // }

    // // Up
    // if ((cursor - stepSize) >= 0) {
    // if (getExtension(arr[cursor - stepSize]).equals("rec")) {
    // debugln("UP!, Step: " + stepSize);
    // nextCursor = cursor - stepSize;
    // return nextCursor;
    // }
    // }

    // stepSize++;
    // }
    // }

    public static int closestAvailableCursor(String[] arr, int cursor) {
        int minDis = 10000000;
        int closestI = -1;
        for (int i = 0; i < arr.length; i++) {
            if (getExtension(arr[i]).equals("rec")) {
                int dist = Math.abs(i - cursor);
                if (dist < minDis) {
                    minDis = dist;
                    closestI = i;
                }
            }
        }
        return closestI;
    }

    public static String viewArray(String arr[]) {
        String otp = "";
        for (int i = 0; i < arr.length; i++) {
            otp += arr[i];
            if (i != arr.length - 1) {
                otp += "\n";
            }
        }

        return otp;
    }

    public static String[] swap(String[] arr, int sIndex, int eIndex) {
        String temp = arr[sIndex];
        arr[sIndex] = arr[eIndex];
        arr[eIndex] = temp;

        return arr;
    }

    public static String[] sort(String arr[]) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i].compareTo(arr[j]) > 0) {
                    arr = swap(arr, i, j);
                }
            }
        }
        return arr;
    }

    public static String returnCost(int arr[]) {
        String otp = "";
        for (int i = 0; i < arr.length; i++) {
            otp += (i + 1) + ": " + arr[i];
            if (i != arr.length - 1) {
                otp += "\n";
            }
        }

        return otp;
    }

    public static void displayTest(String arr[]) {
        System.out.println("Current Test Case:");
        String otp = "";
        for (int i = 0; i < arr.length; i++) {
            otp += arr[i] + "\n";
        }
        System.out.println(otp);
    }

    public static void testStringMethods() {
        System.out.println("\nTesting String methods:");
        String input = "Arlo_Steyn.rec";
        System.out.println("Input String: " + input);
        System.out.println("Get Name: " + getName(input));
        System.out.println("Get Surname: " + getSurname(input));
        System.out.println("getExtension: " + getExtension(input));
        System.out.println("Rename: " + rename(input));
    }

    public static String rename(String input) {
        StringBuilder otp = new StringBuilder("");
        otp.append(getSurname(input) + "," + getName(input) + ".dat");

        return otp.toString();
    }

    public static String getName(String input) {
        if (getExtension(input).equals("rec")) {
            int unds = input.indexOf('_');
            return input.substring(0, unds);

        } else {
            int com = input.indexOf(',');
            int dot = input.lastIndexOf('.');
            return input.substring(com + 1, dot);
        }
    }

    public static String getSurname(String input) {
        if (getExtension(input).equals("rec")) {
            int unds = input.indexOf('_');
            int dot = input.lastIndexOf('.');
            return input.substring(unds + 1, dot);

        } else {
            int com = input.indexOf(',');
            return input.substring(0, com);
        }
    }

    public static String getExtension(String input) {
        StringBuilder otp = new StringBuilder(input);
        return otp.substring((input.length() - 3), input.length()).toString();
    }

    public static String[][] readData(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        int numIterations = Integer.parseInt(scanner.nextLine());
        System.out.println("Number of Tests: " + numIterations + "\n");
        String array[][] = new String[numIterations][];

        for (int i = 0; i < numIterations; i++) {
            int numNames = scanner.nextInt();
            String names[] = new String[numNames];
            int counter = 0;
            while (counter < numNames) {
                names[counter] = scanner.next();
                counter++;
            }

            array[i] = names;

        }

        return array;
    }

    public static void displayInfo(String arr[][]) {
        String otp = "";
        for (int i = 0; i < arr.length; i++) {
            otp += "Test Number: " + (i + 1) + "\n";
            for (int j = 0; j < arr[i].length; j++) {
                otp += "Name " + (i + 1) + " Read: " + arr[i][j] + "\n";
            }
        }
        System.out.println(otp);
    }

    private static void debugln(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }
}
