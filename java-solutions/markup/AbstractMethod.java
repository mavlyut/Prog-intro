package markup;

import java.util.List;

public abstract class AbstractMethod implements MarkdownHtml {
    protected final List<?extends MarkdownHtml> a;
    protected final String openTag, closeTag;
    protected String pref;

    protected AbstractMethod(List<?extends MarkdownHtml> a, String pref, String tag) {
        this.a = a;
        this.pref = pref;
        if (tag.length() > 0) {
            openTag = "<" + tag + ">";
            closeTag = "</" + tag + ">";
        } else {
            openTag = "";
            closeTag = "";
        }
    }

    protected AbstractMethod(List<?extends MarkdownHtml> a, String tag) {
        this.a = a;
        if (tag.length() > 0) {
            openTag = "<" + tag + ">";
            closeTag = "</" + tag + ">";
        } else {
            openTag = "";
            closeTag = "";
        }
    }

    @Override
    public void toHtml(StringBuilder tmp) {
        tmp.append(openTag);
        for (MarkdownHtml i : a) {
            i.toHtml(tmp);
        }
        tmp.append(closeTag);
    }

    @Override
    public void toMarkdown(StringBuilder tmp) {
        tmp.append(pref);
        for (MarkdownHtml i : a) {
            i.toMarkdown(tmp);
        }
        tmp.append(pref);
    }
}
