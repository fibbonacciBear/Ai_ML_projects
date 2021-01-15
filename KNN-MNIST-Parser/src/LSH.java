import java.lang.reflect.Array;
import java.util.*;
import java.io.IOException;

public class LSH {
   final static int NUMROWS = 28;
   final static int NUMCOLS = 28;
   final static int KEYLENGTH = 10;
   final static int NUMHASHTABLES = 10;
   final static int K = 7;

   public Image[] hyperplaneList;
   public Hashtable<String,ArrayList<Image>>[] hashtables;


   public LSH() {
       hyperplaneList = new Image[KEYLENGTH * NUMHASHTABLES];
       for (int i = 0; i < KEYLENGTH * NUMHASHTABLES; i++) {
           hyperplaneList[i] = generateHyperplane();
       }
       //for()
       hashtables = new Hashtable[NUMHASHTABLES];
       for(int i = 0; i < NUMHASHTABLES; i++){
           hashtables[i] = new Hashtable<String, ArrayList<Image>>();
           int maxHash = (int)Math.pow(2,KEYLENGTH);
           for(int j = 0; j < maxHash; j++){
               String hash = Integer.toString(j,2);
               while(hash.length() < KEYLENGTH){
                   hash = "0" + hash;
               }
               hashtables[i].put(hash, new ArrayList<Image>());
           }
       }


   }

    public static Image generateHyperplane(){
        Image output = new Image(NUMROWS,NUMCOLS);
        Random random = new Random();
        for(int i = 0; i < NUMROWS; i++){
            for(int j = 0; j < NUMCOLS; j++) output.set(i, j, (byte) random.nextInt(256));
        }
        return output;
    }


    public static char getProjection(Image hyperplane, Image image){
        int dotProduct = 0;
        for(int i = 0; i < NUMROWS; i++){
            for(int j = 0; j < NUMCOLS; j++){
                dotProduct += ((int)Byte.toUnsignedInt(hyperplane.get(i,j)) - 127) * ((int)Byte.toUnsignedInt(image.get(i,j)));
            }
        }
        return (dotProduct > 0 ? '1':'0');
    }

    public String[] getHashes(Image image){
       String[] hashes = new String[NUMHASHTABLES];
       for(int j = 0; j < NUMHASHTABLES; j++) {
           String hash = "";
           for (int i = 0; i < KEYLENGTH; i++) {
               Image hyperplane = hyperplaneList[(j * KEYLENGTH) + i];
               hash += getProjection(hyperplane, image);
           }
           hashes[j] = hash;
       }
       return hashes;
    }

    public void buildHashTables(Image[] images){
       String[] hashes;
       for(int i = 0; i < images.length; i++){
           hashes = getHashes(images[i]);
           for(int j = 0; j < NUMHASHTABLES; j++){
               Object hashtable = hashtables[j];
               String hash = hashes[j];
               Image image = images[i];
               hashtables[j].get(hashes[j]).add(image);
           }
       }


    }

    public Image[] getCandidates(Image image){
       HashSet<Image> candidates = new HashSet<Image>();

       String[] hashes = getHashes(image);

       for(int i = 0; i < NUMHASHTABLES; i++){
           ArrayList<Image> candidateList = hashtables[i].get(hashes[i]);
           for(Image candidate: candidateList){
               candidates.add(candidate);
           }

       }

       Image[] candidateArray = new Image[candidates.size()];
       int i = 0;
       for(Image candidate : candidates){
           candidateArray[i] = candidate;
           i++;
       }
       return candidateArray;

    }

    public int findClass(Image testImage, Image[] candidateImages){
       double[][] entries = new double[K][2];

        for(int i = 0; i < K; i++){
            entries[i][0] = Double.MAX_VALUE;
        }

       double[] farthest = entries[0];
       L2Norm distanceFinder = new L2Norm();

       for(Image candidate: candidateImages){
           double distance = distanceFinder.distance(testImage, candidate);
           if(distance < farthest[0]){
               farthest[0] = distance;
               farthest[1] = candidate.digit();
               double maxVal = 0;
               for(int i = 0; i < K; i++){
                   if(maxVal < entries[i][0]){
                       maxVal = entries[i][0];
                       farthest = entries[i];
                   }
               }
           }

       }

       int[] prevalence = new int[10];

       for(int i = 0; i < K; i++){
           prevalence[(int)entries[i][1]]++;
       }

       int maxVal = -1;
       int digit = -1;

       for(int i = 0; i < 10; i++){
           if(prevalence[i] > maxVal){
               maxVal = prevalence[i];
               digit = i;
           }
       }

       return digit;

    }

    public double findAccuracy(Image[] testSet){
       int total = 0;
       int totalCorrect = 0;

       for(Image image : testSet){
           if(findClass(image, getCandidates(image)) == image.digit){
               totalCorrect++;
           }
           total++;
       }

       return (double)totalCorrect/(double)total;
    }




    public static void main(String[] args) throws IOException {
        String trainingImageFile = "train-images.idx3-ubyte";
        String trainingLabelFile = "train-labels.idx1-ubyte";
        Reader trainingReader = new Reader(trainingImageFile, trainingLabelFile);
        Image[] trainingSet = trainingReader.read();


        String testImageFile = "t10k-images.idx3-ubyte";
        String testLabelFile = "t10k-labels.idx1-ubyte";
        Reader testReader = new Reader(testImageFile, testLabelFile);
        Image[] testSet = testReader.read();



        LSH test = new LSH();
        test.buildHashTables(trainingSet);
        System.out.println(test.findAccuracy(testSet));

//       for(int i = 0; i < K; i++){
//           System.out.println(neighbors[i][0] +  " " + neighbors[i][1]);
//       }





    }




}