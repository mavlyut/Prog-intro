package expression;

import java.math.BigDecimal;

public interface BinOperation extends Operation {
    int makeOp(int x, int y);
    BigDecimal makeOp(BigDecimal x, BigDecimal y);
}
