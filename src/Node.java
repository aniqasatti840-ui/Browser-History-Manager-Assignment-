/** A node of the singly linked list. It holds one Page and a link to the next node. */
public class Node {
    Page data;
    Node next;

    public Node(Page data) {
        this.data = data;
        this.next = null;
    }
}
