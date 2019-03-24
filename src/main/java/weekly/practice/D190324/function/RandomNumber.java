package weekly.practice.D190324.function;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName: RandomNumber
 * @Description:
 * @author: hengsheng
 * @create: 2019-03-17 14:34
 **/
public class RandomNumber {

    public static void main(String[] args) {




        Random random = new Random();
        /*
        1、step为1，即生成1~100的随机数
        2、step为2，即生成1~200的基数
        3、以此类推
         */
        int start = 1, step = 1, end = 100;
        List<Integer> originalList = Stream.iterate(start, i -> i + step).limit(end).collect(Collectors.toCollection(LinkedList::new));

//add comment by Ethan
        //这块的代码写得乱，看一下JDK，用IntStream.



        System.out.println("originalList: " + originalList);
        List<Integer> result = Stream.iterate(start, i -> i + step).limit(end)
                .map(i -> originalList.remove(random.nextInt(((end + 1) * step - i) / step))).collect(Collectors.toList());

        //这里写的也不工整。


        System.out.println("size: " + result.size());
        System.out.println("result: " + result);
        System.out.println("sortedResult: " + result.stream().sorted().collect(Collectors.toList()));
    }

// add comment by Ethan
    //1.代码行数少
    //2.代码质量还有待提升，写的不工整，Sout写的太多，太乱
    //3.让你做这些例子，不仅是提升你的编程技巧，也要提升代码质量和风格
    //4.你看了Stream，也要看一下Stream的源码，看看具体实现，你自己做一个lambda表达式要怎么写
    //5.另外，那个包里还有很多类，都挺有用的。要多看看。
    //6.千万不要有敷衍的心，我对你有耐心，但是我也有底线。

}
