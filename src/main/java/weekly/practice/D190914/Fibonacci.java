package weekly.practice.D190914;

/**
 * @ClassName: Fibonacci
 * @Description:
 * @author: hengsheng
 * @create: 2019-09-15 00:37
 **/
public class Fibonacci {

    public long caculate(int n) {
        int[] result = {0, 1};
        if (n < 2) {
            return result[n];
        }
        long fMinus2 = 0;
        long fMinus1 = 1;
        long f = 0;
        for (int i = 2; i <= n; ++i) {
            f = fMinus1 + fMinus2;
            fMinus2 = fMinus1;
            fMinus1 = f;
        }
        return f;
    }

}
