package com.company;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

class MyScanner {
    private final BufferedReader in;
    private StringTokenizer stringTokenizer;

    public MyScanner(InputStream inputStream) {
        in = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String next() throws Exception {
        try {
            return stringTokenizer.nextToken();
        } catch (NoSuchElementException | NullPointerException e) {
            stringTokenizer = new StringTokenizer(in.readLine(), " ");
            return next();
        }
    }

    public int nextInt() throws Exception {
        return Integer.parseInt(next());
    }

    public void close() throws Exception {
        in.close();
    }
}

public class D {
    public static void main(String[] args) throws Exception {
        new Solution().run();
    }
}

class Solution {
    private int n;
    private int k;
    private final int C = 998_244_353;

    public void run() throws Exception {
        MyScanner in = new MyScanner(System.in);
        n = in.nextInt();
        k = in.nextInt();
        in.close();

        long[] r = new long[n + 1];
        long[] d = new long[n + 1];
        long answer = 0;
        long[] powersOfK = new long[n / 2 + 2];
        powersOfK[0] = 1;
        for (int i = 1; i < powersOfK.length; i++) {
            powersOfK[i] = multi(powersOfK[i - 1], k);
        }
        for (int n0 = 1; n0 <= n; n0++) {
            if (n0 % 2 == 0) {
                r[n0] = multi(multi(k + 1, powersOfK[n0 / 2]), n0 / 2);
            } else {
                r[n0] = multi(n0, powersOfK[(n0 + 1) / 2]);
            }
            d[n0] = r[n0];
            for (int l = 1; l * l <= n0; l++) {
                if (n0 % l == 0 && l != n0) {
                    int l0 = n0 / l;
                    r[n0] -= multi(d[l], l0 - 1);
                    d[n0] -= multi(d[l], l0);
                    r[n0] %= C;
                    d[n0] %= C;
                    if (l * l != n0 && l != 1) {
                        r[n0] -= multi(d[l0], l - 1);
                        d[n0] -= multi(d[l0], l);
                        r[n0] %= C;
                        d[n0] %= C;
                    }
                }
            }
            answer = (answer + r[n0] + C) % C;
        }
        System.out.println(answer % C);
    }

    private long multi(long a, long b) {
        return (a * b) % C;
    }
}