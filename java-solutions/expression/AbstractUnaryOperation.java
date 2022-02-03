package expression;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class AbstractUnaryOperation extends AbstractExpression implements UnOperation {
    private final CommonExp expression;

    protected AbstractUnaryOperation(CommonExp expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate(int x) {
        return makeOp(expression.evaluate(x));
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        throw new AssertionError("Can't evaluate UnaryOperation(BigDecimal)");
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOp(expression.evaluate(x, y, z));
    }

    @Override
    public BigDecimal makeOp(BigDecimal x) {
        throw new AssertionError("Can't evaluate UnaryOperation(BigDecimal)");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractUnaryOperation exp) {
            return expression.equals(exp.expression) && getOperator().equals(exp.getOperator());
        }
        return false;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(getOperator()).append("(");
        expression.toString(sb);
        sb.append(")");
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(getOperator());
        if (expression instanceof AbstractVarConst) {
            sb.append(" ");
            expression.toMiniString(sb);
        } else {
            sb.append("(");
            expression.toMiniString(sb);
            sb.append(")");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, getOperator());
    }
}
