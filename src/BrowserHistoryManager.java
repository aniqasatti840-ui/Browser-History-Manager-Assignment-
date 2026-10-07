import java.util.List;
import java.util.Scanner;

/**
 * Console-based Browser History Manager.
 * Linked list -> stores all visited pages (add, delete, search, sort)
 * Two stacks  -> Back stack and Forward stack for navigation
 */
public class BrowserHistoryManager {
    private final HistoryLinkedList history = new HistoryLinkedList();
    private final PageStack backStack = new PageStack(10);
    private final PageStack forwardStack = new PageStack(10);
    private Page currentPage = null;
    private final Scanner scanner = new Scanner(System.in);
    private int nextId = 1;

    public static void main(String[] args) {
        new BrowserHistoryManager().run();
    }

    private void run() {
        System.out.println("=========================================");
        System.out.println("        BROWSER HISTORY MANAGER");
        System.out.println("=========================================");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ", 0, 10);
            System.out.println();
            switch (choice) {
                case 1: visitPage(); break;
                case 2: goBack(); break;
                case 3: goForward(); break;
                case 4: showCurrentPage(); break;
                case 5: viewHistory(); break;
                case 6: searchHistory(); break;
                case 7: sortHistory(); break;
                case 8: deleteHistoryEntry(); break;
                case 9: viewStacks(); break;
                case 10: clearHistory(); break;
                case 0:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("------------------ MENU -----------------");
        System.out.println(" 1. Visit a new page");
        System.out.println(" 2. Back");
        System.out.println(" 3. Forward");
        System.out.println(" 4. Show current page");
        System.out.println(" 5. View history");
        System.out.println(" 6. Search history");
        System.out.println(" 7. Sort history (A-Z)");
        System.out.println(" 8. Delete a history entry");
        System.out.println(" 9. View Back/Forward stacks");
        System.out.println("10. Clear all history");
        System.out.println(" 0. Exit");
        System.out.println("-----------------------------------------");
    }

    // ------------------------------------------------------------------
    // FEATURES
    // ------------------------------------------------------------------

    /** Visiting a new page: add to the history list, push old page on Back, clear Forward. */
    private void visitPage() {
        String url = readUrl("Enter URL (e.g. www.google.com): ");
        String title = readNonEmptyString("Enter page title: ", 40);

        Page page = new Page(nextId++, title, url);
        history.addLast(page);

        if (currentPage != null) {
            backStack.push(currentPage);
        }
        forwardStack.clear();          // a new visit erases the forward history
        currentPage = page;

        System.out.println("Visited: " + page.shortInfo());
    }

    private void goBack() {
        if (backStack.isEmpty()) {
            System.out.println("Cannot go back: no previous page.");
            return;
        }
        forwardStack.push(currentPage);
        currentPage = backStack.pop();
        System.out.println("Went back to: " + currentPage.shortInfo());
    }

    private void goForward() {
        if (forwardStack.isEmpty()) {
            System.out.println("Cannot go forward: no next page.");
            return;
        }
        backStack.push(currentPage);
        currentPage = forwardStack.pop();
        System.out.println("Went forward to: " + currentPage.shortInfo());
    }

    private void showCurrentPage() {
        if (currentPage == null) {
            System.out.println("No page is open. Visit a page first.");
        } else {
            System.out.println("Current page: " + currentPage.shortInfo());
        }
    }

    private void viewHistory() {
        System.out.println("Total pages in history: " + history.size());
        history.display();
    }

    private void searchHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty, nothing to search.");
            return;
        }
        String keyword = readNonEmptyString("Enter keyword (title or URL): ", 40);
        List<Page> matches = history.search(keyword);
        if (matches.isEmpty()) {
            System.out.println("No pages match \"" + keyword + "\".");
        } else {
            System.out.println(matches.size() + " page(s) found:");
            HistoryLinkedList.printHeader();
            for (Page p : matches) System.out.println(p);
            HistoryLinkedList.printLine();
        }
    }

    private void sortHistory() {
        if (history.size() < 2) {
            System.out.println("Need at least 2 pages in history to sort.");
            return;
        }
        System.out.println(" 1. Sort by title (A-Z)");
        System.out.println(" 2. Sort by URL (A-Z)");
        int option = readInt("Choose sort type: ", 1, 2);
        history.sort(option == 1 ? HistoryLinkedList.SORT_BY_TITLE : HistoryLinkedList.SORT_BY_URL);
        System.out.println("History sorted by " + (option == 1 ? "title." : "URL."));
        history.display();
    }

    private void deleteHistoryEntry() {
        if (history.isEmpty()) {
            System.out.println("History is empty, nothing to delete.");
            return;
        }
        int id = readInt("Enter ID of the history entry to delete: ", 1, Integer.MAX_VALUE);
        if (history.removeById(id)) {
            System.out.println("History entry #" + id + " deleted.");
        } else {
            System.out.println("No history entry found with ID " + id + ".");
        }
    }

    private void viewStacks() {
        System.out.println("Current page: " + (currentPage == null ? "(none)" : currentPage.shortInfo()));
        System.out.println();
        System.out.println("  BACK stack (" + backStack.size() + "):");
        backStack.display();
        System.out.println("  FORWARD stack (" + forwardStack.size() + "):");
        forwardStack.display();
    }

    private void clearHistory() {
        if (history.isEmpty() && currentPage == null) {
            System.out.println("History is already empty.");
            return;
        }
        history.clear();
        backStack.clear();
        forwardStack.clear();
        currentPage = null;
        System.out.println("All history cleared. Back and Forward stacks are empty.");
    }

    // ------------------------------------------------------------------
    // INPUT VALIDATION HELPERS
    // ------------------------------------------------------------------

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
                System.out.println("  Please enter a number between " + min + " and "
                        + (max == Integer.MAX_VALUE ? "a larger value" : String.valueOf(max)) + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a whole number.");
            }
        }
    }

    private String readNonEmptyString(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("  Input cannot be empty.");
            } else if (line.length() > maxLength) {
                System.out.println("  Too long. Maximum " + maxLength + " characters.");
            } else {
                return line;
            }
        }
    }

    /** A URL must not contain spaces and must contain a dot (e.g. www.google.com). */
    private String readUrl(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("  URL cannot be empty.");
            } else if (line.contains(" ") || !line.contains(".")) {
                System.out.println("  Invalid URL. Use a format like www.example.com (no spaces).");
            } else if (line.length() > 60) {
                System.out.println("  URL too long. Maximum 60 characters.");
            } else {
                return line;
            }
        }
    }
}
