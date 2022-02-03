import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class WordStatInput {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> words = new LinkedHashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(args[0], StandardCharsets.UTF_8));
        StringBuilder s = new StringBuilder();
        while (in.ready()) {
            char tmp = (char) in.read();
            if (isCharacter(tmp)) {
                s.append(tmp);
            } else {
                if (s.length() > 0) {
                    String tmpStr = s.toString().toLowerCase();
                    words.put(tmpStr, words.getOrDefault(tmpStr, 0) + 1);
                }
                s.setLength(0);
            }
        }
        in.close();
        if (s.length() > 0) {
            String tmpStr = s.toString().toLowerCase();
            words.put(tmpStr, words.getOrDefault(tmpStr, 0) + 1);
        }
        FileWriter out = new FileWriter(args[1], StandardCharsets.UTF_8);
        for (Map.Entry<String, Integer> word : words.entrySet()) {
            out.write(word.getKey() + " " + word.getValue() + '\n');
        }
        out.close();
    }

    private static boolean isCharacter(char s) {
        return (Character.getType(s) == Character.DASH_PUNCTUATION || s == '\'' || Character.isLetter(s));
    }
}
