package expression;

public interface CommonExp extends BigDecimalExpression, Expression, TripleExpression, ToMiniString {
    void toString(StringBuilder sb);
    void toMiniString(StringBuilder sb);
}
