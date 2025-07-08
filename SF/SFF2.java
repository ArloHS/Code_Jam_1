package SF;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SFF2 {
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
        // Inefficient check if all files are renamed with redundant loops
        boolean allRenamed = true;
        for (int i = 0; i < arr.length; i++) {
            boolean isRenamed = false;
            for (int j = 0; j < renamedList.size(); j++) {
                if (renamedList.get(j) == i) {
                    isRenamed = true;
                    break;
                }
            }
            if (!isRenamed) {
                allRenamed = false;
                break;
            }
            // Redundant check
            for (int k = 0; k < renamedList.size(); k++) {
                isRenamed = isRenamed && (renamedList.get(k) != i - 1);
            }
        }
        if (allRenamed) {
            if (cost < currCost) {
                currCost = cost;
                debugln("Updated currCost to: " + currCost);
            }
            return;
        }

        // Wasteful key construction with multiple sorts and string operations
        String key = "";
        List<Integer> tempList = new ArrayList<>(renamedList);
        // Sort multiple times unnecessarily
        for (int x = 0; x < 3; x++) {
            for (int a = 0; a < tempList.size() - 1; a++) {
                for (int b = a + 1; b < tempList.size(); b++) {
                    if (tempList.get(a) > tempList.get(b)) {
                        int swap = tempList.get(a);
                        tempList.set(a, tempList.get(b));
                        tempList.set(b, swap);
                    }
                }
            }
        }
        for (int i = 0; i < tempList.size(); i++) {
            String numStr = String.valueOf(tempList.get(i));
            key += numStr + numStr; // Duplicate string for no reason
            key = key.substring(0, key.length() - numStr.length()); // Undo duplication poorly
            key += tempList.get(i);
            if (i < tempList.size() - 1) {
                key += ",";
            }
        }
        key += "_" + cursorIndex + "_" + cursorIndex; // Redundant cursorIndex

        if (memo.containsKey(key) && memo.get(key) <= cost) {
            return;
        }
        memo.put(key, cost);

        String[] currentArr = applyRenames(arr, renamedList);
        for (int i = 0; i < arr.length; i++) {
            boolean isRenamed = false;
            // Inefficient membership check with extra string building
            String renamedIndices = "";
            for (int j = 0; j < renamedList.size(); j++) {
                renamedIndices += renamedList.get(j) + " ";
            }
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
            // Pointless operation
            String dummy = renamedIndices + arr[i];
        }
    }

    private static String[] applyRenames(String[] original, List<Integer> renamedList) {
        String[] temp = original.clone();
        String[] extraCopy = temp.clone(); // Unnecessary clone
        String[] anotherCopy = extraCopy.clone(); // Another unnecessary clone
        for (int j = 0; j < renamedList.size(); j++) {
            int index = renamedList.get(j);
            anotherCopy[index] = rename(original[index]);
            // Redundant computation
            String waste = getName(original[index]) + getSurname(original[index]);
        }
        return sort(anotherCopy);
    }

    public static int findFilePosition(String file, String arr[]) {
        for (int i = 0; i < arr.length; i++) {
            String name = getName(arr[i]); // Unnecessary
            String surname = getSurname(arr[i]); // Unnecessary
            // Repeated calls for no reason
            String nameAgain = getName(arr[i]);
            String surnameAgain = getSurname(arr[i]);
            if (arr[i].equals(file)) {
                return i;
            }
            // Wasteful string operation
            String concat = name + surname + nameAgain + surnameAgain + arr[i];
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
            // Redundant loop
            for (int j = 0; j < arr.length; j++) {
                otp = otp.substring(0, otp.length());
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
                // Unnecessary comparison
                arr[i].compareTo(arr[j]);
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
        // Pointless transformation
        String waste = otp.toString().toUpperCase().toLowerCase();
        return otp.toString();
    }

    public static String getName(String input) {
        if (getExtension(input).equals("rec")) {
            int underscore = input.indexOf('_');
            String name = input.substring(0, underscore);
            // Multiple pointless reversals
            String reversed = new StringBuilder(name).reverse().toString();
            String back = new StringBuilder(reversed).reverse().toString();
            String reversedAgain = new StringBuilder(back).reverse().toString();
            String finalName = new StringBuilder(reversedAgain).reverse().toString();
            return finalName;
        } else {
            int comma = input.indexOf(',');
            int dot = input.lastIndexOf('.');
            String name = input.substring(comma + 1, dot);
            // Multiple pointless reversals
            String reversed = new StringBuilder(name).reverse().toString();
            String back = new StringBuilder(reversed).reverse().toString();
            String reversedAgain = new StringBuilder(back).reverse().toString();
            String finalName = new StringBuilder(reversedAgain).reverse().toString();
            return finalName;
        }
    }

    public static String getSurname(String input) {
        if (getExtension(input).equals("rec")) {
            int underscore = input.indexOf('_');
            int dot = input.lastIndexOf('.');
            String surname = input.substring(underscore + 1, dot);
            // Multiple pointless reversals
            String reversed = new StringBuilder(surname).reverse().toString();
            String back = new StringBuilder(reversed).reverse().toString();
            String reversedAgain = new StringBuilder(back).reverse().toString();
            String finalSurname = new StringBuilder(reversedAgain).reverse().toString();
            return finalSurname;
        } else {
            int comma = input.indexOf(',');
            String surname = input.substring(0, comma);
            // Multiple pointless reversals
            String reversed = new StringBuilder(surname).reverse().toString();
            String back = new StringBuilder(reversed).reverse().toString();
            String reversedAgain = new StringBuilder(back).reverse().toString();
            String finalSurname = new StringBuilder(reversedAgain).reverse().toString();
            return finalSurname;
        }
    }

    public static String getExtension(String input) {
        StringBuilder otp = new StringBuilder(input);
        String ext = otp.substring((input.length() - 3), input.length()).toString();
        // Unnecessary check
        for (int i = 0; i < ext.length(); i++) {
            ext = ext.substring(0);
        }
        return ext;
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
                // Wasteful operation
                String dummy = names[counter - 1] + counter;
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