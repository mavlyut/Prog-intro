package expression.exceptions;

import base.TestCounter;
import expression.TripleExpression;
import expression.Variable;
import expression.common.Op;
import expression.common.Reason;
import expression.parser.ParserTester;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * @author Niyaz Nigmatullin
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ExceptionsTester extends ParserTester {
    private static final int D = 5;
    private static final List<Integer> OVERFLOW_VALUES = new ArrayList<>();
    private final char[] CHARS = "AZ+-*%()[]<>".toCharArray();

    private static final Variable VX = new Variable("x");
    private static final Variable VY = new Variable("y");

    static {
        addRange(OVERFLOW_VALUES, D, Integer.MIN_VALUE + D);
        addRange(OVERFLOW_VALUES, D, Integer.MIN_VALUE / 2);
        addRange(OVERFLOW_VALUES, D, (int) -Math.sqrt(Integer.MAX_VALUE));
        addRange(OVERFLOW_VALUES, D, 0);
        addRange(OVERFLOW_VALUES, D, (int) Math.sqrt(Integer.MAX_VALUE));
        addRange(OVERFLOW_VALUES, D, Integer.MAX_VALUE / 2);
        addRange(OVERFLOW_VALUES, D, Integer.MAX_VALUE - D);
    }

    private final List<Op<String>> parsingTest = list(
            Op.of("No first argument", "* y * z"),
            Op.of("No middle argument", "x *  * z"),
            Op.of("No last argument", "x * y * "),
            Op.of("No first argument'", "1 + (* y * z) + 2"),
            Op.of("No middle argument'", "1 + (x *  / 9) + 3"),
            Op.of("No last argument'", "1 + (x * y - ) + 3"),
            Op.of("No opening parenthesis", "x * y)"),
            Op.of("No closing parenthesis", "(x * y"),
            Op.of("Start symbol", "@x * y"),
            Op.of("Middle symbol", "x @ * y"),
            Op.of("End symbol", "x * y@"),
            Op.of("Constant overflow 1", Integer.MIN_VALUE - 1L + ""),
            Op.of("Constant overflow 2", Integer.MAX_VALUE + 1L + ""),
            Op.of("Bare +", "+"),
            Op.of("Bare -", "-"),
            Op.of("Bare a", "a"),
            Op.of("(())", "(())"),
            Op.of("Spaces in numbers", "10 20")
    );

    public ExceptionsTester(final TestCounter counter, final int mode) {
        super(counter, mode, false);
    }

    private void parsingTests(final String... tests) {
        for (final String test : tests) {
            parsingTest.add(Op.of(test, test));
        }
    }

    private void testParsingErrors() {
        final ExpressionParser parser = new ExpressionParser();
        counter.testForEach(parsingTest, op -> {
            try {
                parser.parse(op.value);
                counter.fail("Successfully parsed %s", op.value);
            } catch (final Exception e) {
                counter.format("%-30s %s%n", op.name, e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        });
    }

    private void testOverflow() {
        //noinspection Convert2MethodRef
        testOverflow((a, b) -> a + b, "+", new CheckedAdd(VX, VY));
        testOverflow((a, b) -> a - b, "-", new CheckedSubtract(VX, VY));
        testOverflow((a, b) -> a * b, "*", new CheckedMultiply(VX, VY));
        testOverflow((a, b) -> b == 0 ? Long.MAX_VALUE : a / b, "/", new CheckedDivide(VX, VY));
        testOverflow((a, b) -> -b, "<- ignore first argument, unary -", new CheckedNegate(VY));
    }

    private void testOverflow(final LongBinaryOperator f, final String op, final TripleExpression expression) {
        for (final int a : OVERFLOW_VALUES) {
            for (final int b : OVERFLOW_VALUES) {
                final long expected = f.applyAsLong(a, b);
                try {
                    final int actual = expression.evaluate(a, b, 0);
                    counter.checkTrue(actual == expected, "%d %s %d == %d", a, op, b, actual);
                } catch (final Exception e) {
                    if (Integer.MIN_VALUE <= expected && expected <= Integer.MAX_VALUE) {
                        counter.fail(e, "Unexpected error in %d %s %d", a, op, b);
                    }
                }
            }
        }
    }

    @Override
    protected void test() {
        counter.scope("Overflow tests", (Runnable) this::testOverflow);
        super.test();
        counter.scope("Parsing error tests", this::testParsingErrors);
    }

    @Override
    protected TripleExpression parse(final String expression, final boolean reparse) {
        final Parser parser = new ExpressionParser();
        final String expr = expression.strip();
        if (expr.length() > 10) {
            for (final char ch : CHARS) {
                for (int i = 0; i < 10; i++) {
                    final int index = 1 + random.nextInt(expr.length() - 2);
                    int pi = index - 1;
                    while (Character.isWhitespace(expr.charAt(pi))) {
                        pi--;
                    }
                    int ni = index;
                    while (Character.isWhitespace(expr.charAt(ni))) {
                        ni++;
                    }
                    final char pc = expr.charAt(pi);
                    final char nc = expr.charAt(ni);
                    if ("-(*".indexOf(nc) < 0 && nc != ch && pc != ch && !Character.isLetterOrDigit(nc)) {
                        final String input = expr.substring(0, index) + ch + expr.substring(index);
                        counter.shouldFail(
                                "Parsing error expected for " + expr.substring(0, index) + "<ERROR_INSERTED -->" + ch + "<-- ERROR_INSERTED>" + expr.substring(index),
                                () -> parser.parse(input)
                        );
                        break;
                    }
                }
            }
        }

        return counter.testV(() -> counter.call("parse", () -> parser.parse(expr)));
    }

    @Override
    protected int cast(final long value) {
        return Reason.overflow(value);
    }

    @Override
    public void unary(final String name, final LongUnaryOperator op) {
        parsingTests(name, name + "()", name + "(1, 2)", "1 * " + name, name + " * 1");
        if (!"-".equals(name)) {
            parsingTests(name + "x");
        }
        if (allLetterAndDigit(name)) {
            parsingTests(name + "1", name + "x");
        }
        super.unary(name, op);
    }

    @Override
    public void binary(final String name, final int priority, final LongBinaryOperator op) {
        parsingTests(name, "1 " + name, "1 " + name + " * 3");
        if (!"-".equals(name)) {
            parsingTests(name + " 1", "1 * " + name + " 2");
        }
        if (allLetterAndDigit(name)) {
            parsingTests("5" + name + "5", "1" + name + "x 1");
        }
        super.binary(name, priority, op);
    }

    private static boolean allLetterAndDigit(final String name) {
        return name.chars().allMatch(Character::isLetterOrDigit);
    }
}
