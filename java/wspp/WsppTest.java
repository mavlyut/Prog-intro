package wspp;

import base.ModelessSelector;
import base.Named;
import base.VariantTester;

import java.util.Comparator;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WsppTest {
    private static final Named<Comparator<Map.Entry<String, Integer>>> INPUT = Named.of("", Comparator.comparingInt(e -> 0));
    private static final Named<Comparator<Map.Entry<String, Integer>>> SORTED = Named.of("Sorted", Map.Entry.comparingByKey());

    private static final Named<IntFunction<IntStream>> ALL = Named.of("", size -> IntStream.range(0, size));
    private static final Named<IntFunction<IntStream>> SECOND = Named.of("Second", size -> IntStream.range(0, size).filter(i -> i % 2 == 1));

    private static final Named<WsppTester.Extractor<Integer>> GLOBAL = Named.of("G", (r, w, g) -> g);
    private static final Named<WsppTester.Extractor<String>> POSITION = Named.of("Position", (r, w, g) -> r + ":" + w);

    public static final ModelessSelector<?> SELECTOR = VariantTester.selector(WsppTester.class, WsppTester::test)
            .variant("Base", VariantTester.variant("", WsppTester::answer))
            .variant("SecondG",         WsppTester.variant(INPUT, SECOND, GLOBAL))
            .variant("SortedSecondG",   WsppTester.variant(SORTED, SECOND,GLOBAL))
            .variant("Position",        WsppTester.variant(INPUT, ALL, POSITION))
            .variant("SortedPosition",  WsppTester.variant(SORTED, ALL, POSITION))
            ;

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
