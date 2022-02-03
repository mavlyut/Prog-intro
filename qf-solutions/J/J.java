import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

class MyScannerQf {
    private final BufferedReader in;
    private String nextLine;
    private StringTokenizer stringTokenizer;

    public MyScannerQf(InputStream inputStream) throws Exception {
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

public class J {
    public static void main(String[] args) throws Exception {
        MyScannerQf in = new MyScannerQf(System.in);
        int n = in.nextInt();
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) {
            String tmp = in.next();
            for (int j = 0; j < n; j++) {
                a[i][j] = Integer.parseInt(tmp.charAt(j) + "");
            }
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (a[i][j] == 1) {
                    for (int k = j + 1; k < n; k++) {
                        a[i][k] = (10 + a[i][k] - a[j][k]) % 10;
                    }
                }
            }
        }
        for (int[] i : a) {
            for (int j : i) {
                System.out.print(j);
            }
            System.out.println();
        }
    }
}