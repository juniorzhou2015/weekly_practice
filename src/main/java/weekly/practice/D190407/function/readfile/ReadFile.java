package weekly.practice.D190407.function.readfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName: ReadFile
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 10:47
 **/
public class ReadFile {

    public static String processFile() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return br.readLine();
        }
    }

    public static String processFile(ReaderFileProcessor p) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(processFile((BufferedReader br) -> br.readLine()));
        System.out.println(processFile((BufferedReader br) -> br.readLine() + br.readLine()));
    }

}
