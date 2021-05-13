package cx.rain.mc.savemyenchantments.utility;

public class Tuple<L, R> {
    public L left;
    public R right;

    public Tuple(L leftIn, R rightIn) {
        left = leftIn;
        right = rightIn;
    }
}
