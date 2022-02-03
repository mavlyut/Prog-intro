package expression.parser;

import expression.*;


public class ExpressionParser implements Parser {
    @Override
    public TripleExpression parse(String expression) {
        System.out.println(expression);
        return new ParserExpression(new StringCharSource(expression)).parse();
    }
}

class ParserExpression extends BaseParser {
    public ParserExpression(CharSource source) {
        super(source);
    }

    public CommonExp parse() {
        return parseE();
    }

    private CommonExp parseE() {
        skipWhitespace();
        CommonExp expression1 = parseM();
        skipWhitespace();
        if (take('<')) {
            expect('<');
            return makeBinOperation(expression1, "<<", parseE());
        } else if (take('>')) {
            expect('>');
            if (take('>')) {
                return makeBinOperation(expression1, ">>>", parseE());
            } else {
                return makeBinOperation(expression1, ">>", parseE());
            }
        }
        return expression1;
    }

    private CommonExp parseM() {
        skipWhitespace();
        CommonExp expression1 = parseT();
        skipWhitespace();
        if (test('+') || test('-')) {
            return makeBinOperation(expression1, take() + "", parseM());
        }
        return expression1;
    }

    private CommonExp parseT() {
        skipWhitespace();
        CommonExp expression1 = parseF();
        skipWhitespace();
        if (test('*') || test('/')) {
            return makeBinOperation(expression1, take() + "", parseT());
        }
        return expression1;
    }

    private CommonExp parseF() {
        skipWhitespace();
        if (take('(')) {
            CommonExp toReturn = parseE();
            expect(')');
            return toReturn;
        }
        if (between('a', 'z')) {
            return new Variable(parseString());
        } else if (take('-')) {
            if (between('0', '9')) {
                return new Const(parseNumber(true));
            } else {
                return new Negate(parseE());
            }
        } else if (between('0', '9')) {
            return new Const(parseNumber(false));
        } else if (test('l') || test('t')) {
            String operator = take() + "";
            expect('0');
            operator += "0";
            return switch (operator) {
                case "l0" -> new L0(parseE());
                case "t0" -> new T0(parseE());
                default -> throw error("Unknown UnaryOperation");
            };
        }
        throw error("Unknown operation");
    }

    private String parseString() {
        skipWhitespace();
        StringBuilder sb = new StringBuilder();
        while (between('a', 'z') || between('A', 'Z')) {
            sb.append(take());
        }
        return sb.toString();
    }

    private int parseNumber(boolean isNegative) {
        skipWhitespace();
        StringBuilder sb = new StringBuilder();
        if (isNegative) {
            sb.append("-");
        }
        while (between('0', '9')) {
            sb.append(take());
        }
        return Integer.parseInt(sb.toString());
    }

    private Operation makeBinOperation(CommonExp expression1, String operator, CommonExp expression2) {
        return switch (operator) {
            case "*" -> new Multiply(expression1, expression2);
            case "/" -> new Divide(expression1, expression2);
            case "+" -> new Add(expression1, expression2);
            case "-" -> new Subtract(expression1, expression2);
            case ">>>" -> new ShiftArithmeticRight(expression1, expression2);
            case ">>" -> new ShiftRight(expression1, expression2);
            case "<<" -> new ShiftLeft(expression1, expression2);
            default -> throw error("Unknown binOperator: " + operator);
        };
    }
}
