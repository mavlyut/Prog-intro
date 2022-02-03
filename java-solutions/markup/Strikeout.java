package markup;

import java.util.List;

public class Strikeout extends AbstractParagraphArgument {
    public Strikeout(List<AbstractParagraphArgument> a) {
        super(a, "~", "s");
    }
}
