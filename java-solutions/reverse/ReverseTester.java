package reverse;

import base.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTester {
    private static final int[] DIVISORS = {100, 10, 1};

    private final Function<int[][], int[][]> transform;
    private final BiFunction<ExtendedRandom, Integer, String> inputToString;
    private final BiFunction<ExtendedRandom, Integer, String> outputToString;
    private final String name;

    private ReverseTester(final String className, final Function<int[][], int[][]> transform) {
        this(className, transform, (r, i) -> Integer.toString(i), (r, i) -> Integer.toString(i));
    }

    private ReverseTester(
            final String className,
            final Function<int[][], int[][]> transform,
            final BiFunction<ExtendedRandom, Integer, String> inputToString,
            final BiFunction<ExtendedRandom, Integer, String> outputToString
    ) {
        name = className;
        this.transform = transform;
        this.inputToString = inputToString;
        this.outputToString = outputToString;
    }

    public static Consumer<VariantTester<ReverseTester>> variant(final Named<Function<int[][], int[][]>> transform) {
        return VariantTester.variant(new ReverseTester("Reverse" + transform.getName(), transform.getValue()));
    }

    public static Consumer<? super VariantTester<ReverseTester>> variant(
            final String suffix,
            final Named<BiFunction<ExtendedRandom, Integer, String>> input,
            final Named<BiFunction<ExtendedRandom, Integer, String>> output
    ) {
        return VariantTester.variant(new ReverseTester(
                "Reverse" + input.getName() + (input.getName().contains(output.getName()) ? "" : output.getName()) + suffix,
                ReverseTester::transform,
                input.getValue(),
                output.getValue()
        ));
    }

    private void run(final TestCounter counter, final int maxSize) {
        new Checker(counter, maxSize, Runner.std(name)).test();
    }

    @Override
    public String toString() {
        return name;
    }

    public static int[][] transform(final int[][] ints) {
        return IntStream.range(1, ints.length + 1)
                .mapToObj(i -> ints[ints.length - i])
                .map(is -> IntStream.range(1, is.length + 1).map(i -> is[is.length - i]).toArray())
                .toArray(int[][]::new);
    }
    
    public static BiConsumer<ReverseTester, TestCounter> test(final int maxSize) {
        return (tester, counter) -> tester.run(counter, maxSize);
    }

    /**
     * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
     */
    private class Checker extends BaseChecker {
        private final int maxSize;
        private final Runner runner;

        public Checker(final TestCounter counter, final int maxSize, final Runner runner) {
            super(counter);
            this.maxSize = maxSize;
            this.runner = runner;
        }

        public void test(final int[][] ints) {
            test(toString(ints, inputToString), toString(transform.apply(ints), outputToString));
        }

        public void test(final String[][] input, final String[][] output) {
            final List<String> expected = toLines(output, " ");
            final List<String> actual = toLines(input, random.randomString(" ", 1, 10));
            runner.testEquals(counter, actual, expected);
        }

        private String[][] toString(final int[][] ints, final BiFunction<ExtendedRandom, Integer, String> toString) {
            return Arrays.stream(ints)
                    .map(row -> Arrays.stream(row).mapToObj(i -> toString.apply(random, i)).toArray(String[]::new))
                    .toArray(String[][]::new);
        }

        private List<String> toLines(final String[][] data, final String delimiter) {
            if (data.length == 0) {
                return Collections.singletonList("");
            }
            return Arrays.stream(data)
                    .map(row -> String.join(delimiter, row))
                    .collect(Collectors.toList());
        }

        public int[][] random(final int[] profile) {
            final int col = random.nextInt(Arrays.stream(profile).max().orElse(0));
            final int row = random.nextInt(profile.length);
            final int m = random.nextInt(5) - 2;
            final int[][] ints = Arrays.stream(profile).mapToObj(random.getRandom()::ints).map(IntStream::toArray).toArray(int[][]::new);
            Arrays.stream(ints).filter(r -> col < r.length).forEach(r -> r[col] = Math.abs(r[col]) / 2 * m);
            ints[row] = Arrays.stream(ints[row]).map(Math::abs).map(v -> v / 2 * m).toArray();
            return ints;
        }

        public void test() {
            test(new int[][]{
                    {1}
            });
            test(new int[][]{
                    {1, 2},
                    {3}
            });
            test(new int[][]{
                    {1, 2, 3},
                    {4, 5},
                    {6}
            });
            test(new int[][]{
                    {1, 2, 3},
                    {},
                    {4, 5},
                    {6}
            });
            test(new int[][]{
                    {1, 2, 3},
                    {-4, -5},
                    {6}
            });
            test(new int[][]{
                    {1, -2, 3},
                    {},
                    {4, -5},
                    {6}
            });
            test(new int[][]{
                    {1, -2, 3},
                    {},
                    {-4, -5},
                    {6}
            });
            test(new int[][]{
                    {},
            });
            test(new int[][]{
                    {},
                    {},
                    {},
            });
            testRandom(tweakProfile(constProfile(10, 10), new int[][]{}));
            testRandom(tweakProfile(constProfile(100, 100), new int[][]{}));
            testRandom(randomProfile(100, maxSize));
            testRandom(randomProfile(maxSize / 10, maxSize));
            testRandom(randomProfile(maxSize, maxSize));
            for (final int d : DIVISORS) {
                final int size = maxSize / d;
                testRandom(tweakProfile(constProfile(size, 0), new int[][]{new int[]{size, 0}}));
                testRandom(tweakProfile(randomProfile(size, maxSize), new int[][]{new int[]{size, 0}}));
                testRandom(tweakProfile(constProfile(size, 0), new int[][]{new int[]{size / 2, size / 2 - 1}}));
                testRandom(tweakProfile(constProfile(size, 1), new int[][]{new int[]{size / 3, size / 3, size * 2 / 3}}));
            }
        }

        private int[] randomProfile(final int length, final int values) {
            final int[] profile = new int[length];
            for (int i = 0; i < values; i++) {
                profile[random.nextInt(0, length - 1)]++;
            }
            return profile;
        }

        private void testRandom(final int[] profile) {
            test(random(profile));
        }

        private int[] constProfile(final int length, final int value) {
            final int[] profile = new int[length];
            Arrays.fill(profile, value);
            return profile;
        }

        private int[] tweakProfile(final int[] profile, final int[][] mods) {
            for (final int[] mod : mods) {
                Arrays.stream(mod).skip(1).forEach(i -> profile[i] = mod[0]);
            }
            return profile;
        }
    }
}
