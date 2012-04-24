package df.util.android;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 12-3-10
 * Time: обнГ4:46
 * To change this template use File | Settings | File Templates.
 */
public class ScreenUtil {


    public static int toWidthFrom480320(int screenW, int xOf480320) {
        return xOf480320 * screenW / 480;
    }

    public static int toHeightFrom480320(int screenH, int yOf480320) {
        return yOf480320 * screenH / 320;
    }
}
