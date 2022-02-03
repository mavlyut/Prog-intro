package expression;

public abstract class AbstractExpression implements CommonExp {
    private final StringBuilder toStr = new StringBuilder();
    private final StringBuilder toMiniStr = new StringBuilder();

    @Override
    public String toString() {
        if (toStr.isEmpty()) {
            toString(toStr);
        }
        return toStr.toString();
    }

    @Override
    public String toMiniString() {
        if (toMiniStr.isEmpty()) {
            toMiniString(toMiniStr);
        }
        return toMiniStr.toString();
    }
}
