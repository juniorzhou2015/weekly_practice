package weekly.practice.D190914.find;

/**
 * @ClassName: BinarySearch
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 13:45
 **/
public class BinarySearch<Key extends Comparable<Key>, Value> extends SymbolTable {

    private Key[] keys;
    private Value[] values;
    private int size;

    public BinarySearch(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        values = (Value[]) new Comparable[capacity];
    }

    public Value get(Key key) {
        if (0 == size) {
            return null;
        }
        int index = rank(key);
        if (0 < index && index < size && equal(keys[index], key)) {
            return values[index];
        }
        return null;
    }

    private int rank(Key key) {
        int lo = 0, hi = size - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (less(key, keys[mid])) {
                hi = mid - 1;
            } else if (more(key, keys[mid])) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

}
