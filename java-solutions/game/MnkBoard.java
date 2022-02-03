package game;

public class MnkBoard extends AbstractBoard {
    public MnkBoard(BoardGame board) {
        super(board);
    }

    @Override
    public String toString() {
        final int m = board.m;
        final int n = board.n;
        final int lenM = lengthOfNumber(m) + 1;
        final String spaces = "        ";
        final StringBuilder sb = new StringBuilder().append(spaces, 0, lenM);

        for (int i = 1; i <= n; i++) {
            sb.append(i).append(" ");
        }
        sb.append(System.lineSeparator());

        for (int r = 0; r < m; r++) {
            sb.append(r + 1).append(spaces, 0, lenM - lengthOfNumber(r + 1));

            for (int c = 0; c < n; c++) {
                int lenC = lengthOfNumber(c + 1);
                sb.append(spaces, 0, lenC / 2)
                        .append(BoardGame.CELL_TO_STRING.get(board.field[r][c]))
                        .append(spaces, 0, (lenC + 1) / 2);
            }

            sb.append(System.lineSeparator());
        }

        sb.setLength(sb.length() - System.lineSeparator().length());
        return sb.toString();
    }
}
