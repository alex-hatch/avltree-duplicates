package cis233.a1;

class A1233AHatListNode<AnyType>
{
    // Constructors
    public A1233AHatListNode( AnyType theElement )
    {
        this( theElement, null );
    }

    public A1233AHatListNode( AnyType theElement, A1233AHatListNode<AnyType> n )
    {
        element = theElement;
        next    = n;
    }

    public AnyType   element;
    public A1233AHatListNode<AnyType> next;
}