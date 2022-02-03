package expression;

public class Negate extends AbstractUnaryOperation {
    public Negate(CommonExp expression) {
        super(expression);
    }

    @Override
    public String getOperator() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int makeOp(int x) {
        return -x;
    }
}
