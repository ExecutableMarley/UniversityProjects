package com.company;

abstract class DataStructureIterator implements java.util.Iterator<Integer>
{
    int nextIndex;
    DataStructure ds;

    DataStructureIterator(DataStructure ds)
    {
        this.ds = ds;
    }
}