import myBaseFiles.NewScanner;

import java.io.File;
import java.io.IOException;

public class ScannerTest {
    public static void main(String[] args) throws IOException {
        NewScanner in = new NewScanner(new File("in.txt"));
        int num = 0;

        while (in.hasNext()) {
            while (in.hasNext()) {
                if (num > 0) {
                    System.out.print(System.lineSeparator().repeat(num));
                }
                System.out.print(in.next() + " ");
                num = in.eoln();
                if (num == 0) {
                    break;
                }
            }
        }

        in.close();
    }
}
