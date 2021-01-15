import java.io.IOException;
import java.util.Arrays;

public class L1Norm implements DistanceFinder {
    public double distance(Image testingImage, Image trainingImage) {

        if (trainingImage.distanceID == testingImage.imageID) {
            return trainingImage.distance;
        }

        double[][] diff = new double[testingImage.rows()][testingImage.cols()];
        double sum = 0;
        for (int i = 0; i < testingImage.rows(); i++) {
            for (int j = 0; j < testingImage.cols(); j++) {
                sum += Math.abs(Byte.toUnsignedInt(testingImage.get(i, j)) - Byte.toUnsignedInt(trainingImage.get(i, j)));
            }
        }

        trainingImage.distanceID = testingImage.imageID;
        trainingImage.distance = sum;
        return sum;
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
            L1Norm l1Norm = new L1Norm();
            System.out.println(image1 + "\n" + image2);
            System.out.println(l1Norm.distance(image1, image2));

        } catch (IOException e) {
            System.out.println(e);
        }

    }

}