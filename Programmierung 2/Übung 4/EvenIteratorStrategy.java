package com.company;

public class EvenIteratorStrategy implements IteratorStrategy
{
    @Override
    public DataStructureIterator getIterator(DataStructure ds)
    {
        return new EvenIterator(ds);
    }
}
