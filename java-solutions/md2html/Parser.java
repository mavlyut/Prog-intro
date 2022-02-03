package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Parser {
    public Parser(String fileIn, String fileOut) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileIn, StandardCharsets.UTF_8));
        BufferedWriter out = new BufferedWriter(new FileWriter(fileOut, StandardCharsets.UTF_8));

        ParseText parser = new ParseText();
        parser.initLists();
        while (true) {
            String tmp = in.readLine();
            StringBuilder buf = new StringBuilder();
            while (tmp != null && tmp.length() != 0) {
                buf.append(tmp).append('\n');
                tmp = in.readLine();
            }
            parser.reset();
            if (buf.length() > 0) {
                out.write(parser.toHtmlTag(buf.substring(0, buf.length() - 1)).append('\n').toString());
            }
            if (tmp == null) {
                break;
            }
        }

        in.close();
        out.close();
    }
}