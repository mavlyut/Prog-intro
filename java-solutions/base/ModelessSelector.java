package base;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ModelessSelector<T> {
    private final Class<?> owner;
    private final Supplier<T> tester;
    private final BiConsumer<T, TestCounter> runner;

    private final Map<String, List<Consumer<? super T>>> variants = new LinkedHashMap<>();

    private ModelessSelector(final Class<?> owner, final Supplier<T> tester, final BiConsumer<T, TestCounter> runner) {
        this.owner = owner;
        this.tester = tester;
        this.runner = runner;
    }

    public static <T> ModelessSelector<T> create(final Class<?> owner, final Supplier<T> tester, final BiConsumer<T, TestCounter> runner) {
        return new ModelessSelector<>(owner, tester, runner);
    }

    @SafeVarargs
    public final ModelessSelector<T> variant(final String name, final Consumer<? super T>... operations) {
        Asserts.assertTrue("Duplicate variant " + name, variants.put(name, List.of(operations)) == null);
        return this;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private void check(final boolean condition, final String format, final Object... args) {
        if (!condition) {
            System.err.println("ERROR: " + String.format(format, args));
            System.err.println("Usage: " + owner.getName() + " VARIANT...");
            System.err.println("Variants: " + String.join(", ", variants.keySet()));
            System.exit(1);
        }
    }

    public void main(final String... args) {
        check(args.length >= 1, "At least one argument expected, found %s", args.length);
        final List<String> vars = Arrays.stream(args)
                .flatMap(arg -> Arrays.stream(arg.split("[ +]+")))
                .collect(Collectors.toList());
        if (variants.containsKey("Base") && !vars.contains("Base")) {
            vars.add(0, "Base");
        }

        vars.forEach(var -> check(variants.containsKey(var), "Unknown variant '%s'", var));

        final T test = tester.get();
        vars.forEach(var -> variants.get(var).forEach(v -> v.accept(test)));
        final TestCounter counter = new TestCounter(owner, Map.of("variant", String.join("+", vars)));
        runner.accept(test, counter);
        counter.printStatus();
    }

    public List<String> getVariants() {
        return List.copyOf(variants.keySet());
    }
}
