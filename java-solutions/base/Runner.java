package base;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FunctionalInterface
public interface Runner {
    static Runner args(final String className) {
        final CommentRunner main = main(className);
        return (counter, input) -> {
            final int total = input.stream().mapToInt(String::length).sum();
            final String comment = total <= 1000
                    ? input.stream().collect(Collectors.joining("\" \"", "\"", "\""))
                    : input.size() <= 100
                    ? String.format("[%d arguments, sizes: %s]", input.size(), input.stream()
                        .mapToInt(String::length)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(", ")))
                    : String.format("[%d arguments, total size: %d]", input.size(), total);
            return main.run(comment, counter, input);
        };
    }

    static Runner files(final String className) {
        final Runner args = args(className);
        return (counter, input) -> counter.call("io", () -> {
            final Path inf = counter.getFile("in");
            final Path ouf = counter.getFile("out");
            Files.write(inf, input);
            args.run(counter, List.of(inf.toString(), ouf.toString()));
            final List<String> output = Files.readAllLines(ouf);
            Files.delete(inf);
            Files.delete(ouf);
            return output;
        });
    }

    static Runner std(final String className) {
        final CommentRunner main = main(className);
        return (counter, input) -> counter.call("io", () -> {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (final PrintWriter writer = new PrintWriter(baos)) {
                input.forEach(writer::println);
            }

            final InputStream oldIn = System.in;
            try {
                System.setIn(new ByteArrayInputStream(baos.toByteArray()));
                return main.run(String.format("[%d input lines]", input.size()), counter, List.of());
            } finally {
                System.setIn(oldIn);
            }
        });
    }

    static CommentRunner main(final String className) {
        final Method method;
        try {
            final URL url = new File(".").toURI().toURL();
            method = new URLClassLoader(new URL[]{url}).loadClass(className).getMethod("main", String[].class);
        } catch (final Exception e) {
            throw Asserts.error("Could not find method main(String[]) in class %s", className, e);
        }

        return (comment, counter, input) -> {
            counter.format("Running test %02d: java %s %s\n", counter.getTestNo(), method.getDeclaringClass().getName(), comment);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            @SuppressWarnings("UseOfSystemOutOrSystemErr") final PrintStream oldOut = System.out;
            try {
                System.setOut(new PrintStream(out, false, StandardCharsets.UTF_8));
                method.invoke(null, new Object[]{input.toArray(String[]::new)});
                final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(out.toByteArray()), StandardCharsets.UTF_8));
                final List<String> result = new ArrayList<>();
                while (true) {
                    final String line = reader.readLine();
                    if (line == null) {
                        if (result.isEmpty()) {
                            result.add("");
                        }
                        return result;
                    }
                    result.add(line.trim());
                }
            } catch (final InvocationTargetException e) {
                final Throwable cause = e.getCause();
                throw Asserts.error("main thrown exception %s: %s", cause.getClass().getSimpleName(), cause);
            } catch (final Exception e) {
                throw Asserts.error("Cannot invoke main: %s: %s", e.getClass().getSimpleName(), e.getMessage());
            } finally {
                System.setOut(oldOut);
            }
        };
    }

    List<String> run(final TestCounter counter, final List<String> input);

    default List<String> run(final TestCounter counter, final String... input) {
        return run(counter, Arrays.asList(input));
    }

    default void testEquals(final TestCounter counter, final List<String> input, final List<String> expected) {
        counter.test(() -> {
            final List<String> actual = run(counter, input);
            for (int i = 0; i < Math.min(expected.size(), actual.size()); i++) {
                final String exp = expected.get(i);
                final String act = actual.get(i);
                if (!exp.equalsIgnoreCase(act)) {
                    Asserts.assertEquals("Line " + (i + 1), exp, act);
                    return;
                }
            }
            Asserts.assertEquals("Number of lines", expected.size(), actual.size());
        });
    }

    @FunctionalInterface
    interface CommentRunner {
        List<String> run(String comment, TestCounter counter, List<String> input);
    }
}
