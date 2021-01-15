
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;
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
class Board {

    int[][] state;

    public Board() {
        state = new int[3][3];
    }

    public Board(int[][] state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        int hashNum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                hashNum = hashNum * 3 + state[i][j];
            }
        }
        return hashNum;
    }

    @Override
    public boolean equals(Object other) {
        return Arrays.equals((((Board) other).state), this.state);
    }

    public void printBoard() {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] == 0) {
                    System.out.print("-");
                }
                if (state[i][j] == 1) {
                    System.out.print("X");
                }
                if (state[i][j] == 2) {
                    System.out.print("O");
                }

            }
            System.out.println("");
        }
        System.out.println("");
    }

    public ArrayList<int[]> generatePossibleMoves(int newChar) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    int[] arr = {i, j};
                    moves.add(arr);
                }
            }
        }
        return moves;
    }

    public int[][] cloneState() {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(state[i], 0, newState[i], 0, 3);
        }
        return newState;
    }

    // 0 means no winner yet, 1 means X wins, 2 means O wins, 3 means the game is a tie
    public static int checkWinner(Board boardState) {
        int[][] board = boardState.state;
        for (int character = 1; character < 3; character++) {
            if (board[0][0] == board[0][1]
                    && board[0][1] == board[0][2]
                    && board[0][2] == character) {
                return character;
            }
            if (board[1][0] == board[1][1]
                    && board[1][1] == board[1][2]
                    && board[1][2] == character) {
                return character;
            }
            if (board[2][0] == board[2][1]
                    && board[2][1] == board[2][2]
                    && board[2][2] == character) {
                return character;
            }

            if (board[0][0] == board[1][0]
                    && board[1][0] == board[2][0]
                    && board[2][0] == character) {
                return character;
            }
            if (board[0][1] == board[1][1]
                    && board[1][1] == board[2][1]
                    && board[2][1] == character) {
                return character;
            }
            if (board[0][2] == board[1][2]
                    && board[1][2] == board[2][2]
                    && board[2][2] == character) {
                return character;
            }

            if (board[0][0] == board[1][1]
                    && board[1][1] == board[2][2]
                    && board[2][2] == character) {
                return character;
            }
            if (board[2][0] == board[1][1]
                    && board[1][1] == board[0][2]
                    && board[0][2] == character) {
                return character;
            }

        }

        boolean zeroExists = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    zeroExists = true;
                }
            }
        }

        return (zeroExists ? 0 : 3);

    }

    public boolean gameIsContinuing(Board State) {
        return checkWinner(State) == 0;
    }

}

class Player {

    int character;
    String mode;
    ReinforcementLearner learner;

    public Player(int character, String mode) {
        this.character = character;
        this.mode = mode;
    }

    public Player(int character, ReinforcementLearner learner) {
        this.learner = learner;
        this.mode = "learner";
        this.character = character;
    }

    int[] makeMove(Board boardState) {
        if (mode.equals("random")) {
            Random rand = new Random();
            ArrayList<int[]> moves = boardState.generatePossibleMoves(character);
            return moves.get(rand.nextInt(moves.size()));
        }
        if (mode.equals("human")) {
            int[] moves = new int[2];
            Scanner s = new Scanner(System.in);
            System.out.println("Your Move:");
            while (s.hasNextInt()) {
                moves[0] = s.nextInt();
                moves[1] = s.nextInt();
                return moves;
            }

        }
        if (mode.equals("learner")) {
            return learner.makeMove(boardState, character);
            // todo
        }

        return null;

    }

}

class PlayGame {

    HashMap<Board, Board> gameStates;
    Board gameState;
    Player p1;
    Player p2;

    public boolean hasPlayerWon(boolean isPlayer1) {
        return false;
    }

    public PlayGame(boolean isX) {
        gameState = new Board();
        ReinforcementLearner learner = new ReinforcementLearner();
        if (isX) {
            p1 = new Player(1, learner);
            p2 = new Player(2, "human");
            learner.train(gameState, 1, 1);
            System.out.println("hashTable " + learner.boardStates.size());
            System.out.println("hashTable " + learner.boardStates);
        } else {
            p1 = new Player(1, "random");
            p2 = new Player(2, learner);
            learner.train(gameState, 1, 2);
            System.out.println("hashTable " + learner.boardStates.size());
        }

    }

    public void play() {
        int[] move;
        gameState.printBoard();
        Player player = p1;
        while (Board.checkWinner(gameState) == 0) {
            move = player.makeMove(gameState);
            gameState.state[move[0]][move[1]] = player.character;
            player = (player == p1 ? p2 : p1);
            gameState.printBoard();
        }
    }

    // return 0 if p1 won, 1 if p2 won, and 2 if it is a tie. 
}

class ReinforcementLearner {

    Hashtable<Integer, Integer> boardStates;

    public ReinforcementLearner() {
        boardStates = new Hashtable<>();

    }

    public int train(Board boardState, int character, int myCharacter) {
        int otherCharacter = (character == 1 ? 2 : 1);
        int val = Board.checkWinner(boardState);
        // if its not my turn
        if (character != myCharacter) {
            // and if my opponent has NOT won
            if (val == myCharacter || val == 3) {
                boardStates.put(boardState.hashCode(), 1);
            }
        }
        if (val != 0) {
            return val;
        }
        ArrayList<int[]> successorStates = boardState.generatePossibleMoves(character);
        boolean winExists = false;
        boolean allLoss = true;
        for (int[] coord : successorStates) {
            Board nextState = new Board(boardState.cloneState());
            nextState.state[coord[0]][coord[1]] = character;
            int retVal = train(nextState, otherCharacter, myCharacter);
            if (retVal == character) {
                winExists = true;
                allLoss = false;
            }
            if (retVal == 3) {
                allLoss = false;
            }
        }
        if (character == myCharacter) {
            // we are making decision
            if (allLoss) {
                return otherCharacter;
            }
            return character;
        } else {
            if (winExists) {

                return character;
            }
            boardStates.put(boardState.hashCode(), 1);
            return otherCharacter;
            // opponent is making decision
        }
    }

    public int[] makeMove(Board boardState, int character) {
        ArrayList<int[]> moves = boardState.generatePossibleMoves(character);
        int max_val = -1000000;
        int[] ret_move = moves.get(0).clone();
        for (int[] coord : moves) {
            int val;
            Board nextState = new Board(boardState.cloneState());
            nextState.state[coord[0]][coord[1]] = character;
            val = boardStates.containsKey(nextState.hashCode()) ? 1 : 0;
            if (val > max_val) {
                ret_move = coord.clone();
            }
        }
        return ret_move;

    }
}

public class TickTackToe {

    public static void main(String[] args) {
        PlayGame game = new PlayGame(args[0].equals("-X"));
        game.play();

        //test_all_winning_patterns()
    }

}
