package df.util.android;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 11-12-16
 * Time: ����6:24
 * To change this template use File | Settings | File Templates.
 */
public class DimensionUtil {


    //todo: df: ����
    public static float floatFromMdpiToLdpiOfWidth(float dimen) {
        // 320��240320��h��480��320480��h������Ϊ2/3
        return dimen * 66.667f / 100.f;
    }

    //todo: df: ����
    public static float floatFromMdpiToLdpiOfHeight(float dimen) {
        // 240��240320��h��320��320480��h������Ϊ3/4
        return dimen * 75.f / 100.f;
    }

    //todo: df: ����
    public static int intFromMdpiToLdpiWidth(int dimen) {
        return (int) floatFromMdpiToLdpiOfWidth(dimen);
    }

    //todo: df: ����
    public static int intFromMdpiToLdpiHeight(int dimen) {
        return (int) floatFromMdpiToLdpiOfHeight(dimen);
    }

}
