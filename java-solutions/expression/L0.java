package expression;

public class L0 extends AbstractUnaryOperation {
    public L0(CommonExp expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "l0";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int makeOp(int x) {
        return Integer.numberOfLeadingZeros(x);
    }
}
