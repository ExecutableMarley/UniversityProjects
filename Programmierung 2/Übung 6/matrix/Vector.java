package matrix;

import static java.lang.System.out;

class Vector
{
    private final int[] values;
    private final int rows;

    Vector(int rows)
    {
        this.rows = rows;
        values = new int[rows];
    }

    int getRows()
    {
        return rows;
    }

    int getValue(int row)
    {
        return values[row];
    }

    void setValue(int row, int value)
    {
        values[row] = value;
    }

    void print()
    {
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < rows; i++)
        {
            strb.append(values[i]).append(" ");
        }
        strb.append("\n");
        out.printf("%s", strb.toString());
    }
}