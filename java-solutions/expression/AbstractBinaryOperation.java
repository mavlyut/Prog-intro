package expression;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class AbstractBinaryOperation extends AbstractExpression implements BinOperation {
    // priority
    //      1 l0 t0 unMinus
    //      2 + -
    //      3 * /
    //      4 >> << >>>
    private final CommonExp expression1;
    private final CommonExp expression2;

    protected AbstractBinaryOperation(CommonExp expression1, CommonExp expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public int evaluate(int x) {
        return makeOp(expression1.evaluate(x), expression2.evaluate(x));
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return makeOp(expression1.evaluate(x), expression2.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOp(expression1.evaluate(x, y, z), expression2.evaluate(x, y, z));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractBinaryOperation exp) {
            return expression1.equals(exp.expression1)
                    && this.getClass().equals(obj.getClass())
                    && expression2.equals(exp.expression2);
        }
        return false;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append("(");
        expression1.toString(sb);
        sb.append(" ").append(getOperator()).append(" ");
        expression2.toString(sb);
        sb.append(")");
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        toMiniString(expression1, needBrackets1(expression1), sb);
        sb.append(" ").append(getOperator()).append(" ");
        toMiniString(expression2, needBrackets1(expression2) || needBrackets2(expression2), sb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression1, expression2, getOperator());
    }

    private boolean needBrackets1(CommonExp expression) {
        if (expression instanceof AbstractBinaryOperation expr) {
            return expr.getPriority() > getPriority();
        }
        return false;
    }

    private void toMiniString(CommonExp expression, boolean brackets, StringBuilder sb) {
        sb.append(brackets ? "(" : "") ;
        expression.toMiniString(sb);
        sb.append(brackets ? ")" : "");
    }

    private boolean needBrackets2(CommonExp expression) {
        if (!(expression instanceof AbstractBinaryOperation exp)) {
            return false;
        }
        int expPriority = exp.getPriority();
        int thisPriority = getPriority();
        return (exp.getOperator().equals("/") || getOperator().equals("-")) && thisPriority == expPriority
                || getOperator().equals("/");
    }
}
