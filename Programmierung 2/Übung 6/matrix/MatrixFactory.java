package matrix;

final class MatrixFactory
{
    private MatrixFactory() {}

    static Matrix createTestMatrix(final int rows, final int columns)
    {
        Matrix m = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                m.setValue(i, j, j);
            }
        }
        return m;
    }
}