package com.company;

import java.util.*;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class MyScannerK {
    private final BufferedReader in;
    private StringTokenizer stringTokenizer;

    public MyScannerK(InputStream inputStream) {
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

public class K {
    public static void main(String[] args) throws Exception {
        new SolutionK().run();
    }
}

class SolutionK {
    private int[][] a;
    private int[][] busy;
    private int n;
    private int m;

    public void run() throws Exception {
        MyScannerK in = new MyScannerK(System.in);
        n = in.nextInt();
        m = in.nextInt();

        a = new int[n][m];
        busy = new int[n][m];
        for (int i = 0; i < n; i++) {
            String str = in.next();
            for (int j = 0; j < m; j++) {
                a[i][j] = str.charAt(j);
                if (a[i][j] != 46) {
                    busy[i][j] = 1;
                }
            }
        }
        in.close();

        int[] d  = new int[m];
        int[] d1 = new int[m];
        int[] d2 = new int[m];
        for (int i = 0; i < m; i++) {
            d[i] = -1;
            d1[i] = 0;
            d2[i] = 0;
        }

        int indI = 0, indJ = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (a[i][j] == 65) {
                    indI = i;
                    indJ = j;
                    busy[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (i < indI) {
                for (int j = 0; j < indJ; j++) {
                    if (busy[i][j] == 1) {
                        for (int i_ = 0; i_ <= i; i_++) {
                            for (int j_ = 0; j_ <= j; j_++) {
                                busy[i_][j_] = 1;
                            }
                        }
                    }
                }
                for (int j = indJ + 1; j < m; j++) {
                    if (busy[i][j] == 1) {
                        for (int i_ = 0; i_ <= i; i_++) {
                            for (int j_ = j; j_ < m; j_++) {
                                busy[i_][j_] = 1;
                            }
                        }
                    }
                }
            } else if (i > indI) {
                for (int j = 0; j < indJ; j++) {
                    if (busy[i][j] == 1) {
                        for (int i_ = i; i_ < n; i_++) {
                            for (int j_ = 0; j_ <= j; j_++) {
                                busy[i_][j_] = 1;
                            }
                        }
                    }
                }
                for (int j = indJ + 1; j < m; j++) {
                    if (busy[i][j] == 1) {
                        for (int i_ = i; i_ < n; i_++) {
                            for (int j_ = j; j_ < m; j_++) {
                                busy[i_][j_] = 1;
                            }
                        }
                    }
                }
            }
        }

        for (int i = indI - 1; i >= 0; i--) {
            if (busy[i][indJ] == 1) {
                for (int j = 0; j < m; j++) {
                    busy[i][j] = 1;
                }
                break;
            }
        }

        for (int i = indI + 1; i < n; i++) {
            if (busy[i][indJ] == 1) {
                for (int j = 0; j < m; j++) {
                    busy[i][j] = 1;
                }
                break;
            }
        }

        for (int j = indJ - 1; j >= 0; j--) {
            if (busy[indI][j] == 1) {
                for (int i = 0; i < n; i++) {
                    busy[i][j] = 1;
                }
                break;
            }
        }

        for (int j = indJ + 1; j < m; j++) {
            if (busy[indI][j] == 1) {
                for (int i = 0; i < n; i++) {
                    busy[i][j] = 1;
                }
                break;
            }
        }

        int answer = 0;
        Stack<Integer> st = new Stack<>();
        int e1 = 0, e2 = 0, e3 = 0, e4 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (busy[i][j] == 1 && a[i][j] != 65) {
                    d[j] = i;
                }
            }
            while (!st.empty()) {
                st.pop();
            }
            for (int j = 0; j < m; j++) {
                while (!st.empty() && d[st.peek()] <= d[j])  st.pop();
                d1[j] = st.empty() ? -1 : st.peek();
                st.push(j);
            }
            while (!st.empty()) {
                st.pop();
            }
            for (int j = m - 1; j >= 0; j--) {
                while (!st.empty() && d[st.peek()] <= d[j])  st.pop();
                d2[j] = st.empty() ? m : st.peek();
                st.push(j);
            }
            for (int j = 0; j < m; j++) {
                int q = (i - d[j]) * (d2[j] - d1[j] - 1);
                if (q > answer) {
                    answer = q;
                    e1 = d[j] + 1;
                    e2 = d1[j] + 1;
                    e3 = i;
                    e4 = d2[j] - 1;
                }
            }
        }
        for (int i = e1; i <= e3; i++) {
            for (int j = e2; j <= e4; j++) {
                a[i][j] = 65 + (a[i][j] == 46 ? 32 : 0);
                busy[i][j] = 1;
            }
        }

        fillRect(0, 0, e3, e2 - 1);
        fillRect(0, e2, e1 - 1, m - 1);
        fillRect(e3 + 1, 0, n - 1, e4);
        fillRect(e1, e4 + 1, n - 1, m - 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print((char)a[i][j]);
            }
            System.out.println();
        }
    }

    private void fillRect(int x1, int y1, int x2, int y2) {
        for (int j = y1; j <= y2; j++) {
            int ind = -1;
            int tmp_char = 46;
            for (int i = x1; i <= x2; i++) {
                if (a[i][j] != 46) {
                    if (ind == -1) {
                        ind = i;
                    }
                    tmp_char = a[i][j] + 32;
                }
                if (a[i][j] < 65 || a[i][j] > 91) {
                    a[i][j] = tmp_char;
                }
            }
            if (ind != -1) {
                tmp_char = a[ind][j] + (65 <= a[ind][j] && a[ind][j] <= 91 ? 32 : 0);
                for (int i = x1; i < ind; i++) {
                    a[i][j] = tmp_char;
                }
            }
        }
        int k = x1;
        if (x1 == -1) {
            k++;
        }
        if (k > Math.min(m - 1, x2)) {
            k = -1;
        }
        if (k != -1) {
            int ind = -1;
            int tmp_char = 46;
            for (int j = y1; j <= y2; j++) {
                if (a[k][j] == 46) {
                    for (int i = k; i <= x2; i++) {
                        a[i][j] = tmp_char;
                    }
                } else {
                    if (ind == -1) {
                        ind = j;
                    }
                    tmp_char = a[k][j] + (65 <= a[k][j] && a[k][j] <= 91 ? 32 : 0);
                }
            }
            if (ind != -1) {
                tmp_char = a[k][ind] + (65 <= a[k][ind] && a[k][ind] <= 91 ? 32 : 0);
                for (int j = y1; j < ind; j++) {
                    for (int i = k; i <= x2; i++) {
                        a[i][j] = tmp_char;
                    }
                }
            }
        }
    }
}