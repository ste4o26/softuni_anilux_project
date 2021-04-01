package anilux.anilux_spring_mvc.exceptions;

public class GenreAlreadyExistsException extends RuntimeException{

    public GenreAlreadyExistsException() {
    }

    public GenreAlreadyExistsException(String message) {
        super(message);
    }

}
