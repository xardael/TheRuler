package TheRuler.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception used for returning HTTP 500 Internal Server Error.
 *
 * @author Peter Gren
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public final class InternalErrorException extends RuntimeException {
}
