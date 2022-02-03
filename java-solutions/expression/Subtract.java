package expression;

import java.math.BigDecimal;

public class Subtract extends AbstractBinaryOperation {
    public Subtract(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        return x - y;
    }

    @Override
    public BigDecimal makeOp(BigDecimal x, BigDecimal y) {
        return x.subtract(y);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
