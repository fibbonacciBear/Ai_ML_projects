import java.io.IOException;

public class Main {

    public enum PrintSetting {
        NEVER,
        ALWAYS,
        WHEN_WRONG
    }

    public DataSource trainingData;
    public DataSource<Image> testingData;
    public KNN knn;
    public DistanceFinder distanceFinder;


    public Main(DataSource trainingData, DataSource testingData, DistanceFinder distanceFinder, KNN knn) {
        this.distanceFinder = distanceFinder;
        this.trainingData = trainingData;
        this.trainingData.init();
        this.testingData = testingData;
        this.testingData.init();
        this.knn = knn;
        this.knn.initDistanceFinder(distanceFinder);
        this.knn.initDataSource(trainingData);
    }

    public boolean run(Image i, PrintSetting print) {
        Image[] r = knn.knn(i);

        int[] buckets = new int[10];
        for (Image result : r) {
            buckets[result.digit()]++;
        }
        int max = -1;
        int index = -1;
        for (int b = 0; b < buckets.length; b++) {
            if (buckets[b] > max) {
                index = b;
                max = buckets[b];
            }else if (buckets[b] == max) {
                double dCur = 0;
                double dNew = 0;
                for (Image result : r) {
                    if (result.digit() == index) dCur += result.distance;
                    if (result.digit() == b) dNew += result.distance;
                }
                if (dNew > dCur) break;
                index = b;
                max = buckets[b];
            }
        }

        boolean right = index == i.digit();

        if (print == PrintSetting.ALWAYS || (print == PrintSetting.WHEN_WRONG && !right)) {
            System.out.println(i);
            System.out.println("Guess: " + index + "\tCertainty: " +  buckets[index] + "/" + r.length);
        }

        return right;
    }

    /*
    Command Line Arguments:
    -print <always/never/wrong> When to print the output of each test (default never)
    -steps <int> The number of values to test (requires an integer argument) (if steps not specified runs for all data)
     */
    public static void main(String[] args) throws IOException {
        //TODO drop in real classes

        PrintSetting print = PrintSetting.NEVER;
        int steps = -1;
        int trainingSize = -1;
        int p = 1;
        int k = 1;
        boolean normIsSquared = true;
        for (int i = 0; i<args.length; i++) {
            if (args[i].charAt(0) == '-') {
                String s = args[i].substring(1);
                switch(s) {
                    case "print":
                        if (args[i+1].contains("-")) {
                            print = PrintSetting.ALWAYS;
                        }else {
                            switch (args[i+1]) {
                                case "always":
                                case "a":
                                    print = PrintSetting.ALWAYS;
                                    break;
                                case "never":
                                case "n":
                                    print = PrintSetting.NEVER;
                                    break;
                                case "wrong":
                                case "w":
                                    print = PrintSetting.WHEN_WRONG;
                                    break;
                            }
                            i++;
                        }
                        break;
                    case "step":
                        steps = Integer.parseInt(args[i+1]);
                        i++;
                        break;
                    case "train":
                        trainingSize = Integer.parseInt(args[i+1]);
                        i++;
                        break;
                    case "p":
                        p = Integer.parseInt(args[i+1]);
                        i++;
                        break;
                    case "k":
                        k = Integer.parseInt(args[i+1]);
                        i++;
                        break;
                    case "squared":
                        //Hack v
                        normIsSquared = args[i+1].contains("f")?false:true;
                        i++;
                }
            }

        }
        Main m = new Main(
                new DataSourceInitPreProcess("train-images.idx3-ubyte", "train-labels.idx1-ubyte", trainingSize>0?trainingSize:Integer.MAX_VALUE),
                new DataSourceInitPreProcess("t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte", steps>0?steps:Integer.MAX_VALUE),
                new LPNorm(p, normIsSquared),
                new PriorityTreeKNN(k)
        );
        int right = 0;
        int total = steps>0?steps:m.testingData.size();


        //hehehe
        int count = 0;
        int timer = 0;
        int barSize = 100;
        int itsPerBar = total/barSize;

        int filled = count/itsPerBar;
        String bar = "\r[";
        for (int i = 0; i < filled-1; i++) {
            bar += "=";
        }
        bar += ">";
        for (int i = filled; i < barSize; i++) {
            bar += " ";
        }
        System.out.print(bar + "]");

        long start = System.currentTimeMillis();
        for (Image im : m.testingData) {
            if (m.run(im, print)) right++;
            count++;
            timer++;
            if (timer == itsPerBar) {
                timer = 0;
                filled = count/itsPerBar;
                bar = "\r[";
                for (int i = 0; i < filled-1; i++) {
                    bar += "=";
                }
                bar += ">";
                for (int i = filled; i < barSize; i++) {
                    bar += " ";
                }
                System.out.print(bar + "]");
            }


        }
        System.out.println();
        System.out.println("Digits tested: " + total + "\tDigits correct: " + right + "\tAccuracy: " + (((double)right)/total)*100 + "%" + "\tTime elapsed (ms)L: " + (System.currentTimeMillis()-start));
    }

}
