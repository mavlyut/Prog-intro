package md2html;

public class Node {
    private final String tag;
    private final String open;
    private final String close;

    public Node(String tag, String open, String close) {
        this.tag = tag;
        this.open = open;
        this.close = close;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public String getTag() {
        return tag;
    }
}
