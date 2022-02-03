package expression;

import java.math.BigDecimal;

public class Variable extends AbstractVarConst {
    public Variable(String name) {
        super(name);
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return x;
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (str) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new AssertionError("Enter valid name of variables: [\"x\", \"y\", \"z\"]");
        };
    }
}
