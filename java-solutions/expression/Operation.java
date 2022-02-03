package expression;

public interface Operation extends CommonExp {
    String getOperator();
    int getPriority();
}
