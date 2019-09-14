package weekly.practice.D190914.sort;

/**
 * @ClassName: Selection
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 15:10
 **/
public class Selection extends Sort {

    @Override
    public void sort(Comparable[] a) {
        if (null == a || 0 == a.length) {
            return;
        }
        int len = a.length;
        for (int i = 0; i < len; i++) {
            int min = i;
            for (int j = i + 1; j < len; j++) {
                if (less(a[j], a[min])) {
                    min = j;
                }
            }
            exch(a, i, min);
        }
    }

}
