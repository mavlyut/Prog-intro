package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final int m, n;
    private final Random random = new Random();

    public RandomPlayer(int m, int n, int k) {
        this.m = m;
        this.n = n;
    }

    @Override
    public Move makeMove(Position position) {
        while (true) {
            final Move move = new Move(
                    random.nextInt(m),
                    random.nextInt(n),
                    position.getTurn()
            );
            if (position.isValid(move)) {
                return move;
            }
        }
    }
}
