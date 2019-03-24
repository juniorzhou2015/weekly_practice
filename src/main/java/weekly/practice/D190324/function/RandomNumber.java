package weekly.practice.D190324.function;

import com.google.common.collect.Lists;
import org.junit.Assert;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: RandomNumber
 * @Description:
 * @author: hengsheng
 * @create: 2019-03-24 14:34
 **/
public class RandomNumber {

    public static void main(String[] args) {
        Random random = new Random();
        List<Integer> list = Lists.newLinkedList();
        List<Integer> result = Lists.newArrayList();
        int start = 1, end = 101;
        IntStream.range(start, end).forEach(list::add);
        IntStream.range(start, end).forEach(i -> result.add(list.remove(random.nextInt(end - i))));
        System.out.println("result: " + result);
        Assert.assertEquals(100, result.size());
        Assert.assertEquals(0, list.size());
    }

    // add comment by Ethan
    //1.代码行数少
    //2.代码质量还有待提升，写的不工整，Sout写的太多，太乱
    //3.让你做这些例子，不仅是提升你的编程技巧，也要提升代码质量和风格
    //4.你看了Stream，也要看一下Stream的源码，看看具体实现，你自己做一个lambda表达式要怎么写
    //5.另外，那个包里还有很多类，都挺有用的。要多看看。
    //6.千万不要有敷衍的心，我对你有耐心，但是我也有底线。

}
