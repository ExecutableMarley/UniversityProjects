package com.company;

class OddIterator extends DataStructureIterator
{
    OddIterator(DataStructure ds)
    {
        super(ds);
        nextIndex = 1;
    }

    public boolean hasNext()
    {
        // Check if the current element is the last in the array
        return (nextIndex <= ds.getSize() - 1);
    }

    public Integer next()
    {
        // Record a value of an even index of the array
        Integer retValue = ds.getInts()[nextIndex];
        // Get the next even element
        nextIndex += 2;
        return retValue;
    }
}

public class OddIteratorStrategy implements IteratorStrategy
{
    @Override
    public DataStructureIterator getIterator(DataStructure ds)
    {
        return new OddIterator(ds);
    }
}
