package df.util.type;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {
    private final static NumberFormat theNumberFormatter = NumberFormat.getNumberInstance();

    //0字符的ascii编码
    public final static int ZERO_ASCII_CODE = (int) '0';
    //0字符的ascii编码
    public final static int NINE_ASCII_CODE = (int) '9';
    //0字符的ascii编码
    public final static int DOT_ASCII_CODE = (int) '.';

    //private final static GeneralLogger theLogger = LoggerFactory.getLogger(NumberUtil.class.getName());

    /*private static GeneralLogger getLogger() {
        return Util.getLogger();
        //return theLogger;
    }*/

    ///////////////////////////////////////////////////////////////////

    public static boolean empty(int[] set) {
        return ((null == set) || (set.length <= 0));
    }

    public static boolean isLong(String str) {
        long l;
        try {
            l = Long.parseLong(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isFloat(String str) {
        float l;
        try {
            l = Float.parseFloat(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        int i;
        try {
            i = Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String toString(double num, int fractionDigit) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(1);
        nf.setMinimumFractionDigits(fractionDigit);
        nf.setMaximumFractionDigits(fractionDigit);
        nf.setGroupingUsed(false);
        return nf.format(num);
    }

    public static String toStringForInteger(double num, int integerDigit) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(integerDigit);
        nf.setMaximumIntegerDigits(integerDigit);
        nf.setGroupingUsed(false);
        return nf.format(num);
    }

    public static String toString(double num) {
        NumberFormat f = NumberFormat.getInstance();
        if (f instanceof DecimalFormat) {
            ((DecimalFormat) f).setDecimalSeparatorAlwaysShown(true);
        }
        f.setParseIntegerOnly(true);
        return f.format(num);
    }

    public static int toPowerOf10(int a) {
        if (a <= 0)
            return 0;
        int p;
        for (p = 0; (a = (a / 10)) > 0; p++)
            ;
        return p;
    }

    ///////////////////////////////////////////////////////////////////

    public static int toInt(String str) {
        return toInt(str, 0);
    }

    public static Integer toIntObject(String str) {
        return new Integer(str);
    }

    public static int toInt(String str, int defaultValue) {
        return toInt(str, defaultValue, true);
    }

    public static int toInt(String str, int defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawInt(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toInt, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static float toFloat(String str) {
        return toFloat(str, 0);
    }

    public static float toFloat(String str, float defaultValue) {
        return toFloat(str, defaultValue, true);
    }

    public static float toFloat(String str, float defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toFloat, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static int toRawInt(String str) throws Exception {
        return Integer.parseInt(str.trim());
    }

    public static int toFormattedInt(String str) {
        return toFormattedInt(str, 0);
    }

    public static int toFormattedInt(String str, int defaultValue) {
        return toFormattedInt(str, defaultValue, true);
    }

    public static int toFormattedInt(String str, int defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawFormattedInt(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toFormattedInt, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static int toRawFormattedInt(String str) throws Exception {
        return Integer.parseInt(theNumberFormatter.parse(str.trim()).toString());
    }

    ///////////////////////////////////////////////////////////////////

    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    public static Long toLongObject(String str) {
        return new Long(str.trim());
    }

    public static Long toLongObject(String str, long defaultValue) {
        if (StringUtil.empty(str))
            return new Long(defaultValue);
        return new Long(str.trim());
    }

    public static long toLong(String str, long defaultValue) {
        return toLong(str, defaultValue, true);
    }

    public static long toLong(String str, long defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawLong(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toLong, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static long toRawLong(String str) throws Exception {
        return Long.parseLong(str.trim());
    }

    public static long toFormattedLong(String str) {
        return toFormattedLong(str, 0L);
    }

    public static long toFormattedLong(String str, long defaultValue) {
        return toFormattedLong(str, defaultValue, true);
    }

    public static long toFormattedLong(String str, long defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawFormattedLong(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toFormattedLong, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static long toRawFormattedLong(String str) throws Exception {
        return Long.parseLong(theNumberFormatter.parse(str.trim()).toString());
    }

    ///////////////////////////////////////////////////////////////////

    public static double toDouble(String str) {
        return toDouble(str, 0.0);
    }

    /**
     * 指定给定的double的小数位数
     *
     * @param num
     * @param fractionDigit
     * @return
     */
    public static double toDouble(double num, int fractionDigit) {
        String d = toString(num, fractionDigit);
        return toDouble(d, 0.00);
    }

    public static double toDouble(String str, double defaultValue) {
        return toDouble(str, defaultValue, true);
    }

    public static double toDouble(String str, double defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawDouble(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toDouble, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static double toRawDouble(String str) throws Exception {
        return Double.parseDouble(str.trim());
    }

    public static double toFormattedDouble(String str) {
        return toFormattedDouble(str, 0.0);
    }

    public static double toFormattedDouble(String str, double defaultValue) {
        return toFormattedDouble(str, defaultValue, true);
    }

    public static double toFormattedDouble(String str, double defaultValue, boolean logException) {
        if (StringUtil.empty(str))
            return defaultValue;
        try {
            return toRawFormattedDouble(str);
        } catch (Exception e) {
            if (logException) {
                //getLogger().error("toFormattedDouble, failure, str = " + str, e);
            }
            return defaultValue;
        }
    }

    public static double toRawFormattedDouble(String str) throws Exception {
        return Double.parseDouble(theNumberFormatter.parse(str.trim()).toString());
    }

    ///////////////////////////////////////////////////////////////////

    public static Integer[] toIntegerObjectSet(int[] set) {
        if (set == null)
            return null;
        Integer[] s = new Integer[set.length];
        for (int i = 0; i < set.length; i++) {
            s[i] = new Integer(set[i]);
        }
        return s;
    }

    ///////////////////////////////////////////////////////////////////

    /**
     * 将数组转换为字符串。
     */
    public final static String toString(long[] array) {
        if (null == array)
            return "count=0:[]";

        String s = "count=" + array.length + ":[ ";
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                s += ", ";
            s += array[i];
        }
        s += " ]";
        return s;
    }

    public final static String toString(int[] array) {
        if (null == array)
            return "count=0:[]";

        String s = "count=" + array.length + ":[ ";
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                s += ", ";
            s += array[i];
        }
        s += " ]";
        return s;
    }

    public static long removePrefix(long value, String prefix) {
        String s = String.valueOf(value);
        try {
            if (s.startsWith(prefix))
                value = Long.parseLong(s.substring(prefix.length()));
        } catch (Exception ex) {
        }
        return value;
    }

    public static int[] splitToIntSet(String str, String delim) {
        int[] intSet = new int[0];
        String[] strSet = StringUtil.split(str, delim, true);
        if (null == strSet)
            return intSet;
        for (int i = 0; i < strSet.length; i++) {
            try {
                intSet = ArrayUtil.addIntArray(intSet, toRawInt(strSet[i]));
            } catch (Exception e) {
            }
        }
        return intSet;
    }

    /**
     * 将set数组合并成以delim为分割符的字符串
     *
     * @param set
     * @param delim
     * @return
     */
    public static String join(int[] set, String delim) {
        if ((null == set) || (set.length <= 0))
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append(set[0]);
        for (int i = 1; i < set.length; i++) {
            sb.append(delim);
            sb.append(set[i]);
        }
        return sb.toString();
    }

    ///////////////////////////////////////////////////////////////////

    public static final int adjustRange(int old, int min, int max) {
        if (old < min)
            old = min;
        if (old > max)
            old = max;
        return old;
    }

    public static final long adjustRange(long old, long min, long max) {
        if (old < min)
            old = min;
        if (old > max)
            old = max;
        return old;
    }

    public static final int adjustMinRange(int old, int min) {
        if (old < min)
            old = min;
        return old;
    }

    public static final long adjustMinRange(long old, long min) {
        if (old < min)
            old = min;
        return old;
    }

    public static final int adjustMaxRange(int old, int max) {
        if (old > max)
            old = max;
        return old;
    }

    public static final long adjustMaxRange(long old, long max) {
        if (old > max)
            old = max;
        return old;
    }

    //turn dec to hex
    public static char[] getHexNumber(int i) {
        char octet[] = new char[2];

        if (i / 16 >= 10)
            octet[0] = (char) (i / 16 + 55);
        else
            octet[0] = (char) (i / 16 + 48);
        if (i % 16 >= 10)
            octet[1] = (char) (i % 16 + 55);
        else
            octet[1] = (char) (i % 16 + 48);

        return octet;
    }

    //turn hex to dec
    public static int getDecNumber(char[] ectet) {
        int dec = 0;

        if (ectet[0] >= 65)
            dec += (short) (((ectet[0] - 65) + 10) * 16);
        else
            dec += (short) ((ectet[0] - 48) * 16);
        if (ectet[1] >= 65)
            dec += (short) ((ectet[1] - 65) + 10);
        else
            dec += (short) (ectet[1] - 48);

        return dec;
    }

    public static String getBinary(int i) {
        StringBuffer binary = new StringBuffer("");

        //int b = i & 0x7F;
        int b = i & 0xFF;
        while (true) {
            if (b / 2 == 0) {
                if (b % 2 != 0) {
                    binary.insert(0, b % 2);
                }
                break;
            }
            binary.insert(0, b % 2);
            b = b / 2;
        }
        int l = binary.length();
        for (int j = 8; j > l; j--) {
            binary.insert(0, '0');
        }
        return binary.toString();
    }

    public static int getRowLength(String binary) {
        int rowLength = 0;
        long l = Long.parseLong(binary);

        int length = String.valueOf(l).length();
        for (int i = 0; i < length; i++) {
            rowLength += (l % 2) * Math.pow(2, i);
            l = Long.parseLong(binary.substring(0, binary.length() - (i + 1)));
        }
        return rowLength;
    }

    ///////////////////////////////////////////////////////////////////////////

    public static int byteToInt(byte mybyte) {
        // 防止出现负数
        //return  (int)mybyte;
        return (int) 0x000000ff & mybyte;
    }

    public static byte intToByte(int i) {
        return (byte) i;
    }

    public static int bytesToInt(byte[] mybytes) {
        return (int) ((0x000000ff & (int) mybytes[0]) << 8) + (int) mybytes[1];
    }

    public static byte[] intToBytes(int i) {
        byte[] mybytes = new byte[2];
        mybytes[1] = (byte) (0x000000ff & (int) i);
        mybytes[0] = (byte) ((0x0000ff00 & (int) i) >> 8);
        return mybytes;
    }

    public static byte[] intToBytes4(int i) {
        byte[] mybytes = new byte[4];
        mybytes[3] = (byte) (0x000000ff & (int) i);
        mybytes[2] = (byte) ((0x0000ff00 & (int) i) >> 8);
        mybytes[1] = (byte) ((0x00ff0000 & (int) i) >> 16);
        mybytes[0] = (byte) ((0xff000000 & (int) i) >> 24);
        return mybytes;
    }

    public static void intToBytes(int i, byte[] mybytes) {
        mybytes[1] = (byte) (0x000000ff & (int) i);
        mybytes[0] = (byte) ((0x0000ff00 & (int) i) >> 8);
    }

    public static void intToBytes4(int i, byte[] mybytes) {
        mybytes[3] = (byte) (0x000000ff & (int) i);
        mybytes[2] = (byte) ((0x0000ff00 & (int) i) >> 8);
        mybytes[1] = (byte) ((0x00ff0000 & (int) i) >> 16);
        mybytes[0] = (byte) ((0xff000000 & (long) i) >> 24);
    }

    public static int bytes4ToInt(byte[] mybytes) {
        return (int)
                ((0x000000ff & (int) mybytes[0]) << 24) |
                ((0x000000ff & (int) mybytes[1]) << 16) |
                ((0x000000ff & (int) mybytes[2]) << 8) |
                (0x000000ff & (int) mybytes[3]);
    }

    public static void bytesCopy(byte[] source, byte[] dest, int sourcebegin, int sourceend, int destbegin) {
        int i, j = 0;
        for (i = sourcebegin; i <= sourceend; i++) {
            dest[destbegin + j] = source[i];
            j++;
        }
    }

    // 返回字符串形式的long取值，对于0返回“”空串
    public static String toLongStringIgnoreZero(Long longObject) {
        if (longObject == null) {
            return "";
        }
        if (longObject.longValue() == 0) {
            return "";
        }
        return String.valueOf(longObject);
    }

    // 返回Long对象，对于空串“”返回0。
    public static Long toLongObjectDefaultZero(String longString) {
        long phone = toLong(longString, 0);
        return new Long(phone);
    }

    //TODO????专用于提取数字，从目标输入开始，截止到非数字字符。比如实际中存在150元这样的字段
    public static String pickNumberChars(String inValue) {
        int numchars = 0;
        int dotchars = 0;
        StringBuffer sb = new StringBuffer();

        char[] chars = inValue.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int ascCode = (int) c;
            if (ascCode != DOT_ASCII_CODE
                    && (ascCode < ZERO_ASCII_CODE || ascCode > NINE_ASCII_CODE)) {
                //如果当前字符既不是点号，也不是数字的退出
                break;
            }

            if (ascCode != DOT_ASCII_CODE) {
                numchars++;
            } else {
                if (dotchars != 0) {
                    //同时不能截取多个点号
                    break;
                } else if (numchars == 0) {
                    //如果以点号开头，则附加前缀"0"
                    sb.append('0');
                }
                dotchars++;
            }
            sb.append(c);
        }
        if (sb.length() == 0) {
            //输入为空时，默认设置为0
            sb.append("0");
        }
        return sb.toString();
    }
}

