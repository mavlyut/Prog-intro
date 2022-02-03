package myBaseFiles;

public class WordStart implements Comparable<WordStart> {
    private final String str;
    private final int num;

    public WordStart(String str, int num) {
        this.str = str;
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    public String getStr() {
        return this.str;
    }

    @Override
    public int compareTo(WordStart a) {
        return this.str.compareTo(a.getStr());
    }

    @Override
    public String toString() {
        return str + " " + num;
    }
}