/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TheRuler.Exceptions;

/**
 *
 * @author pyty
 */
public class GenericException extends RuntimeException{
    public GenericException() { super(); }
    public GenericException(String message) { super(message); }
    public GenericException(String message, Throwable cause) { super(message, cause); }
    public GenericException(Throwable cause) { super(cause); }
}
