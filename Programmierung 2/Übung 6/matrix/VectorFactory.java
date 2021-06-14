package matrix;

final class VectorFactory
{
    private VectorFactory() {}

    static Vector createTestVector(final int rows)
    {
        Vector v = new Vector(rows);
        for (int i = 0; i < rows; i++)
        {
            v.setValue(i, i);
        }
        return v;
    }
}