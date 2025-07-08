package SF;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SFF {
    private static final int maxTests = 100;
    private static final int maxNumNames = 13;
    private static int currCost = 0;
    private static boolean DEBUG = false;
    private static Map<String, Integer> memo;

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "SF/Input/1.in";
        String arr[][] = readData(filePath);
        int moveCountPerTest[] = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            System.out.println("Running test: " + (i + 1));
            System.out.println("\nTest Input:");
            displayTest(arr[i]);
            moveCountPerTest[i] = runTesti(arr[i]);
        }

        System.out.println("\nCOSTS PER TEST CASE:\n" + returnCost(moveCountPerTest));
    }

    public static int runTesti(String[] arr) {
        currCost = Integer.MAX_VALUE;
        memo = new HashMap<>();
        simulate(arr, 0, 0, new ArrayList<>());
        return currCost;
    }

    private static void simulate(String[] arr, int cursorIndex, int cost, List<Integer> renamedList) {
        if (renamedList.size() == arr.length) {
            if (cost < currCost) {
                currCost = cost;
                debugln("Updated currCost to: " + currCost);
            }
            return;
        }

        // Make a messy key string without fancy streams
        String key = "";
        List<Integer> tempList = new ArrayList<>(renamedList);
        for (int i = 0; i < tempList.size(); i++) {
            for (int j = i + 1; j < tempList.size(); j++) {
                if (tempList.get(i) > tempList.get(j)) {
                    int swap = tempList.get(i);
                    tempList.set(i, tempList.get(j));
                    tempList.set(j, swap);
                }
            }
        }
        for (int i = 0; i < tempList.size(); i++) {
            key += tempList.get(i);
            if (i < tempList.size() - 1) {
                key += ",";
            }
        }
        key += "_" + cursorIndex;

        if (memo.containsKey(key) && memo.get(key) <= cost) {
            return;
        }
        memo.put(key, cost);

        String[] currentArr = applyRenames(arr, renamedList);
        for (int i = 0; i < arr.length; i++) {
            boolean isRenamed = false;
            for (int j = 0; j < renamedList.size(); j++) {
                if (renamedList.get(j) == i) {
                    isRenamed = true;
                    break;
                }
            }
            if (!isRenamed) {
                int moveCost = Math.abs(cursorIndex - findFilePosition(arr[i], currentArr));
                List<Integer> newList = new ArrayList<>(renamedList);
                newList.add(i);
                String[] nextArr = applyRenames(arr, newList);
                String newFile = rename(arr[i]);
                int nextCursor = findFilePosition(newFile, nextArr);
                if (nextCursor != -1) {
                    simulate(arr, nextCursor, cost + moveCost + 1, newList);
                }
            }
        }
    }

    private static String[] applyRenames(String[] original, List<Integer> renamedList) {
        String[] temp = original.clone();
        String[] extraCopy = temp.clone(); // Totally unnecessary, just to make it worse
        for (int j = 0; j < renamedList.size(); j++) {
            int index = renamedList.get(j);
            extraCopy[index] = rename(original[index]);
        }
        return sort(extraCopy);
    }

    public static int findFilePosition(String file, String arr[]) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(file)) {
                return i;
            }
        }
        debugln("FILE NOT FOUND: " + file);
        return -1;
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

    public static String rename(String input) {
        StringBuilder otp = new StringBuilder("");
        otp.append(getSurname(input) + "," + getName(input) + ".dat");

        return otp.toString();
    }

    public static String getName(String input) {
        if (getExtension(input).equals("rec")) {
            int underscore = input.indexOf('_');
            return input.substring(0, underscore);
        } else {
            int comma = input.indexOf(',');
            int dot = input.lastIndexOf('.');
            return input.substring(comma + 1, dot);
        }
    }

    public static String getSurname(String input) {
        if (getExtension(input).equals("rec")) {
            int underscore = input.indexOf('_');
            int dot = input.lastIndexOf('.');
            return input.substring(underscore + 1, dot);
        } else {
            int comma = input.indexOf(',');
            return input.substring(0, comma);
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

    private static void debugln(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }
}