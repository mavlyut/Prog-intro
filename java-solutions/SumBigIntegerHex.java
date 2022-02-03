import java.math.BigInteger;

public class SumBigIntegerHex {
    public static void main(String[] args) {
        BigInteger sum = BigInteger.ZERO;

        for (String str : args) {
            int left = -1;
            for (int i = 0; i <= str.length(); i++) {
                if (i == str.length() || Character.isWhitespace(str.charAt(i))) {
                    if (left != -1) {
                        String tmpNum = str.substring(left, i);
                        if (tmpNum.startsWith("0x") || tmpNum.startsWith("0X")) {
                            sum = sum.add(new BigInteger(tmpNum.substring(2), 16));
                        } else {
                            sum = sum.add(new BigInteger(tmpNum));
                        }
                    }
                    left = -1;
                } else if (left == -1) {
                    left = i;
                }
            }
        }

        System.out.println(sum);
    }
}