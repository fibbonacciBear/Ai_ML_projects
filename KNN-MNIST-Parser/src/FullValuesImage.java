public class FullValuesImage extends Image implements DataPoint<Byte> {

    public FullValuesImage(int rows, int cols) {
        super(rows, cols);
    }
    //will probably have to change this to return a simplified version of the values later
    @Override
    public Byte[] getValues() {
        Byte[] values = new Byte[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                values[i * cols + j] = pixels[i][j];
            }
        }

        return values;
    }

}
