import myBaseFiles.MyScanner;

import java.util.Arrays;

public class ReverseHexAbc2 {
    public static void main(String[] args) throws Exception {
        MyScanner scan = new MyScanner(System.in);
        int[][] a = new int[1][1];
        int max_len = 0;
        int size = 0;
        while (scan.hasNextLine()) {
            MyScanner scan_int = new MyScanner(scan.nextLine());
            if (size == a.length) {
                a = Arrays.copyOf(a, size * 2);
            }
            a[size] = new int[1];
            int size_tmp = 1;
            while (scan_int.hasNextInt()) {
                if (size_tmp == a[size].length) {
                    a[size] = Arrays.copyOf(a[size], size_tmp * 2);
                }
                a[size][size_tmp++] = scan_int.nextInt();
            }
            a[size++][0] = size_tmp - 1;
            max_len = Math.max(max_len, size_tmp - 1);
            scan_int.close();
        }
        scan.close();

        for (int i = size - 1; i >= 0; i--) {
            for (int j = a[i][0]; j > 0; j--) {
                System.out.print(modification(a[i][j]) + " ");
            }
            System.out.print(System.lineSeparator());
        }
    }

    private static String modification(int num) {
        if (num == 0) {
            return "a";
        }
        StringBuilder ans = new StringBuilder();
        if (num < 0) {
            ans.append('-');
            num = -num;
        }
        String num_str = Integer.toString(num);
        for (int i = 0; i < num_str.length(); i++) {
            ans.append((char)(num_str.charAt(i) - '0' + 'a'));
        }
        return ans.toString();
    }
}
