package game;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private final Scanner in;

    public HumanPlayer(int n, int m, int k) {
        this.in = new Scanner(System.in);
    }

    @Override
    public Move makeMove(final Position position) {
        /*((Board) position).makeMove(new Move(0, 0, position.getTurn()));*/

        boolean offerDraw = false;
        System.out.println();
        Cell turn = position.getTurn();
        System.out.printf("%s player (%s) moves:%n", turn == Cell.X ? "FIRST" : "SECOND", turn);
        System.out.println(position);
        System.out.println("Enter your move for " + turn);
        while (true) {
            try {
                Scanner in2 = new Scanner(in.nextLine());
                while (!in2.hasNext()) {
                    in2 = new Scanner(in.nextLine());
                }

                String next = in2.next();
                if (next.equals("give")) {
                    return new Move(-2, -2, turn);
                } else if (next.equals("draw") && !offerDraw) {
                    offerDraw = true;
                    Move turn1 = offeringDraw(position, turn);
                    if (turn1 != null) return turn1;
                    System.out.println("Enter your move for " + turn);
                    continue;
                }

                int x_ = Integer.parseInt(next) - 1;
                while (!in2.hasNext()) {
                    in2 = new Scanner(in.nextLine());
                }
                int y_ = Integer.parseInt(in2.next()) - 1;
                Move move = new Move(x_, y_, turn);
                if (!in2.hasNext() && position.isValid(move)) {
                    return move;
                }
            } catch (Exception ignored) {}
            System.out.println("This move is not valid. Enter valid move.");
        }
    }

    private Move offeringDraw(Position position, Cell turn) {
        if (position.getNumOfPlayers() == 1) {
            return new Move(-1, -1, turn);
        }
        System.out.println((turn == Cell.O ? "FIRST" : "SECOND")
                + " player, do you agree to a draw? [enter y/n] ");
        while (true) {
            String word = in.next();
            if (word.equals("y")) {
                return new Move(-1, -1, turn);
            } else if (word.equals("n")) {
                break;
            }
            System.out.println("[enter y/n]");
        }
        return null;
    }
}
