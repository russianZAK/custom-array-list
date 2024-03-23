package by.russianzak.customarraylist.CustomArrayList;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * CustomArrayList is a custom implementation of the {@link List} interface
 * that provides a dynamic array-based data structure.
 *
 * @param <T> the type of elements in this list
 */
public class CustomArrayList<T> extends AbstractList<T> implements List<T> {

  private static final int INITIAL_CAPACITY = 10;
  private Object[] elements;
  private int size;
  private int modificationCount;

  /**
   * Constructs an empty list with an initial capacity of ten.
   */
  public CustomArrayList() {
    elements = new Object[INITIAL_CAPACITY];
    size = 0;
    modificationCount = 0;
  }

  /**
   * Constructs an empty list with the specified initial capacity.
   *
   * @param capacity the initial capacity of the list
   * @throws IllegalArgumentException if the specified initial capacity is negative
   */
  public CustomArrayList(int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException("Illegal capacity: " + capacity);
    }
    elements = new Object[capacity];
    modificationCount = 0;
    size = 0;
  }

  /**
   * Returns the number of elements in this list.
   *
   * @return the number of elements in this list
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Checks if this list is empty.
   *
   * @return {@code true} if this list contains no elements, {@code false} otherwise
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Checks if this list contains the specified element.
   *
   * @param o the element to be checked for containment in this list
   * @return {@code true} if this list contains the specified element, {@code false} otherwise
   */
  @Override
  public boolean contains(Object o) {
    return indexOf(o) != -1;
  }

  /**
   * Returns the index of the first occurrence of the specified element in this list,
   * or -1 if this list does not contain the element.
   *
   * @param o the element to search for
   * @return the index of the first occurrence of the specified element in this list,
   *         or -1 if this list does not contain the element
   */
  @Override
  public int indexOf(Object o) {
    for (int i = 0; i < size; i++) {
      if (o == null && elements[i] == null || o != null && o.equals(elements[i])) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the index of the last occurrence of the specified element in this list,
   * or -1 if this list does not contain the element.
   *
   * @param o the element to search for
   * @return the index of the last occurrence of the specified element in this list,
   *         or -1 if this list does not contain the element
   */
  @Override
  public int lastIndexOf(Object o) {
    for (int i = size - 1; i >= 0; i--) {
      if (o == null && elements[i] == null || o != null && o.equals(elements[i])) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns an array containing all the elements in this list in proper sequence.
   *
   * @return an array containing all the elements in this list in proper sequence
   */
  @Override
  public Object[] toArray() {
    return Arrays.copyOf(elements, size);
  }

  /**
   * Returns an array containing all the elements in this collection.
   *
   * @param a the array into which the elements of this collection are to be stored,
   *          if it is big enough; otherwise, a new array of the same runtime type is allocated.
   * @param <E> the runtime type of the array to contain the collection.
   * @return an array containing all the elements in this collection.
   */
  @SuppressWarnings("unchecked")
  @Override
  public <E> E[] toArray(E[] a) {
    if (a.length < size) {
      return (E[]) Arrays.copyOf(elements, size, a.getClass());
    }
    System.arraycopy(elements, 0, a, 0, size);
    if (a.length > size) {
      a[size] = null;
    }
    return a;
  }


  /**
   * Returns the element at the specified position in this list.
   *
   * @param index the index of the element to be returned
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException if the index is out of range
   *         (index < 0 || index >= size)
   */
  @SuppressWarnings("unchecked")
  public T get(int index) {
    Objects.checkIndex(index, size);

    return (T) elements[index];
  }

  /**
   * Replaces the element at the specified position in this list with the specified element.
   *
   * @param index the index of the element to replace
   * @param element the element to be stored at the specified position
   * @return the element previously at the specified position
   * @throws IndexOutOfBoundsException if the index is out of range
   *         (index < 0 || index >= size)
   */
  @Override
  public T set(int index, T element) {
    Objects.checkIndex(index, size);

    T oldValue = get(index);
    elements[index] = element;
    return oldValue;
  }

  /**
   * Appends the specified element to the end of this list.
   *
   * @param element the element to be appended to this list
   * @return true (as specified by {@link Collection#add})
   */
  @Override
  public boolean add(T element) {
    if (size == elements.length) {
      increaseCapacity(size + 1);
    }
    elements[size++] = element;
    modificationCount++;
    return true;
  }

  /**
   * Inserts the specified element at the specified position in this list.
   *
   * @param index the index at which the specified element is to be inserted
   * @param element the element to be inserted
   * @throws IndexOutOfBoundsException if the index is out of range
   *         (index < 0 || index > size)
   */
  @Override
  public void add(int index, T element) {
    checkIndexForAdd(index);

    if (size == elements.length) {
      increaseCapacity(size + 1);
    }
    System.arraycopy(elements, index, elements, index + 1, size - index);
    elements[index] = element;
    size++;
    modificationCount++;
  }

  /**
   * Appends all the elements in the specified collection to the end of this list
   *
   * @param c the collection containing elements to be added to this list
   * @return true if this list changed as a result of the call
   */
  @Override
  public boolean addAll(Collection<? extends T> c) {
    return addAll(size, c);
  }

  /**
   * Inserts all the elements in the specified collection into this list at the specified position.
   *
   * @param index the index at which to insert the first element from the specified collection
   * @param c the collection containing elements to be added to this list
   * @return true if this list changed as a result of the call
   * @throws IndexOutOfBoundsException if the index is out of range
   *         (index < 0 || index > size)
   */
  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    checkIndexForAdd(index);

    if (c.isEmpty()) {
      return false;
    }

    ensureCapacity(size + c.size());
    System.arraycopy(elements, index, elements, index + c.size(), size - index);
    int i = index;
    for (T element : c) {
      elements[i++] = element;
    }
    size += c.size();
    modificationCount++;
    return true;
  }

  /**
   * Removes the first occurrence of the specified element from this list, if it is present.
   *
   * @param o the element to be removed from this list, if present
   * @return true if this list contained the specified element
   */
  @Override
  public boolean remove(Object o) {
    Iterator<T> iterator = iterator();
    while (iterator.hasNext()) {
      T current = iterator.next();
      if (Objects.equals(o, current)) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  /**
   * Removes the element at the specified position in this list.
   *
   * @param index the index of the element to be removed
   * @return the element that was removed from the list
   * @throws IndexOutOfBoundsException if the index is out of range
   *         (index < 0 || index >= size)
   */
  @Override
  public T remove(int index) {
    Objects.checkIndex(index, size);

    T removedElement = get(index);
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    elements[--size] = null;
    modificationCount++;
    return removedElement;
  }



  /**
   * Removes from this list all of its elements that are contained in the specified collection.
   *
   * @param c the collection containing elements to be removed from this list
   * @return true if this list changed as a result of the call
   * @throws NullPointerException if the specified collection is null
   */
  @Override
  public boolean removeAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator<T> iterator = iterator();
    while (iterator.hasNext()) {
      T element = iterator.next();
      if (c.contains(element)) {
        iterator.remove();
        modified = true;
      }
    }
    return modified;
  }

  /**
   * Retains only the elements in this list that are contained in the specified collection.
   *
   * @param c the collection containing elements to be retained in this list
   * @return true if this list changed as a result of the call
   * @throws NullPointerException if the specified collection is null
   */
  @Override
  public boolean retainAll(Collection<?> c) {
    Objects.requireNonNull(c);
    boolean modified = false;
    Iterator<T> iterator = iterator();
    while (iterator.hasNext()) {
      if (!c.contains(iterator.next())) {
        iterator.remove();
        modified = true;
      }
    }
    return modified;
  }

  /**
   * Checks if this list equals to the specified object.
   *
   * @param o the object to compare with this list
   * @return true if the specified object is equal to this list
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof List<?> other)) {
      return false;
    }
    if (size != other.size()) {
      return false;
    }
    for (int i = 0; i < size; i++) {
      if (!Objects.equals(get(i), other.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the hash code value for this list.
   *
   * @return the hash code value for this list
   */
  @Override
  public int hashCode() {
    int result = 1;
    for (int i = 0; i < size; i++) {
      result = 31 * result + Objects.hashCode(get(i));
    }
    return result;
  }

  /**
   * Removes all the elements from this list.
   * The list will be empty after this call returns.
   */
  @Override
  public void clear() {
    Arrays.fill(elements, null);
    size = 0;
    modificationCount++;
  }

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   *
   * @return an iterator over the elements in this list in proper sequence
   */
  @Override
  public Iterator<T> iterator() {
    return new CustomArrayListIterator();
  }

  /**
   * Returns a list iterator over the elements in this list in proper sequence.
   *
   * @return a list iterator over the elements in this list in proper sequence
   */
  @Override
  public ListIterator<T> listIterator() {
    return new CustomArrayListIterator();
  }

  /**
   * Returns a list iterator over the elements in this list in proper sequence,
   * starting at the specified position in the list.
   *
   * @param index the starting position of the list iterator
   * @return a list iterator over the elements in this list in proper sequence,
   * starting at the specified position in the list
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  @Override
  public ListIterator<T> listIterator(int index) {
    checkIndexForAdd(index);

    return new CustomArrayListIterator(index);
  }

  /**
   * Returns a view of the portion of this list between the specified {@code fromIndex},
   * inclusive, and {@code toIndex}, exclusive.
   *
   * @param fromIndex the starting index (inclusive) of the sublist
   * @param toIndex   the ending index (exclusive) of the sublist
   * @return a view of the specified range within this list
   * @throws IndexOutOfBoundsException if {@code fromIndex} or {@code toIndex}
   *                                   is out of range, or if {@code fromIndex} is
   *                                   greater than {@code toIndex}
   */
  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    subListRangeCheck(fromIndex, toIndex, size);

    List<T> subList = new CustomArrayList<>();
    for (int i = fromIndex; i < toIndex; i++) {
      subList.add(get(i));
    }
    return subList;
  }

  /**
   * Performs the given action for each element of the list until all elements have been processed
   * or the action throws an exception.
   *
   * @param action the action to be performed for each element
   * @throws NullPointerException       if the specified action is null
   * @throws ConcurrentModificationException if the list was modified during the iteration
   */
  @Override
  public void forEach(Consumer<? super T> action) {
    Objects.requireNonNull(action);

    final int expectedModCount = modificationCount;
    for (int i = 0; modificationCount == expectedModCount && i < size; i++) {
      action.accept(get(i));
    }
    if (modificationCount != expectedModCount) {
      throw new ConcurrentModificationException();
    }
  }

  /**
   * Returns a string representation of this list.
   *
   * @return a string representation of this list
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(elements[i]);
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Sorts this list according to the order induced by the specified comparator.
   * All elements in the list must be mutually comparable using the specified comparator.
   *
   * @param comparator the comparator to determine the order of the list
   * @throws IllegalArgumentException if the list contains elements that are not mutually comparable
   *                                  using the specified comparator
   */
  @Override
  @SuppressWarnings("unchecked")
  public void sort(Comparator<? super T> comparator) {
    Arrays.sort((T[]) elements, 0, size, comparator);
    modificationCount++;
  }

  /**
   * Sorts this list according to the quicksort method.
   * All elements in the list must implement the {@link Comparable} interface.
   *
   * @throws ClassCastException if the list contains elements that are not mutually comparable
   * @throws UnsupportedOperationException if the elements' natural ordering is found to violate the Comparable contract
   */
  public void quickSort(Comparator<? super T> comparator) {
    quickSort(elements, 0, size - 1, comparator);
    modificationCount++;
  }

  private void quickSort(Object[] arr, int begin, int end, Comparator<? super T> comparator) {
    if (begin < end) {
      int partitionIndex = partition(arr, begin, end, comparator);
      quickSort(arr, begin, partitionIndex - 1, comparator);
      quickSort(arr, partitionIndex + 1, end, comparator);
    }
  }

  @SuppressWarnings("unchecked")
  private int partition(Object[] arr, int begin, int end, Comparator<? super T> comparator) {
    T pivot = (T) arr[end];
    int i = begin - 1;
    for (int j = begin; j < end; j++) {
      if (comparator.compare((T) arr[j], pivot) <= 0) {
        i++;
        swap(arr, i, j);
      }
    }
    swap(arr, i + 1, end);
    return i + 1;
  }

  private void swap(Object[] arr, int i, int j) {
    Object temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  private void ensureCapacity(int minCapacity) {
    if (minCapacity > elements.length) {
      increaseCapacity(minCapacity);
    }
  }

  private void increaseCapacity(int minCapacity) {
    int newCapacity = Math.max(minCapacity, (elements.length * 3) / 2 + 1);
    elements = Arrays.copyOf(elements, newCapacity);
    modificationCount++;
  }

  private void checkIndexForAdd(int index) {
    if (index > size || index < 0) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  static void subListRangeCheck(int fromIndex, int toIndex, int size) {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
    }
    if (toIndex > size) {
      throw new IndexOutOfBoundsException("toIndex = " + toIndex);
    }
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException("fromIndex(" + fromIndex +
          ") > toIndex(" + toIndex + ")");
    }
  }

  /**
   * An iterator over the elements in this list.
   */
  private class CustomArrayListIterator implements ListIterator<T> {
    private int currentIndex;
    private int expectedModCount;

    /**
     * Constructs a new iterator starting at the beginning of the list.
     */
    CustomArrayListIterator() {
      this.currentIndex = 0;
      this.expectedModCount = modificationCount;
    }

    /**
     * Constructs a new iterator starting at the specified index.
     *
     * @param index the index to start the iterator from
     */
    CustomArrayListIterator(int index) {
      this.currentIndex = index;
      this.expectedModCount = modificationCount;
    }

    /**
     * Checks if there is a next element in the iteration.
     *
     * @return true if the iteration has more elements, false otherwise
     * @throws ConcurrentModificationException if the list was modified during iteration
     */
    @Override
    public boolean hasNext() {
      checkForModification();
      return currentIndex < size;
    }

    /**
     * Retrieves the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if there are no more elements in the iteration
     */
    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return get(currentIndex++);
    }

    /**
     * Checks if there is a previous element in the iteration.
     *
     * @return true if the iteration has more previous elements, false otherwise
     * @throws ConcurrentModificationException if the list was modified during iteration
     */
    @Override
    public boolean hasPrevious() {
      checkForModification();
      return currentIndex > 0;
    }

    /**
     * Retrieves the previous element in the iteration.
     *
     * @return the previous element in the iteration
     * @throws NoSuchElementException if there are no more previous elements in the iteration
     */
    @Override
    public T previous() {
      if (!hasPrevious()) {
        throw new NoSuchElementException();
      }
      return get(--currentIndex);
    }

    /**
     * Returns the index of the next element.
     *
     * @return the index of the next element
     */
    @Override
    public int nextIndex() {
      return currentIndex;
    }

    /**
     * Returns the index of the previous element.
     *
     * @return the index of the previous element
     */
    @Override
    public int previousIndex() {
      return currentIndex - 1;
    }

    /**
     * Removes the last element returned by {@code next()} or {@code previous()} from the list.
     *
     * @throws ConcurrentModificationException if the list was modified after the last call to {@code next()}
     *                                       or {@code previous()}
     */
    @Override
    public void remove() {
      checkForModification();
      CustomArrayList.this.remove(currentIndex - 1);
      currentIndex--;
      expectedModCount = modificationCount;
    }

    /**
     * Replaces the last element returned by {@code next()} or {@code previous()} with the specified element.
     *
     * @param element the element to replace the last returned element
     * @throws ConcurrentModificationException if the list was modified after the last call to {@code next()}
     *                                       or {@code previous()}
     */
    @Override
    public void set(T element) {
      checkForModification();
      CustomArrayList.this.set(currentIndex, element);
      expectedModCount = modificationCount;
    }

    /**
     * Inserts the specified element into the list immediately before the next element that would be returned by
     * {@code next()}, if any, and after the next element that would be returned by {@code previous()}, if any.
     *
     * @param element the element to insert
     * @throws ConcurrentModificationException if the list was modified after the last call to {@code next()}
     *                                       or {@code previous()}
     */
    @Override
    public void add(T element) {
      checkForModification();
      CustomArrayList.this.add(currentIndex++, element);
      expectedModCount = modificationCount;
    }

    private void checkForModification() {
      if (modificationCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
}
