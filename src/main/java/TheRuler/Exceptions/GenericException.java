package TheRuler.Exceptions;

/**
 * Exception used for runtime error reporting with FreeMarker.
 *
 * @author Peter Gren
 */
public class GenericException extends RuntimeException {

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }
}
