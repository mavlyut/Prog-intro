package expression.parser;

import base.Asserts;
import base.Either;
import base.ExtendedRandom;
import base.TestCounter;
import expression.ToMiniString;
import expression.TripleExpression;
import expression.common.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.regex.Pattern;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ParserTester extends BaseTester {
    private static final int D = 5;

    private static final List<Integer> TEST_VALUES = new ArrayList<>();
    static {
        addRange(TEST_VALUES, D, D);
        addRange(TEST_VALUES, D, -D);
    }

    public static final List<Integer> CONSTS = List.of(0, 1, -1, 4, -4, 10, -10, 30, -30, 100, -100, Integer.MAX_VALUE, Integer.MIN_VALUE);

    private final TestGenerator<Integer> generator = new TestGenerator<>(counter, random, random::nextInt, CONSTS, true);
    private final Renderer<Integer, TExpression> expr = new Renderer<>(c -> vars -> c);

    private final List<Op<TExpression>> tests = new ArrayList<>();
    private final Parser parser = new ExpressionParser();
    private final boolean safe;

    public ParserTester(final TestCounter counter, final int mode) {
        this(counter, mode, true);
    }

    protected ParserTester(final TestCounter counter, final int mode, final boolean safe) {
        super(counter, mode);
        this.safe = safe;

        example("x+2", (x, y, z) -> x + 2);
        example("2-y", (x, y, z) -> 2 - y);
        example("  3*  z  ", (x, y, z) -> 3 * z);
        example("x/  -  2", (x, y, z) -> -x / 2);
        example("x*y+(z-1   )/10", (x, y, z) -> x * y + (int) (z - 1) / 10);
        example("-(-(-\t\t-5 + 16   *x*y) + 1 * z) -(((-11)))", (x, y, z) -> -(-(5 + 16 * x * y) + z) + 11);
        example("" + Integer.MAX_VALUE, (x, y, z) -> (long) Integer.MAX_VALUE);
        example("" + Integer.MIN_VALUE, (x, y, z) -> (long) Integer.MIN_VALUE);
        example("x--y--z", (x, y, z) -> x + y + z);
        example("((2+2))-0/(--2)*555", (x, y, z) -> 4L);
        example("x-x+y-y+z-(z)", (x, y, z) -> 0L);
        example("(".repeat(400) + "x + y + (-10*-z)" + ")".repeat(400), (x, y, z) -> x + y + 10 * z);
        example("x / y / z", (x, y, z) -> y == 0 || z == 0 ? Reason.DBZ.error() : (int) x / (int) y / z);
    }

    public void variable(final String name, final int index) {
        generator.variable(name);
        expr.nullary(name, vars -> vars[index]);
    }

    private void example(final String name, final XYZExpression expression) {
        tests.add(Op.of(name, vars -> expression.evaluate(vars[0], vars[1], vars[2])));
    }

    @Override
    protected void test() {
        counter.scope("Basic tests", () -> generator.testBasic(this::test));
        counter.scope("Handmade tests", this::handmade);
        counter.scope("Random tests", () -> generator.testRandom(1, this::test));
    }

    private void handmade() {
        for (final Op<TExpression> test : tests) {
            counter.scope(test.name, () -> {
                final TripleExpression expression = parse(test.name, true);
                for (final Integer x : TEST_VALUES) {
                    for (final Integer y : TEST_VALUES) {
                        for (final Integer z : TEST_VALUES) {
                            check(test.value, expression, new int[]{x, y, z});
                        }
                    }
                }
            });
        }
    }

    private void test(final TestGenerator.Test<Integer> test) {
        final String full = test.full;
        final String mini = test.mini;
        final String safe = test.safe;
        final Node<Integer> extraTest = extraParens(test.node);

        final TripleExpression fullParsed = parse(full, false);
        final TripleExpression miniParsed = parse(mini, false);
        final TripleExpression safeParsed = parse(safe, false);

        checkToString(full, mini, "base", fullParsed);
        if (mode > 0) {
            counter.test(() -> Asserts.assertEquals("mini.toMiniString", mini, miniParsed.toMiniString()));
            counter.test(() -> Asserts.assertEquals("safe.toMiniString", mini, safeParsed.toMiniString()));
        }
        checkToString(full, mini, "extraParentheses", parse(generator.full(extraTest), false));
        checkToString(full, mini, "noSpaces", parse(removeSpaces(full), false));
        checkToString(full, mini, "extraSpaces", parse(extraSpaces(full), false));

        final TExpression expected = expr.render(test.node);
        check(expected, fullParsed, new int[]{random.nextInt(), random.nextInt(), random.nextInt()});
        if (this.safe) {
            check(expected, safeParsed, new int[]{random.nextInt(), random.nextInt(), random.nextInt()});
        }
    }

    private static final String LOOKBEHIND = "(?<![a-zA-Z0-9<>*/+-])";
    private static final String LOOKAHEAD = "(?![a-zA-Z0-9<>*/])";
    private static final Pattern SPACES = Pattern.compile(LOOKBEHIND + " | " + LOOKAHEAD + "|" + LOOKAHEAD + LOOKBEHIND);
    private String extraSpaces(final String expression) {
        return SPACES.matcher(expression).replaceAll(r -> random.randomString(ExtendedRandom.SPACES, random.nextInt(5)));
    }

    private static String removeSpaces(final String expression) {
        return SPACES.matcher(expression).replaceAll("");
    }

    private void checkToString(final String full, final String mini, final String prefix, final ToMiniString parsed) {
        counter.test(() -> {
            Asserts.assertEquals(prefix + ".toString", full, parsed.toString());
            if (mode > 0) {
                Asserts.assertEquals(prefix + ".toMiniString", mini, parsed.toMiniString());
            }
        });
    }

    private Node<Integer> addExtraParens(final Node<Integer> node) {
        return random.getRandom().nextBoolean() ? node : addExtraParens(Node.op("(:1", List.of(node)));
    }

    private Node<Integer> extraParens(final Node<Integer> node) {
        return node.cata(
                c -> addExtraParens(Node.constant(c)),
                (name, args) -> addExtraParens(Node.op(name, args))
        );
    }

    private Either<Reason, Integer> eval(final TExpression expression, final int[] vars) {
        return Reason.eval(() -> cast(expression.evaluate(vars)));
    }

    protected TripleExpression parse(final String expression, final boolean reparse) {
        return counter.testV(() -> {
            final TripleExpression parsed = counter.testV(() -> counter.call("parse", () -> parser.parse(expression)));
            if (reparse) {
                counter.testV(() -> counter.call("parse", () -> parser.parse(parsed.toString())));
            }
            return parsed;
        });
    }

    private void check(final TExpression expectedExpression, final TripleExpression expression, final int[] vars) {
        counter.test(() -> {
            final Either<Reason, Integer> answer = eval(expectedExpression, vars);
            try {
                final int actual = expression.evaluate(vars[0], vars[1], vars[2]);
                counter.checkTrue(answer.isRight(), "Error expected for x=%d, y=%d, z=%d", vars[0], vars[1], vars[2]);
                Asserts.assertEquals(String.format("f(%d, %d, %d)\n%s", vars[0], vars[1], vars[2], expression), answer.getRight(), actual);
            } catch (final Exception e) {
                if (answer.isRight()) {
                    counter.fail(e, "No error expected for x=%d, y=%d, z=%d", vars[0], vars[1], vars[2]);
                }
            }
        });
    }

    protected int cast(final long value) {
        return (int) value;
    }

    @FunctionalInterface
    private interface TExpression {
        long evaluate(int[] vars);
    }

    @FunctionalInterface
    protected interface XYZExpression {
        long evaluate(long x, long y, long z);
    }

    public void unary(final String name, final LongUnaryOperator op) {
        generator.unary(name);
        expr.unary(name, a -> vars -> cast(op.applyAsLong(a.evaluate(vars))));
    }

    public void binary(final String name, final int priority, final LongBinaryOperator op) {
        generator.binary(name, priority);
        expr.binary(name, (a, b) -> vars -> cast(op.applyAsLong(a.evaluate(vars), b.evaluate(vars))));
    }
}
