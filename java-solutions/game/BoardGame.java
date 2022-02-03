package game;

import java.util.Arrays;
import java.util.Map;

public class BoardGame implements Board {
    protected static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, ".",
            Cell.X, "X",
            Cell.O, "0"
    );
    protected final Cell[][] field;
    private Cell turn;
    protected final int m, n, k;
    private final int numOfPlayers;
    private int emptyCell, row, col;
    private final String gameMode;

    protected BoardGame(int m, int n, int k, int numOfPlayers, String gameMode) {
        this.m = m;
        this.n = n;
        this.k = k;
        this.numOfPlayers = numOfPlayers;
        field = new Cell[m][n];
        for (Cell[] row : field) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        emptyCell = n * m;
        this.gameMode = gameMode;
    }

    @Override
    public GameResult makeMove(Move move) {
        row = move.getRow();
        col = move.getCol();
        if (row == -1 && col == -1) {
            return GameResult.DRAW;
        }

        if (!isValid(move)) {
            return GameResult.LOOSE;
        }
        field[row][col] = move.getValue();
        emptyCell--;
        if (checkWin()) {
            return GameResult.WIN;
        }
        if (emptyCell == 0) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;

        return GameResult.UNKNOWN;
    }

    protected int cntInLine(int dx, int dy) {
        int cnt = 0;
        for (int i = 0; i <= k; i++) {
            int x_ = row + i * dx;
            int y_ = col + i * dy;
            if (0 <= x_ && x_ < m && 0 <= y_ && y_ < n && field[x_][y_] == turn) {
                cnt++;
            } else {
                return cnt;
            }
        }
        return cnt;
    }

    @Override
    public boolean checkWin() {
        return cntInLine(1, 0) + cntInLine(-1, 0) > k
                || cntInLine(0, 1) + cntInLine(0, -1) > k
                || cntInLine(1, -1) + cntInLine(-1, 1) > k
                || (gameMode.equals("mnk") && cntInLine(1, 1) + cntInLine(-1, -1) > k);
    }

    protected boolean isValid(final Move move) {
        return  0 <= move.getRow() && move.getRow() < m &&  0 <= move.getCol() && move.getCol() < n
                && field[move.getRow()][move.getCol()] == Cell.E && turn == move.getValue();
    }

    protected Cell getTurn() {
        return turn;
    }

    protected int getNumOfPlayers() {
        return numOfPlayers;
    }

    @Override
    public Position getPosition() {
        return switch (gameMode) {
            case "hex" -> new HexBoard(this);
            case "mnk" -> new MnkBoard(this);
            default -> throw new AssertionError("Unknown game mode");
        };
    }
}
