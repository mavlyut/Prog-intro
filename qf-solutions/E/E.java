package com.company;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class E {
    public static void main(String[] args) throws Exception {
        new SolutionE().run();
    }
}

class MyScannerE {
    private final BufferedReader in;
    private StringTokenizer stringTokenizer;

    public MyScannerE(InputStream inputStream) {
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


class SolutionE {
    private int n, m;
    private ArrayList<ArrayList<Integer>> a;
    private int[] visited;
    private int[] maxLens;

    public void run() throws Exception {
        MyScannerE in = new MyScannerE(System.in);
        n = in.nextInt();
        m = in.nextInt();
        a = new ArrayList<>();
        visited = new int[n];
        maxLens = new int[n];
        for (int i = 0; i < n; i++) {
            a.add(new ArrayList<>());
        }
        for (int i = 0; i < n - 1; i++) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            ArrayList<Integer> tmp = a.get(u);
            tmp.add(v);
            a.set(u, tmp);
            ArrayList<Integer> tmp1 = a.get(v);
            tmp1.add(u);
            a.set(v, tmp1);
        }
        int[] cities = new int[m];
        for (int i = 0; i < m; i++) {
            cities[i] = in.nextInt() - 1;
        }
        in.close();

        doDfs(cities[0]);
        int maxLen = 0;
        for (int i : cities) {
            if (maxLens[i] > maxLen) {
                maxLen = maxLens[i];
            }
        }

        if (maxLen % 2 == 0) {
            System.out.println("NO");
        } else {
            maxLen = 0;
            for (int i : cities) {
                if (i > maxLen) {
                    maxLen = i;
                }
            }
            int midPath = maxLen / 2;
            ArrayList<Integer> f = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (maxLens[i] == midPath - 1) {
                    f.add(i);
                }
            }

            /*System.out.println(maxLen);
            System.out.println(midPath);
            System.out.println(Arrays.toString(maxLens));
            System.out.println(f);*/

            int capital = -1;
            for (int i : f) {
                doDfs(i);
                boolean isCapital = true;
                maxLen = 0;
                for (int j : cities) {
                    if (maxLens[j] > maxLen) {
                        maxLen = maxLens[j];
                    }
                }
                for (int j : cities) {
                    if (j != i) {
                        isCapital = (maxLens[j] == maxLen);
                    }
                    if (!isCapital) {
                        break;
                    }
                }
                if (isCapital) {
                    capital = i;
                    break;
                }
            }
            if (capital == -1) {
                System.out.println("NO");
            } else {
                System.out.println("YES");
                System.out.println(capital + 1);
            }
        }
    }

    private void dfs(int x, int len) {
        visited[x] = 1;
        ArrayList<Integer> tmp = a.get(x);
        for (int i : tmp) {
            if (visited[i] == 0) {
                maxLens[i] = len;
                dfs(i, len + 1);
            }
        }
    }

    private void doDfs(int x) {
        maxLens = new int[n];
        visited = new int[n];
        dfs(x, 0);
    }

    private int midPath(int l, int r) {
        return 2;
    }
}
