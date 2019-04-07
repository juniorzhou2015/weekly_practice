package weekly.practice.D190407.function.color;

/**
 * @ClassName: Color
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 11:28
 **/
public class Color {

    private Integer red;
    private Integer green;
    private Integer blue;
    private Integer color;

    public Color(Integer red, Integer green, Integer blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.color = red << 16 | green << 8 | blue;
    }

    @Override
    public String toString() {
        return "color=" + color;
    }

    public static void main(String[] args) {
        TriFunction<Integer, Integer, Integer, Color> colorFactory = Color::new;
        System.out.println(colorFactory.apply(100, 100, 100));
    }

}
