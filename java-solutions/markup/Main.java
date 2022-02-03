package markup;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Paragraph paragraph = new Paragraph(List.of(
                new Text("1")
        ));
        StringBuilder sb = new StringBuilder();
        paragraph.toMarkdown(sb);
        System.out.println(sb);

        ListItem listItem = new ListItem(List.of(
                paragraph,
                paragraph
        ));
        UnorderedList unorderedList = new UnorderedList(List.of(
                listItem
        ));
        StringBuilder tmp = new StringBuilder();
        unorderedList.toHtml(tmp);
        System.out.println(tmp);
    }
}
