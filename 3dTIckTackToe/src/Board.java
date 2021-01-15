
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author akashganesan
 */
public class Board {
//    public static enum Weight {
//        public static int EQUAL = {1, 1, 1};
//        MAG{1,10,100};
//        LIN{1,2,3};
//    }

    public static enum Mark {

        X("X") {
            public Mark other() {
                return O;
            }
        },
        O("O") {
            public Mark other() {
                return X;
            }
        },
        BLANK(".");

        private String image;

        private Mark(String image) {
            this.image = image;
        }

        public String toString() {
            return image;
        }

        public Mark other() {
            return null;
        }
    }

    private long x;  // Positions of the X's
    private long o;  // Positions of the O's

    public Board() {
        this.x = 0;
        this.o = 0;
    }

    public Board(Board board) {  // Copy construtor
        this.x = board.x;
        this.o = board.o;
    }

    public Board(String s) {     // Inverse of toString()
        this.x = 0;
        this.o = 0;
        int position = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '.':
                    this.set(position, Mark.BLANK);
                    position++;
                    break;

                case 'x':
                case 'X':
                    this.set(position, Mark.X);
                    position++;
                    break;

                case 'o':
                case 'O':
                    this.set(position, Mark.O);
                    position++;
                    break;

                case ' ':  // Row divider
                case '|':  // Level divider
                    break;

                default:
                    throw new IllegalArgumentException("Bad board marker: " + c);
            }
        }
    }

    // Selector/settor methods to determine/alter the contents of a board position.
    public boolean isEmpty(int position) {
        return !Positions.contains(this.x | this.o, position);
    }

    public boolean isEmpty(int x, int y, int z) {
        return isEmpty(Position.position(x, y, z));
    }

    public Mark get(int position) {
        if (Positions.contains(this.x, position)) {
            return Mark.X;
        }
        if (Positions.contains(this.o, position)) {
            return Mark.O;
        }
        return Mark.BLANK;
    }

    public Mark get(int x, int y, int z) {
        return get(Position.position(x, y, z));
    }

    public void set(int position, Mark mark) {
        switch (mark) {
            case X:
                this.x = Positions.add(this.x, position);
                this.o = Positions.remove(this.o, position);
                break;

            case O:
                this.x = Positions.remove(this.x, position);
                this.o = Positions.add(this.o, position);
                break;

            case BLANK:
                this.x = Positions.remove(this.x, position);
                this.o = Positions.remove(this.o, position);
                break;

            default:
                throw new IllegalArgumentException(mark.toString());
        }
    }
    
    public void makeMove(int position, Mark mark){
        if(isEmpty(position)){
            set(position,mark);
            return;
        } 
        System.out.println("ILLEGAL MOVE MAKE ANOTHER");
    }
    public void makeMove(int x, int y, int z, Mark mark){
        if(isEmpty(x,y,z)){
            set(x,y,z,mark);
            return;
        }
        System.out.println("ILLEGAL MOVE MAKE ANOTHER");
    }
    
    

    public void set(int x, int y, int z, Mark mark) {
        set(Position.position(x, y, z), mark);
    }

    public void clear(int position) {
        this.x = Positions.remove(this.x, position);
        this.o = Positions.remove(this.o, position);
    }

    public void clear(int x, int y, int z) {
        clear(Position.position(x, y, z));
    }

    public int occupied(long positions, Mark player) {

        // Returns the number of positions occupied by the specified 
        // player within a given set of board positions.  Useful
        // when combined with the Lines class to determine the
        // number of markers along given line.
        switch (player) {
            case X:
                return Positions.count(this.x & positions);
            case O:
                return Positions.count(this.o & positions);
            case BLANK:
                return Positions.count(~(this.x | this.o) & positions);
            default:
                throw new IllegalArgumentException(player.toString());
        }
    }
    
    public LinkedList<Integer> generatePossibleMoves(){
        LinkedList<Integer> moves = new LinkedList<>();
        int pos = 0;
        for(long mask = 1; mask != 0; mask <<= 1){
            if((~this.x & ~this.o & mask) != 0){
                moves.add(pos);
            }
            pos++;
        }
        return moves;
    }
    
    public static long[] getPositionAndMove(long moves){
        //System.out.println("moves: " + moves);
        long pos = moves - (moves & (moves - 1));
        //System.out.println("moves: " + moves);
       
        int move = -1;
        while(pos != 0){
            move ++;
            pos = pos << 1;
          //  System.out.println("move: " + move);
        }
        move = 64 - move;
        
        moves = moves & (moves - 1);
        //System.out.println("moves: " + moves);
        return new long[] {moves, move};
        
    }
    
    public long generatePossibleMovesLong(){
       return ~(this.x | this.o);
        
    }
    
    

    // Matching equality and hashCode methods.
    public boolean equals(Board other) {
        return this.x == other.x && this.o == other.o;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Board && equals((Board) other);
    }

    @Override
    public int hashCode() {
        return ((Long) this.x).hashCode() + ((Long) this.o).hashCode();
    }

    // toString produces a string that can be passed to the constructor.
    @Override
    public String toString() {
        String result = "";

        for (int position = 0; position < 64; position++) {
            if (position > 0) {
                if (position % 16 == 0) {  // New level
                    result += '|';
                } else if (position % 4 == 0) {
                    result += ' ';         // New row
                }
            }
            result += get(position);
        }
        return result;
    }

    public boolean hasPlayerWon(Mark mark) {
        long player;
        if (mark == Mark.X) {
            player = x;
        } else {
            player = o;
        }
        for (Line line : Line.lines()) {
            if (line.positions() == (line.positions() & player)) {
                return true;
            }
        }
        return false;
    }
    
    public void printWinningRow(Mark winningMark){
         long player;
        if (winningMark == Mark.X) {
            player = x;
        } else {
            player = o;
        }
        for (Line line : Line.lines()) {
            if (line.positions() == (line.positions() & player)) {
                System.out.println( "Along line " + line.toString());
            }
        }
        
    }

    public int numOfOnesInLong(long num) {
        int count = 0;
        while (num != 0) {
            num = num & (num - 1);
            count++;

        }
        return count;
    }
    
    

    public int boardEvaluationFunction(Mark playerEvaluating, Mark playerTurn) {
        long player;
        if (playerEvaluating == Mark.X) {
            player = x;
        } else {
            player = o;
        }
        //this.print();
        
        int numOfOneInARow = 0;
        int numOfTwoInARow = 0;
        int numOfThreeInARow = 0;
        int numOfFourInARow = 0;
        
        for (Line line : Line.lines()) {
            int num1s = numOfOnesInLong(line.positions() & player);
            if(num1s == 1) numOfOneInARow++;
            if(num1s == 2) numOfTwoInARow++;
            if(num1s == 3) numOfThreeInARow++;
            if(num1s == 4) numOfFourInARow++;
        }
        if(numOfFourInARow > 0) return Integer.MAX_VALUE/2;
        if(numOfThreeInARow > 0 && playerEvaluating == playerTurn) return Integer.MAX_VALUE/3;
        //System.out.println(numOfOneInARow + " " + numOfTwoInARow + " " + numOfThreeInARow);
        return numOfOneInARow + 10 * numOfTwoInARow + 100 * numOfThreeInARow;


    }
    
//    public int completeBoardEvaluationFunction(Mark playerEvaluating){
//        int result = boardEvaluationFunction(playerEvaluating) -  boardEvaluationFunction(playerEvaluating.other());
//        System.out.println("cbe called " + result);
//        return result;
//    }
    
    public int completeBoardEvaluationFunction(Mark startingPlayer, Mark currentPlayer){
        int result = boardEvaluationFunction(startingPlayer, currentPlayer) -  boardEvaluationFunction(startingPlayer.other(), currentPlayer);
        //System.out.println("cbe called " + result);
        return result;
    }
    
    public boolean isTerminalState(){
        for (Line line : Line.lines()) {
            int num1so = numOfOnesInLong(line.positions() & o);
            int num1x = numOfOnesInLong(line.positions() & x);
            if(num1so == 4 || num1x == 4) return true;
        }
        
        
        
        return false;
    }
    
    
    
    

    // A more readable display of the 3D tic tac toe board.
    public void print() {
        System.out.println();
        for (int level = 0; level < 4; level++) {
            System.out.print(" Z = " + level + "    ");
        }
        System.out.println();
        for (int row = 3; row >= 0; row--) {
            for (int level = 0; level < 4; level++) {
                for (int column = 0; column < 4; column++) {
                    System.out.print(get(column, row, level));
                    System.out.print(" ");
                }
                System.out.print("  ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
