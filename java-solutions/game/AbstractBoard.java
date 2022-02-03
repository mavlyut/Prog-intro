package game;

public abstract class AbstractBoard implements Position {
    protected final BoardGame board;

    protected AbstractBoard(BoardGame board) {
        this.board = board;
    }

    @Override
    public Cell getTurn() {
        return board.getTurn();
    }

    @Override
    public int getNumOfPlayers() {
        return board.getNumOfPlayers();
    }

    @Override
    public boolean isValid(Move move) {
        return board.isValid(move);
    }

    protected int lengthOfNumber(final int k) {
        return (int) Math.ceil(Math.log10(k + 1));
    }
}
