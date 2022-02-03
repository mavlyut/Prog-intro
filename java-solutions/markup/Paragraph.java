package markup;

import java.util.List;

public class Paragraph extends AbstractMethod implements ListItemArgument {
    public Paragraph(List<ParagraphArgument> a) {
        super(a, "", "");
    }
}
