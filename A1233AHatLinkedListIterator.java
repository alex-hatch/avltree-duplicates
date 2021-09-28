package cis233.a1;

public class A1233AHatLinkedListIterator<AnyType>
{
    /**
     * Construct the list iterator
     * @param theNode any node in the linked list.
     */
    A1233AHatLinkedListIterator( A1233AHatListNode<AnyType> theNode )
    {
        current = theNode;
    }

    /**
     * Test if the current position is a valid position in the list.
     * @return true if the current position is valid.
     */
    public boolean isValid( )
    {
        return current != null;
    }

    /**
     * Return the item stored in the current position.
     * @return the stored item or null if the current position
     * is not in the list.
     */
    public AnyType retrieve( )
    {
        return isValid( ) ? current.element : null;
    }

    /**
     * Advance the current position to the next node in the list.
     * If the current position is null, then do nothing.
     */
    public void advance( )
    {
        if( isValid( ) )
            current = current.next;
    }

    A1233AHatListNode<AnyType> current;    // Current position
}
