package base;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@SuppressWarnings("StaticMethodOnlyUsedInOneClass")
public final class Triple<F, S, T> {
    public final F first;
    public final S second;
    public final T third;

    public Triple(final F first, final S second, final T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <F, S, T> Triple<F, S, T> of(final F first, final S second, final T third) {
        return new Triple<>(first, second, third);
    }

    public static <F, S, T> UnaryOperator<Triple<F, S, T>> lift(
            final UnaryOperator<F> f,
            final UnaryOperator<S> s,
            final UnaryOperator<T> t
    ) {
        return p -> of(f.apply(p.first), s.apply(p.second), t.apply(p.third));
    }

    public static <F, S, T> BinaryOperator<Triple<F, S, T>> lift(
            final BinaryOperator<F> f,
            final BinaryOperator<S> s,
            final BinaryOperator<T> t
    ) {
        return (p1, p2) -> of(f.apply(p1.first, p2.first), s.apply(p1.second, p2.second), t.apply(p1.third, p2.third));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Triple<?, ?, ?> that = (Triple<?, ?, ?>) o;

        return first.equals(that.first) && second.equals(that.second) && third.equals(that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }
}
