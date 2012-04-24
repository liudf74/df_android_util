package df.util.type;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cuijianfeng
 * Date: 2007-11-7
 * Time: 9:51:43
 */
public class MathUtil {

    /**
     * 组合算法。计算组合的结果个数
     *
     * @param m 总数
     * @param n 选取的个数
     * @return 结果个数
     */
    public static int combination(int m, int n) {
        if ((m == 0) || (n == 0)) {
            return 0;
        }
        if (m < n) {
            return 0;
        }
        int r = 1;
        for (int i = 0; i < n; i++) {
            r = r * (m - i);
        }
        for (int i = n; i > 1; i--) {
            r = r / (i);
        }
        return r;
    }

    public static List<String> toCombinationList(int m, int n, String delim) {
        if (m < n) {
            return new ArrayList<String>();
        }
        int[] array = new int[m];
        for (int i = 0; i < m; i++) {
            array[i] = i + 1;
        }
        BitSet bs = new BitSet(m);
        for (int i = 0; i < n; i++) {
            bs.set(i, true);
        }
        List<String> list = new ArrayList<String>();
        do {
            list.add(toFullNumber(array, bs, delim));
        } while (moveNext(bs, m));
        return list;
    }

    private static boolean moveNext(BitSet bs, int m) {
        int start = -1;
        while (start < m) {
            if (bs.get(++start)) {
                break;
            }
        }
        if (start >= m) {
            return false;
        }
        int end = start;
        while (end < m) {
            if (!bs.get(++end)) {
                break;
            }
        }
        if (end >= m) {
            return false;
        }
        for (int i = start; i < end; i++) {
            bs.set(i, false);
        }
        for (int i = 0; i < end - start - 1; i++) {
            bs.set(i);
        }
        bs.set(end);
        return true;
    }

    private static String toFullNumber(int[] array, BitSet bs, String delim) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (bs.get(i)) {
                sb.append(array[i]).append(delim);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - delim.length());
        }
        return sb.toString();
    }


    /**
     * 排列算法。计算排列的结果个数
     *
     * @param m 总数
     * @param n 选取的个数
     * @return 结果个数
     */
    public static int permutation(int m, int n) {
        if ((m == 0) || (n == 0)) {
            return 0;
        }
        if (m < n) {
            return 0;
        }
        int r = 1;
        for (int i = 0; i < n; i++) {
            r = r * (m - i);
        }
        return r;
    }

}
