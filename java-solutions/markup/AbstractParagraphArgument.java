package markup;

import java.util.List;

public abstract class AbstractParagraphArgument extends AbstractMethod implements ParagraphArgument {
    protected AbstractParagraphArgument(List<AbstractParagraphArgument> a, String pref, String tag) {
        super(a, pref, tag);
    }
}
