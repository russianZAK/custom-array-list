package by.russianzak.customarraylist;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import by.russianzak.customarraylist.CustomArrayList.CustomArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomArrayListTest {

  private static final int LIST_SIZE = 10000;

  private List<Integer> list;

  @Before
  public void setUp() {
    list = new CustomArrayList<>();
  }

  @After
  public void tearDown() {
    list.clear();
  }

  @Test
  public void testEmptyConstructor() {
    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test
  public void testCapacityConstructor() {
    List<Integer> list = new CustomArrayList<>(20);
    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCapacityConstructor() {
    List<Integer> list = new CustomArrayList<>(-10);
  }

  @Test
  public void testAddAndSize() {
    List<Integer> integerList = generateRandomList();
    assertEquals(CustomArrayListTest.LIST_SIZE, integerList.size());
  }

  @Test
  public void testContains() {
    generateList();

    assertTrue(list.contains(0));
    assertTrue(list.contains(999));
    assertFalse(list.contains(1000));
  }

  @Test
  public void testIndexOf() {
    generateList();

    assertEquals(0, list.indexOf(0));
    assertEquals(999, list.indexOf(999));
    assertEquals(-1, list.indexOf(1000));
  }

  @Test
  public void testLastIndexOf() {
    generateList();

    list.add(500);

    assertEquals(1000, list.lastIndexOf(500));
    assertEquals(-1, list.lastIndexOf(1000));
  }

  @Test
  public void testToArray() {
    generateList();

    Object[] array = list.toArray();
    assertEquals(1000, array.length);

    for (int i = 0; i < 1000; i++) {
      assertEquals(i, array[i]);
    }
  }

  @Test
  public void testToArrayWithParameter() {
    generateList();

    Integer[] array = list.toArray(new Integer[0]);
    assertEquals(1000, array.length);

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), array[i]);
    }
  }

  @Test
  public void testToArrayWithParameterLarger() {
    generateList();

    Integer[] array = list.toArray(new Integer[1500]);
    assertEquals(1500, array.length);

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), array[i]);
    }
    for (int i = 1000; i < 1500; i++) {
      assertNull(array[i]);
    }
  }

  @Test
  public void testGet() {
    generateList();

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), list.get(i));
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetOutOfBounds() {
    List<String> list = new CustomArrayList<>();
    list.get(0);
  }

  @Test
  public void testSet() {
    generateList();

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), list.set(i, i * 2));
    }

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i * 2), list.get(i));
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetOutOfBounds() {
    List<String> list = new CustomArrayList<>();
    list.set(-1, "Hello");
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSetOutOfBoundsEmptyList() {
    list.set(0, 717);
  }

  @Test
  public void testAdd() {
    for (int i = 0; i < 1000; i++) {
      assertTrue(list.add(i));
      assertEquals(i + 1, list.size());
      assertEquals(Integer.valueOf(i), list.get(i));
    }
  }

  @Test
  public void testAddWithIndex() {
    generateList();

    list.add(500, 10000);

    assertEquals(1001, list.size());
    assertEquals(Integer.valueOf(10000), list.get(500));
    assertEquals(Integer.valueOf(499), list.get(499));
    assertEquals(Integer.valueOf(500), list.get(501));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddWithIndexOutOfBounds() {
    list.add(1, 717);
  }

  @Test
  public void testAddAll() {
    List<Integer> toAdd = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      toAdd.add(i);
    }

    assertTrue(list.addAll(toAdd));
    assertEquals(1000, list.size());

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), list.get(i));
    }
  }

  @Test
  public void testAddAllWithIndex() {
    list.add(100);
    list.add(200);

    List<Integer> toAdd = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      toAdd.add(i);
    }

    assertTrue(list.addAll(1, toAdd));

    assertEquals(1002, list.size());
    assertEquals(Integer.valueOf(100), list.get(0));
    assertEquals(Integer.valueOf(0), list.get(1));
    assertEquals(Integer.valueOf(1), list.get(2));
    assertEquals(Integer.valueOf(200), list.get(1001));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testAddAllWithIndexOutOfBounds() {
    List<Integer> toAdd = Arrays.asList(1, 2);
    list.addAll(1, toAdd);
  }

  @Test
  public void testRemoveObject() {
    generateList();

    List<Integer> elementsToRemove = new ArrayList<>();
    for (int i = 0; i < 1000; i += 2) {
      elementsToRemove.add(i);
    }

    for (Integer element : elementsToRemove) {
      assertTrue(list.remove(element));
    }

    assertEquals(500, list.size());
    for (int i = 0; i < 1000; i++) {
      if (i % 2 == 0) {
        assertFalse(list.contains(i));
      } else {
        assertTrue(list.contains(i));
      }
    }
  }

  @Test
  public void testRemoveObjectNotFound() {
    CustomArrayList<String> list1 = new CustomArrayList<>();
    list1.add("Hello");
    list1.add("World");

    assertFalse(list1.remove("Java"));
    assertEquals(2, list1.size());
  }

  @Test
  public void testRemoveIndex() {
    generateList();

    assertEquals(Integer.valueOf(500), list.remove(500));
    assertEquals(999, list.size());
    assertFalse(list.contains(500));
    for (int i = 0; i < 999; i++) {
      if (i < 500) {
        assertEquals(Integer.valueOf(i), list.get(i));
      } else {
        assertEquals(Integer.valueOf(i + 1), list.get(i));
      }
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemoveIndexOutOfBounds() {
    list.remove(0);
  }

  @Test
  public void testRemoveAll() {
    generateList();

    List<Integer> toRemove = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      toRemove.add(i);
    }

    assertTrue(list.removeAll(toRemove));
    assertEquals(500, list.size());
    for (int i = 0; i < 1000; i++) {
      if (i < 500) {
        assertFalse(list.contains(i));
      } else {
        assertTrue(list.contains(i));
      }
    }
  }

  @Test
  public void testRemoveAllNotFound() {
    generateList();

    List<Integer> toRemove = new ArrayList<>();
    for (int i = 1000; i < 1500; i++) {
      toRemove.add(i);
    }

    assertFalse(list.removeAll(toRemove));
    assertEquals(1000, list.size());
  }

  @Test(expected = NullPointerException.class)
  public void testRemoveAllNullCollection() {
    list.removeAll(null);
  }

  @Test
  public void testRetainAll() {
    generateList();

    List<Integer> toRetain = new ArrayList<>();
    for (int i = 500; i < 1500; i++) {
      toRetain.add(i);
    }

    assertTrue(list.retainAll(toRetain));
    assertEquals(500, list.size());

    for (int i = 0; i < 500; i++) {
      assertTrue(list.contains(i + 500));
    }
  }

  @Test
  public void testRetainAllEmptyCollection() {
    List<String> stringList = new CustomArrayList<>();
    stringList.add("Hello");
    stringList.add("World");

    List<String> toRetain = List.of();

    assertTrue(stringList.retainAll(toRetain));
    assertEquals(0, stringList.size());
  }

  @Test
  public void testRetainAllNoCommonElements() {
    List<String> stringList = new CustomArrayList<>();
    stringList.add("Hello");
    stringList.add("World");

    List<String> toRetain = Arrays.asList("Java", "Python");

    assertTrue(stringList.retainAll(toRetain));
    assertEquals(0, stringList.size());
  }

  @Test
  public void testRetainAllNoModification() {
    List<String> stringList = new CustomArrayList<>();

    stringList.add("Hello");
    stringList.add("World");

    List<String> toRetain = Arrays.asList("Hello", "World");

    assertFalse(stringList.retainAll(toRetain));
    assertEquals(2, stringList.size());
  }

  @Test(expected = NullPointerException.class)
  public void testRetainAllNullCollection() {
    list.retainAll(null);
  }

  @Test
  public void testEqualsSameInstance() {
    assertEquals(list, list);
  }

  @Test
  public void testEqualsDifferentInstancesSameElements() {
    List<String> list1 = new CustomArrayList<>();
    list1.add("Hello");
    list1.add("World");

    List<String> list2 = new ArrayList<>();
    list2.add("Hello");
    list2.add("World");

    assertEquals(list1, list2);
  }

  @Test
  public void testEqualsDifferentInstancesDifferentElements() {
    List<String> list1 = new CustomArrayList<>();
    list1.add("Hello");
    list1.add("World");

    List<String> list2 = new ArrayList<>();
    list2.add("Java");
    list2.add("Python");

    assertNotEquals(list1, list2);
  }

  @Test
  public void testEqualsDifferentSize() {
    List<String> list1 = new CustomArrayList<>();
    list1.add("Hello");
    list1.add("World");

    List<String> list2 = new ArrayList<>();
    list2.add("Hello");

    assertNotEquals(list1, list2);
  }

  @Test
  public void testNotNull() {
    assertNotNull(list);
  }

  @Test
  public void testEqualsDifferentType() {
    assertNotEquals("Hello", list);
  }

  @Test
  public void testEqualsDifferentTypeNull() {
    assertNotEquals(null, list);
  }

  @Test
  public void testEqualsEmptyLists() {
    List<String> list1 = new CustomArrayList<>();
    List<String> list2 = new ArrayList<>();
    assertEquals(list1, list2);
  }

  @Test
  public void testEqualsNullElement() {
    List<String> list1 = new CustomArrayList<>();
    list1.add(null);
    List<String> list2 = new ArrayList<>();
    list2.add(null);
    assertEquals(list1, list2);
  }

  @Test
  public void testEqualsDifferentOrder() {
    List<String> list1 = new CustomArrayList<>();
    list1.add("Hello");
    list1.add("World");

    List<String> list2 = new ArrayList<>();
    list2.add("World");
    list2.add("Hello");

    assertNotEquals(list1, list2);
  }

  @Test
  public void testClear() {
    list.add(1);
    list.add(2);

    list.clear();

    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test
  public void testClearEmptyList() {
    list.clear();

    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test
  public void testClearNullElements() {
    list.add(null);
    list.add(null);

    list.clear();

    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test
  public void testClearAfterAddAll() {
    list.addAll(Arrays.asList(1, 2));

    list.clear();

    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test
  public void testClearAfterRemove() {
    list.add(1);
    list.add(2);
    list.remove(1);

    list.clear();

    assertEquals(0, list.size());
    assertTrue(list.isEmpty());
  }

  @Test
  public void testSubList() {
    generateList();

    List<Integer> subList = list.subList(200, 700);
    assertEquals(500, subList.size());

    for (int i = 0; i < 500; i++) {
      assertEquals(Integer.valueOf(i + 200), subList.get(i));
    }
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSubListInvalidRange() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.subList(1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSubListInvalidIndices() {
    list.add(1);
    list.add(2);
    list.add(3);
    list.subList(3, 1);
  }

  @Test
  public void testForEach() {
    generateList();

    List<Integer> resultList = new ArrayList<>();
    list.forEach(resultList::add);

    assertEquals(1000, resultList.size());
    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), resultList.get(i));
    }
  }

  @Test
  public void testForEachEmptyList() {
    List<Integer> resultList = new ArrayList<>();
    list.forEach(resultList::add);

    assertEquals(0, resultList.size());
  }

  @Test(expected = NullPointerException.class)
  public void testForEachNullConsumer() {
    list.add(1);
    list.add(2);

    list.forEach(null);
  }

  @Test
  public void testSort() {
    List<Integer> list = generateRandomList();
    list.sort(Comparator.naturalOrder());
    assertArrayEquals(list.toArray(), list.stream().sorted().toArray());
  }

  @Test
  public void testQuickSort() {
    CustomArrayList<Integer> list = generateRandomList();
    list.quickSort(Comparator.naturalOrder());
    assertArrayEquals(list.toArray(), list.stream().sorted().toArray());
  }

  @Test()
  public void testIteratorOperations() {
    list.add(1);
    list.add(2);
    list.add(3);

    Iterator<Integer> iterator = list.iterator();

    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(1), iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(2), iterator.next());

    assertTrue(iterator.hasNext());
    assertEquals(Integer.valueOf(3), iterator.next());

    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
  public void testIteratorRemove() {
    list.add(1);
    list.add(2);
    list.add(3);

    Iterator<Integer> iterator = list.iterator();

    iterator.next();
    iterator.remove();

    assertEquals(2, list.size());
    assertEquals(Integer.valueOf(2), list.get(0));

    iterator.next();
    iterator.remove();

    assertEquals(1, list.size());
    assertEquals(Integer.valueOf(3), list.get(0));
  }

  @Test()
  public void testAddAfterIterator() {
    list.add(1);
    list.add(2);
    list.add(3);

    Iterator<Integer> iterator = list.iterator();
    assertTrue(iterator.hasNext());

    list.add(4);
    assertThrows(ConcurrentModificationException.class, iterator::next);
  }

  @Test()
  public void testRemoveAfterIterator() {
    list.add(1);
    list.add(2);
    list.add(3);

    Iterator<Integer> iterator = list.iterator();
    assertTrue(iterator.hasNext());

    list.remove(1);
    assertThrows(ConcurrentModificationException.class, iterator::next);
  }

  @Test()
  public void testRemoveWithIterator() {
    list.add(1);
    list.add(2);
    list.add(3);

    Iterator<Integer> iterator = list.iterator();
    assertTrue(iterator.hasNext());

    iterator.next();
    iterator.remove();

    assertEquals(2, list.size());
    assertEquals(Integer.valueOf(2), list.get(0));
    assertEquals(Integer.valueOf(3), list.get(1));
  }

  @Test
  public void testListIterator() {
    list.add(1);
    list.add(2);
    list.add(3);

    ListIterator<Integer> iterator = list.listIterator();

    assertFalse(iterator.hasPrevious());
    assertTrue(iterator.hasNext());

    assertEquals(Integer.valueOf(1), iterator.next());
    assertTrue(iterator.hasNext());

    assertEquals(Integer.valueOf(2), iterator.next());
    assertTrue(iterator.hasNext());

    assertEquals(Integer.valueOf(3), iterator.next());
    assertFalse(iterator.hasNext());

    assertTrue(iterator.hasPrevious());
    assertEquals(Integer.valueOf(3), iterator.previous());

    assertEquals(Integer.valueOf(2), iterator.previous());
    assertEquals(Integer.valueOf(1), iterator.previous());

    assertFalse(iterator.hasPrevious());
  }

  @Test
  public void testAddAndAccessWithLoop() {
    generateList();

    int index = 0;
    for (Integer element : list) {
      assertEquals(Integer.valueOf(index), element);
      index++;
    }
  }

  @Test
  public void testRemoveAndAccessWithLoop() {
    generateList();

    list.removeIf(element -> element % 2 == 0);

    int index = 1;
    for (Integer element : list) {
      assertEquals(Integer.valueOf(index), element);
      index += 2;
    }
  }

  @Test
  public void testIterationOnEmptyList() {
    assertFalse(list.iterator().hasNext());
  }

  @Test(expected = ConcurrentModificationException.class)
  public void testConcurrentModificationException() {
    generateList();

    for (int element : list) {
      if (element == 5) {
        list.remove(Integer.valueOf(element));
      }
    }
  }

  @Test
  public void testAddWithIndexAndAccessWithLoop() {
    for (int i = 0; i < 1000; i++) {
      list.add(i, i);
    }

    for (int i = 0; i < 1000; i++) {
      assertEquals(Integer.valueOf(i), list.get(i));
    }
  }

  @Test
  public void testRemoveAllElements() {
    generateList();

    list.clear();

    assertTrue(list.isEmpty());
  }

  @Test
  public void testSetElementsAndAccessWithLoop() {
    for (int i = 0; i < 500; i++) {
      list.add(i);
    }

    for (int i = 0; i < 500; i++) {
      list.set(i, i * 2);
    }

    for (int i = 0; i < 500; i++) {
      assertEquals(Integer.valueOf(i * 2), list.get(i));
    }
  }

  @Test
  public void testIterationOverSubList() {
    generateList();

    List<Integer> subList = list.subList(37, 354);
    int index = 37;
    for (Integer element : subList) {
      assertEquals(Integer.valueOf(index), element);
      index++;
    }
  }

  @Test
  public void testIteratorForwardTraversal() {
    generateList();

    Iterator<Integer> iterator = list.iterator();
    int expectedValue = 0;
    while (iterator.hasNext()) {
      assertEquals(Integer.valueOf(expectedValue), iterator.next());
      expectedValue++;
    }
  }

  @Test
  public void testIteratorRemoval() {
    for (int i = 0; i < 5; i++) {
      list.add(i);
    }

    list.removeIf(integer -> integer % 2 == 0);

    assertEquals(Arrays.asList(1, 3), list);
  }

  @Test
  public void testIteratorBackwardTraversal() {
    for (int i = 0; i < 5; i++) {
      list.add(i);
    }

    ListIterator<Integer> iterator = list.listIterator(list.size());
    int expectedValue = 4;
    while (iterator.hasPrevious()) {
      assertEquals(Integer.valueOf(expectedValue), iterator.previous());
      expectedValue--;
    }
  }

  private void generateList() {
    for (int i = 0; i < 1000; i++) {
      list.add(i);
    }
  }

  private CustomArrayList<Integer> generateRandomList() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    for (int i = 0; i < CustomArrayListTest.LIST_SIZE; i++) {
      list.add((int) (Math.random() * CustomArrayListTest.LIST_SIZE));
    }
    return list;
  }
}
