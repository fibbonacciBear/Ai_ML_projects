public class CoordinateSet implements DataPoint<Integer> {

    public Integer[] coordinateSet;

    public CoordinateSet(Integer[] coordinateSet) {
        this.coordinateSet = coordinateSet;
    }

    @Override
    public Integer[] getValues() {
        return coordinateSet;
    }

    public String toString() {
        String s = "{";
        boolean first = true;
        for (int i: coordinateSet) {
            if (!first) {
                s += ", ";
            }
            s += i;
            first = false;
        }
        s += "}";
        return s;
    }

}
