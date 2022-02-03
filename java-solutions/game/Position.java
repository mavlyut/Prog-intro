package game;

public interface Position {
    Cell getTurn();
    int getNumOfPlayers();
    boolean isValid(Move move);
}
