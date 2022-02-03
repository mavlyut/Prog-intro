package expression;

import java.math.BigDecimal;

public class Divide extends AbstractBinaryOperation {
    public Divide(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        return x / y;
    }

    @Override
    public BigDecimal makeOp(BigDecimal x, BigDecimal y) {
        return x.divide(y);
    }

    @Override
    public String getOperator() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
