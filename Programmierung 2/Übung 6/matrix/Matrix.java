package matrix;

import static java.lang.System.out;

class Matrix
{
    private final int[][] values;
    private final int rows;
    private final int columns;

    Matrix(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        values = new int[rows][columns];
    }

    long cumulativeSumOfMultiplicationWith(Vector v)
    {
        if (columns != v.getRows())
        {
            throw new IllegalArgumentException("The number of rows doesn't match the number of columns in order to multiply the matrices!");
        }
        long sum = 0;
        for (int i = 0; i < rows; i++)
        {
            int value = 0;
            for (int j = 0; j < columns; j++)
            {
                value += this.getValue(i, j) * v.getValue(j);
            }
            sum += value;
        }
        return sum;
    }

    int getRows()
    {
        return rows;
    }

    int getColumns()
    {
        return columns;
    }

    int getValue(int row, int column)
    {
        return values[row][column];
    }

    void setValue(int row, int column, int value)
    {
        this.values[row][column] = value;
    }

    void print()
    {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                strb.append(values[i][j]).append(" ");
            }
            strb.append("\n");
        }
        out.printf("%s", strb.toString());
    }
}
