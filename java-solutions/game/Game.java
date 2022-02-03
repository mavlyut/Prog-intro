package game;

public class Game {
    private final Board board;
    private final Player player1, player2;

    public Game(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int play(boolean log) {
        int no = 1;
        while (true) {
            final int result = makeMove((no == 1) ? player1 : player2, no, log);
            if (result != -1) {
                System.out.println("\nFINAL POSITION");
                System.out.println(board.getPosition());
                return result;
            }
            no = 3 - no;
        }
    }

    private int makeMove(Player player, int no, boolean log) {
        final Move move = player.makeMove(board.getPosition());
        final GameResult result = board.makeMove(move);
        if (log) {
            System.out.println();
            System.out.println("Player " + no);
            System.out.println(move);
            System.out.println(board);
            System.out.println("Result: " + result);
        }
        return switch (result) {
            case WIN -> no;
            case LOOSE -> 3 - no;
            case DRAW -> 0;
            case UNKNOWN -> -1;
        };
    }
}
