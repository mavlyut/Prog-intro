import java.util.*;

public class IntList {
    private int[] a;
    private int size;
    private int count;

    public IntList() {
        a = new int[1];
        size = 0;
        count = 0;
    }

    public IntList(int x) {
        size = 1;
        count = 1;
        a = new int[1];
        a[0] = x;
    }

    public IntList(int[] b, int count) {
        size = b.length;
        a = Arrays.copyOf(b, size);
        this.count = count;
    }

    public void push(int x) {
        if (a.length == size) {
            a = Arrays.copyOf(a, size * 2 + 1);
        }
        a[size++] = x;
    }

    public int[] getA() {
        return Arrays.copyOf(a, size);
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }
}
