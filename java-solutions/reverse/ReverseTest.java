package reverse;

import base.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTest {
    private static final Named<Function<int[][], int[][]>> REVERSE = Named.of("", ReverseTester::transform);

    private static final Named<Function<int[][], int[][]>> TRANSPOSE = Named.of("Transpose", ints -> {
        final List<int[]> rows = new ArrayList<>(List.of(ints));
        return IntStream.range(0, Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0))
                .mapToObj(c -> {
                    rows.removeIf(r -> r.length <= c);
                    return rows.stream().mapToInt(r -> r[c]).toArray();
                })
                .toArray(int[][]::new);
    });

    private static final Named<Function<int[][], int[][]>> SUM2 = Named.of("Sum2", ints -> {
        // This code is intentionally obscure
        final int[] cs = new int[Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0)];
        return Arrays.stream(ints).map(anInt -> scan(anInt, (j, n) -> cs[j] += n, Integer::sum)).toArray(int[][]::new);
    });


    private static final Named<Function<int[][], int[][]>> ODD2 = Named.of("Odd2", ((Function<int[][], Stream<IntStream>>) ints -> IntStream.range(0, ints.length)
            .mapToObj(i -> IntStream.range(0, ints[i].length).filter(j -> (i + j) % 2 == 1).map(j -> ints[i][j]))).andThen(s -> s.map(IntStream::toArray).toArray(int[][]::new)).andThen(ReverseTester::transform));

    private static final Named<Function<int[][], int[][]>> MIN2 = Named.of("Min2", ints -> {
        final int[] cs = new int[Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0)];
        Arrays.fill(cs, Integer.MAX_VALUE);
        return Arrays.stream(ints).map(anInt -> scan(anInt, (j, n) -> cs[j] = Math.min(cs[j], n), Math::min)).toArray(int[][]::new);
    });

    private static final Named<BiFunction<ExtendedRandom, Integer, String>> DEC = Named.of("", (r, i) -> Integer.toString(i));
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX_IN = Named.of("Hex", (r, i) -> Integer.toHexString(i));
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX_OUT = Named.of("Hex", (r, i) -> "0x" + Integer.toHexString(i));
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> ABC = Named.of("Abc", (r, i) -> toAbc(i));
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX_DEC = Named.of("HexDec", (r, i) ->
            r.nextBoolean()
                    ? Integer.toString(i)
                    : (r.nextBoolean() ? "0x" : "0X") + Integer.toHexString(i));
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> HEX_ABC = Named.of("HexAbc", (r, i) ->
            r.nextInt(10) == 0 ? toAbc(i) :
            r.nextInt(10) > 0 ? Integer.toString(i) :
                            (r.nextBoolean() ? "0x" : "0X") + Integer.toHexString(i));

    private static int[] scan(final int[] input, final IntBinaryOperator map, final IntBinaryOperator reduce) {
        final int[] ints = IntStream.range(0, input.length).map(i -> map.applyAsInt(i, input[i])).toArray();
        Arrays.parallelPrefix(ints, reduce);
        return ints;
    }


    private static String toAbc(final int value) {
        final char[] chars = Integer.toString(value).toCharArray();
        for (int i = value < 0 ? 1 : 0; i < chars.length; i++) {
            chars[i] += 49;
        }
        return new String(chars);
    }

    public static final int MAX_SIZE = 10_000 / TestCounter.DENOMINATOR;
    public static final ModelessSelector<?> SELECTOR = selector(ReverseTest.class, MAX_SIZE);

    public static ModelessSelector<VariantTester<ReverseTester>> selector(final Class<?> owner, final int maxSize) {
        return VariantTester.selector(owner, ReverseTester.test(maxSize))
                .variant("Base",        ReverseTester.variant(REVERSE))

                .variant("Transpose",   ReverseTester.variant(TRANSPOSE))
                .variant("Sum2",        ReverseTester.variant(SUM2))
                .variant("Odd2",        ReverseTester.variant(ODD2))
                .variant("Min2",        ReverseTester.variant(MIN2))

                .variant("Abc2",        ReverseTester.variant("2", ABC, DEC))
                .variant("Hex2",        ReverseTester.variant("2", HEX_IN, DEC))
                .variant("HexAbc2",     ReverseTester.variant("2", HEX_ABC, ABC))
                .variant("HexDec2",     ReverseTester.variant("2", HEX_DEC, HEX_OUT));
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
