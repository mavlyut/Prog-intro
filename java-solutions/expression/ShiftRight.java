package expression;

import java.math.BigDecimal;

public class ShiftRight extends AbstractBinaryOperation {
    public ShiftRight(CommonExp expression1, CommonExp expression2) {
        super(expression1, expression2);
    }

    @Override
    public int makeOp(int x, int y) {
        return x >> y;
    }

    @Override
    public BigDecimal makeOp(BigDecimal x, BigDecimal y) {
        throw new AssertionError("Can't makeOp >>> for BigDecimal");
    }

    @Override
    public String getOperator() {
        return ">>";
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
