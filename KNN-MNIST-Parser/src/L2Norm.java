import java.io.IOException;
import java.util.Arrays;

public class L2Norm implements DistanceFinder {


    public double distance(Image trainingImage, Image testingImage) {

        if (trainingImage.distanceID == testingImage.imageID) {
            return trainingImage.distance;
        }

        double[][] diff = new double[trainingImage.rows()][trainingImage.cols()];
        double sum = 0;
        for (int i = 0; i < trainingImage.rows(); i++) {
            for (int j = 0; j < trainingImage.cols(); j++) {
                sum += Math.pow((Byte.toUnsignedInt(trainingImage.get(i, j)) - Byte.toUnsignedInt(testingImage.get(i, j))), 2);
            }
            //System.out.println();
        }

        trainingImage.distanceID = testingImage.imageID;
        trainingImage.distance = Math.sqrt(sum);

        return Math.sqrt(sum);
    }

    public static void main(String[] args) {
        // write your code here
        try {
            System.out.println("Hello World");
            String imageFile = "train-images.idx3-ubyte";
            String labelFile = "train-labels.idx1-ubyte";
            Reader reader = new Reader(imageFile, labelFile);
            //Image[] images = reader.read();
            Image[] testSet = new Image[600];
            Image image1 = reader.readImage();
            Image image2 = reader.readImage();
            L2Norm l2Norm = new L2Norm();
            System.out.println(image1 + "\n" + image2);
            System.out.println(l2Norm.distance(image1, image2));

        } catch (IOException e) {
            System.out.println(e);
        }

    }

}