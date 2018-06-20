

public interface List<AnyType>
{
	boolean isEmpty();
  boolean add(AnyType newValue);
  int size();
  
  AnyType remove(int index);

}