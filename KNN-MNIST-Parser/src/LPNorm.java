import java.io.IOException;
import java.util.Arrays;

public class LPNorm implements DistanceFinder {

    int p;
    boolean isToTheP;


    public LPNorm(int p, boolean isSquared) {
        this.p = p;
        this.isToTheP = isSquared;
    }

    public LPNorm() {
        this.p = 2;
        this.isToTheP = false;
    }


    public double distance(Image trainingImage, Image testingImage) {
        if (trainingImage.distanceID == testingImage.imageID) {
            return trainingImage.distance;
        }

        double[][] diff = new double[trainingImage.rows()][trainingImage.cols()];
        double sum = 0;
        for (int i = 0; i < trainingImage.rows(); i++) {
            for (int j = 0; j < trainingImage.cols(); j++) {
                sum += Math.abs(Math.pow(Byte.toUnsignedInt(trainingImage.get(i, j)) - Byte.toUnsignedInt(testingImage.get(i, j)), p));
            }
        }

        trainingImage.distanceID = testingImage.imageID;

        if (isToTheP) {
            trainingImage.distance = sum;
        } else {
            trainingImage.distance = Math.pow(sum, ((double) (1 / p)));
        }

        return trainingImage.distance;

    }


    public static void main(String[] args) {
        // write your code here
        try {
            System.out.println("Hello World");
            String imageFile = "train-images-idx3-ubyte";
            String labelFile = "train-labels-idx1-ubyte";
            Reader reader = new Reader(imageFile, labelFile);
            //Image[] images = reader.read();
            Image[] testSet = new Image[600];
            Image image1 = reader.readImage();
            Image image2 = reader.readImage();
            System.out.println(image1 + "\n" + image2);
            //   System.out.println(L1Norm.distance(image1, image2));

        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
