package weekly.practice.D190914;

import java.util.Arrays;

/**
 * @ClassName: Poker
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-14 22:58
 **/
public class Poker {

    public boolean isContinous(int[] a) {
        if (null == a || 0 == a.length) {
            return false;
        }
        Arrays.sort(a);
        int zeroNum = 0, gapNum = 0, len = a.length;
        for (int i = 0; i < len && 0 == a[i]; i++) {
            ++zeroNum;
        }
        int small = zeroNum;
        int big = small + 1;
        while (big < len) {
            if (a[small] == a[big]) {
                return false;
            }
            gapNum += a[big++] - a[small++] - 1;
        }
        return zeroNum >= gapNum;
    }

}
