package md2html;

import java.util.*;

public class ParseText {
    private String s;
    private ArrayList<Node> toTags;
    private StringBuilder ans;
    private Map<Integer, Integer> left;
    private Set<String> openMd;

    private boolean isMarkable(int i) {
        return i < s.length() && switch (s.charAt(i)) {
            case '*', '_', '-', '`', '}', '{', '>', '<' -> true;
            default -> false;
        };
    }

    public StringBuilder toHtmlTag(String s0) {
        s = s0;
        ans = new StringBuilder();
        int tmp = 0, h = 0, len = s.length();
        while (tmp < len && s.charAt(tmp) == '#') {
            tmp++;
        }
        if (tmp > 0 && Character.isWhitespace(s.charAt(tmp))) {
            h = tmp++;
            ans.append("<h").append(h).append(">");
        } else {
            ans.append("<p>");
            tmp = 0;
        }
        s = s.substring(tmp);//.trim();
        len = s.length();

        int i = 0;
        while (i < len) {
            if (i > 0 && s.charAt(i - 1) == '\\' && isMarkable(i)) {
                ans.replace(ans.length() - 1, ans.length(), special(s.charAt(i)));
                i++;
                continue;
            }
            if (isMarkable(i)) {
                i = ifTmpIsMarkable(i);
            } else {
                ans.append(special(i++));
            }
        }
        return ans.append((h > 0) ? "</h" + h + ">" : "</p>");
    }

    private int ifTmpIsMarkable(int i) {
        String mark;
        int nextPos = i + 1;
        if (isMarkable(i + 1) && s.charAt(i) == s.charAt(i + 1)) {
            mark = s.substring(i, i + 2);
            nextPos++;
        } else {
            mark = s.substring(i, i + 1);
        }
        if (openMd.contains(mark) && left.get(getTypeOfMarkable(mark, "open")) == -1) {
            left.put(getTypeOfMarkable(mark, "open"), ans.length());
            ans.append(mark);
            return nextPos;
        }
        int type = getTypeOfMarkable(mark, "close");
        if (type == -1 || left.get(type) == -1) {
            for (int j = 0; j < mark.length(); j++) {
                ans.append(special(mark.charAt(j)));
            }
            return nextPos;
        }

        //если пустой тэг
        if (left.get(type) + mark.length() == ans.length()) {
            for (int j = ans.length() - mark.length(); j < ans.length(); j++) {
                ans.append(special(ans.delete(ans.length() - 1, ans.length()).charAt(0)));
            }
            if (openMd.contains(mark)) {
                left.put(getTypeOfMarkable(mark, "open"), ans.length());
            } else {
                for (int j = 0; j < mark.length(); j++) {
                    ans.append(special(mark.charAt(j)));
                }
                left.put(type, -1);
            }
            return nextPos;
        }

        String openTag = toTags.get(type).getTag() + ">";
        String closeTag = "</" + openTag;
        openTag = "<" + openTag;
        ans.replace(left.get(type), left.get(type) + mark.length(), openTag);
        left.put(type, -1);
        ans.append(closeTag);
        return nextPos;
    }

    private String special(char i) {
        return switch (i) {
            case '<' -> "&lt;";
            case '>' -> "&gt;";
            case '&' -> "&amp;";
            default -> i + "";
        };
    }

    private String special(int i) {
        return special(s.charAt(i));
    }

    private int getTypeOfMarkable(String mark, String mode) {
        if (mode.equals("open")) {
            for (int i = 0; i < toTags.size(); i++) {
                if (toTags.get(i).getOpen().equals(mark)) {
                    return i;
                }
            }
        } else if (mode.equals("close")) {
            for (int i = 0; i < toTags.size(); i++) {
                if (toTags.get(i).getClose().equals(mark)) {
                    return i;
                }
            }
        } else {
            throw new IllegalArgumentException("mode must be equals \"open\" or \"close\"");
        }
        return -1;
    }

    public void initLists() {
        toTags = new ArrayList<>();
        left = new LinkedHashMap<>();
        openMd = new LinkedHashSet<>();
        fillLists("__", "__", "strong");
        fillLists("_", "_", "em");
        fillLists("**", "**", "strong");
        fillLists("*", "*", "em");
        fillLists("--", "--", "s");
        fillLists("`", "`", "code");
        fillLists("}}", "{{", "del");
        fillLists("<<", ">>", "ins");
        reset();
    }

    private void fillLists(String open, String close, String tag) {
        toTags.add(new Node(tag, open, close));
        openMd.add(open);
    }

    public void reset() {
        for (String open : openMd) {
            left.put(getTypeOfMarkable(open, "open"), -1);
        }
    }
}