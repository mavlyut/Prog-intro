package sum;

import base.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class SumTest {
    private static final BiConsumer<Number, String> TO_STRING = (expected, out) -> Asserts.assertEquals("Sum", expected.toString(), out);

    private static BiConsumer<Number, String> approximate(final Function<String, Number> parser, final double precision) {
        return (expected, out) ->
                Asserts.assertEquals("Sum", expected.doubleValue(), parser.apply(out).doubleValue(), precision);
    }

    private static final Named<Supplier<SumTester<Integer>>> BASE = Named.of("", () -> new SumTester<>(
            Integer::sum, n -> (int) n, (r, max) -> r.nextInt() % max, TO_STRING,
            10, 100, Integer.MAX_VALUE
    ));
    private static final Named<UnaryOperator<SumTester<Integer>>> INTEGER_HEX = hex(Integer::toHexString);

    private static final Named<Supplier<SumTester<Long>>> LONG = Named.of("Long", () -> new SumTester<>(
            Long::sum, n -> n, (r, max) -> r.getRandom().nextLong() % max, TO_STRING,
            10L, 100L, (long) Integer.MAX_VALUE, Long.MAX_VALUE)
            .test(12345678901234567L, " +12345678901234567 ")
            .test(0L, " +12345678901234567 -12345678901234567")
            .test(0L, " +12345678901234567 -12345678901234567"));
    private static final Named<UnaryOperator<SumTester<Long>>> LONG_HEX = hex(Long::toHexString);

    private static final Named<Supplier<SumTester<BigInteger>>> BIG_INTEGER = Named.of("BigInteger", () -> new SumTester<>(
            BigInteger::add, BigInteger::valueOf, (r, max) -> new BigInteger(max.bitLength(), r.getRandom()), TO_STRING,
            BigInteger.TEN, BigInteger.TEN.pow(10), BigInteger.TEN.pow(100), BigInteger.TWO.pow(1000))
            .test(0, "10000000000000000000000000000000000000000 -10000000000000000000000000000000000000000"));
    private static final Named<UnaryOperator<SumTester<BigInteger>>> BIG_INTEGER_HEX = hex(number -> number.toString(16));

    private static final Named<Supplier<SumTester<Double>>> DOUBLE = Named.of("Double", () -> new SumTester<>(
            Double::sum, n -> (double) n, (r, max) -> (r.getRandom().nextDouble() - 0.5) * 2 * max,
            approximate(Double::parseDouble, 1e-10),
            10.0, 0.01, 1e20, 1e100, Double.MAX_VALUE / 10000)
            .test(5, "2.5 2.5")
            .test(0, "1e100 -1e100")
            .testT(2e100, "1.5e100 0.5e100"));

    private static final Named<Supplier<SumTester<Float>>> FLOAT = Named.of("Float", () -> new SumTester<>(
            Float::sum, n -> (float) n, (r, max) -> (r.getRandom().nextFloat() - 0.5f) * 2 * max,
            approximate(Float::parseFloat, 1e-5),
            10.0f, 0.01f, 1e20f, Float.MAX_VALUE / 10000)
            .test(5, "2.5 2.5")
            .test(0, "1e10 -1e10")
            .testT(2e10f, "1.5e10 0.5E10"));

    private static final Named<UnaryOperator<SumTester<?>>> PLAIN = Named.of("", t -> t);
    private static final Named<UnaryOperator<SumTester<?>>> SPACE = Named.of("Space",
            t -> t.setSpaces(List.of(" \u2000\u2001\u2002\u2003\u00A0")));
    private static final Named<UnaryOperator<SumTester<?>>> DIGIT = Named.of("Digit",
            t -> t.setSpaces(List.of(" abc\u2000\u2001\u2002\u2003\u00A0\u2007\u202F\u2382")));

    private static <T extends Number> Named<UnaryOperator<SumTester<T>>> hex(final Function<T, String> toHex) {
        return Named.of("Hex", test -> test
                .test(1, "0x1")
                .test(0x1a, "0x1a")
                .test(0xA2, "0xA2")
                .testSpaces(62, " 0X0 0X1 0XF 0XF 0x0 0x1 0xF 0xf")
                .test(0x12345678, "0x12345678")
                .test(0x09abcdef, "0x09abcdef")
                .test(0x3CafeBab, "0x3CafeBab")
                .test(0x3DeadBee, "0x3DeadBee")

                .test(Integer.MAX_VALUE, "0" + Integer.MAX_VALUE)
                .test(Integer.MIN_VALUE, "" + Integer.MIN_VALUE)
                .test(Integer.MAX_VALUE, "0x" + Integer.toHexString(Integer.MAX_VALUE))
                .setToString(number -> {
                    final int hashCode = number.hashCode();
                    if ((hashCode & 1) == 0) {
                        return number.toString();
                    }

                    final String lower = "0x" + toHex.apply(number).toLowerCase();
                    if ((hashCode & 2) == 0) {
                        return lower;
                    } else {
                        return lower.toUpperCase();
                    }
                })
        );
    }

    private static final Named<UnaryOperator<SumTester<?>>> ABC = Named.of("Abc", test -> test
            .test(1, "b")
            .test(6, "b", "C", "d")
            .test(1, " b")
            .test(1, "b ")
            .test(1, " b ")
            .test(12345, " abcdef ")
            .test(1368, " abcd efg hij ")
            .testSpaces(60, "Aba", "acA", "aDa")

            .test(12345678, "bcdefghi")
            .test(20541014, "CafeBabe")
            .test(34031445, "DeadBeef")

            .test(-1, "-b")
            .test(-6, "-b", "-c", "-d")
            .test(-12345, " -abCDef ")
            .testSpaces(-60, " -ABA -aca -ADA ")
            .test(1, "+b")
            .test(6, "+b", "+c", "+d")
            .test(12345, " +abCDef ")
            .testSpaces(60, " +ABA +aca +ADA ")
            .test(0)
            .test(0, " ")
            .setToString(number -> number.toString().chars()
                    .map("+-0123456789"::indexOf)
                    .map("+-abcdefghij"::charAt)
                    .mapToObj(ch -> String.valueOf((char) ch))
                    .collect(Collectors.joining())));


    private static final Map<Integer, List<String>> LOCAL_DIGITS = IntStream.range(0, Character.MAX_VALUE)
            .filter(Character::isDigit)
            .boxed()
            .collect(Collectors.groupingBy(
                    c -> (int) "0123456789".charAt(Character.getNumericValue(c)),
                    Collectors.mapping(c -> String.valueOf((char) c.intValue()), Collectors.toList())
            ));

    private static final Named<UnaryOperator<SumTester<?>>> LOCAL = Named.of("", test -> test
            .setToString((r, n) -> n.toString().chars()
                    .mapToObj(c -> {
                        final List<String> items = LOCAL_DIGITS.get(c);
                        return items == null ? String.valueOf((char) c) : r.randomItem(items);
                    })
                    .collect(Collectors.joining())));

    public static final ModelessSelector<?> SELECTOR = selector((variant, counter) ->
            variant.getValue().test("Sum" + variant.getName(), counter, Runner::args));

    public static ModelessSelector<VariantTester<Named<SumTester<?>>>> selector(final BiConsumer<Named<SumTester<?>>, TestCounter> test) {
        return VariantTester.selector(SumTest.class, test)
                .variant("Base",            SumTester.variant(BASE, PLAIN))
                .variant("Abc",             SumTester.variant(BASE, ABC))
                .variant("Space",           SumTester.variant(BASE, SPACE))
                .variant("Digit",           SumTester.variant(BASE, DIGIT))
                .variant("Local",           SumTester.variant(BASE, LOCAL))
                .variant("Hex",             SumTester.variantT(BASE, INTEGER_HEX))

                .variant("Long",            SumTester.variant(LONG, PLAIN))
                .variant("LongSpace",       SumTester.variant(LONG, SPACE))
                .variant("LongDigit",       SumTester.variant(LONG, DIGIT))
                .variant("LongLocal",       SumTester.variant(LONG, LOCAL))
                .variant("LongHex",         SumTester.variantT(LONG, LONG_HEX))

                .variant("BigInteger",      SumTester.variant(BIG_INTEGER, PLAIN))
                .variant("BigIntegerSpace", SumTester.variant(BIG_INTEGER, SPACE))
                .variant("BigIntegerDigit", SumTester.variant(BIG_INTEGER, DIGIT))
                .variant("BigIntegerLocal", SumTester.variant(BIG_INTEGER, LOCAL))
                .variant("BigIntegerHex",   SumTester.variantT(BIG_INTEGER, BIG_INTEGER_HEX))

                .variant("Double",          SumTester.variant(DOUBLE, PLAIN))
                .variant("DoubleSpace",     SumTester.variant(DOUBLE, SPACE))

                .variant("Float",           SumTester.variant(FLOAT, PLAIN))
                .variant("FloatSpace",      SumTester.variant(FLOAT, SPACE));
    }


    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
