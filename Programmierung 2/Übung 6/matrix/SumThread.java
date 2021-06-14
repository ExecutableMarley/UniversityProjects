package matrix;

final class SumThread implements Runnable
{
    private final int row;
    private final MatrixVectorSum cm;

    SumThread(MatrixVectorSum cm, int row)
    {
        this.cm = cm;
        this.row = row;
    }

    @Override
    public void run()
    {
        Matrix m = cm.getMatrix();
        Vector v = cm.getVector();
        int value = 0;
        for (int j = 0; j < m.getColumns(); j++)
        {
            value += m.getValue(row, j) * v.getValue(j);
        }

        cm.addValueToSum(value);
        cm.threadDone();
    }
}
