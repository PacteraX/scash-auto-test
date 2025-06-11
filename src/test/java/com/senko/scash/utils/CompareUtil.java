package com.senko.scash.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompareUtil {

    public static <T> boolean compareLists(List<T> list1, List<T> list2) {
        if ((list1 == null || list1.isEmpty()) && (list2 == null || list2.isEmpty())) {
            return true;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        Set<T> set1 = new HashSet<>(list1);
        Set<T> set2 = new HashSet<>(list2);
        return set1.equals(set2);
    }

}
