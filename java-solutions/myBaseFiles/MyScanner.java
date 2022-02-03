package myBaseFiles;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

public class MyScanner {
    private final Reader in;

    private final int SIZE = 1000;
    private final char[] tmp = new char[SIZE];
    private int i = 0;
    private int kol;

    private final StringBuilder tmpWordStat = new StringBuilder();
    private final StringBuilder tmpWord = new StringBuilder();
    private final StringBuilder tmpStr = new StringBuilder();

    public MyScanner(String fileName, Charset charset) throws IOException {
        in = new FileReader(fileName, charset);
    }

    public MyScanner(InputStream inputStream) throws IOException {
        in = new InputStreamReader(inputStream);
        kol = in.read(tmp);
    }

    public MyScanner(String inputStr) throws IOException {
        in = new StringReader(inputStr);
        kol = in.read(tmp);
    }

    private void reading() throws IOException {
        i++;
        if (i >= kol) {
            kol = in.read(tmp);
            i = 0;
        }
    }

    public int skipWhitespaces() throws IOException {
        int toReturn = 0;
        while (!isEndOfLine(tmp[i]) && Character.isWhitespace(tmp[i])) {
            toReturn += isEndOfLine(tmp[i]) ? 1 : 0;
            reading();
        }
        return toReturn;
    }

    //методы NEXT
    private void nextIfTmpWordIsEmpty() throws IOException {
        while (kol != -1 && (isCharacterOrDigit(tmp[i]) || tmpWord.isEmpty())) {
            if (isCharacterOrDigit(tmp[i])) {
                tmpWord.append(tmp[i]);
            }
            reading();
        }
    }

    public String next() throws IOException {
        if (tmpWord.isEmpty()) {
            nextIfTmpWordIsEmpty();
        }
        String toReturn = tmpWord.toString().toLowerCase();
        tmpWord.setLength(0);
        return toReturn;
    }

    public boolean hasNext() throws IOException {
        if (kol == -1) {
            return false;
        }
        if (tmpWord.isEmpty()) {
            nextIfTmpWordIsEmpty();
        }
        return !tmpWord.isEmpty();
    }

    //методы NEXT WORD
    private void nextIfTmpWordStatIsEmpty() throws IOException {
        while (kol != -1 && (isCharacter(tmp[i]) || tmpWordStat.isEmpty())) {
            if (isCharacter(tmp[i])) {
                tmpWordStat.append(tmp[i]);
            }
            reading();
        }
    }

    public String nextWord() throws IOException {
        if (tmpWordStat.isEmpty()) {
            nextIfTmpWordStatIsEmpty();
        }
        String toReturn = tmpWordStat.toString().toLowerCase();
        tmpWordStat.setLength(0);
        return toReturn;
    }

    public boolean hasNextWord() throws IOException {
        if (kol == -1) {
            return false;
        }
        if (tmpWordStat.isEmpty()) {
            nextIfTmpWordStatIsEmpty();
        }
        return !tmpWordStat.isEmpty();
    }

    //методы NEXT INT
    public int nextInt() throws IOException {
        return nextIntMOD(next());
    }

    private int nextIntMOD(String num) {
        boolean inAbc = true;
        long answer = 0;
        int sgn = 1;
        if (num.charAt(0) == '-') {
            sgn = -1;
            num = num.substring(1);
        }
        for (int i = 0; i < num.length(); i++) {
            int tmpDigit = (int) num.charAt(i) - 97;
            if (tmpDigit < 0 || tmpDigit > 9) {
                inAbc = false;
                break;
            }
            answer = 10 * answer + tmpDigit;
        }
        if (inAbc) {
            return sgn * (int) answer;
        }
        try {
            return sgn * (int) Long.parseLong(num);
        } catch (NumberFormatException e) {
            return sgn * (int) Long.parseLong(num.substring(2), 16);
        }
    }

    public boolean hasNextInt() throws IOException, NumberFormatException {
        if (!hasNext()) {
            return false;
        }
        try {
            nextIntMOD(tmpWord.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //методы NEXT LINE
    private void nextLineIfTmpIsEmpty() throws IOException {
        while (kol != -1) {
            tmpStr.append(tmp[i]);
            char stop = tmp[i];
            reading();
            if (isEndOfLine(stop)) {
                reading();
                break;
            }
        }
    }

    public boolean hasNextLine() throws IOException {
        if (tmpStr.length() == 0) {
            nextLineIfTmpIsEmpty();
        }
        return tmpStr.length() > 0;
    }

    public String nextLine() throws IOException {
        if (tmpStr.length() == 0) {
            nextLineIfTmpIsEmpty();
        }
        String toReturn = tmpStr.toString().toLowerCase();
        tmpStr.setLength(0);
        return toReturn;
    }

    //проверка на конец строки
    private boolean isEndOfLine(char r) {
        return r == '\n' || r == '\r' || (r + "").equals(System.lineSeparator());
    }

    public int isEndOfLine() {
        return kol;
    }

    private boolean isCharacter(char s) {
        return (
                Character.getType(s) == Character.DASH_PUNCTUATION ||
                        Character.isLetter(s) ||
                        s == '\''
        );
    }

    private boolean isCharacterOrDigit(char s) {
        return isCharacter(s) || Character.isDigit(s);
    }

    //закрытие сканера
    public void close() throws IOException {
        in.close();
    }
}