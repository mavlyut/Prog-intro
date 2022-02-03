package myBaseFiles;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class NewScanner {
    private static Reader in;
    private static final int SIZE = 10;
    private static final char END = 0;
    private static final char[] buf = new char[SIZE];
    private static int num, pos;
    private static int nextIsEoln;
    private String tmpWord = "";

    public NewScanner(File file) throws IOException {
        in = new FileReader(file, StandardCharsets.UTF_8);
        take();
    }

    public NewScanner(InputStream input) throws IOException {
        in = new InputStreamReader(input);
        take();
    }

    public NewScanner(String text) throws IOException {
        in = new StringReader(text);
        take();
    }

    private char take() throws IOException {
        if (num == -1) {
            return END;
        }
        char toReturn = tmp();
        if (++pos >= num) {
            num = in.read(buf);
            pos = 0;
        }
        return toReturn;
    }

    private char tmp() {
        return buf[pos];
    }

    public String next() throws IOException {
        if (tmpWord.length() > 0) {
            String toReturn = tmpWord;
            tmpWord = "";
            return toReturn;
        }
        skipWhitespaces();
        StringBuilder answer = new StringBuilder();
        while (num != -1 && !Character.isWhitespace(tmp())) {
            answer.append(take());
        }
        nextIsEoln = skipWhitespaces();
        if (answer.isEmpty()) {
            throw new AssertionError("next word not found");
        }
        return answer.toString();
    }

    public boolean hasNext() throws IOException {
        try {
            tmpWord = next();
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public int skipWhitespaces() throws IOException {
        int toReturn = 0;
        while (notEof() && Character.isWhitespace(tmp())) {
            toReturn += isEndOfLine() ? 1 : 0;
            take();
        }
        return toReturn;
    }

    private boolean isEndOfLine() {
        return !notEof() || tmp() == '\n' || tmp() == '\r'
                || (tmp() + "").equals(System.lineSeparator());
    }

    public int eoln() {
        return nextIsEoln;
    }

    public boolean notEof() {
        return num != -1;
    }

    public int nextInt() throws IOException {
        try {
            return strToInt(next());
        } catch (Exception e) {
            throw new IllegalArgumentException("next int not found");
        }
    }

    public static int strToInt(final String str) {
        return (str.startsWith("0x")) ?
            Integer.parseInt(str.substring(2), 16) :
            Integer.parseInt(str);
    }

    public void close() throws IOException {
        in.close();
    }
}
