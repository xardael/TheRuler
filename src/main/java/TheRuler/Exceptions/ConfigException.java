package TheRuler.Exceptions;

/**
 * Exception used for configuration errors reporting.
 *
 * @author Peter Gren
 */
public class ConfigException extends Exception {
    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
