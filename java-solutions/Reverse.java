import myBaseFiles.MyScanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Reverse {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int height = 0;
        int[][] finArray = new int[10][];
        int[] currentArray = new int[10];
        while (sc.hasNextLine()) {
            Scanner scint = new Scanner(sc.nextLine());
            int n = 0;
            while (scint.hasNextInt()) {
                if (n >= currentArray.length) {
                    currentArray = Arrays.copyOf(currentArray, currentArray.length * 2);
                }
                currentArray[n] = scint.nextInt();
                n++;
            }
            if (height >= finArray.length) {
                finArray = Arrays.copyOf(finArray, finArray.length * 2);
            }
            finArray[height] = Arrays.copyOf(currentArray, n);
            height = height + 1;
        }
        finArray = Arrays.copyOf(finArray, height);
        for (int o = finArray.length - 1; o >= 0; o--) {
            for (int u = finArray[o].length - 1; u >= 0; u--) {
                System.out.print(finArray[o][u] + " ");
            }
            System.out.println();
        }
    }
}