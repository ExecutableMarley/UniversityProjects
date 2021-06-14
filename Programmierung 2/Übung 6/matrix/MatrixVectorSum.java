package matrix;

final class MatrixVectorSum
{
    private long sum;
    private final Matrix m;
    private final Vector v;

    //The lock we use to wait inside the main thread
    private final Object waitLock = new Object();

    MatrixVectorSum(Matrix m, Vector v)
    {
        sum = 0;
        this.m = m;
        this.v = v;
        threadsDone = m.getRows();
    }

    private int threadsDone;

    private boolean done()
    {
        return threadsDone == m.getRows();
    }

    private boolean resetDoneThreads()
    {
        if (!done())
            return false;

        threadsDone = 0;
        return true;
    }

    //Synchronized
    synchronized void threadDone()
    {
        threadsDone++;
        //Wake up waiting main thread
        synchronized (waitLock)
        {
            waitLock.notify();
        }
    }

    long computeMatrixVectorSum() throws Exception
    {
        if (!resetDoneThreads())
        {
            throw new Exception("Computation not done yet!");
        }
        sum = 0;
        if (m.getColumns() != v.getRows())
        {
            throw new IllegalArgumentException("The number of columns does not match the number of rows!");
        }

        for (int i = 0; i < m.getRows(); i++)
        {
            new Thread(new SumThread(this, i)).start();
        }

        //Wait for threads (Using future would be an superior method)
        synchronized (waitLock)
        {
            while (!done())
            {
                waitLock.wait();
            }
        }

        return sum;
    }

    Matrix getMatrix()
    {
        return m;
    }

    Vector getVector()
    {
        return v;
    }

    private long getSum()
    {
        return sum;
    }

    private void setSum(long sum)
    {
        this.sum = sum;
    }

    //Synchronized
    synchronized void addValueToSum(int value)
    {
        long currentSum = getSum();
        currentSum = currentSum + value;
        try
        {
            Thread.sleep(10);
        } catch (Exception e)
        {
            System.out.println(e.toString());
            //ignored
        }
        setSum(currentSum);
    }
}
