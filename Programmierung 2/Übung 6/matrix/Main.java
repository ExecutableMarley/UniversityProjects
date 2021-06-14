package matrix;

import static java.lang.System.out;

final class Main
{
    private Main() {}

    public static void main(String[] args)
    {
        final int rows = 100;
        final int columns = 1000;

        Matrix m = MatrixFactory.createTestMatrix(rows, columns);

        Vector v = VectorFactory.createTestVector(columns);

        long mvSum = m.cumulativeSumOfMultiplicationWith(v);

        out.printf("single threaded sum = %d\n", mvSum);

        try
        {
            MatrixVectorSum matrixVectorSum = new MatrixVectorSum(m, v);

            mvSum = matrixVectorSum.computeMatrixVectorSum();

            out.printf("multi-threaded sum = %d\n", mvSum);

        } catch (Exception e)
        {
            // ignored
            e.printStackTrace();
        }
    }
}
