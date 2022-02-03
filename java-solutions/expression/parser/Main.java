package expression.parser;

import expression.CommonExp;
import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {
        String expr = "\u000B((\t \u2029(y\n" +
                "\u000B+ x)\n" +
                "\n" +
                "\f\u000B\n" +
                "--2147483648)+\u2029\f\n" +
                "\t (x\u000B+ x)\t\f)\u2029\u000B";

        // Add(y, Sub(x, Add(-MIN, Add(x, x))))

        ExpressionParser parser = new ExpressionParser();
        TripleExpression exp = parser.parse(expr);

        System.out.println(exp.toString());
    }
}
