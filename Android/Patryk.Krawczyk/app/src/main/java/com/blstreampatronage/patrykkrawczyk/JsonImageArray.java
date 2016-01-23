package com.blstreampatronage.patrykkrawczyk;

import java.util.Collection;

/**
 * Used by Jackson to map JSON
 */
public class JsonImageArray {
    private Collection<SingleImage> array;
    public  Collection<SingleImage> getArray()         { return array; }

    public  void setArray(Collection<SingleImage> a) { array = a; }
}
