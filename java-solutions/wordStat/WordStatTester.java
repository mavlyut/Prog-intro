package wordStat;

import base.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WordStatTester {
    public static Consumer<VariantTester<Named<Function<Stream<String>, Stream<Pair<String, Integer>>>>>> variant(final Named<Comparator<Pair<String, Integer>>> first, final Named<Function<String, Stream<String>>> second) {
        return VariantTester.variant(first.getName() + second.getName(), input ->
                input.flatMap(second.getValue())
                        .collect(Collectors.toMap(String::toLowerCase, v -> 1, Integer::sum, LinkedHashMap::new)).entrySet().stream()
                        .map(Pair::of)
                        .sorted(first.getValue()));
    }

    public static void test(final Named<Function<Stream<String>, Stream<Pair<String, Integer>>>> variant, final TestCounter counter) {
        WordStatChecker.test(
                counter,
                "WordStat" + variant.getName(),
                text -> variant.getValue().apply(Arrays.stream(text)
                                .flatMap(Arrays::stream)
                                .filter(s -> !s.isEmpty()))
                        .collect(Collectors.toUnmodifiableList()),
                checker -> {
                    checker.test("To be, or not to be, that is the question:");
                    checker.test("Monday's child is fair of face.", "Tuesday's child is full of grace.");
                    checker.test("Шалтай-Болтай", "Сидел на стене.", "Шалтай-Болтай", "Свалился во сне.");
                    checker.test("10 октября — Всемирный день психического здоровья", "Тема 2017 года: Психическое здоровье на рабочем месте");

                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.ENGLISH, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(10, 3, 5, 5, ExtendedRandom.RUSSIAN, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(4, 10, 10, 3, ExtendedRandom.GREEK, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(4, 10, 10, 3, WordStatChecker.DASH, WordStatChecker.SIMPLE_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.ENGLISH, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(10, 3, 5, 5, ExtendedRandom.RUSSIAN, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, ExtendedRandom.GREEK, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(3, 10, 10, 3, WordStatChecker.DASH, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(3, 10, 10, 10, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);

                    final int d = TestCounter.DENOMINATOR;
                    final int d2 = TestCounter.DENOMINATOR;
                    checker.randomTest(10, 10000 / d, 10, 10, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(10, 1, 10, 10, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(10, 1000 / d, 100 / d2, 100 / d2, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(4, 1000 / d, 10, 3000 / d, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);
                    checker.randomTest(4, 1000 / d, 3000 / d, 10, WordStatChecker.ALL, WordStatChecker.ADVANCED_DELIMITERS);
                }
        );
    }

}
