
public interface KNN {

    public void initDistanceFinder(DistanceFinder distanceFinder);

    public void initDataSource(DataSource dataSource);

    public Image[] knn(Image image);

}
