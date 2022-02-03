package md2html;

import base.ModelessSelector;

import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Md2HtmlTest {
    private static final Consumer<Md2HtmlTester> VAR = tester -> tester
            .test("%переменная%", "<p><var>переменная</var></p>")
            .test("Это %переменная%, вложенная в текст", "<p>Это <var>переменная</var>, вложенная в текст</p>")
            .test("Это не \\%переменная\\%", "<p>Это не %переменная%</p>")
            .addElement("var", "%");

    private static final Consumer<? super Md2HtmlTester> QUOTE = tester -> tester
            .test("''цитата''", "<p><q>цитата</q></p>")
            .test("Это ''цитата'', вложенная в текст", "<p>Это <q>цитата</q>, вложенная в текст</p>")
            .test("Это не \\''цитата'\\'", "<p>Это не ''цитата''</p>")
            .addElement("q", "''");

    private static final Consumer<? super Md2HtmlTester> INS = tester -> tester
            .test("<<вставка>>", "<p><ins>вставка</ins></p>")
            .test("Это <<вставка>>, вложенная в текст", "<p>Это <ins>вставка</ins>, вложенная в текст</p>")
            .test("Это не \\<<вставка>\\>", "<p>Это не &lt;&lt;вставка&gt;&gt;</p>")
            .addElement("ins", "<<", ">>");

    private static final Consumer<? super Md2HtmlTester> DEL = tester -> tester
            .test("}}удаление{{", "<p><del>удаление</del></p>")
            .test("Это }}удаление{{, вложенное в текст", "<p>Это <del>удаление</del>, вложенное в текст</p>")
            .test("Это не \\}}удаление{\\{", "<p>Это не }}удаление{{</p>")
            .addElement("del", "}}", "{{");

    private static final Consumer<? super Md2HtmlTester> PRE = tester -> tester
            .test("```код __без__ форматирования```", "<p><pre>код __без__ форматирования</pre></p>")
            .test(
                    "Это не `\\``код __без__ форматирования``\\`",
                    "<p>Это не <code>`</code>код <strong>без</strong> форматирования<code></code>`</p>"
            )
            .addElement("pre", "```", (checker, markup, input, output) -> {
                final StringBuilder contents = new StringBuilder();
                checker.generate(markup, contents, new StringBuilder());

                while (contents.charAt(contents.length() - 1) == '`') {
                    contents.setLength(contents.length() - 1);
                }

                input.append("```").append(contents).append("```");
                output.append("<pre>").append(contents).append("</pre>");
            });

    public static final ModelessSelector<Md2HtmlTester> SELECTOR = ModelessSelector.create(Md2HtmlTest.class, Md2HtmlTester::new, Md2HtmlTester::test)
            .variant("Base")
            .variant("Var", VAR)
            .variant("Quote", QUOTE)
            .variant("InsDel", INS, DEL)
            .variant("Pre", PRE)
            ;

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
