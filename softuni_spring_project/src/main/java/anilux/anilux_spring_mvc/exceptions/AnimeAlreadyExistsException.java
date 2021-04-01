package anilux.anilux_spring_mvc.exceptions;

public class AnimeAlreadyExistsException extends RuntimeException {

    public AnimeAlreadyExistsException() {
    }

    public AnimeAlreadyExistsException(String message) {
        super(message);
    }
}
