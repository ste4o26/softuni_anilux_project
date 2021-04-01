package anilux.anilux_spring_mvc.utils;

import java.util.Collection;
import java.util.List;

public interface CollectionMapperUtil {
    <E, O> List<O> map(Collection<E> collection, Class<O> clazzToBeMapped);
}
