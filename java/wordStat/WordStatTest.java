package wordStat;

import base.ModelessSelector;
import base.Named;
import base.Pair;
import base.VariantTester;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WordStatTest {
    private static final Named<Comparator<Pair<String, Integer>>> INPUT = Named.of("Input", Comparator.comparingInt(p -> 0));
    private static final Named<Comparator<Pair<String, Integer>>> COUNT = Named.of("Count", Comparator.comparingInt(Pair::getSecond));
    private static final Named<Comparator<Pair<String, Integer>>> WORDS = Named.of("Words", Comparator.comparing(Pair::getFirst));

    private static final Named<Function<String, Stream<String>>> ID  = Named.of("", Stream::of);

    public static final ModelessSelector<?> SELECTOR = VariantTester.selector(WordStatTester.class, WordStatTester::test)
            .variant("Base",            WordStatTester.variant(INPUT, ID))
            .variant("Count",           WordStatTester.variant(COUNT, ID))
            .variant("Words",           WordStatTester.variant(WORDS, ID));

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
