package cis233.a1;

public class A1233AHatLinkedList<AnyType>
{
    /**
     * Construct the list
     */
    public A1233AHatLinkedList( )
    {
        header = new A1233AHatListNode<AnyType>( null );
    }

    /**
     * Test if the list is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return header.next == null;
    }

    /**
     * Make the list logically empty.
     */
    public void makeEmpty( )
    {
        header.next = null;
    }

    /**
     * Return an iterator representing the header node.
     */
    public A1233AHatLinkedListIterator<AnyType> zeroth( )
    {
        return new A1233AHatLinkedListIterator<AnyType>( header );
    }

    /**
     * Return an iterator representing the first node in the list.
     * This operation is valid for empty lists.
     */
    public A1233AHatLinkedListIterator<AnyType> first( )
    {
        return new A1233AHatLinkedListIterator<AnyType>( header.next );
    }

    public int size()
    {
        int count = 0;
        A1233AHatLinkedListIterator listItr = first();
        while(listItr.isValid())
        {
            count++;
            listItr.advance();
        }
        return count;
    }
    /**
     * Insert after p.
     * @param x the item to insert.
     * @param p the position prior to the newly inserted item.
     */
    public void insert( AnyType x, A1233AHatLinkedListIterator<AnyType> p )
    {
        if( p != null && p.current != null )
            p.current.next = new A1233AHatListNode<AnyType>( x, p.current.next );
    }

    public void prepend( AnyType x )
    {
        A1233AHatListNode<AnyType> newHead = new A1233AHatListNode<>(x);
        newHead.next = header.next;
        header.next = newHead;
    }

    /**
     * Return iterator corresponding to the first node containing an item.
     * @param x the item to search for.
     * @return an iterator; iterator is not valid if item is not found.
     */
    public A1233AHatLinkedListIterator<AnyType> find( AnyType x )
    {
        A1233AHatListNode<AnyType> itr = header.next;

        while( itr != null && !itr.element.equals( x ) )
            itr = itr.next;

        return new A1233AHatLinkedListIterator<AnyType>( itr );
    }

    /**
     * Return iterator prior to the first node containing an item.
     * @param x the item to search for.
     * @return appropriate iterator if the item is found. Otherwise, the
     * iterator corresponding to the last element in the list is returned.
     */
    public A1233AHatLinkedListIterator<AnyType> findPrevious( AnyType x )
    {
        A1233AHatListNode<AnyType> itr = header;

        while( itr.next != null && !itr.next.element.equals( x ) )
            itr = itr.next;

        return new A1233AHatLinkedListIterator<AnyType>( itr );
    }

    /**
     * Remove the first occurrence of an item.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        A1233AHatLinkedListIterator<AnyType> p = findPrevious( x );

        if( p.current.next != null )
            p.current.next = p.current.next.next;  // Bypass deleted node
    }

    // Simple print method
    public static <AnyType> void printList( A1233AHatLinkedList<AnyType> theList )
    {
        if( theList.isEmpty( ) )
            System.out.print( "Empty list" );
        else
        {
            A1233AHatLinkedListIterator<AnyType> itr = theList.first( );
            for( ; itr.isValid( ); itr.advance( ) )
                System.out.print( itr.retrieve( ) + " " );
        }

        System.out.println( );
    }

    private A1233AHatListNode<AnyType> header;

    // In this routine, LinkedList and LinkedListIterator are the
    // classes written in Section 17.2.
    public static <AnyType> int listSize( A1233AHatLinkedList<AnyType> theList )
    {
        A1233AHatLinkedListIterator<AnyType> itr;
        int size = 0;

        for( itr = theList.first(); itr.isValid(); itr.advance() )
            size++;

        return size;
    }



}

