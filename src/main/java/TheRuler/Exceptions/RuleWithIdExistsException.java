/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TheRuler.Exceptions;

/**
 *
 * @author pyty
 */
public class RuleWithIdExistsException extends Exception {
    public RuleWithIdExistsException() { super(); }
    public RuleWithIdExistsException(String message) { super(message); }
    public RuleWithIdExistsException(String message, Throwable cause) { super(message, cause); }
    public RuleWithIdExistsException(Throwable cause) { super(cause); }
}
