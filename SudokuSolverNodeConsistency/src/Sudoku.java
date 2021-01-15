
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author akashganesan
 */
public class Sudoku {

    public static class Board {

        private int[][] values;
        private ConstrainedSatesfactionProblem solution;
        private int n;
        private int m;

        private int sqrt(int n) {
            // Returns ceiling(sqrt(n))
            int result = 1;
            while (result * result < n) {
                result++;
            }
            return result;
        }

        public Board(int n) { // Board of size n x n
            this.values = new int[n][n];
            this.m = sqrt(n);
            this.n = n;
            assert m * m == n;
        }
        
        public ConstrainedSatesfactionProblem generateCSP(){
            ConstrainedSatesfactionProblem problem = new ConstrainedSatesfactionProblem(this.m);
            for(int row = 0; row < n; row++){
                for(int col = 0; col< n; col++){
                    if(values[row][col] != 0 ){
                        problem.SetDomainAndChangePeersDomain(row, col, values[row][col]);
                    }
                }
            }
            return problem;
        }

        public Board(Board board) { // Copy constructor
            this.n = board.n;
            this.m = board.m;
            this.values = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    this.values[i][j] = board.values[i][j];
                }
            }
        }
        
        public void copyCSPIntoValues( ConstrainedSatesfactionProblem problem){
            for(int row = 0; row < n; row++){
                for(int col = 0; col < n; col++){
                   values[row][col] = (int)problem.domains[row][col].stream().findAny().get();
                }
            }
        }

        public Board(String s) {  // Constructor from string
            String[] values = s.split("[ \\-\\+|\\n]+");
            this.n = sqrt(values.length);
            this.m = sqrt(n);
            if (m * m != n) {
                throw new IllegalArgumentException();
            }
            this.values = new int[n][n];
            int k = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    String val = values[k++].trim();
                    if (!val.equals(".")) {
                        int value = Integer.parseInt(val);
                        if (value <= 0 | value > n) {
                           // value = value + '0' + 10 -'A';
                            //this.values[i][j] = value ;
                            System.out.println("BADB BAD ");
                        } else{
                            this.values[i][j] = value;
                        }
                        
                    }
                }
            }
        }

        public int size() {
            return this.n;
        }

        public int blockSize() {
            return this.m;
        }

        public boolean isEmpty(int row, int col) {
            return this.values[row][col] == 0;
        }

        public int get(int row, int col) {
            return this.values[row][col];
        }

        public void set(int row, int col, int value) {
            this.values[row][col] = value;
        }

        public boolean isValidRow(int row) {
            // Check no duplicate digits in specified row
            boolean[] used = new boolean[n + 1];
            for (int col = 0; col < n; col++) {
                int digit = this.values[row][col];
                if (digit > 0 && used[digit]) {
                    return false;
                }
                used[digit] = true;
            }
            return true;
        }

        public boolean isValidColumn(int col) {
            // Check no duplicate digits in specified column
            boolean[] used = new boolean[n + 1];
            for (int row = 0; row < n; row++) {
                int digit = this.values[row][col];
                if (digit > 0 && used[digit]) {
                    return false;
                }
                used[digit] = true;
            }
            return true;
        }

        public boolean isValidBlock(int row, int col) {
            // Check no duplicate digits in block containing specified row, col
            boolean[] used = new boolean[n + 1];
            row = row / m * m;
            col = col / m * m;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    int digit = this.values[row + i][col + j];
                    if (digit > 0 && used[digit]) {
                        return false;
                    }
                    used[digit] = true;
                }
            }
            return true;
        }

        public boolean isValidBoard() {
            // Check entire values for validity
            for (int i = 0; i < n; i++) {
                if (!isValidRow(i)) {
                    return false;
                }
                if (!isValidColumn(i)) {
                    return false;
                }
            }
            for (int i = 0; i < n; i += m) {
                for (int j = 0; j < n; j += m) {
                    if (!isValidBlock(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public void print() {
            for (int row = 0; row < n; row++) {
                if (row > 0 && row % m == 0) {
                    for (int col = 0; col < n; col++) {
                        if (col > 0 && col % m == 0) {
                            System.out.print("+-");
                        }
                        System.out.print("---");
                    }
                    System.out.println();
                }
                for (int col = 0; col < n; col++) {
                    int value = this.values[row][col];
                    if (col > 0 && col % m == 0) {
                        System.out.print("| ");
                    }
                    if (value > 0) {
                        System.out.printf("%2d ", value);
                    } else {
                        System.out.print(" . ");
                    }
                }
                System.out.println();
            }
        }

        public int[] findFirstEmpty() {
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    if (values[row][col] == 0) {
                        int[] arr = {row, col};
                        return arr;
                    }
                }
            }
            int[] arr = {-1, -1};
            return arr;
        }

        public boolean solve() {
            int[] empty = findFirstEmpty();
            if (empty[0] == -1) {
                return true;
            }
            for (int i = 1; i <= n; i++) {
                values[empty[0]][empty[1]] = i;
                if (isValidRow(empty[0]) && isValidColumn(empty[1]) && isValidBlock(empty[0], empty[1])) {
                    if (solve()) {
                        return true;
                    }
                }

            }
            values[empty[0]][empty[1]] = 0;
            return false;

        }

    
    
    public boolean betterSolve(ConstrainedSatesfactionProblem problem) {
            int[] empty = problem.findMRV();
            if (empty[0] == -1) {
                copyCSPIntoValues(problem);
                return isValidBoard();
            }
            for (Iterator it = problem.domains[empty[0]][empty[1]].iterator(); it.hasNext();) {
               int val = (int) it.next();
               ConstrainedSatesfactionProblem problem2 = new ConstrainedSatesfactionProblem(problem);
               if(!problem2.SetDomainAndChangePeersDomain(empty[0], empty[1], val)) continue;
               if(betterSolve(problem2)){
                   return true;
               }
               
            }
            return false;

        }
    
    
    
    public void fullySolve(){
        ConstrainedSatesfactionProblem problem = generateCSP();
        betterSolve(problem);
        print();
        System.out.println("isValid   " + isValidBoard());
    }
    
    
    }
    public static void main(String[] args) {

        String s =  ". 6 . . | . . . 8 | 11 . . 15 | 14 . . 16 \n"
                + "15 11 . . | . 16 14 . | . . 12 . | . 6 . . \n" 
                + "13 . 9 12 | . . . . | 3 16 14 . | 15 11 10 . \n" 
                + "2 . 16 . | 11 . 15 10 | 1 . . .| . . . . \n"
                + "------+------+------+------ \n" 
                + ". 15 11 10 | . . 16 2 | 13 8 9 12 | . . . . \n"
                + "12 13 . . | 4 1 5 6 | 2 3 . .| . . 11 10 \n" 
                + "5 . 6 1 | 12 . 9 . | 15 11 10 7 | 16 . . 3 \n"
                + ". 2 . . | . 10 . 11 | 6 . 5 . | . 13 . 9 \n"
                + "------+------+------+------ \n"  
                + "10 7 15 11 |16 . . .|12 13 . .| . . . 6 \n"
                + "9 . . . |. . 1 .|. 2 . 16| 10 . . 11 \n"
                + "1 . 4 6 |9 13 . .|7 . 11 .| 3 16 . . \n"
                + "16 14 . . |7 . 10 15|4 6 1 .| . . 13 8 \n"
                + "------+------+------+------ \n"  
                + "11 10 . 15| . . . 16|9 12 13 .| . 1 5 4 \n"
                + ". . 12 .  | 1 4 6 . |16 . . .|11 10 . . \n"
                + ". .  5 .  |8 12 13 .|10 . . 11| 2 . . 14 \n"
                + " 3 16 . . |10 . . 7 | . . 6 . | . . 12 . \n";

        String s_hard = ""
                + "4 . . |. . . |8 . 5 \n"
                + ". 3 . |. . . |. . . \n"
                + ". . . |7 . . |. . . \n"
                + "------+------+----- \n"
                + ". 2 . |. . . |. 6 . \n"
                + ". . . |. 8 . |4 . . \n"
                + ". . . |. 1 . |. . . \n"
                + "------+------+----- \n"
                + ". . . |6 . 3 |. 7 . \n"
                + "5 . . |2 . . |. . . \n"
                + "1 . 4 |. . . |. . . ";
        
        s = s_hard;
        
        
        System.out.println(s);
        System.out.println("\n\n\n\n");
        for (String arg : args) {
            s += arg;
        }
        Board board = new Board(s);
        
        
        //board.solve();
        board.print();
         System.out.println("\n\n\n\n");
        board.fullySolve();
//        System.out.println(board.isValidBoard() ? "Valid" : "Invalid");
//
//        ConstrainedSatesfactionProblem problem = new ConstrainedSatesfactionProblem(board.m);
//
//        for (int i = 0; i < board.n; i++) {
//            for (int j = 0; j < board.n; j++) {
//                if (board.get(i, j) != 0) {
//                    problem.SetDomainAndChangePeersDomain(i, j, board.get(i, j));
//                }
//            }
//        }
//
//        problem.printDomain();
//        System.out.println(Arrays.toString(problem.findMRV()));

    }
}
