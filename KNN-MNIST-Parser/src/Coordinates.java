import java.util.Iterator;

public class Coordinates implements DataSource<CoordinateSet> {

    CoordinateSet[] coordinates;

    @Override
    public void init() {

        CoordinateSet[] coordinates = {new CoordinateSet(new Integer[]{0, 0, 0}), new CoordinateSet(new Integer[]{0, 0, 1}),
                new CoordinateSet(new Integer[]{0, 1, 0}), new CoordinateSet(new Integer[]{0, 1, 1}), new CoordinateSet(new Integer[]{1, 0, 0}),
                new CoordinateSet(new Integer[]{1, 0, 1}), new CoordinateSet(new Integer[]{1, 1, 0}), new CoordinateSet(new Integer[]{1,1,1})};
        init(coordinates);
    }

    public void init(CoordinateSet[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int size() {
        return coordinates.length;
    }

    @Override
    public Iterator<CoordinateSet> iterator() {
        return new Iterator<CoordinateSet>() {
            int i = 0;


            @Override
            public boolean hasNext() {
                return i < coordinates.length;
            }

            @Override
            public CoordinateSet next() {
                return coordinates[i++];
            }
        };
    }

}
