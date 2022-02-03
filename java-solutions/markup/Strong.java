package markup;

import java.util.List;

public class Strong extends AbstractParagraphArgument {
    public Strong(List<AbstractParagraphArgument> a) {
        super(a, "__", "strong");
    }
}
