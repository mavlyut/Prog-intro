package markup;

import java.util.List;

public class Emphasis extends AbstractParagraphArgument {
    public Emphasis(List<AbstractParagraphArgument> a) {
        super(a, "*", "em");
    }
}
