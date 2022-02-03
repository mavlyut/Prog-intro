package expression.parser;

public class BaseParser {
    private static final char END = 0;

    private final CharSource source;
    private char ch;

    public BaseParser(CharSource source) {
        this.source = source;
        take();
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error(String.format(
                    "Expected '%s', found '%s'",
                    expected, ch != END ? ch : "end-of-input"
            ));
        }
    }

    protected IllegalArgumentException error(String message) {
        return source.error(message);
    }

    protected void expect(final String expected) {
        for (final char c : expected.toCharArray()) {
            expect(c);
        }
    }

    protected boolean end() {
        return test(END);
    }

    protected boolean between(final char min, final char max) {
        return min <= ch && ch <= max;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }
}
