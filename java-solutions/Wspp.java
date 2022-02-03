import myBaseFiles.IntList;
import myBaseFiles.MyScanner;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class Wspp {
    public static void main(String[] args) throws IOException {
        MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
        LinkedHashMap<String, IntList> words = new LinkedHashMap<>();
        int cnt = 1;
        while (in.hasNextWord()) {
            String word = in.nextWord();
            IntList tmp = words.getOrDefault(word, new IntList());
            tmp.push(cnt++);
            words.put(word, tmp);
        }
        in.close();

        FileWriter out = new FileWriter(args[1], StandardCharsets.UTF_8);
        for (String word : words.keySet()) {
            out.write(word);
            IntList tmp = words.get(word);
            out.write(" " + tmp.getSize());
            for (int i = 0; i < tmp.getSize(); i++) {
                out.write(" " + tmp.get(i) + "");
            }
            out.write('\n');
        }
        out.close();
    }
}
