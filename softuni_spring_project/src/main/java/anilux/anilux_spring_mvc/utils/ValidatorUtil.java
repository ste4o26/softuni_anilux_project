package anilux.anilux_spring_mvc.utils;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidatorUtil {
    <T> boolean isValid(T entity);

    <E> Set<ConstraintViolation<E>> violations(E entity);
}
