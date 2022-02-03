package expression;

public class Main {
    private static Const c(int c) {
        return new Const(c);
    }

    private static Variable var(String name) {
        return new Variable(name);
    }

    public static void main(String[] args) {
        CommonExp exp = new Negate(c(2));
        System.out.println(exp);
        exp = new Negate(new Add(var("y"), c(8)));
        System.out.println(exp);
    }

    private static void check(Expression e1, Expression e2) {
        boolean eq = e1.toString().equals(e2.toString());
        System.out.println(eq + " " + (e1.hashCode() == e2.hashCode()) + " " + e1.equals(e2));
    }
}