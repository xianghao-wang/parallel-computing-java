package edu.coursera.parallel;

import java.util.Random;

import static edu.rice.pcdp.PCDP.*;

/**
 * Wrapper class for implementing matrix multiply efficiently in parallel.
 */
public final class MatrixMultiply {
    /**
     * Default constructor.
     */
    private MatrixMultiply() {
    }

    /**
     * Perform a two-dimensional matrix multiply (A x B = C) sequentially.
     *
     * @param A An input matrix with dimensions NxN
     * @param B An input matrix with dimensions NxN
     * @param C The output matrix
     * @param N Size of each dimension of the input matrices
     */
    public static void seqMatrixMultiply(final double[][] A, final double[][] B,
            final double[][] C, final int N) {
        double start = System.nanoTime();

        forseq2d(0, N - 1, 0, N - 1, (i, j) -> {
            C[i][j] = 0.0;
            for (int k = 0; k < N; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        });

        printResult("seqMatrixMultiply", System.nanoTime() - start);
    }

    /**
     * Perform a two-dimensional matrix multiply (A x B = C) in parallel.
     *
     * @param A An input matrix with dimensions NxN
     * @param B An input matrix with dimensions NxN
     * @param C The output matrix
     * @param N Size of each dimension of the input matrices
     */
    public static void parMatrixMultiply(final double[][] A, final double[][] B,
            final double[][] C, final int N) {
        double start = System.nanoTime();

        int nTasks = Runtime.getRuntime().availableProcessors();
        forall2dChunked(0, N - 1, 0, N - 1, nTasks, (i, j) -> {
            C[i][j] = 0.0;
            for (int k = 0; k < N; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        });

        printResult("parMatrixMultiply", System.nanoTime() - start);
    }

    public static void printResult(String name, double timeInNanos) {
        System.out.printf(" %s completed in %8.3f milliseconds\n", name, timeInNanos / 1e6);
    }

    public static void main(String[] args) {
        int N = 2048;
        Random random = new Random();
        double[][] A = new double[N][N];
        double[][] B = new double[N][N];
        double[][] C = new double[N][N];
        for (int i = 0; i < N; i ++)
            for (int j = 0; j < N; j ++) {
                A[i][j] = random.nextDouble();
                B[i][j] = random.nextDouble();
            }

        seqMatrixMultiply(A, B, C, N);
        parMatrixMultiply(A, B, C, N);
    }
}
