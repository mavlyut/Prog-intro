package markup;

import java.util.List;

public class Text extends AbstractParagraphArgument {
    private final String text;

    public Text(String str) {
        super(List.of(), "", "");
        text = str;
    }

    @Override
    public void toHtml(StringBuilder tmp) {
        tmp.append(text);
    }

    @Override
    public void toMarkdown(StringBuilder tmp) {
        tmp.append(text);
    }
}
