import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        process(new Scanner(System.in), System.out);
    }

    public static void process(Scanner in, PrintStream out) {
        int N = in.nextInt();
        for (int i = 1; i <= N; i++) {
            out.println(i + ":");
            // Delegates
            int D = in.nextInt();
            // Operations
            int M = in.nextInt();
            // Per Run
            compute(in, out, D, M);
        }
    }

    private static void compute(Scanner in, PrintStream out, int D, int M) {
        UnionFind unionFind = new UnionFind(D);

        for (int i = 0; i < M; i++) {
            // Code type
            int c = in.nextInt();
            // 1st delegate
            int x = in.nextInt();
            // 2nd delegate
            int y = in.nextInt();

            // X, Y same party
            if (c == 1) {
                int res = unionFind.inSamePar(x, y);
                if (res == -1) {
                    out.println(-1);
                }
                // Make X, Y different parites
            } else if (c == 2) {
                int res = unionFind.inDiffPar(x, y);
                if (res == -1) {
                    out.println(-1);
                }
                // X, Y same party
            } else if (c == 3) {
                out.println(unionFind.areTheSame(x, y));

                // X, Y different parities
            } else if (c == 4) {
                out.println((unionFind.areDiff(x, y)));
            }
        }
    }

    private static class UnionFind {
        private int parents[];
        private int parities[];

        public UnionFind(int size) {
            // Pointers to the parents
            parents = new int[size];
            // Array that tracks pariities(0=same and 1=different)
            parities = new int[size];

            // Init all delgates as own parent with a aprity of 0
            for (int i = 0; i < size; i++) {
                // Own root
                parents[i] = i;
                // Same
                parities[i] = 0;
            }
        }

        // Find root X with its parity using path compresion
        public int[] findwPar(int x) {
            // Init arr storing a root and parity pair
            int res[] = new int[2];

            // Case x it its own parent
            if (parents[x] == x) {
                res[0] = x;
                res[1] = 0;

                return res;
            }

            // Find root and parity of xs parent
            int rootX[] = findwPar(parents[x]);
            int root = rootX[0];
            // Xor to calc parity
            int parToRoot = (rootX[1]) ^ (parities[x]);
            // Point back to route
            parents[x] = root;
            // Update parity
            parities[x] = parToRoot;
            res[0] = root;
            res[1] = parToRoot;

            return res;

        }

        public int inDiffPar(int x, int y) {
            int xX[] = findwPar(x);
            int yX[] = findwPar(y);
            int rootX = xX[0];
            int rootY = yX[0];
            int parX = xX[1];
            int parY = yX[1];

            if (rootX == rootY) {
                if (parX == parY) {
                    return -1;
                }
                return 0;
            }

            parents[rootY] = rootX;
            parities[rootY] = parX ^ parY ^ 1;
            return 0;
        }

        // X, Y same party
        public int inSamePar(int x, int y) {
            // Roots X and Y
            int xX[] = findwPar(x);
            int yX[] = findwPar(y);

            // Get parities and roots
            int rootX = xX[0];
            int rootY = yX[0];
            int parX = xX[1];
            int parY = yX[1];

            // Case same set
            if (rootX == rootY) {
                if (parX != parY) {
                    return -1;
                }
                return 0;
            }

            // Join and make rootY point to rootX
            parents[rootY] = rootX;
            parities[rootY] = parX ^ parY;
            return 0;
        }

        // Check to se if X and Y i same party
        public int areTheSame(int x, int y) {
            // Roots X and Y
            int xX[] = findwPar(x);
            int yX[] = findwPar(y);

            // Case diffent sets
            if (xX[0] != yX[0]) {
                return -1;
            }

            // Same parity implies same party
            if (xX[1] == yX[1]) {
                return 1;
            }

            // Case Not
            return 0;
        }

        public int areDiff(int x, int y) {
            // Roots X and Y
            int xX[] = findwPar(x);
            int yX[] = findwPar(y);

            // Case diffent sets
            if (xX[0] != yX[0]) {
                return -1;
            }

            // Diff parity implies Diff party
            if (xX[1] != yX[1]) {
                return 1;
            }

            // Case Not
            return 0;
        }

    }
}