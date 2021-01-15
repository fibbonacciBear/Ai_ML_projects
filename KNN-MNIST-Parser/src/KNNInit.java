public abstract class KNNInit implements KNN {

    DistanceFinder distanceFinder;
    DataSource<Image> dataSource;

    @Override
    public void initDistanceFinder(DistanceFinder distanceFinder) {
        this.distanceFinder = distanceFinder;
    }

    @Override
    public void initDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
