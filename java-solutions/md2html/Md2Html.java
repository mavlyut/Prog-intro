package md2html;

import java.io.IOException;

public class Md2Html {
    public static void main(String[] args) throws IOException {
        new Parser(args[0], args[1]);
    }
}
