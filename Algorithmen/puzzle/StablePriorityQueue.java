/**
 * @author Marley Arns
 * @version 1.0
 * @github: github.com/ExecutableMarley
 */

package puzzle;

import java.util.*;

/**
 * Custom Priority Queue that guarantees the stability of its elements
 * @param <E> the type of elements held in this Queue
 */
public class StablePriorityQueue<E> extends LinkedList<E> implements Queue<E>
{
    private final Comparator<E> comparator;

    /**
     * Creates a StablePriorityQueue Object
     * @param comparator orders its elements according to the specified comparator
     */
    StablePriorityQueue(Comparator<E> comparator)
    {
        super();

        this.comparator = comparator;
    }

    @Override
    public boolean add(E puzzle)
    {
        //Iterate through the list and insert just before the next element is greater
        //Thus preserving the order if scores are equal
        ListIterator<E> iterator = this.listIterator();
        while (iterator.hasNext())
        {
            if (comparator.compare(iterator.next(), puzzle) > 0)
            {
                //Step one element back
                iterator.previous();
                //Insert new element
                iterator.add(puzzle);
                return true;
            }
        }
        //Add to last place
        return super.add(puzzle);
    }

    //For sanity checks
    public boolean isSorted()
    {
        Iterator<E> iterator = this.iterator();
        E previous = null;
        E cur;
        while (iterator.hasNext())
        {
            cur = iterator.next();
            if (previous != null && comparator.compare(cur, previous) > 0)
                return false;

            previous = cur;
        }
        return true;
    }
}
