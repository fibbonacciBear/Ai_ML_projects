
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author akashganesan
 */
public class Game {
     static  int DEPTH = 4;

    public static void main(String[] args) {

        playInteractive(args);
        //String[] string = {"first", "X"};
        //playInteractive(args);
        //playInteractive(args);
       // playSelf(args);
    }

    public static void playInteractive(String[] args) {
        System.out.println(Arrays.toString(args));
        Board board = new Board();
        Board.Mark myMark = Board.Mark.O;
        boolean startFirst = true;
        if (args[0].equals("second")) {
            startFirst = false;
        }
        if (args[1].equals("X")) {
            myMark = Board.Mark.X;
        }
        System.out.println(args[1]);
        System.out.println(myMark);
        board.print();
        Scanner scanner = new Scanner(System.in);
        if (startFirst) {
            System.out.println("starting search");
            int move = Position.position(0,2,0);
            board.set(move, myMark);
            System.out.println("My move: " + Position.toString(move));
        }
        boolean gameInProgress = true;
        int moveNum = 1;
        while (gameInProgress) {
            System.out.print("Please enter your move: ");
            String input = scanner.next();
            System.out.println(input);
            int otherMove = getPosition(input);
            board.set(otherMove, myMark.other());
            System.out.println("board has been set");
            if(moveNum % 8 == 0){
                DEPTH++;
            }
            int move = miniMaxDecision(DEPTH, board, myMark);
            board.set(move, myMark);
            System.out.println("My move: " + Position.toString(move));
            gameInProgress = !board.isTerminalState();
            board.print();
        }
        if (board.hasPlayerWon(myMark)) {
            System.out.print("I win: ");
            board.printWinningRow(myMark);

        } else {
            System.out.print("You win: ");
            board.printWinningRow(myMark.other());
        }
    }
    
    
    

    public static void playSelf(String[] args) {
        Board board = new Board();
        Board.Mark myMark = Board.Mark.O;
        boolean startFirst = true;
        if (args[0].equals("second")) {
            startFirst = false;
        }
        if (args[1].equals("X")) {
            myMark = Board.Mark.X;
        }
        System.out.println(args[1]);
        System.out.println(myMark);
        board.print();
        //Scanner scanner = new Scanner(System.in);
        if (startFirst) {
            int move = miniMaxAlphaBeta(DEPTH, board, myMark);
            board.set(move, myMark);
            System.out.println("My move: " + Position.toString(move));
        }
        boolean gameInProgress = true;
        while (gameInProgress) {
            System.out.print("Please enter your move: ");
            //String input = scanner.next();
            int otherMove = miniMaxAlphaBeta(DEPTH, board, myMark.other());
            gameInProgress = !board.isTerminalState();
            if (!gameInProgress) {
                break;
            }
            board.set(otherMove, myMark.other());
            int move = miniMaxAlphaBeta(DEPTH, board, myMark);
            board.set(move, myMark);
            System.out.println("My move: " + Position.toString(move));
            gameInProgress = !board.isTerminalState();
            board.print();
        }
        if (board.hasPlayerWon(myMark)) {
            System.out.print("I win: ");
            board.printWinningRow(myMark);
        } else {
            System.out.print("You win: ");
            board.printWinningRow(myMark.other());
        }
    }

    public static int getPosition(String coordinate) {
        return Position.position(coordinate.charAt(1) - '0', coordinate.charAt(3) - '0', coordinate.charAt(5) - '0');
    }

    // Minimax
    public static int miniMaxDecision(int depth, Board board, Board.Mark mark) {
        int maxMove = Integer.MIN_VALUE; //bogus value
        int maxVal = Integer.MIN_VALUE;
        for (int move : board.generatePossibleMoves()) {
            // System.out.println("move"+ move);
            Board board2 = new Board(board);
            board2.makeMove(move, mark);
            int min = min(depth - 1, board2, mark.other());
            //System.out.println("Min is " + min);
            if (maxVal < min) {
                maxVal = min;
                maxMove = move;
            }
        }
        return maxMove;
    }

    public static int min(int depth, Board board, Board.Mark mark) {
        LinkedList<Integer> moves = board.generatePossibleMoves();
        if(moves.isEmpty()) return 0;
        if (depth == 0 || board.isTerminalState()) {
            return board.completeBoardEvaluationFunction(mark.other(), mark.other());
        }

        int nodeVal = Integer.MAX_VALUE;
        for (Integer position : moves) {
            Board board2 = new Board(board);
            board2.makeMove(position, mark);
            nodeVal = Math.min(nodeVal, max(depth - 1, board2, mark.other()));
        }
        return nodeVal;
    }

    public static int max(int depth, Board board, Board.Mark mark) {
        if (depth == 0 || board.isTerminalState()) {
            return board.completeBoardEvaluationFunction(mark, mark);
        }
        int nodeVal = Integer.MIN_VALUE;
        for (Integer position : board.generatePossibleMoves()) {
            Board board2 = new Board(board);
            board2.makeMove(position, mark);
            nodeVal = Math.max(nodeVal, min(depth - 1, board2, mark.other()));
        }
        return nodeVal;

    }

    // Alpha Beta
    public static int miniMaxAlphaBeta(int depth, Board board, Board.Mark mark) {
        int maxMove = Integer.MIN_VALUE; //bogus value
        int alpha = Integer.MIN_VALUE;
        for (int move : board.generatePossibleMoves()) {
            Board board2 = new Board(board);
            board2.makeMove(move, mark);
            int min = miniAlphaBeta(depth - 1, board2, mark.other(), alpha, Integer.MAX_VALUE);
            if (alpha < min) {
                alpha = min;
                maxMove = move;
            }
        }
        return maxMove;
    }
    
    
//    public static int miniMaxAlphaBeta(int depth, Board board, Board.Mark mark) {
//        int maxMove = Integer.MIN_VALUE; //bogus value
//        int alpha = Integer.MIN_VALUE;
//        long moves = board.generatePossibleMovesLong();
//        System.out.println(moves);
//        while (moves != 0) {
//            long[] arr = Board.getPositionAndMove(moves);
//            moves = arr[0];
//            int move = (int) arr[1];
//
//            board.set(move, mark);
//            int min = miniAlphaBeta(depth - 1, board, mark.other(), alpha, Integer.MAX_VALUE);
//            board.clear(move);
//            if (alpha < min) {
//                alpha = min;
//                maxMove = move;
//            }
//        }
//        return maxMove;
//    }

    public static int miniAlphaBeta(int depth, Board board, Board.Mark mark, int alpha, int beta) {
        if (depth == 0 || board.isTerminalState()) {
            return board.completeBoardEvaluationFunction(mark.other(), mark.other());
        }

        int nodeVal = Integer.MAX_VALUE;
        for (Integer position : board.generatePossibleMoves()) {
            Board board2 = new Board(board);
            board2.makeMove(position, mark);
            nodeVal = Math.min(nodeVal, maxiAlphaBeta(depth - 1, board2, mark.other(), alpha, beta));
            if (nodeVal <= alpha) {
                return nodeVal;
            }
            beta = Math.min(beta, nodeVal);
        }
        return nodeVal;
    }

    public static int maxiAlphaBeta(int depth, Board board, Board.Mark mark, int alpha, int beta) {
        if (depth == 0 || board.isTerminalState()) {
            return board.completeBoardEvaluationFunction(mark, mark);
        }
        int nodeVal = Integer.MIN_VALUE;
        for (Integer position : board.generatePossibleMoves()) {
            Board board2 = new Board(board);
            board2.makeMove(position, mark);
            nodeVal = Math.max(nodeVal, miniAlphaBeta(depth - 1, board2, mark.other(), alpha, beta));
            if (nodeVal >= beta) {
                return nodeVal;
            }
            alpha = Math.max(alpha, nodeVal);
        }
        return nodeVal;

    }
}
