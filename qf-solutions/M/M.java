import java.util.*;

class Int {
    private final int a;

    public Int(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }
}

public class M {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (t-- > 0) {
            int n = in.nextInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }
            HashMap<Integer, Integer> c = new HashMap<>();
            int answer = 0;
            for (int j = n - 1; j >= 0; j--) {
                for (int i = 0; i < j; i++) {
                    answer += c.getOrDefault(2 * a[j] - a[i], 0);
                }
                c.put(a[j], c.getOrDefault(a[j], 0) + 1);
            }
            c.clear();
            System.out.println(answer);
        }
    }
}