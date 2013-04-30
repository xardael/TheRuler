package TheRuler.Exceptions;

/**
 * Exception used for runtime error reporting with FreeMarker.
 *
 * @author Peter Gren
 */
public class RuleExistsException extends Exception {

    public RuleExistsException() {
        super();
    }

    public RuleExistsException(String message) {
        super(message);
    }

    public RuleExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleExistsException(Throwable cause) {
        super(cause);
    }
}
