# ConcurrentModificationException in Java Collections

## What is ConcurrentModificationException?
`ConcurrentModificationException` is a runtime exception in Java that occurs when a collection (such as a List, Set, or Map) is structurally modified while iterating over it using methods other than the iterator's own `remove()` method. This is a fail-fast behavior to prevent unpredictable results during iteration.

---

## When Does It Occur?
- When you modify (add/remove) elements from a collection while iterating over it using an iterator or enhanced for-loop, **except** through the iterator's `remove()` method.
- Applies to most implementations of `List`, `Set`, and `Map` in the Java Collections Framework (e.g., `ArrayList`, `HashSet`, `HashMap`).

---

## Examples

### 1. List
#### a) Index-based for loop (No Exception)
```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
for (int i = 0; i < list.size(); i++) {
    if (list.get(i) == 3) {
        list.remove(i); // No exception, but may skip elements
    }
}
```
- **No exception** because no iterator is used, but logic errors (skipped elements) can occur.

#### b) Enhanced for-loop (Exception)
```java
for (Integer val : list) {
    if (val == 3) {
        list.remove(val); // Throws ConcurrentModificationException
    }
}
```
- **Throws exception** because the underlying iterator detects modification.

#### c) Safe removal using Iterator
```java
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() == 3) {
        it.remove(); // Safe
    }
}
```

---

### 2. Set
#### a) Enhanced for-loop (Exception)
```java
Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
for (Integer val : set) {
    if (val == 3) {
        set.remove(val); // Throws ConcurrentModificationException
    }
}
```

#### b) Safe removal using Iterator
```java
Iterator<Integer> it = set.iterator();
while (it.hasNext()) {
    if (it.next() == 3) {
        it.remove(); // Safe
    }
}
```

---

### 3. Map
#### a) Enhanced for-loop over entrySet (Exception)
```java
Map<String, Integer> map = new HashMap<>();
// ... populate map ...
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    map.remove(entry.getKey()); // Throws ConcurrentModificationException
}
```

#### b) Safe removal using Iterator
```java
Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> entry = it.next();
    if (entry.getValue() == 3) {
        it.remove(); // Safe
    }
}
```

---

## Why Does This Happen?
- Java's collection iterators are **fail-fast**: they track structural modifications (add/remove) via a modification count.
- If the collection is modified outside the iterator during iteration, the iterator detects this and throws `ConcurrentModificationException`.

---

## How to Avoid ConcurrentModificationException
- **Never modify a collection directly while iterating with an iterator or enhanced for-loop.**
- Use the iterator's `remove()` method for safe removal.
- For bulk removals, consider using `removeIf()` (Java 8+):
  ```java
  list.removeIf(val -> val == 3);
  set.removeIf(val -> val == 3);
  map.entrySet().removeIf(entry -> entry.getValue() == 3);
  ```
- For concurrent modifications from multiple threads, use concurrent collections like `CopyOnWriteArrayList`, `ConcurrentHashMap`, etc.

---

## Summary Table
| Collection | Unsafe Removal (Exception)         | Safe Removal (No Exception)         |
|------------|------------------------------------|-------------------------------------|
| List       | Enhanced for-loop, iterator + direct remove | Iterator's remove(), removeIf()    |
| Set        | Enhanced for-loop, iterator + direct remove | Iterator's remove(), removeIf()    |
| Map        | Enhanced for-loop, iterator + direct remove | Iterator's remove(), removeIf()    |

---

## References
- [Java Docs: ConcurrentModificationException](https://docs.oracle.com/javase/8/docs/api/java/util/ConcurrentModificationException.html)
- [Java Docs: Iterator](https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html) 