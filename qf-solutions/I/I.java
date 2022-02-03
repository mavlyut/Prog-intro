import java.util.*;

public class I {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int xl = 0, xr = 0, yl = 0, yr = 0;
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int h = in.nextInt();
            if (i == 0) {
                xl = x - h;
                xr = x + h;
                yl = y - h;
                yr = y + h;
            } else {
                xl = Math.min(xl, x - h);
                xr = Math.max(xr, x + h);
                yl = Math.min(yl, y - h);
                yr = Math.max(yr, y + h);
            }
        }
        int h = (Math.max(xr - xl, yr - yl) + 1) / 2;
        System.out.println((xl + xr) / 2 + " " + (yl + yr) / 2 + " " + h);
    }
}