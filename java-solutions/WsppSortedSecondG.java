import myBaseFiles.IntList;
import myBaseFiles.MyScanner;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class WsppSortedSecondG {
    public static void main(String[] args) throws Exception {
        MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
        TreeMap<String, IntList> words = new TreeMap<>();
        LinkedHashMap<String, IntList> tmpWords = new LinkedHashMap<>();
        int cnt = 1;
        while (in.hasNextLine()) {
            MyScanner in1 = new MyScanner(in.nextLine());
            while (in1.hasNextWord()) {
                String k = in1.nextWord();
                IntList aB = tmpWords.getOrDefault(k, new IntList());
                aB.push(cnt++);
                tmpWords.put(k, aB);
            }
            for (Map.Entry<String, IntList> tmp : tmpWords.entrySet()) {
                int h = 0;
                IntList tmpArray = words.getOrDefault(tmp.getKey(), new IntList());
                tmpArray.setCount(tmpArray.getCount() + tmp.getValue().getSize());
                for (int i = 0; i < tmp.getValue().getSize(); i++) {
                    if (h == 1) {
                        tmpArray.push(tmp.getValue().get(i));
                    }
                    h = 1 - h;
                }
                words.put(tmp.getKey(), tmpArray);
            }
            tmpWords.clear();
        }
        in.close();

        FileWriter writer = new FileWriter(args[1], StandardCharsets.UTF_8);
        for (Map.Entry<String, IntList> tmpWord : words.entrySet()) {
            IntList tmpList = tmpWord.getValue();
            writer.write(tmpWord.getKey() + " " + tmpList.getCount());
            for (int i = 0; i < tmpList.getSize(); i++) {
                writer.write(" " + tmpList.get(i));
            }
            writer.write('\n');
        }
        writer.close();
    }
}