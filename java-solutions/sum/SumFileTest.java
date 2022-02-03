package sum;

import base.ModelessSelector;
import base.Runner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class SumFileTest {
    public static final ModelessSelector<?> SELECTOR = SumTest.selector((variant, counter) ->
            variant.getValue().test("Sum" + variant.getName() + "File", counter, Runner::files));

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
