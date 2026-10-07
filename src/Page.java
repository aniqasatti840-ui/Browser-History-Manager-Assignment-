import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Represents one visited web page (one entry in the browsing history). */
public class Page {
    private final int id;
    private final String title;
    private final String url;
    private final String visitTime;

    public Page(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.visitTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getVisitTime() { return visitTime; }

    /** One-line summary used for the Back/Forward stack display. */
    public String shortInfo() {
        return "#" + id + " " + title + " (" + url + ")";
    }

    @Override
    public String toString() {
        return String.format("| %-4d | %-22s | %-30s | %-8s |", id, cut(title, 22), cut(url, 30), visitTime);
    }

    private static String cut(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 2) + "..";
    }
}
