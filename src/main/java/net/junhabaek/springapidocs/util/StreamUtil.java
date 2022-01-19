package net.junhabaek.springapidocs.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface StreamUtil {
    public static <T> List<T> convertToList(Iterable<T> iterable){
        return StreamSupport.stream(iterable.spliterator(),false).collect(Collectors.toList());
    }
}
