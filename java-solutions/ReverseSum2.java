import java.io.IOException;
import java.util.Scanner;

public class ReverseSum2 {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        int[][] a = new int[1][1];
        int maxLen = 0;
        int size = 0;
        while (scan.hasNextLine()) {
            if (size == a.length) {
                int[][] b = new int[a.length * 2][1];
                System.arraycopy(a, 0, b, 0, size);
                a = b;
            }
            int sizeTmp = 1;
            Scanner scNum = new Scanner(scan.nextLine());
            while (scNum.hasNextInt()) {
                if (sizeTmp == a[size].length) {
                    int[] b = new int[a[size].length * 2];
                    System.arraycopy(a[size], 0, b, 0, sizeTmp);
                    a[size] = b;
                }
                a[size][sizeTmp++] = scNum.nextInt();
            }
            a[size][0] = sizeTmp;
            maxLen = Math.max(maxLen, a[size][0] - 1);
            size++;
            scNum.close();
        }
        scan.close();
        int[] prSum = new int[maxLen];
        for (int i = 0; i < size; i++) {
            for (int j = 1; j < a[i][0]; j++) {
                prSum[j - 1] += a[i][j];
            }
            int tmpSum = 0;
            for (int j = 0; j < a[i][0] - 1; j++) {
                tmpSum += prSum[j];
                System.out.print(tmpSum);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
}