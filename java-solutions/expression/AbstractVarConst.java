package expression;

public abstract class AbstractVarConst extends AbstractExpression {
    protected final String str;

    protected AbstractVarConst(String str) {
        this.str = str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractVarConst exp) {
            return str.equals(exp.str);
        }
        return false;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(str);
    }

    @Override
    public void toMiniString(StringBuilder sb) {
        sb.append(str);
    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}