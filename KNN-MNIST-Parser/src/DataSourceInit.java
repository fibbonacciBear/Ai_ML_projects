import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class DataSourceInit implements DataSource<Image> {
    private Reader reader;
    private int randomDigitSampleSize;

    private Image[] data;
    private Image[] randSample;
    
    public DataSourceInit(String imageFile, String labelFile, int randomDigitSampleSize) throws IOException {
        this.reader = new Reader(imageFile, labelFile);
        this.randomDigitSampleSize = randomDigitSampleSize>-1?randomDigitSampleSize:reader.count;


        this.data = reader.read(randomDigitSampleSize);
        this.randSample = new Image[randomDigitSampleSize * 9];

    }

    @Override
    public int size() {
        return randomDigitSampleSize;
    }
    
    @Override
    public void init(){
        for(int i = 0; i < 10; i++){
            int count = 0;
            for(int j = 0; j < this.data.length; j++){
                if(this.data[j].digit() == i){
                    this.randSample[count * (i + 1)] = this.data[j];
                    count++;
                }
                if(count > randomDigitSampleSize){
                    break;
                }
            }
        }
    }

    @Override
    public Iterator<Image> iterator() {
        return new Iterator<Image>() {
            int position = 0;

            @Override
            public boolean hasNext() {
               return position < data.length;
            }

            @Override
            public Image next() {
                Image next = data[position];

                ++position;

                return next;
            }
        };
    }
}
