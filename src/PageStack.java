
public class PageStack {
    private Page[] items;
    private int top;   // index of the top element, -1 when empty

    public PageStack(int capacity) {
        items = new Page[capacity];
        top = -1;
    }

    public boolean isEmpty() { return top == -1; }
    public boolean isFull()  { return top == items.length - 1; }
    public int size()        { return top + 1; }

    /** Pushes a page on top. Time: O(1) amortized (O(n) only when the array grows). */
    public void push(Page page) {
        if (isFull()) {
            resize();
        }
        items[++top] = page;
    }

    /** Removes and returns the top page, or null if the stack is empty. Time: O(1). */
    public Page pop() {
        if (isEmpty()) return null;
        Page page = items[top];
        items[top--] = null;
        return page;
    }

    /** Returns the top page without removing it. Time: O(1). */
    public Page peek() {
        if (isEmpty()) return null;
        return items[top];
    }

    /** Empties the stack. Time: O(n). */
    public void clear() {
        while (!isEmpty()) {
            items[top--] = null;
        }
    }

    private void resize() {
        Page[] bigger = new Page[items.length * 2];
        for (int i = 0; i < items.length; i++) {
            bigger[i] = items[i];
        }
        items = bigger;
    }

    /** Prints the stack from top to bottom. */
    public void display() {
        if (isEmpty()) {
            System.out.println("    (empty)");
            return;
        }
        for (int i = top; i >= 0; i--) {
            String marker = (i == top) ? "   <-- top" : "";
            System.out.println("    " + items[i].shortInfo() + marker);
        }
    }
}
