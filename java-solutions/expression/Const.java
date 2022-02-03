package expression;

import java.math.BigDecimal;

public class Const extends AbstractVarConst {
    private final Number value;

    public Const(BigDecimal value) {
        super(value.toString());
        this.value = value;
    }

    public Const(int value) {
        super(value + "");
        this.value = value;
    }

    public Const(Number value) {
        super(value.toString());
        this.value = value;
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return (BigDecimal) value;
    }
}