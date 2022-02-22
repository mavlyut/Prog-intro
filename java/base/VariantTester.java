package base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class VariantTester<V> {
    private final BiConsumer<V, TestCounter> test;
    private final List<V> variants = new ArrayList<>();

    private VariantTester(final BiConsumer<V, TestCounter> test) {
        this.test = test;
    }

    public static <V> Consumer<VariantTester<V>> variant(final V variant) {
        return test -> test.variants.add(variant);
    }

    public static <V> Consumer<VariantTester<Named<V>>> variant(final String name, final V variant) {
        return test -> test.variants.add(Named.of(name, variant));
    }

    public static <V> ModelessSelector<VariantTester<V>> selector(final Class<?> owner, final BiConsumer<V, TestCounter> test) {
        return ModelessSelector.create(owner, () -> new VariantTester<>(test), VariantTester::run);
    }

    private void run(final TestCounter counter) {
        for (final V variant : variants) {
            counter.scope("Testing " + variant.toString(), () -> test.accept(variant, counter));
        }
    }
}
