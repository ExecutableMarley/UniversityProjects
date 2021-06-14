package com.company;

import java.util.concurrent.ThreadLocalRandom;

final class PiTrialThread extends Thread
{
    private final PiTrial piTrial;

    private final long maxTrials;

    private static int threadInitNumber;

    private static synchronized int nextThreadNum()
    {
        return threadInitNumber++;
    }

    PiTrialThread(PiTrial piTrial, long maxTrials)
    {
        super("PiTrialThread-" + nextThreadNum());
        this.piTrial = piTrial;
        this.maxTrials = maxTrials;
    }

    @Override
    public void run()
    {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        long end = maxTrials;
        final long BATCH_SIZE = 1000000;
        long loopEnd = BATCH_SIZE;

        while (!isInterrupted() && end > 0)
        {
            //First if statement prevents unnecessary blocking
            if (piTrial.curState == PiTrial.States.paused)
            {
                synchronized (piTrial.pauseLock)
                {
                    //In case of exceptions we don't let the thread escape with the loop
                    while (piTrial.curState == PiTrial.States.paused)
                    {
                        try
                        {
                            piTrial.pauseLock.wait();
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

            long inCircleCount = 0;
            long trialCount = 0;

            if (end > BATCH_SIZE)
            {
                end = end - BATCH_SIZE;
            }
            else
            {
                loopEnd = end;
                end = 0;
            }

            for (long i = 0; i < loopEnd; i++)
            {
                double x = threadLocalRandom.nextDouble();
                double y = threadLocalRandom.nextDouble();
                if (x * x + y * y < 1)
                {
                    inCircleCount++;
                }
                trialCount++;
            }

            piTrial.addTrials(trialCount, inCircleCount);
        }
        piTrial.threadDone();
    }
}
