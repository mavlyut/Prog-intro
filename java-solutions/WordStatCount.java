import myBaseFiles.Word;
import myBaseFiles.WordStart;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordStatCount {
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0], StandardCharsets.UTF_8));
            List<WordStart> words0 = new ArrayList<>();
            int i = 0;
            StringBuilder tmpWord = new StringBuilder();
            int tmpChar = in.read();
            while (tmpChar != -1) {
                if (isCharacter((char)tmpChar)) {
                    tmpWord.append(toLower(tmpChar));
                } else {
                    if (tmpWord.length() > 0) {
                        words0.add(new WordStart(tmpWord.toString(), i++));
                    }
                    tmpWord.setLength(0);
                }
                tmpChar = in.read();
            }
            if (tmpWord.length() > 0) {
                words0.add(new WordStart(tmpWord.toString(), i++));
            }
            in.close();

            Collections.sort(words0);
            words0.add(new WordStart(";", i));
            List<Word> words = new ArrayList<>();
            int tmp = 1, ind = 0;
            for (int j = 0; j < i; j++) {
                if (words0.get(j).getStr().equals(words0.get(j + 1).getStr())) {
                    tmp++;
                } else {
                    words.add(new Word(words0.get(j).getStr(), tmp, words0.get(ind).getNum()));
                    tmp = 1;
                    ind = j + 1;
                }
            }

            Collections.sort(words);
            try (FileWriter writer = new FileWriter(args[1], StandardCharsets.UTF_8)) {
                for (Word word : words) {
                    writer.write(word.toString());
                    writer.flush();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static char toLower(int c) {
        return ((char)c + "").toLowerCase().charAt(0);
    }

    private static boolean isCharacter(char s) {
        return (Character.getType(s) == Character.DASH_PUNCTUATION || s == '\'' || Character.isLetter(s));
    }
}