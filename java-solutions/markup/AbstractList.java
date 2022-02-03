package markup;

import java.util.List;

public abstract class AbstractList extends AbstractMethod implements ListItemArgument {
    protected AbstractList(List<ListItem> a, String tag) {
        super(a, tag);
    }
}
