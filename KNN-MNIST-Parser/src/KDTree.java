import java.util.*;

public class KDTree<Data extends DataPoint<Value>, Value extends Comparable<Value>> {

    private Node root;
    int k;
    final int totalLevels;

    public KDTree(int k, int levels) {
        this.k = k;
        this.totalLevels = levels;
        constructEmptyTree();
    }

    //constructs an empty tree of k levels with
    public void constructEmptyTree() {
        if (totalLevels == 0) {
            return;
        }
        root = new Node(1);
        constructEmptyTree(root, 1);
    }

    public void constructEmptyTree(Node n, int level) {
        int i = level;
        if (i == totalLevels) {
            return;
        }
        i++;
        root.left = new Node(i);
        constructEmptyTree(root.left, i);
        i = level + 1;
        root.right = new Node(i);
        constructEmptyTree(root.right, i);
    }

    //sets up the KDTree
    public void initDataSource(DataSource<Data> trainingData) {
        Node rover = root;
        if (rover == null) {
            return;
        }
        for (Data dataPoint : trainingData) {
            rover.add(dataPoint);
        }

        ArrayList<Data> left = new ArrayList<Data>();
        ArrayList<Data> right = new ArrayList<Data>();
        for (Data dataPoint : trainingData) {
            if (dataPoint.getValues()[rover.dimension].compareTo(rover.median()) < 0) {
                left.add(dataPoint);
            } else {
                right.add(dataPoint);
            }
        }

        addData(left, rover.left);
        addData(right, rover.right);

    }

    public void addData(ArrayList<Data> trainingData, Node n) {
        if (n == null) {
            return;
        }
        for (Data dataPoint : trainingData) {
            n.add(dataPoint);
        }

        ArrayList<Data> left = new ArrayList<Data>();
        ArrayList<Data> right = new ArrayList<Data>();
        for (Data dataPoint : trainingData) {
            if (dataPoint.getValues()[n.dimension].compareTo(n.median()) < 0) {
                left.add(dataPoint);
            } else {
                right.add(dataPoint);
            }
        }

        addData(left, n.left);
        addData(right, n.right);

    }

    public void print() {
        Node n = root;
        if (n == null) {
            return;
        }
        for (int i = 0; i < n.level - 1; i++) {
            System.out.print("\t");
        }
        System.out.println(n);
        print(n.left);
        print(n.right);

    }

    public void print(Node n) {
        if (n == null) {
            return;
        }
        for (int i = 0; i < n.level - 1; i++) {
            System.out.print("\t");
        }
        System.out.println(n);
        print(n.left);
        print(n.right);

    }

    public class Node {
        Node left;
        Node right;
        int level;

        //an array list of the values of the data points sorted along that dimension
        //maybe we need a dictionary/map?
        ArrayList<Data> sortedData = new ArrayList<Data>();
        //I could also implement a new compareTo method that just compares along the dimension and sorts that way...

        //the median of the above sorted data
        Data median;

        int dimension;

        public Node(Node left, Node right, int level) {
            this.left = left;
            this.right = right;
            this.level = level;
            this.dimension = level % k;
        }

        public Node(int level) {
            this(null, null, level);
        }

        public void add(Data dataPoint) {
            //dimension value is the data point's value at that dimension
            sortedData.add(dataPoint);
            sortedData.sort(new Comparator<Data>() {
                @Override
                public int compare(Data data, Data t1) {
                    return data.getValues()[dimension].compareTo(t1.getValues()[dimension]);
                }
            });
            setMedian();
        }

        public int size() {
            return sortedData.size();
        }

        public void setMedian() {
            median = sortedData.get(size()/2);
        }

        public Value median() {
            return median.getValues()[dimension];
        }

        //for lookup
        public Data find() {
            return null;
        }

        public String toString() {
            return "(" + dimension + ": " + median() + ")";
        }


    }

    public static void main(String[] args) {
        Coordinates c = new Coordinates();
        c.init();
        KDTree tree = new KDTree(3, 4);
        tree.initDataSource(c);
        tree.print();

    }


}
