package myBaseFiles;

public class Word implements Comparable<Word> {
    private final String str;
    private final int kol;
    private final int num;

    public Word(String str, int kol, int num) {
        this.str = str;
        this.kol = kol;
        this.num = num;
    }

    public int getKol() {
        return kol;
    }

    public int getNum() {
        return num;
    }

    @Override
    public int compareTo(Word b) {
        int ans = this.getKol() - b.getKol();
        if (ans == 0) ans = this.getNum() - b.getNum();
        return Integer.signum(ans);
    }

    @Override
    public String toString() {
        return this.str + " " + this.kol + '\n';
    }
}