package df.util.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liudongfeng
 * Date: 2003-7-18
 * Time: 9:57:04
 * To change this template use Options | File Templates.
 */
public class ArrayUtil {
    public static boolean empty(Object[] set) {
        return ((null == set) || (set.length <= 0));
    }

    public static boolean empty(int[] set) {
        return ((null == set) || (set.length <= 0));
    }

    public static boolean has(int[] set, int val) {
        if (empty(set))
            return false;
        for (int i = 0; i < set.length; i++) {
            if (set[i] == val)
                return true;
        }
        return false;
    }

    public static int max(int[] set) {
        if (empty(set))
            return 0;
        int m = set[0];
        for (int i = 1; i < set.length; i++) {
            m = Math.max(m, set[i]);
        }
        return m;
    }

    public static int min(int[] set) {
        if (empty(set))
            return 0;
        int m = set[0];
        for (int i = 1; i < set.length; i++) {
            m = Math.min(m, set[i]);
        }
        return m;
    }

    public static String[] clone(String[] set) {
        if (null == set)
            return null;
        String[] clone = new String[set.length];
        for (int i = 0; i < set.length; i++) {
            clone[i] = set[i];
        }
        return clone;
    }

    public static ArrayList toArrayList(int[] set) {
        ArrayList list = new ArrayList();
        if (!empty(set)) {
            for (int i = 0; i < set.length; i++) {
                int val = set[i];
                list.add(new Integer(val));
            }
        }
        return list;
    }

    public static ArrayList toArrayList(Object[] set) {
        ArrayList list = new ArrayList();
        if (!empty(set)) {
            for (int i = 0; i < set.length; i++) {
                list.add(set[i]);
            }
        }
        return list;
    }

    public static ArrayList toArrayList(Object[] set, int startIndex) {
        ArrayList list = new ArrayList();
        if (!empty(set)) {
            for (int i = startIndex; i < set.length; i++) {
                list.add(set[i]);
            }
        }
        return list;
    }

    public static long[] toLongArray(String[] strArray) {
        if (empty(strArray)) {
            return new long[0];
        }
        long[] arr = new long[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            String str = strArray[i];
            arr[i] = NumberUtil.toLong(str);
        }
        return arr;
    }

    public static int[] addIntArray(int[] srcArray, int addElement) {
        int srcLength = 0;
        if (null != srcArray)
            srcLength = srcArray.length;

        int[] dstArray = new int[srcLength + 1];
        for (int i = 0; i < srcLength; i++)
            dstArray[i] = srcArray[i];
        dstArray[srcLength] = addElement;

        return dstArray;
    }

    public static int[] addIntArray(int[] srcArray, int[] addArray) {
        int srcLength = 0;
        if (null != srcArray)
            srcLength = srcArray.length;

        int addLength = 0;
        if (null != addArray)
            addLength = addArray.length;

        int[] dstArray = new int[srcLength + addLength];
        for (int i = 0; i < srcLength; i++)
            dstArray[i] = srcArray[i];
        for (int i = 0; i < addLength; i++)
            dstArray[srcLength + i] = addArray[i];

        return dstArray;
    }

    public static String[] addStringArray(String[] srcArray, String addElement) {
        int srcLength = 0;
        if (null != srcArray)
            srcLength = srcArray.length;

        String[] dstArray = new String[srcLength + 1];
        for (int i = 0; i < srcLength; i++)
            dstArray[i] = srcArray[i];
        dstArray[srcLength] = addElement;

        return dstArray;
    }

    public static String[] addIntArray(String[] srcArray, String[] addArray) {
        int srcLength = 0;
        if (null != srcArray)
            srcLength = srcArray.length;

        int addLength = 0;
        if (null != addArray)
            addLength = addArray.length;

        String[] dstArray = new String[srcLength + addLength];
        for (int i = 0; i < srcLength; i++)
            dstArray[i] = srcArray[i];
        for (int i = 0; i < addLength; i++)
            dstArray[srcLength + i] = addArray[i];

        return dstArray;
    }

    public static String[] toIntersectSet(String[] set1, String[] set2) {
        if (empty(set1) || empty(set2))
            return new String[0];
        List list = new ArrayList();
        for (int i = 0; i < set1.length; i++) {
            String s1 = set1[i];
            for (int j = 0; j < set2.length; j++) {
                String s2 = set2[j];
                if (StringUtil.equals(s1, s2)) {
                    list.add(s1);
                    break;
                }
            }
        }
        String[] set = new String[list.size()];
        list.toArray(set);
        return set;
    }

    public static Object[] toObjectSet(List list) {
        if ((list == null) || (list.size() <= 0)) {
            return new Object[0];
        }
        Object[] set = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            set[i] = obj;
        }
        return set;
    }

    public static String[] toStringSet(List list) {
        if ((list == null) || (list.size() <= 0)) {
            return new String[0];
        }
        String[] set = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String s = (String) list.get(i);
            set[i] = s;
        }
        return set;
    }

    /**
     * 返回数组的对应下表的元素
     *
     * @param array
     * @param index
     * @return
     */
    public static String toElementSafe(String[] array, int index) {
        if (array == null) return "";
        if ((array.length > index)) return array[index];
        return "";
    }

    public static int toElementSafe(int[] array, int index) {
        if (array == null) return 0;
        if ((array.length > index)) return array[index];
        return 0;
    }

    public static long toElementSafe(long[] array, int index) {
        if (array == null) return 0;
        if ((array.length > index)) return array[index];
        return 0;
    }
}
