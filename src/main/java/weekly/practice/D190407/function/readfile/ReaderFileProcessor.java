package weekly.practice.D190407.function.readfile;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @ClassName: ReaderFileProcessor
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 10:50
 **/
@FunctionalInterface
public interface ReaderFileProcessor {

    String process(BufferedReader b) throws IOException;

}
