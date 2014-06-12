package com._42six.amino.common.util;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

// This class can compare a list of objects that to which the objects are sub classes of T.
public class LexigraphicalListComparator<T extends Comparable<? super T>> implements Comparator<List<T>> {

    @Override
    public int compare(List<T> a, List<T> b) {

        if ( a == b )    return  0;  // same object or both null
        if ( a == null ) return -1;
        if ( b == null ) return  1;
        ListIterator<T> iteratorA = a.listIterator();
        ListIterator<T> iteratorB = b.listIterator();


        while ( iteratorA.hasNext() && iteratorB.hasNext() ) {
            T entryA = iteratorA.next();
            T entryB = iteratorB.next();
            int result = entryA.compareTo(entryB);
            if ( result != 0 ) return result;
        }
        if ( iteratorA.hasNext() ) return 1;
        if ( iteratorB.hasNext() ) return -1;
        return 0;
    }
}