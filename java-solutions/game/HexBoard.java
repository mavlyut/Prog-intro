package game;

public class HexBoard extends AbstractBoard {
    public HexBoard(BoardGame board) {
        super(board);
    }

    @Override
    public String toString() {
        int m = board.m;
        int n = board.n;
        final int lenM = lengthOfNumber(m) + 1;
        final int lenN = lengthOfNumber(n);
        final StringBuilder sb = new StringBuilder(getSpaces(lenM - 1)).append(1);

        for (int i = 2; i <= n; i++) {
            sb.append(getSpaces(lenN - lengthOfNumber(i) + 1)).append(i);
        }
        sb.append(System.lineSeparator());

        for (int r = 0; r < m; r++) {
            sb.append(r + 1).append(getSpaces(r * 2))
                    .append(getSpaces(lenM - lengthOfNumber(r + 1)));

            for (int c = 0; c < n; c++) {
                sb .append(BoardGame.CELL_TO_STRING.get(board.field[r][c]))
                        .append(getSpaces(lenN));
            }

            sb.append(System.lineSeparator());
        }

        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }

    private String getSpaces(int k) {
        final String spaces = "          ";
        StringBuilder sb = new StringBuilder();
        sb.append(spaces.repeat(Math.max(0, k / spaces.length() + 1)));
        return sb.substring(0, k);
    }
}
