import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;



public class CircularLinkedList<AnyType> implements List<AnyType>
{
  private static class Node<AnyType>
  {
    private AnyType data;
    private Node<AnyType> next;

    public Node(AnyType d, Node<AnyType> n)
    {
      setData(d);
      setNext(n);
    }

    public AnyType getData() { return data; }

    public void setData(AnyType d) { data = d; }

    public Node<AnyType> getNext() { return next; }

    public void setNext(Node<AnyType> n) { next = n; }
  }

  private int theSize=0;
  private int modCount;
  private Node<AnyType> tail;

  public CircularLinkedList()
  {
	  tail= new Node<AnyType>(null, null);
	  
	  // if only modifation happend
	  //modCount =0;

  }

  public void clear()
  {
 
	  tail.setNext(tail);
	  theSize =0;
  }

  public int size()
  {
	  return theSize;
  }

  public boolean isEmpty()
  {
	return theSize==0 ;  
  }

  public AnyType get(int index)
  {
	  Node<AnyType> indexNode = getNode(index);
	  return indexNode.getData();

  }

  public AnyType set(int index, AnyType newValue)
  {
	  Node<AnyType> indexNode = getNode(index);
	    AnyType oldValue = indexNode.getData();
	 
	    indexNode.setData(newValue);
	    return oldValue;
  }

  public boolean add(AnyType newValue)
  {
    add(size()-1, newValue);
    return true;
  }

  public void add(int index, AnyType newValue)
  {
	  // if list is empty
	  if (isEmpty()) {
		  Node<AnyType> temp= new Node<AnyType>(newValue,tail);
		  tail.setNext(temp);
		  theSize++;
	  }
	  // list is not empty.
	  else {
		  //create a new node that points to the node that tail had
		  Node<AnyType> last = getNode(index);
		  Node<AnyType> temp = new Node<AnyType>(newValue, last.next);
		  last.setNext(temp);
		  last = temp;
		  theSize++;
	  }
		  
  }

  public AnyType remove(int index)
  {
	  if (index==0) {
		  Node<AnyType> currNode = tail.next;
		  tail.setNext(currNode.next);
		  theSize--;
		  modCount++;
		  return currNode.getData();
		  
	  }
	  else {
	  Node<AnyType> currNode = getNode(index);
	  Node <AnyType> prevNode = getNode(index-1);
	  prevNode.setNext(currNode.getNext());
	  theSize--;
	  modCount++;
	  return currNode.getData();
	  }  
	    
  }
  public Iterator<AnyType> iterator()
  {
    return new LinkedListIterator();    
  }

  private Node<AnyType> getNode(int index)
  {
    return (getNode(index, 0, size()-1));
  }

  private Node<AnyType> getNode(int index, int lower, int upper)
  {
    Node<AnyType> currNode;
 
    if (index < lower || index > upper) {
      throw new IndexOutOfBoundsException();
  }
  
      currNode = tail.getNext();
      for (int i = 0; i < index; i++) 
    	  currNode = currNode.getNext();
    return currNode;
  }

  private class LinkedListIterator implements Iterator<AnyType>
  {
    private Node<AnyType> current;
    private int expectedModCount;
    private boolean okToRemove;

    LinkedListIterator()
    {
    	 current = tail.next;
    
         expectedModCount = modCount;
         okToRemove = false;
    }

    public boolean hasNext()
    {
    	return (current.next.getData() != null );
    }

    public AnyType next()
    {
    	 if (modCount != expectedModCount)
    	        throw new ConcurrentModificationException();
    	      if (!hasNext())
    	        throw new NoSuchElementException();
    	 
    	      AnyType nextValue = current.getData();
    	      current = current.next;
    	      okToRemove = true;
    	      return nextValue;

    }

    public void remove()
    {
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (!okToRemove)
        throw new IllegalStateException();
 
      CircularLinkedList.this.remove(0);
      expectedModCount++;
      okToRemove = false;
    }
  }
}