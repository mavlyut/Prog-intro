import java.math.*;

public class SumHex {
    public static void main(String[] args) {
        BigInteger sum = BigInteger.ZERO;
        for (String s : args) {
            int left = -1, base = 10;
            s = s + " ";
            for (int i = 0; i < s.length(); i++) {
                int digit = isDigit(s.charAt(i));
                if (digit > 0) {
                    if (left == -1) left = i;
                    if (digit == 2) base = 16;
                } else if (left != -1) {
                    if (base == 16) left += 2;
                    long r = Integer.parseInt(s.substring(left, i), base);
                    sum = sum.add(BigInteger.valueOf(r));
                    left = -1;
                    base = 10;
                }
            }
        }
        System.out.println(sum);
    }

    private static int isDigit(char r) {
        if (48 <= r && r <= 57 || r == '-') {
            return 1;
        }
        if (97 <= r && r <= 102 || r == (int)('x')) {
            return 2;
        }
        return 0;
    }
}