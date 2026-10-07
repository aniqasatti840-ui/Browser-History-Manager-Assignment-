import java.util.ArrayList;
import java.util.List;

/**
 * Singly linked list (head + tail pointers) that stores the visited pages.
 * Supports add, delete, linear search and merge sort.
 */
public class HistoryLinkedList {
    public static final int SORT_BY_TITLE = 1;
    public static final int SORT_BY_URL = 2;

    private Node head;
    private Node tail;
    private int size;

    public HistoryLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    // ------------------------------------------------------------------
    // ADD
    // ------------------------------------------------------------------

    /** Adds a visited page at the end of the list. Time: O(1) because of the tail pointer. */
    public void addLast(Page page) {
        Node newNode = new Node(page);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // ------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------

    /** Deletes the entry with the given id. Returns true if it was found. Time: O(n). */
    public boolean removeById(int id) {
        Node prev = null;
        Node current = head;
        while (current != null) {
            if (current.data.getId() == id) {
                if (prev == null) {
                    head = current.next;          // deleting the first node
                } else {
                    prev.next = current.next;
                }
                if (current == tail) {
                    tail = prev;                  // deleting the last node
                }
                current.next = null;              // detach the removed node completely
                current.data = null;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /** Deletes every node one by one. Time: O(n). */
    public void clear() {
        Node current = head;
        while (current != null) {
            Node next = current.next;
            current.next = null;
            current.data = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
    }

    // ------------------------------------------------------------------
    // SEARCH (Linear Search)
    // ------------------------------------------------------------------

    /** Linear search: returns all pages whose title or URL contains the keyword. Time: O(n * m). */
    public List<Page> search(String keyword) {
        List<Page> matches = new ArrayList<>();
        String key = keyword.toLowerCase();
        Node current = head;
        while (current != null) {
            Page p = current.data;
            if (p.getTitle().toLowerCase().contains(key) || p.getUrl().toLowerCase().contains(key)) {
                matches.add(p);
            }
            current = current.next;
        }
        return matches;
    }

    // ------------------------------------------------------------------
    // SORT (Merge Sort on a linked list)
    // ------------------------------------------------------------------

    /** Sorts the history A-Z by title or by URL. Stable merge sort. Time: O(n log n). */
    public void sort(int sortBy) {
        head = mergeSort(head, sortBy);
        tail = head;                              // fix the tail pointer after sorting
        while (tail != null && tail.next != null) {
            tail = tail.next;
        }
    }

    private Node mergeSort(Node start, int sortBy) {
        if (start == null || start.next == null) return start;

        Node slow = start;                        // find the middle with slow/fast pointers
        Node fast = start.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node secondHalf = slow.next;
        slow.next = null;

        Node left = mergeSort(start, sortBy);
        Node right = mergeSort(secondHalf, sortBy);
        return merge(left, right, sortBy);
    }

    private Node merge(Node a, Node b, int sortBy) {
        Node dummy = new Node(null);
        Node current = dummy;
        while (a != null && b != null) {
            if (compare(a.data, b.data, sortBy) <= 0) {   // <= keeps the sort stable
                current.next = a;
                a = a.next;
            } else {
                current.next = b;
                b = b.next;
            }
            current = current.next;
        }
        current.next = (a != null) ? a : b;
        return dummy.next;
    }

    private int compare(Page x, Page y, int sortBy) {
        if (sortBy == SORT_BY_URL) {
            return x.getUrl().compareToIgnoreCase(y.getUrl());
        }
        return x.getTitle().compareToIgnoreCase(y.getTitle());
    }

    // ------------------------------------------------------------------
    // DISPLAY
    // ------------------------------------------------------------------

    public void display() {
        if (isEmpty()) {
            System.out.println("  (History is empty)");
            return;
        }
        printHeader();
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
        printLine();
    }

    public static void printHeader() {
        printLine();
        System.out.println("| ID   | Title                  | URL                            | Time     |");
        printLine();
    }

    public static void printLine() {
        System.out.println("+------+------------------------+--------------------------------+----------+");
    }
}
