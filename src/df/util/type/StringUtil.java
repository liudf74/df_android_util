package df.util.type;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class StringUtil {

    private final static String SPACE_16 = "                ";
    private final static String SPACE_8 = "        ";
    private final static String SPACE_4 = "    ";
    private final static String SPACE_2 = "  ";
    private final static String SPACE_1 = " ";


    /**
     * �б��Ƿ��ַ���Ϊnull����û�����ݡ�
     */
    public static boolean zero(String o) {
        return ((null == o) || (o.length() <= 0));
    }

    /**
     * �б��Ƿ��ַ���Ϊnull����û�����ݣ�����ȫ��Ϊ�ո�
     */
    public static boolean empty(String o) {
        return ((null == o) || (o.length() <= 0) || (o.trim().equals("")));
    }

    /**
     * �б��Ƿ��ַ���Ϊnull����û�����ݣ�����ȫ��Ϊ�ո�,��Ϊ0��
     */
    public static boolean emptyOrEqualsZero(String o) {
        return ((null == o) || (o.length() <= 0) || (o.equals("0")) || (o.trim().equals("")));
    }

    /**
     * ��strΪnull����û�����ݵ�����£����ؿմ������򷵻�str��
     */
    public static String toZeroSafe(String str) {
        if (zero(str))
            return "";
        return str;
    }


    /**
     * ��strΪnull����û�����ݵ�����£�����def�����򷵻�str��
     */
    public static String toZeroSafe(String str, String def) {
        if (zero(str))
            return def;
        return str;
    }

    /**
     * ��strΪnull����û�����ݣ�����ȫ��Ϊ�ո������£����ؿմ������򷵻�str��
     */
    public static String toEmptySafe(String str) {
        if (empty(str))
            return "";
        return str;
    }

    /**
     * ��strΪnull����û�����ݣ�����ȫ��Ϊ�ո������£�����def�����򷵻�str��
     */
    public static String toEmptySafe(String str, String def) {
        if (empty(str))
            return def;
        return str;
    }

    /**
     * ���str�ǿգ��򷵻� prefix + str + suffix ���ַ���
     */
    public static String joinIfNotStringEmpty(String str, String prefix, String suffix) {
        String s = "";
        if (!StringUtil.empty(str))
            s = toZeroSafe(prefix) + str + toZeroSafe(suffix);
        return s;
    }

    /**
     *
     */
    public static String trim(String str) {
        if (empty(str))
            return str;
        return str.trim();
    }

    /**
     * �ж��ַ����Ƿ�������ͬ
     */
    public static boolean equals(String s1, String s2) {
        if (s1 == s2)
            return true;
        if (null == s1)
            return false;
        return s1.equals(s2);
    }

    /**
     * �ж��ַ����Ƿ�������ͬ�������ִ�Сд
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == s2)
            return true;
        if (null == s1)
            return false;
        return s1.equalsIgnoreCase(s2);
    }

    public static String toString(char[] array) {
        return String.valueOf(array);
    }

    public static String toHexString(byte[] b) {
        if (b == null)
            return "";
        StringBuffer strBuffer = new StringBuffer(b.length * 3);
        for (int i = 0; i < b.length; i++) {
            String str = Integer.toHexString(b[i] & 0xff).toUpperCase();
            if (str.length() <= 1) {
                strBuffer.append("0");
            }
            strBuffer.append(str);
            strBuffer.append(" ");
        }
        return strBuffer.toString();
    }

    public static String toHexString(String str) {
        if (zero(str))
            return "";
        return toHexString(str.getBytes());
    }

    public static String toHexString(String str, String enc) {
        if (zero(str))
            return "";
        byte[] b = null;
        if (!empty(enc)) {
            try {
                b = str.getBytes(enc);
            } catch (UnsupportedEncodingException e) {
                return "UnsupportedEncodingException = " + e.getMessage();
            }
        }
        return toHexString(b);
    }

    /**
     * convert old char to new string, instead of String.replace();
     *
     * @param charOld
     * @param strNew
     * @return
     */
    public static String replace(String src, char charOld, String strNew) {
        if (null == src)
            return src;
        if (null == strNew)
            return src;

        StringBuffer buf = new StringBuffer();
        for (int i = 0, n = src.length(); i < n; i++) {
            char c = src.charAt(i);
            if (c == charOld)
                buf.append(strNew);
            else
                buf.append(c);
        }
        return buf.toString();
    }

//    public static void replace(StringBuffer src, String strOld, String strNew) {
//        if ((null == src) || (src.length() <= 0))
//            return;
//        //???? TODO:���ܲ���
//        String s = replace(src.toString(), strOld, strNew);
//        src.setLength(0);
//        src.append(s);
//    }
//
//    public static void replace(StringBuilder src, String strOld, String strNew) {
//        if ((null == src) || (src.length() <= 0))
//            return;
//        //???? TODO:���ܲ���
//        String s = replace(src.toString(), strOld, strNew);
//        src.setLength(0);
//        src.append(s);
//    }

    /**
     * @param src
     * @param delim
     * @param target
     * @return ��src�к���delim���а������ַ����滻Ϊtarget�ִ�
     */
    public static String replace2(String src, String delim, String target) {
        if (src == null || src.length() <= 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        String[] arr = split2(src, delim);
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            sb.append(s);
            if (++j != arr.length) {
                sb.append(target);
            }
        }

        return sb.toString();
    }
//
//    public static String replace(String src, String strOld, String strNew) {
//        if (null == src)
//            return src;
//        if (zero(strOld))
//            return src;
//        if (null == strNew)
//            return src;
//        if (equals(strOld, strNew))
//            return src;
//
//        return org.apache.avalon.excalibur.util.StringUtil.replaceSubString(src, strOld, strNew);
//    }

    public static String commonPrefix(String s1, String s2) {
        if ((null == s1) || (null == s2))
            return null;
        StringBuffer sb = new StringBuffer();
        final int len = Math.min(s1.length(), s2.length());
        for (int i = 0; i < len; i++) {
            char c = s1.charAt(i);
            if (c != s2.charAt(i))
                break;
            sb.append(c);
        }
        return sb.toString();
    }

    public static String upperFirst(String s) {
        String str = null;
        if (null != s) {
            if (s.length() == 1) {
                str = s.toUpperCase();
            } else {
                str = s.substring(0, 1).toUpperCase() + s.substring(1);
            }
        }
        return str;
    }

    public static void upperFirst(StringBuffer sb) {
        if ((null != sb) && (sb.length() > 0)) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
    }

    public static String lowerFirst(String s) {
        String str = null;
        if (null != s) {
            if (s.length() == 1) {
                str = s.toLowerCase();
            } else {
                str = s.substring(0, 1).toLowerCase() + s.substring(1);
            }
        }
        return str;
    }

    public static void lowerFirst(StringBuffer sb) {
        if ((null != sb) && (sb.length() > 0)) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }
    }

    public static String getLastSuffix(String str, String delima) {
        if (zero(delima))
            return str;

        String suffix = "";
        if (!zero(str)) {
            int index = str.lastIndexOf(delima);
            if (index >= 0)
                suffix = str.substring(index + delima.length());
            else
                suffix = str;
        }
        return suffix;
    }

    public static String getLastPrefix(String src, String delima) {
        if (zero(delima))
            return src;

        String prefix = "";
        if (!zero(src)) {
            int index = src.lastIndexOf(delima);
            if (index >= 0)
                prefix = src.substring(0, index);
        }
        return prefix;
    }

    /**
     * Append a certain number of whitespace characters to a StringBuffer.
     *
     * @param sb     the StringBuffer
     * @param length the number of spaces to append
     */
    public final static void appendWhiteSpace(final StringBuffer sb, int length) {
        while (length >= 16) {
            sb.append(SPACE_16);
            length -= 16;
        }

        if (length >= 8) {
            sb.append(SPACE_8);
            length -= 8;
        }

        if (length >= 4) {
            sb.append(SPACE_4);
            length -= 4;
        }

        if (length >= 2) {
            sb.append(SPACE_2);
            length -= 2;
        }

        if (length >= 1) {
            sb.append(SPACE_1);
            length -= 1;
        }
    }


    /**
     * ��string��ʽ������СminSize�����maxSize������rightJustify�Ҷ�����ַ��������ÿո���䣩
     */
    /**
     * Utility to format a string given a set of constraints.
     * TODO: Think of a better name than format!!!! ;)
     *
     * @param minSize      the minimum size of output (0 to ignore)
     * @param maxSize      the maximum size of output (0 to ignore)
     * @param rightJustify true if the string is to be right justified in it's box.
     * @param string       the input string
     */
    public final static String format(final int minSize,
                                      final int maxSize,
                                      final boolean rightJustify,
                                      final String string) {
        final StringBuffer sb = new StringBuffer(maxSize);
        format(sb, minSize, maxSize, rightJustify, string);
        return sb.toString();
    }

    /**
     * ��string��ʽ������СminSize�����maxSize������rightJustify�Ҷ�����ַ��������ÿո���䣩
     * ���ؽ����sb�С�
     */
    /**
     * Utility to format a string given a set of constraints.
     * TODO: Think of a better name than format!!!! ;)
     * Note this was thieved from the logkit project.
     *
     * @param sb           the StringBuffer
     * @param minSize      the minimum size of output (0 to ignore)
     * @param maxSize      the maximum size of output (0 to ignore)
     * @param rightJustify true if the string is to be right justified in it's box.
     * @param string       the input string
     */
    public final static void format(final StringBuffer sb,
                                    final int minSize,
                                    final int maxSize,
                                    final boolean rightJustify,
                                    final String string) {
        final int size = string.length();

        if (size < minSize) {
            //assert( minSize > 0 );
            if (rightJustify) {
                appendWhiteSpace(sb, minSize - size);
                sb.append(string);
            } else {
                sb.append(string);
                appendWhiteSpace(sb, minSize - size);
            }
        } else if (maxSize > 0 && maxSize < size) {
            if (rightJustify) {
                sb.append(string.substring(size - maxSize));
            } else {
                sb.append(string.substring(0, maxSize));
            }
        } else {
            sb.append(string);
        }
    }


    /**
     * ��string��ʽ������СminSize�����maxSize������rightJustify�Ҷ�����ַ���������padding���ո�
     * ���string�ı߽���ڿո�����ܱ�padding�滻��
     */
    public static String format(int minSize, int maxSize, boolean rightJustify, char padding, String string) {
        StringBuffer sb = new StringBuffer();
        format(sb, minSize, maxSize, rightJustify, string);
        //
        final int length = sb.length();
        if (length <= 0)
            return sb.toString();
        //
        if (rightJustify) {
            for (int i = 0; i < length; i++) {
                if (sb.charAt(i) != ' ') {
                    break;
                }
                sb.setCharAt(i, padding);
            }
        } else {
            for (int i = length - 1; i >= 0; i--) {
                if (sb.charAt(i) != ' ') {
                    break;
                }
                sb.setCharAt(i, padding);
            }
        }
        return sb.toString();
    }

    /**
     * ��string����length�������ã���߲��㲿�����Ϊpadding
     */
    public static String leftPadding(int length, char padding, String string) {
        return format(length, length, true, padding, string);
    }

    /**
     * ��string����length�������ã���߲��㲿�����Ϊpadding
     */
    public static String rightPadding(int length, char padding, String string) {
        return format(length, length, false, padding, string);
    }

    /**
     * ��str�ַ����������г��ֵ�delim�ָ���ַ�������
     * ��split�ǲ�ͬ��ʵ�ַ�ʽ
     *
     * @param str
     * @param delim
     * @return
     */
    public static String[] split2(String str, String delim) {
        String contents[];
        if (StringUtil.empty(str))
            return new String[]{""};

        StringTokenizer st = new StringTokenizer(str, delim);
        contents = new String[st.countTokens()];
        if (contents.length == 0)
            return new String[]{""};

        int i = 0;
        while (st.hasMoreTokens()) {
            contents[i++] = st.nextToken();
        }

        return contents;
    }

    /**
     * �������ַ�����ָ���±꿪ʼ�ĺ���ַ��������շָ����ָ��󣬵õ��ַ������飬
     *
     * @param str
     * @param delim
     * @param start
     * @return
     */
    public static String[] split(String str, String delim, int start) {
        String[] arr;

        String head = str.substring(0, start);
        String body = str.substring(start);
        String[] temp = split2(body, delim);
        int count = temp.length;
        arr = new String[count + 1];
        arr[0] = head;
        System.arraycopy(temp, 0, arr, 1, temp.length);

        return arr;
    }

    /**
     * ���ַ������շָ����ָ�Ϊ���飬����ȥ�����еĿո����
     *
     * @param str
     * @param delim
     * @param trim
     * @return
     */
    public static String[] split(String str, String delim, boolean trim) {
        String[] set = split2(str, delim);
        if (trim) {
            for (int i = 0; i < set.length; i++) {
                set[i] = set[i].trim();
            }
        }
        return set;
    }

    /**
     * ���ַ����ָ��maxLength���ȵ�����
     *
     * @param dest
     * @param maxLength
     * @return
     */
    public static String[] split(String dest, int maxLength) {
        int length = 0;

        length = dest.length();
        if (length <= maxLength) {
            return new String[]{dest};
        }

        int count = length / maxLength;
        if (length % maxLength > 0)
            count++;

        String[] contents = new String[count];
        int pointer = 0;
        for (int i = 0; i < count - 1; i++) {
            contents[i] = dest.substring
                    (pointer, pointer + maxLength);
            pointer = pointer + maxLength;
        }
        contents[count - 1] = dest.substring(pointer);


        return contents;
    }

    /**
     * ���շָ��ַ���������ȥ�����еĿո����
     *
     * @param str
     * @param delim
     * @return
     */
    public static final String[] splitWithoutBackspace(final String str, final String delim) {
        if (zero(str) || zero(delim))
            return new String[0];
        List list = new ArrayList();
        for (StringTokenizer tokenizer = new StringTokenizer(str, delim); tokenizer.hasMoreTokens(); ) {
            String child = tokenizer.nextToken().trim();
            if (child.equals(""))
                continue;
            list.add(child);
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * ��str�����ָ�ָ��Ϊ���в���0��9���ֺ�С����������ַ�
     *
     * @param str
     * @return
     */
    /*
    public static String[] splitByNonNumberChars(String str) {
        if (StringUtil.empty(str))
            return new String[]{""};

        List l = new ArrayList();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Validator.isDigit(c) || (c == '.')) {
                sb.append(c);
            } else {
                if (sb.length() > 0) {
                    l.add(sb.toString());
                    sb.setLength(0);
                }
            }
        }
        String s = sb.toString();
        if (!StringUtil.empty(s)) {
            l.add(s);
        }
        return (String[]) l.toArray(new String[0]);
    }
    */

    /**
     * �ú��������ָ����ֺ��ַ���
     */
    public static String[] splitNumAndStr(String oldStr) {
        if (oldStr == null || (oldStr.length() <= 0))
            return null;

        String[] tempSeparator = new String[2];
        int i = 0;
        String tempChr = oldStr.substring(i, ++i);
        while (i < oldStr.length()) {
            int j = 0;
            for (j = 0; j <= 9; j++) {
                if (tempChr.equals(String.valueOf(j))) {
                    break;
                }
            }//end inner for

            if (j == 10) {
                //˵����������
                tempSeparator[0] = oldStr.substring(0, --i);//��ʾΪ����
                tempSeparator[1] = oldStr.substring(i, oldStr.length());//��ʾΪ����
                return tempSeparator;
            }//end if

            if (i < oldStr.length()) {
                tempChr = oldStr.substring(i, ++i);
            }//end if

        }//end while

        return null;//˵���д���
    }

    /**
     * �ָ��ַ������ָ���Ϊ������ʽ
     *
     * @param str
     * @param regexDelim
     * @return
     */
    public static String[] splitByRegexDelim(String str, String regexDelim) {
        if (empty(str)) {
            return new String[0];
        }
        return str.split(regexDelim, 0);
    }

    /**
     * �ָ��ַ������ָ���Ϊ������ʽ��ȥ�����еĿո����
     *
     * @param str
     * @param regexDelim
     * @return
     */
    public static String[] splitByRegexDelimWithoutBackspace(String str, String regexDelim) {
        if (empty(str)) {
            return new String[0];
        }
        str = str.replace(" ", "");// ȥ���ڲ��Ŀո�
        String[] set = str.split(regexDelim, 0);
        if ((set != null)) {
            for (int i = 0; i < set.length; i++) {
                set[i] = set[i].trim();
            }
        }
        return set;
    }

    /**
     * �ָ��ַ������ָ���Ϊ������ʽ
     *
     * @param str
     * @param regexDelim
     * @return
     */
    /*
    public static String[] splitStringByRegexDelim(String str, String regexDelim) {
        if (empty(str)) {
            return new String[0];
        }
        //ȥ��ͷβ�ո�
        str.trim();
        //�����ո�ֻ����һ��
        while (str.indexOf("  ") >= 0) {
            str = replace(str, "  ", " ");
        }
        //String[] set = split2(str, regexDelim);
        String[] set = str.split(regexDelim, 0);
        if ((set != null)) {
            for (int i = 0; i < set.length; i++) {
                set[i] = set[i].trim();
            }
        }
        return set;
    }
    */

    /**
     * ��list�ַ����б�ϲ�����delimΪ�ָ�����ַ���
     *
     * @param list
     * @param delim
     * @param appendTailDelim
     * @return
     */
    public static String join(List<String> list, String delim, boolean appendTailDelim) {
        if ((list == null) || (list.size() <= 0)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            String item = list.get(i);
            sb.append(item);
            if (appendTailDelim || (i != (size - 1))) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * ��set�ַ�������ϲ�����delimΪ�ָ�����ַ���
     *
     * @param set
     * @param delim
     * @return
     */
    public static String join(String[] set, String delim) {
        if ((null == set) || (set.length <= 0))
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append(toEmptySafe(set[0]));
        for (int i = 1; i < set.length; i++) {
            if (!empty(set[i])) {
                sb.append(delim);
                sb.append(toEmptySafe(set[i]));
            }
        }
        return sb.toString();
    }

    /**
     * ��set�ַ��������fromIndex��ʼ�Ժ��Ԫ�غϲ�����delimΪ�ָ�����ַ���
     *
     * @param set
     * @param delim
     * @param fromIndex ��0��ʼ
     * @return
     */
    public static String join(String[] set, String delim, int fromIndex) {
        if ((null == set) || (set.length <= 0) || (fromIndex >= set.length))
            return "";

        if (fromIndex < 0)
            fromIndex = 0;

        StringBuffer sb = new StringBuffer();
        sb.append(set[fromIndex]);
        for (int i = fromIndex + 1; i < set.length; i++) {
            sb.append(delim);
            sb.append(set[i]);
        }
        return sb.toString();
    }

    public static String joinWithSpace(String[] keys) {
        return join(keys, " ");
        //StringBuffer sb = null;
        //if (keys == null
        //        || keys.length == 0) {
        //    return null;
        //}
        //sb = new StringBuffer("");
        //for (int i = 0; i < keys.length; i++) {
        //    String key = keys[i];
        //    sb.append(key).append(" ");
        //}
        //return sb.toString();
    }

    public static StringBuffer joinListAsStringBuffer(List<String> numberList, String joinToken) {
        StringBuffer number = new StringBuffer();
        for (int i = 0; i < numberList.size(); i++) {
            number.append(numberList.get(i)).append(joinToken);
        }
        deleteLastChar(number);
        return number;
    }

    public static StringBuffer joinListAsStringBufferWithoutEmpty(List<String> numberList, String joinToken) {
        StringBuffer number = new StringBuffer();
        for (int i = 0; i < numberList.size(); i++) {
            if (!empty(numberList.get(i))) {
                number.append(numberList.get(i)).append(joinToken);
            }
        }
        deleteLastChar(number);
        return number;
    }

    public static StringBuffer deleteLastChar(StringBuffer sb) {
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }

    public static StringBuilder deleteLastChar(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }

    public static StringBuffer deleteFirstChar(StringBuffer sb) {
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb;
    }

    public static StringBuilder deleteFirstChar(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
        }
        return sb;
    }

    public static boolean contains(String str, char searchChar) {
        if (str == null || str.length() == 0)
            return false;
        else
            return str.indexOf(searchChar) >= 0;
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;
        if (searchStr.length() == 0)// ""�մ�����Ϊ��������String.indexOf()��Ϊ�մ�������
            return false;
        else
            return str.indexOf(searchStr) >= 0;
    }

    public static boolean contains(StringBuffer str, String searchStr) {
        if (str == null || searchStr == null)
            return false;
        if (searchStr.length() == 0)// ""�մ�����Ϊ��������String.indexOf()��Ϊ�մ�������
            return false;
        //???? TODO: ��Ҫ���Ч��
        return contains(str.toString(), searchStr);
    }

    //TODO????���������ڶ����������ṩ֧��. ���ҽ���"Ŀ���ַ���"�а�������"ƥ�����"ʱ,����true,���򶼷���false
    public static boolean contains(String str, String[] searchStrSet) {
        if (str == null || searchStrSet == null)
            return false;

        for (int i = 0; i < searchStrSet.length; i++) {
            String searchStr = searchStrSet[i];
            if (!contains(str, searchStr))
                return false;
        }
        return true;
    }

    public static boolean contains(String[] set, String searchStr) {
        if ((set == null) || set.length == 0 || (searchStr == null))//����Դ��û��������֤�ֶ���Ŀʱ��Ĭ����֤������Ŀ
            return true;
        for (int i = 0; i < set.length; i++) {
            String str = set[i];
            if (searchStr.equals(str))
                return true;
        }
        return false;
    }


    /**
     * add '0' before day or month if which lenth is 1
     */
    public static String add02Date(String sDate) {
        if (null == sDate)
            return sDate;
        return sDate.length() == 1 ? "0" + sDate : sDate;
    }
}
