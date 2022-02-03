package expression.parser;

public interface CharSource {
    char next();
    boolean hasNext();
    IllegalArgumentException error(final String message);
}
