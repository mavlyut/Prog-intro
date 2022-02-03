package H;

import myBaseFiles.IntList;
import myBaseFiles.MyScanner;

import java.util.HashMap;

public class H {
    public static void main(String[] args) throws Exception {
        MyScanner in = new MyScanner(System.in);
        int n = in.nextInt();
        int[] pref = new int[n + 1];
        int maxA = 0;
        int sumA = 0;
        IntList f = new IntList();
        pref[0] = 0;
        for (int i = 0; i < n; i++) {
            int a = in.nextInt();
            for (int j = 0; j < a - 1; j++) {
                f.push(i);
            }
            f.push(i + 1);//((a > 1) ? 1 : 0));
            maxA = Math.max(maxA, a);
            sumA += a;
            pref[i + 1] = pref[i] + a;
        }
        int q = in.nextInt();
        HashMap<Integer, Integer> answers = new HashMap<>();
        for (int i = 0; i < q; i++) {
            int t = in.nextInt();
            int d = answers.getOrDefault(t, -10);
            if (d == -10) {
                if (t < maxA) {
                    answers.put(t, -1);
                } else {
                    int j = 0;
                    int num = 1;
                    while (j + t < sumA) {
                        num++;
                        j = pref[f.get(j + t)];
                    }
                    answers.put(t, num);
                }
            }
            d = answers.get(t);
            System.out.println((d == -1) ? "Impossible" : d);
        }
        in.close();
    }
}