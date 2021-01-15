public class OneNN extends KNNInit {

    @Override
    public Image[] knn(Image image) {
        Image[] images = new Image[1];

        double minDistance = Integer.MAX_VALUE;

        double distance;
        for (Image i : this.dataSource) {
            distance = this.distanceFinder.distance(image, i);
            if (distance < minDistance) {
                minDistance = distance;
                images[0] = i;
            }
        }
        return images;
    }
}
