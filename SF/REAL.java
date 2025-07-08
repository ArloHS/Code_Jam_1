package SF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class REAL {
    private static final boolean DEBUG = false;
    private static int currCost = 0;
    private static Map<String, Integer> memo;

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
        int numNames = in.nextInt();
        String names[] = new String[numNames];

        for (int i = 0; i < numNames; i++) {
            names[i] = in.next();
        }

        if (DEBUG) {
            displayTest(names);
        }

        int result = runTesti(names);
        out.println(result);
    }

    public static void process(Scanner in, PrintStream out) {
        int N = in.nextInt();
        for (int i = 1; i <= N; i++) {
            out.print("" + i + ": ");
            processCase(in, out);
        }
    }

    public static void main(String[] argv) {
        process(new Scanner(System.in), System.out);
    }

    public static int runTesti(String arr[]) {
        currCost = 9999999;
        memo = new HashMap<>();
        simulate(arr, 0, 0, new ArrayList<>());

        return currCost;
    }

    private static void simulate(String arr[], int cursorIndex, int costi, List<Integer> listRename) {
        if (listRename.size() == arr.length) {
            if (costi < currCost) {
                currCost = costi;
                debugln("Updated currCost to: " + currCost);
            }

            return;
        }

        String key = "";
        List<Integer> temp = new ArrayList<>(listRename);
        for (int i = 0; i < temp.size(); i++) {
            for (int j = i + 1; j < temp.size(); j++) {
                if (temp.get(i) > temp.get(j)) {
                    int sw = temp.get(i);
                    temp.set(i, temp.get(j));
                    temp.set(j, sw);
                }
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            key += temp.get(i);
            if (i < temp.size() - 1) {
                key += ",";
            }
        }

        key += "_" + cursorIndex;
        if (memo.containsKey(key) && memo.get(key) <= costi) {
            return;
        }

        memo.put(key, costi);
        String currArr[] = doRename(arr, listRename);

        for (int i = 0; i < arr.length; i++) {
            boolean isRenamed = false;
            for (int j = 0; j < listRename.size(); j++) {
                if (listRename.get(j) == i) {
                    isRenamed = true;
                    break;
                }
            }

            if (!isRenamed) {
                int costOfMove = Math.abs(cursorIndex - findCursor(arr[i], currArr));
                List<Integer> newList = new ArrayList<>(listRename);
                newList.add(i);
                String nextArr[] = doRename(arr, newList);
                String newFile = rename(arr[i]);
                int nextCursor = findCursor(newFile, nextArr);
                if (nextCursor != -1) {
                    simulate(arr, nextCursor, (costi + costOfMove + 1), newList);
                }
            }

        }
    }

    private static int findCursor(String file, String arr[]) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(file)) {
                return i;
            }
        }

        debugln("File not found: " + file);
        return -1;
    }

    private static String[] doRename(String curr[], List<Integer> listRename) {
        String temp[] = curr.clone();
        String copy[] = temp.clone();
        for (int j = 0; j < listRename.size(); j++) {
            int index = listRename.get(j);
            copy[index] = rename(curr[index]);
        }

        return sort(copy);
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

    public static String[] swap(String arr[], int sIndex, int eIndex) {
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

}
