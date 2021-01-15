import java.util.Iterator;

public interface DataSource<T> extends Iterable<T> {

    public void init();

    public int size();

}
