package cis233.a1;

/**
 * Exception class for access in empty containers
 * such as stacks, queues, and priority queues.
 * @author Mark Allen Weiss
 */
public class A1233AHatUnderflowException extends RuntimeException
{
    /**
     * Construct this exception object.
     * @param message the error message.
     */
    public A1233AHatUnderflowException( String message )
    {
        super( message );
    }
}
