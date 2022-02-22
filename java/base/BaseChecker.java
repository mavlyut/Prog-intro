package base;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class BaseChecker {
    protected final TestCounter counter;
    protected final ExtendedRandom random = new ExtendedRandom();

    protected BaseChecker(final TestCounter counter) {
        this.counter = counter;
    }

    public final void run(final Runnable tests) {
        tests.run();
        counter.printStatus();
    }
}
