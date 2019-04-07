package weekly.practice.D190407.function.dish;

import lombok.Getter;

/**
 * @ClassName: Dish
 * @Description:
 * @author: hengsheng
 * @create: 2019-04-07 09:52
 **/
@Getter
public class Dish {

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public enum Type {
        MEAT,
        FISH,
        OTHER
    }

}
