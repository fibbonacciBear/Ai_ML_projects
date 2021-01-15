import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityTreeKNN extends KNNInit{

    int k;
    PriorityQueue<Image> images;

    public class DistanceComparator implements Comparator<Image> {

        Image image;

        public DistanceComparator(Image image) {
            this.image = image;
        }

        // because Java uses a min priority queue, removing the head should remove the
        // item with the maximum amount of distance from the image.
        @Override
        public int compare(Image image1, Image image2) {
            double diff = distanceFinder.distance(image, image1) - distanceFinder.distance(image, image2);
            if (diff < 0) {
                return 1;
            } else if (diff > 0) {
                return -1;
            } else {
                return 0;
            }
        }


    }

    public PriorityTreeKNN(int k) {
        this.k = k;
    }


    @Override
    public Image[] knn(Image image) {
        images = new PriorityQueue<Image>(k, new DistanceComparator(image));
        for (Image i : dataSource) {
            if (images.size() == k) {
                if (distanceFinder.distance(image, i) < distanceFinder.distance(image, images.peek())) {
                    images.poll();
                    images.add(i);
                }
            } else {
                images.add(i);
            }
        }

        Image[] imagesArray = new Image[k];

        int index = 0;
        for (Image i : images) {
            imagesArray[index] = i;
            index++;
        }


        return imagesArray;
    }
}
