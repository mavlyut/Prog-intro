package base;

import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ExtendedRandom {
    public static final String ENGLISH = "abcdefghijklmnopqrstuvwxyz";
    public static final String RUSSIAN = "абвгдеежзийклмнопрстуфхцчшщъыьэюя";
    public static final String GREEK = "αβγŋδεζηθικλμνξοπρτυφχψω";
    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static final String SPACES = " \t\n\u000B\u2029\f";

    private final Random random;

    public ExtendedRandom(final Random random) {
        this.random = random;
    }

    public ExtendedRandom() {
        this(new Random(8045702385702345702L));
    }

    public String randomString(final String chars) {
        return randomChar(chars) + (random.nextBoolean() ? "" : randomString(chars));
    }

    public char randomChar(final String chars) {
        return chars.charAt(nextInt(chars.length()));
    }

    public String randomString(final String chars, final int length) {
        final StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; i++) {
            string.append(randomChar(chars));
        }
        return string.toString();
    }

    public String randomString(final String chars, final int minLength, final int maxLength) {
        return randomString(chars, nextInt(minLength, maxLength + 1));
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    public int nextInt() {
        return random.nextInt();
    }

    public int nextInt(final int min, final int max) {
        return nextInt(max - min) + min;
    }

    public int nextInt(final int n) {
        return random.nextInt(n);
    }

    @SafeVarargs
    public final <T> T randomItem(final T... items) {
        return items[nextInt(items.length)];
    }

    public <T> T randomItem(final List<T> items) {
        return items.get(nextInt(items.size()));
    }

    public Random getRandom() {
        return random;
    }
}
