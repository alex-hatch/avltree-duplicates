/*
    Alex Hatch / alexander_hatch@my.cuesta.edu
    CIS 233 / Scovil
    Assignment 1
*/

package cis233.a1;

// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws A1233AHatUnderflowException as appropriate

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 *
 * @author Mark Allen Weiss
 */
public class A1233AHatAVLTree<AnyType extends Comparable<? super AnyType>> {

    public String author() {
        return "Alex Hatch";
    }

    /**
     * Construct the tree.
     */
    public A1233AHatAVLTree() {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(AnyType x) {
        root = remove(x, root);
    }

    /**
     * Internal method to remove from a subtree.
     *
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private A1233AHatAvlNode<AnyType> remove(AnyType x, A1233AHatAvlNode<AnyType> t) {
        if (t == null)
            return null;   // Item not found; do nothing

        int compareResult = x.compareTo(t.elementList.first().retrieve().element);

        if (compareResult < 0)
            t.left = remove(x, t.left);
        else if (compareResult > 0)
            t.right = remove(x, t.right);
        else {
            A1233AHatLinkedListIterator<A1233AHatAvlItem<AnyType>> listItr = t.elementList.first();
            while (listItr.isValid()) {
                A1233AHatAvlItem<AnyType> thisItem = listItr.retrieve();
                if (thisItem.isActive) {
                    thisItem.isActive = false;
                    return t;
                }
                listItr.advance();
            }
        }
        return balance(t);
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public AnyType findMin() {
        if (isEmpty() || allInactive())
            throw new A1233AHatUnderflowException("Empty");
        return getFirst(findMin(root));
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public AnyType findMax() {
        if (isEmpty() || allInactive())
            throw new A1233AHatUnderflowException("Empty");
        return getFirst(findMax(root));
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains(AnyType x) {
        return contains(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty() || allInactive())
            System.out.println("Tree is currently empty.");
        else
            printTree(root);
    }

    public void printBalTree(boolean inOrder) {
        if (isEmpty())
            System.out.println("Tree is currently empty.");
        else if (inOrder) {
            printBalTree(root, true);
        } else {
            printBalTree(root, false);
        }
    }

    public void writeBalTree(boolean inOrder) {
        String fileName = "A1233AHatAVLout.txt";
        try {
            PrintWriter output = new PrintWriter(fileName);
            if (isEmpty())
                output.println("Tree is currently empty.");
            else if (inOrder) {
                writeBalTree(root, true, output);
            } else {
                writeBalTree(root, false, output);
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Assume t is either balanced or within one of being balanced
    private A1233AHatAvlNode<AnyType> balance(A1233AHatAvlNode<AnyType> t) {
        if (t == null)
            return t;

        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
            if (height(t.left.left) >= height(t.left.right))
                t = rotateWithLeftChild(t);
            else
                t = doubleWithLeftChild(t);
        else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
            if (height(t.right.right) >= height(t.right.left))
                t = rotateWithRightChild(t);
            else
                t = doubleWithRightChild(t);

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(A1233AHatAvlNode<AnyType> t) {
        if (t == null)
            return -1;

        if (t != null) {
            int hl = checkBalance(t.left);
            int hr = checkBalance(t.right);
            if (Math.abs(height(t.left) - height(t.right)) > 1 ||
                    height(t.left) != hl || height(t.right) != hr)
                System.out.println("OOPS!!");
        }

        return height(t);
    }

    /**
     * Internal method to insert into a subtree.
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private A1233AHatAvlNode<AnyType> insert(AnyType x, A1233AHatAvlNode<AnyType> t) {
        if (t == null) {
            t = new A1233AHatAvlNode<AnyType>();
            t.elementList.insert(new A1233AHatAvlItem<>(x, true), t.elementList.zeroth());
            return t;
        }

        int compareResult = x.compareTo(t.elementList.first().retrieve().element);

        if (compareResult < 0)
            t.left = insert(x, t.left);
        else if (compareResult > 0)
            t.right = insert(x, t.right);
        else {
            t.elementList.prepend(new A1233AHatAvlItem<>(x, true));
            // No balancing needed; entered in at "equal" height.
            return t;
        }
        return balance(t);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private A1233AHatAvlNode<AnyType> findMin(A1233AHatAvlNode<AnyType> t) {
        if (root.left == null && allInactiveList(root)) {
            if (t.right != null)
                return findMin(t.right);
        }
        if (t == null || t.left == null)
            return t;
        if (t.left.left == null && allInactiveList(t.left)) {
            return t;
        } else {
            return findMin(t.left);
        }
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private A1233AHatAvlNode<AnyType> findMax(A1233AHatAvlNode<AnyType> t) {
        if (root.right == null && allInactiveList(root)) {
            if (t.left != null)
                return findMax(t.left);
        }
        if (t == null || t.right == null)
            return t;
        if (t.right.right == null && allInactiveList(t.right)) {
            return t;
        } else {
            return findMax(t.right);
        }
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains(AnyType x, A1233AHatAvlNode<AnyType> t) {
        while (t != null) {
            int compareResult = x.compareTo(t.elementList.first().retrieve().element);

            if (compareResult < 0)
                t = t.left;
            else if (compareResult > 0)
                t = t.right;
            else {
                return !(allInactiveList(t));
            }
        }
        return false;   // No match
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(A1233AHatAvlNode<AnyType> t) {
        if (t != null) {
            printTree(t.left);
            printWithFormat(t, false);
            printTree(t.right);
        }
    }

    private void printBalTree(A1233AHatAvlNode<AnyType> t, boolean inOrder) {
        if (inOrder) {
            if (t != null) {
                printBalTree(t.left, true);
                printWithFormat(t, true);
                printBalTree(t.right, true);
            }
        } else {
            if (t != null) {
                printBalTree(t.right, false);
                printWithFormat(t, true);
                printBalTree(t.left, false);
            }
        }
    }

    private void writeBalTree(A1233AHatAvlNode<AnyType> t, boolean inOrder, PrintWriter theFile) {
        if (inOrder) {
            if (t != null) {
                writeBalTree(t.left, true, theFile);
                writeWithFormat(t, theFile);
                writeBalTree(t.right, true, theFile);
            }
        } else {
            if (t != null) {
                writeBalTree(t.right, false, theFile);
                writeWithFormat(t, theFile);
                writeBalTree(t.left, false, theFile);
            }
        }
    }

    private void writeWithFormat
            (A1233AHatAvlNode<AnyType> t, PrintWriter file) {
        A1233AHatLinkedListIterator<A1233AHatAvlItem<AnyType>> listItr = t.elementList.first();
        while (listItr.isValid()) {
            file.println("Data: " + listItr.retrieve().element + "      Height: " + t.height +
                    "   Balance: " + (height(t.right) - height(t.left)));

            file.print("      Left:  ");

            if (t.left == null)
                file.println(t.left);

            else {
                file.println("Data: " + getFirst(t.left) + "     Height: " + t.left.height +
                        "   Balance: " + (height(t.left.right) - height(t.left.left)));
            }

            file.print("      Right: ");

            if (t.right == null)
                file.println(t.right);

            else {
                file.println("Data: " + getFirst(t.right) + "     Height: " + t.right.height +
                        "   Balance: " + (height(t.right.right) - height(t.right.left)));
            }
            file.println(" ");
            listItr.advance();
        }
    }

    private void printWithFormat(A1233AHatAvlNode<AnyType> t, boolean allItems) {
        if (allItems) {
            A1233AHatLinkedListIterator<A1233AHatAvlItem<AnyType>> listItr = t.elementList.first();
            while (listItr.isValid()) {
                System.out.println("Data: " + listItr.retrieve().element + "      Height: " + t.height +
                        "   Balance: " + (height(t.right) - height(t.left)));

                System.out.print("      Left:  ");

                if (t.left == null)
                    System.out.println(t.left);

                else {
                    System.out.println("Data: " + getFirst(t.left) + "     Height: " + t.left.height +
                            "   Balance: " + (height(t.left.right) - height(t.left.left)));
                }

                System.out.print("      Right: ");

                if (t.right == null)
                    System.out.println(t.right);

                else {
                    System.out.println("Data: " + getFirst(t.right) + "     Height: " + t.right.height +
                            "   Balance: " + (height(t.right.right) - height(t.right.left)));
                }
                System.out.println(" ");
                listItr.advance();
            }
        } else {
            A1233AHatLinkedListIterator<A1233AHatAvlItem<AnyType>> listItr = t.elementList.first();
            while (listItr.isValid()) {
                if (listItr.retrieve().isActive) {
                    System.out.println("Data: " + listItr.retrieve().element + "     Height: " + t.height +
                            "   Balance: " + (height(t.right) - height(t.left)));

                    System.out.print("      Left:  ");

                    if (t.left == null || allInactiveList(t.left))
                        System.out.println("null");

                    else {
                        System.out.println("Data: " + getFirst(t.left) + "     Height: " + t.left.height +
                                "   Balance: " + (height(t.left.right) - height(t.left.left)));
                    }

                    System.out.print("      Right: ");

                    if (t.right == null || allInactiveList(t.right))
                        System.out.println("null");

                    else {
                        System.out.println("Data: " + getFirst(t.right) + "     Height: " + t.right.height +
                                "   Balance: " + (height(t.right.right) - height(t.right.left)));
                    }
                    System.out.println(" ");
                }
                listItr.advance();
            }
        }
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(A1233AHatAvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private A1233AHatAvlNode<AnyType> rotateWithLeftChild(A1233AHatAvlNode<AnyType> k2) {
        A1233AHatAvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private A1233AHatAvlNode<AnyType> rotateWithRightChild(A1233AHatAvlNode<AnyType> k1) {
        A1233AHatAvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private A1233AHatAvlNode<AnyType> doubleWithLeftChild(A1233AHatAvlNode<AnyType> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private A1233AHatAvlNode<AnyType> doubleWithRightChild(A1233AHatAvlNode<AnyType> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    private AnyType getFirst(A1233AHatAvlNode<AnyType> t) {
        return t.elementList.first().retrieve().element;
    }

    private boolean allInactiveList(A1233AHatAvlNode<AnyType> t) {
        A1233AHatLinkedListIterator<A1233AHatAvlItem<AnyType>> listItr = t.elementList.first();
        while (listItr.isValid()) {
            if (listItr.retrieve().isActive)
                return false;
            listItr.advance();
        }
        return true;
    }

    private boolean allInactive() {
        return allInactiveTree(root);
    }

    private boolean allInactiveTree(A1233AHatAvlNode<AnyType> t) {
        if (t == null) {
            return true;
        }
        if (allInactiveList(t) && t.left == null && t.right == null) {
            return true;
        } else if (t.right != null) {
            return allInactiveTree(t.right);
        } else if (t.left != null) {
            return allInactiveTree(t.left);
        } else
            return false;
    }

    private int occurrence(A1233AHatAvlNode<AnyType> t) {
        int count = 0;
        A1233AHatLinkedListIterator<A1233AHatAvlItem<AnyType>> listItr =
                t.elementList.first();
        while (listItr.isValid()) {
            if (listItr.retrieve().isActive) {
                count++;
            }
            listItr.advance();
        }
        return count;
    }

    private void calculateMode(A1233AHatAvlNode<AnyType> t) {
        if (t != null) {
            int thisCount = occurrence(t);
            if (thisCount > count) {
                count = thisCount;
                mode = getFirst(t);
            }
            calculateMode(t.left);
            calculateMode(t.right);
        }
    }

    public Result<AnyType> findMode() {
        calculateMode(root);
        return new ConcreteResult<>(mode, count);
    }

    private static class ConcreteResult<AnyType extends Comparable<? super AnyType>> implements Result<AnyType> {
        public ConcreteResult(AnyType theMode, int theCount) {
            staticMode = theMode;
            staticCount = theCount;
        }

        @Override
        public AnyType mode() {
            return staticMode;
        }

        @Override
        public int count() {
            return staticCount;
        }

        private AnyType staticMode;
        private int staticCount;
    }

    private static class A1233AHatAvlNode<AnyType> {
        // Constructors
        A1233AHatAvlNode() {
            this(null, null);
        }

        A1233AHatAvlNode(A1233AHatAvlNode<AnyType> lt, A1233AHatAvlNode<AnyType> rt) {
            elementList = new A1233AHatLinkedList<>();
            left = lt;
            right = rt;
            height = 0;
        }

        A1233AHatLinkedList<A1233AHatAvlItem<AnyType>> elementList;
        A1233AHatAvlNode<AnyType> left;         // Left child
        A1233AHatAvlNode<AnyType> right;        // Right child
        int height;       // Height
    }

    private static class A1233AHatAvlItem<AnyType> {
        A1233AHatAvlItem(AnyType theElement, boolean active) {
            element = theElement;
            isActive = active;
        }

        AnyType element;
        boolean isActive;
    }

    private static final int ALLOWED_IMBALANCE = 1;
    /**
     * The tree root.
     */
    private A1233AHatAvlNode<AnyType> root;

    private AnyType mode;
    private int count;

}