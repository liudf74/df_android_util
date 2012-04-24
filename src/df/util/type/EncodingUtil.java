package df.util.type;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

public class EncodingUtil {
    public static final String TAG = "df.util.EncodingUtil";

    public static final String CHARSET_ISO88591 = "ISO-8859-1";         //"8859_1";
    public static final String CHARSET_88591 = "8859_1";   // ��jsp�У������ֱ��뷽ʽ��ͬ����֪��Ϊʲô
    public static final String CHARSET_GB18030 = "GB18030";
    //TODO: ���������ӵĺ���ͨ��GB2312ת������BUG�������Ҫʹ��GB18030���롣
    //TODO: ����"�҂h"����Ϊ��bc d2 82 68��
    public static final String CHARSET_GB2312 = CHARSET_GB18030; // "GB2312";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_UTF16 = "UTF-16";
    public static final String CHARSET_UNICODEBIGUNMARKED = "UnicodeBigUnmarked";
    public static final String CHARSET_USASCII = "US-ASCII";

    private static boolean debug = false;

    // ��Ѱ�Ҳ���Ԥ������ַ���ʱ��ʹ�õ�ȱʡ�ַ�����
    public final static String ENCODING_DEFAULT = CHARSET_ISO88591;

    // ���ڲ�ͬ������ƽ̨��ȡ�ļ�ʱʹ�õ��ǲ�ͬ���ַ�����������������ṩת����
    private static HashMap theLangToEncodingMap = new HashMap();

    static {
        theLangToEncodingMap.put("zh", CHARSET_GB2312);
        theLangToEncodingMap.put("en", CHARSET_ISO88591);
    }

    ;

    private final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    //private static GeneralLogger theLogger = LoggerFactory.getLogger( EncodingUtil.class.getName() );

    ///////////////////////////////////////////////////////////////////

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        EncodingUtil.debug = debug;
    }

    ///////////////////////////////////////////////////////////////////

    public static boolean isChinese(String txt) {
        byte[] bytes = new byte[0];
        try {
            bytes = txt.getBytes(CHARSET_ISO88591);
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] < 0)
                    return true;
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static boolean isChinese(char message) {
        boolean isChinese = false;
        if ((message < 0) || (message > 127)) {
            isChinese = true;
        }
        return isChinese;
    }

    /**
     * ��ȡ��ǰ����ϵͳ��ȱʡ�ַ���
     */
    public static String getSystemEncoding() {
        String lang = Locale.getDefault().getLanguage();
        String enc = (String) (theLangToEncodingMap.get(lang));
        // �����Ĳ���ϵͳ�ϲ�����ת�룬����ϵͳ��ת��ΪӢ�ġ�
        if (StringUtil.zero(enc))
            return ENCODING_DEFAULT;
        return enc;
    }

    public static boolean sameSystemEncoding(String enc) {
        return enc.equals(getSystemEncoding());
    }

    ///////////////////////////////////////////////////////////////////

    /**
     * ���õ�ת�뷽����
     */
    public static String convert(String str, String srcEnc, String dstEnc) {
        if (str == null)
            return "";
        try {
            if (needConvert(str, srcEnc, dstEnc)) {
                loggingConvert(str, srcEnc, "Old");
                str = new String(str.getBytes(srcEnc), dstEnc);
                loggingConvert(str, dstEnc, "New");
            }
        } catch (Exception e) {
            Log.e(TAG, "Convert failure, str = " + str + ", srcEnc = " + srcEnc + ", dstEnc = " + dstEnc, e);
        }
        return str;
    }

    /**
     * �б��Ƿ���Ҫ�����ַ�����ת�봦��
     */
    public static boolean needConvert(String str, String srcEnc, String dstEnc)
            throws Exception {
        if (StringUtil.empty(str))
            return false;
        if (ObjectUtil.equals(srcEnc, dstEnc))
            return false;

        // �ж�byte��char����Ŀ�Ƿ���ͬ��
        //???? ���������жϲ�׼ȷ����֪��Ϊʲô�� ���硰��Ů������false������Ұ�ޡ�����true��
        //return ( ( str.getBytes( ResourceAdapter.ENCODING_INTERNAL ).length ) != ( str.length() ) );
        return true;
    }

    /**
     * convert a string from unicode 8859_1 to gb2312
     *
     * @param gbStr string coding with gb2312
     * @return u8859_1 string
     */
    public static String ISO88591toGB2312(String gbStr) {
        if (gbStr != null) {
            try {
                //if(isChinese(gbStr))
                gbStr = new String(gbStr.getBytes(CHARSET_ISO88591), CHARSET_GB2312);
            } catch (Throwable e) {
                return "";
            }
        }
        return gbStr;
    }

    /**
     * convert a string from unicode gb2312 to 8859_1
     *
     * @return gb2312 string
     */
    public static String GB2312toISO88591(String uStr) {
        if (uStr != null) {
            try {
                uStr = new String(uStr.getBytes(CHARSET_GB2312), CHARSET_ISO88591);
            } catch (Throwable e) {
                return "";
            }
        }
        return uStr;
    }

    /**
     * convert a string from unicode gb18030 to 8859_1
     *
     * @return gb2312 string
     */
    public static String GB18030toISO88591(String uStr) {
        if (uStr != null) {
            try {
                uStr = new String(uStr.getBytes(CHARSET_GB18030), CHARSET_ISO88591);
            } catch (Throwable e) {
                return "";
            }
        }
        return uStr;
    }

    public static String toISO88591(String uStr) {
        return toEncoding(uStr, CHARSET_ISO88591);
    }

    public static String toGB2312(String uStr) {
        return toEncoding(uStr, CHARSET_GB2312);
    }

    /**
     * ��ĳ�ֱ��뷽ʽ�Ĳ����ַ���ת��Ϊ�ڲ�UniCode����
     *
     * @param str
     * @param currEncoding
     * @return ��ת��ʧ������£����ء�����
     */
    public static String toUnicode(String str, String currEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(currEncoding));
            } catch (Throwable e) {
                return "";
            }
        }
        return str;
    }

    /**
     * ��ĳ�ֱ��뷽ʽ�Ĳ����ַ���ת��Ϊ�ڲ�UniCode����
     *
     * @param str
     * @param currEncoding
     * @return ��ת��ʧ������£�����ԭʼ��
     */
    public static String toUnicodeKeep(String str, String currEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(currEncoding));
            } catch (Throwable e) {
                return str;
            }
        }
        return str;
    }

    /**
     * ��UniCode����Ĳ����ַ���ת��ΪĿ����뷽ʽ
     *
     * @param str
     * @param destEncoding
     * @return ��ת��ʧ������£����ء�����
     */
    public static String toEncoding(String str, String destEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(), destEncoding);
            } catch (Throwable e) {
                return "";
            }
        }
        return str;
    }

    /**
     * ��UniCode����Ĳ����ַ���ת��ΪĿ����뷽ʽ
     *
     * @param str
     * @param destEncoding
     * @return ��ת��ʧ������£�����ԭʼ��
     */
    public static String toEncodingKeep(String str, String destEncoding) {
        if (str != null) {
            try {
                str = new String(str.getBytes(), destEncoding);
            } catch (Throwable e) {
                return str;
            }
        }
        return str;
    }

    ///////////////////////////////////////////////////////////////////

    public static byte[] toByteArray(String str, String srcEncoding) {
        try {
            return str.getBytes(srcEncoding);
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    private static void loggingConvert(String str, String enc, String hintHead)
            throws Exception {
        if (!debug)
            return;

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, hintHead + " string = [ " + str + " ], encoding = " + enc);
            Log.d(TAG, hintHead + " bytes = [ " + StringUtil.toHexString(str, enc) + " ]");
        }

        // �����ͷ���Դ���������׳����ڴ治�����쳣��
        System.gc();
    }

//    public static String URLEncoderToUTF8(String str ) {
//        if(StringUtil.empty(str))
//            return "";
//        Log.debug("str=" + str);
//        try {
//            str = URLEncoder.encode(str,CHARSET_UTF8);
//            Log.debug("URLEncoderToUTF8, after encode, str=" + str);
//            str = StringUtil.replace(str,"%","~");
//            Log.debug("URLEncoderToUTF8, after remove %, str=" + str);
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG, "encode fail. str=" + str,e);
//        }
//        return str;
//    }
//
//    public static String URLDecodeFromUTF8(String str ) {
//        Log.debug("str=" + str);
//        try {
//            str = StringUtil.replace(str,"~","%");
//            str = URLDecoder.decode( str, CHARSET_UTF8);
//       } catch (UnsupportedEncodingException e) {
//            Log.e(TAG,  "encoding failure, content = " + str,e );
//        }
//        return str;
//    }
}
