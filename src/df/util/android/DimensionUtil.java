package df.util.android;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */
public class DimensionUtil {


    //todo: df: 缩放
    public static float floatFromMdpiToLdpiOfWidth(float dimen) {
        // 320是240320的h，480是320480的h，比率为2/3
        return dimen * 66.667f / 100.f;
    }

    //todo: df: 缩放
    public static float floatFromMdpiToLdpiOfHeight(float dimen) {
        // 240是240320的h，320是320480的h，比率为3/4
        return dimen * 75.f / 100.f;
    }

    //todo: df: 缩放
    public static int intFromMdpiToLdpiWidth(int dimen) {
        return (int) floatFromMdpiToLdpiOfWidth(dimen);
    }

    //todo: df: 缩放
    public static int intFromMdpiToLdpiHeight(int dimen) {
        return (int) floatFromMdpiToLdpiOfHeight(dimen);
    }

}
