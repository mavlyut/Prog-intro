package expression;

public class T0 extends AbstractUnaryOperation {
    public T0(CommonExp expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "t0";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int makeOp(int x) {
        return Integer.numberOfTrailingZeros(x);
    }
}
