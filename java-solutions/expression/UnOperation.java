package expression;

import java.math.BigDecimal;

public interface UnOperation extends Operation {
    int makeOp(int x);
    BigDecimal makeOp(BigDecimal x);
}
