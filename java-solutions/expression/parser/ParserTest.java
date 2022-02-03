package expression.parser;

import expression.common.Selector;

import static expression.parser.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ParserTest {
    public static final Selector<?> SELECTOR = Selector.create(ParserTest.class, ParserTester::new, "easy", "hard")
            .variant("Base", X, Y, Z, ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE)
            .variant("Shifts", SHIFT_L, SHIFT_R, SHIFT_A)
            .variant("Zeroes", L_ZEROES, T_ZEROES)
            .variant("MinMax", MIN, MAX)
            ;

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
