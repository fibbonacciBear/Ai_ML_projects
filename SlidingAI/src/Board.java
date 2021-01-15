
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author akashganesan
 *
 *
 */
class Board implements Comparable {

    int distanceFromStart;
    State board;
    int size;
    static PriorityQueue<State> frontier;
    static Hashtable<String, State> visited = new Hashtable<>();
    static Hashtable<String, State> Parent = new Hashtable<>();
    State initialState, goalState;

//    public void Board(State board, int distanceFromStart) {
//        this.board = board;
//        this.distanceFromStart = distanceFromStart;
//    }
    private Board(int asize, int[] start, int[] goal) {

        size = asize;

        initialState = new State(size);
        initialState.initializeState(start);

        goalState = new State(size);
        goalState.initializeState(goal);

        Comparator<State> costComparator;
        costComparator = new Comparator<State>() {
            @Override
            public int compare(State s2, State s1) {
                return (s2.heuristic() + s2.path_cost) - (s1.heuristic() + s1.path_cost);
            }
        };

        frontier = new PriorityQueue<>(costComparator);

    }

    private LinkedList<State> AStar() {
        addToFrontier(initialState, null, 0);
        State low;
        while (true) {
            //printFringe();
            while (true) {
                low = frontier.poll();
                String key = low.makeKey();
               // System.out.println("Trying to add key: " + key);
                if (!visited.containsKey(key)) {
                    //System.out.println("Adding key: " + key);
                    visited.put(key, low);
                    break;
                }
            }

            if (low.heuristic() == 0) {
                break;
            }

            State[] nextStates = low.availableState();

            for (State s : nextStates) {
                if (s != null) {
                    addToFrontier(s, low, low.path_cost + 1);
                }
            }
        }

        LinkedList<State> path = new LinkedList<>();
        State parent = low;
        while (parent != null) {
            path.addFirst(parent);
            parent = parent.parent;
        }
        System.out.println(path.toString());
        System.out.println("number of moves:" + (path.size() - 1));
        return path;
    }

    public static void addToFrontier(State state, State parentState, int cost) {
        state.path_cost = cost;
        state.parent = parentState;
        frontier.add(state);
    }

    public static void printFringe() {
        // Creating an iterator 
        Iterator value = frontier.iterator();

        // Displaying the values after iterating through the queue 
        System.out.println("The iterator values are: ");
        while (value.hasNext()) {
            System.out.println(value.next());
        }
    }

    public static int[] parseCommandLineArgument(String[] nums) {
        if (Math.sqrt(nums.length) != Math.floor(Math.sqrt(nums.length))) {
            throw new NumberFormatException("make sure number of inputs is a perfect square");
        }
        int[] numArray = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            numArray[i] = Integer.parseInt(nums[i]);
        }
        return numArray;

    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class State {

        public int[][] state;
        public int size;
        int zeroRow;
        int zeroCol;
        int path_cost;
        State parent;

        public void initializeState(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                int row = (int) (i / this.size);
                int col = i % this.size;
                state[row][col] = nums[i];
                if (nums[i] == 0) {
                    zeroRow = row;
                    zeroCol = col;
                }
            }
        }

        public String toString() {
            String out = "";
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    out = out + state[i][j] + " ";
                }
                if (i != this.size -1) {
                    out = out + "\n";
                }
            }
            return "\n" + "[" + out + "]" + "\n";
        }

        public void initializeState(int[][] nums) {
            this.state = nums;
        }

        private State(int size) {
            state = new int[size][size];
            this.size = size;
        }

        public State(int[][] state) {
            this.state = state;
            this.size = state.length;
        }

        public State[] availableState() {
            State[] returnArr = new State[4];
            returnArr[0] = getAboveState(zeroRow, zeroCol, state);
            returnArr[1] = getBelowState(zeroRow, zeroCol, state);
            returnArr[2] = getLeftState(zeroRow, zeroCol, state);
            returnArr[3] = getRightState(zeroRow, zeroCol, state);

            return returnArr;
        }

        public int[][] clone(int[][] arr) {
            int[][] clone = new int[arr.length][arr[0].length];
            for (int i = 0; i < arr.length; i++) {
                System.arraycopy(arr[i], 0, clone[i], 0, arr[0].length);
            }
            return clone;
        }

        public State getAboveState(int zeroRow, int zeroCol, int[][] slidingPuzzle) {
            if (zeroRow == 0) {
                return null;
            }
            int[][] returnArr = clone(slidingPuzzle);
            returnArr[zeroRow][zeroCol] = state[zeroRow - 1][zeroCol];
            returnArr[zeroRow - 1][zeroCol] = 0;
            State returnState = new State(returnArr);
            returnState.zeroRow = zeroRow - 1;
            returnState.zeroCol = zeroCol;
            return returnState;
        }

        public State getBelowState(int zeroRow, int zeroCol, int[][] slidingPuzzle) {
            if (zeroRow == slidingPuzzle.length - 1) {
                return null;
            }
            int[][] returnArr = clone(slidingPuzzle);
            returnArr[zeroRow][zeroCol] = state[zeroRow + 1][zeroCol];
            returnArr[zeroRow + 1][zeroCol] = 0;
            State returnState = new State(returnArr);
            returnState.zeroRow = zeroRow + 1;
            returnState.zeroCol = zeroCol;
            return returnState;
        }

        public State getRightState(int zeroRow, int zeroCol, int[][] slidingPuzzle) {
            if (zeroCol == slidingPuzzle[0].length - 1) {
                return null;
            }
            int[][] returnArr = clone(slidingPuzzle);
            returnArr[zeroRow][zeroCol] = state[zeroRow][zeroCol + 1];
            returnArr[zeroRow][zeroCol + 1] = 0;
            State returnState = new State(returnArr);
            returnState.zeroRow = zeroRow;
            returnState.zeroCol = zeroCol + 1;
            return returnState;
        }

        public State getLeftState(int zeroRow, int zeroCol, int[][] slidingPuzzle) {
            if (zeroCol == 0) {
                return null;
            }
            int[][] returnArr = clone(slidingPuzzle);
            returnArr[zeroRow][zeroCol] = state[zeroRow][zeroCol - 1];
            returnArr[zeroRow][zeroCol - 1] = 0;
            State returnState = new State(returnArr);
            returnState.zeroRow = zeroRow;
            returnState.zeroCol = zeroCol - 1;
            return returnState;
        }

        // heuristic algorithm just takes the sum of the manhatten distances between
        // the actual locations of the numbers and their sum
        int heuristic() {
            //this.print_state();
            int cost = 0;
            //System.out.println("size: " + this.size);
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    if (this.state[i][j] != 0) {
                        cost += Math.abs(i - (this.state[i][j] - 1) / this.size);
                        cost += Math.abs(j - (this.state[i][j] - 1) % this.size);
                        //System.out.println("cost: " + cost);
                    }
                }
            }
            return cost;
        }

        void print_state() {
            for (int i = 0; i < this.size; i++) {
                System.out.println(Arrays.toString(this.state[i]));
            }
        }

        String makeKey() {
            String output = "";
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    output += this.state[i][j];
                }
            }
            return output;
        }

    }

    public static void main(String[] args) {
        System.out.println("");
        //String[] start = {"1", "2", "3", "4", "5", "6", "7", "0", "8",};
        String[] start = {"2", "4", "3", "7", "1", "6", "0", "5", "8"};
        String[] goal = new String[start.length];
        for(int i = 0; i < start.length - 1; i++){
            goal[i] = (i + 1) + "";
        }
        goal[start.length - 1] = "0";
        int[] start_a = parseCommandLineArgument(args);
        System.out.println(Arrays.toString(start_a));
        int[] goal_a = parseCommandLineArgument(goal);

        int size = (int) Math.sqrt(start_a.length);
        Board board = new Board(size, start_a, goal_a);


        board.AStar();
    }
}