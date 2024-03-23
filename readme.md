# CustomArrayList

`CustomArrayList` is a custom implementation of the `List` interface, providing a dynamic array-based data structure.

## Key Features:

- Addition, removal, and retrieval of elements
- Checking for element presence in the list
- Retrieving the size of the list
- Iteration through the list elements
- Sorting of the list (quicksort)
- Creating sublists
- Efficient resizing of the underlying array
- Support for adding elements at specific positions
- Support for bulk operations such as adding collections
- Ability to remove elements by value or index
- Clearing the list of all elements
- Providing a string representation of the list
- Ensuring fail-fast behavior to detect concurrent modifications

## Key Methods:

- `add(T element)`: Adds an element to the end of the list.
- `remove(int index)`: Removes an element at the specified index.
- `get(int index)`: Returns the element at the specified index.
- `size()`: Returns the size of the list.
- `iterator()`: Returns an iterator to traverse the list elements.

## Usage:

```java
List<Integer> list = new CustomArrayList<>();
list.add(5);
list.add(10);
System.out.println(list); // [5, 10]
```