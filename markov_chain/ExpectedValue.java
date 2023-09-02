package markov_chain;

import java.util.Arrays;

public class ExpectedValue {
    private final int m;
    private final int n;
    private final MarkovChain[] markovChains;

    public ExpectedValue(int m, int n) {
        this.m = m;
        this.n = n;
        this.markovChains = flipCases();
    }

    public void getInfo() {
        for (MarkovChain markovChain : markovChains) {
            // System.out.println(Arrays.toString(markovChain.getAbsorbingState()) + "\n");
            // printMatrix(markovChain.getAbsorbingStateMarkovMatrix());
            double[][] markovMatrix = markovChain.getMarkovMatrix();
            // printMatrix(markovMatrix);
            // for (double[] i : markovMatrix) {
            //    System.out.println(Arrays.toString(i));
            // }
            double[][] noAbsorbingState = new double[n][n];

            for (int i = 0;i < n;i++) {
                System.arraycopy(markovMatrix[i], 0, noAbsorbingState[i], 0, n);
            }

            double[][] identity = getIdentityMatrix(n);
            double[][] product = matrixSubtraction(identity, noAbsorbingState);

            // printMatrix(product);

            double[][] inverseProduct = new double[n][n];
            inverse(product, inverseProduct);

            // printMatrix(inverseProduct);

            double expectedValue = Arrays.stream(inverseProduct[0]).sum();

            System.out.println("E(X): [ " + printArray(markovChain.getAbsorbingState()) + " ] " + expectedValue);
        }
    }

    private MarkovChain[] flipCases() {
        MarkovChain[] markovChains = new MarkovChain[(int) Math.pow(m, n)];
        for (int i = 0;i < Math.pow(m, n);i++) {
            String number = Integer.toString(i, m);
            int[] absorbingState = new int[n];
            for (int j = n - 1, k = number.length();j >= 0;j--, k--) {
                if (k > 0) {
                    absorbingState[j] = Integer.parseInt(number.substring(k - 1, k));
                }
            }
            markovChains[i] = new MarkovChain(absorbingState, m);
        }

        return markovChains;
    }

    private double[][] matrixSubtraction(double[][] A, double[][] B) {
        double[][] result = new double[A.length][A[0].length];

        for (int i = 0;i < A.length;i++) {
            for (int j = 0;j < A[i].length;j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }

        return result;
    }

    private String printArray(int[] arr) {
        StringBuilder result = new StringBuilder();
        for (int i = 0;i < arr.length;i++) {
            if (i != 0) {
                result.append(" ");
            }
            result.append(arr[i]);
        }
        return result.toString();
    }

    private double[][] getIdentityMatrix(int size) {
        double[][] identity = new double[size][size];

        for (int i = 0;i < size;i++) {
            identity[i][i] = 1.0;
        }

        return identity;
    }

    private void getCofactor(double[][] A, double[][] temp, int p, int q, int n) {
        int i = 0, j = 0;

        // Looping for each element of the matrix
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                // Copying into temporary matrix only those element
                // which are not in given row and column
                if (row != p && col != q) {
                    temp[i][j++] = A[row][col];

                    // Row is filled, so increase row index and
                    // reset col index
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    /* Recursive function for finding determinant of matrix.
    n is current dimension of A[][]. */
    private double determinant(double[][] A, int n) {
        double D = 0; // Initialize result

        // Base case : if matrix contains single element
        if (n == 1)
            return A[0][0];

        double[][] temp = new double[n][n]; // To store cofactors

        int sign = 1; // To store sign multiplier

        // Iterate for each element of first row
        for (int f = 0; f < n; f++) {
            // Getting Cofactor of A[0][f]
            getCofactor(A, temp, 0, f, n);
            D += sign * A[0][f] * determinant(temp, n - 1);

            // terms are to be added with alternate sign
            sign = -sign;
        }

        return D;
    }

    // Function to get adjoint of A[n][n] in adj[n][n].
    private void adjoint(double[][] A, double[][] adj) {
        if (n == 1) {
            adj[0][0] = 1;
            return;
        }

        // temp is used to store cofactors of A[][]
        int sign;
        double[][] temp = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Get cofactor of A[i][j]
                getCofactor(A, temp, i, j, n);

                // sign of adj[j][i] positive if sum of row
                // and column indexes is even.
                sign = ((i + j) % 2 == 0)? 1: -1;

                // Interchanging rows and columns to get the
                // transpose of the cofactor matrix
                adj[j][i] = (sign)*(determinant(temp, n-1));
            }
        }
    }

    // Function to calculate and store inverse, returns false if
    // matrix is singular
    private void inverse(double[][] A, double[][] inverse) {
        // Find determinant of A[][]
        double det = determinant(A, n);
        if (det == 0) {
            System.out.print("Singular matrix, can't find its inverse");
            return;
        }

        // Find adjoint
        double [][]adj = new double[n][n];
        adjoint(A, adj);

        // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                inverse[i][j] = adj[i][j]/ det;

    }
}
