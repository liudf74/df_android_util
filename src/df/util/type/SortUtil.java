package df.util.type;

/**
 * Created by IntelliJ IDEA. User: liudongfeng Date: 2003-12-22 Time: 22:59:48 To change this template use Options |
 * File Templates.
 */
public class SortUtil {
    /**
     * 希尔排序法
     */
    public static int[] shellSort(long[] info) {
        int i, j, k;
        long temp;
        int count = info.length;

        int[] index = new int[count];
        int tmp_index;
        for (i = 0; i < count; i++) {
            index[i] = i;
        }
        k = count / 2;
        while (k >= 1) {
            for (i = k; i < count; i++) {
                temp = info[i];
                tmp_index = index[i];
                j = i - k;
                while ((j >= 0) && (temp <= info[j])) {
                    info[j + k] = info[j];
                    index[j + k] = index[j];
                    j = j - k;
                }
                if ((j + k) != i) {
                    info[j + k] = temp;
                    index[j + k] = tmp_index;
                }
            }
            k = k / 2;
        }
        return index;
    }

    /**
     * 冒泡排序
     *
     * @param info
     * @return
     */
    public static int[] bubbleSort(int[] info) {
        int i;
        int j;
        int tempVariables;
        for (i = 0; i < info.length; i++)
            for (j = info.length - 1; j > i; j--) {
                if (info[j - 1] < info[j]) {
                    tempVariables = info[j - 1];
                    info[j - 1] = info[j];
                    info[j] = tempVariables;
                }
            }
        return info;

    }

    /**
     * 判断传进来的数组值是否相同
     *
     * @param value
     * @return
     */
    public static boolean sampleInt(int[] value) {
        for (int i = 1; i < value.length; i++) {
            if (value[i] != value[0]) {
                return false;
            }
        }
        return true;
    }
}
