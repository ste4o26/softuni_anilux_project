package anilux.anilux_spring_mvc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorUtilImpl implements ValidatorUtil {
    private final Validator validator;

    @Autowired
    public ValidatorUtilImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <T> boolean isValid(T entity) {
        return this.validator.validate(entity).isEmpty();
    }

    @Override
    public <E> Set<ConstraintViolation<E>> violations(E entity) {
        return this.validator.validate(entity);
    }
}