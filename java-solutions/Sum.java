public class Sum {
    public static void main(String[] args) {
        int sum = 0;

        for (String str : args) {
            int left = -1;
            for (int i = 0; i <= str.length(); i++) {
                if (i == str.length() || Character.isWhitespace(str.charAt(i))) {
                    if (left != -1) {
                        sum += Integer.parseInt(str.substring(left, i));
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