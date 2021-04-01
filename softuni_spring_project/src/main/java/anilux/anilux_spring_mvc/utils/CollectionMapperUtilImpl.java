package anilux.anilux_spring_mvc.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CollectionMapperUtilImpl implements CollectionMapperUtil {
    private final ModelMapper modelMapper;

    @Autowired
    public CollectionMapperUtilImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public <E, O> List<O> map(Collection<E> collection, Class<O> clazzToBeMapped) {
        return collection
                .stream()
                .map(element -> this.modelMapper.map(element, clazzToBeMapped))
                .collect(Collectors.toList());
    }
}
