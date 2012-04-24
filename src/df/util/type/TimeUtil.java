package df.util.type;


import android.util.Log;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ʱ����صĹ�����
 */
public class TimeUtil {

    public static final String TAG = "df.util.TimeUtil";


    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;


    /**
     * ʱ���ʽ��
     */
    public final static String theTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public final static String otherTimeFormat = "yyyyMMddHHmmss";

    public static final boolean useFastDateFormatter = true;
    private final static SimpleDateFormat theTimeFormator = new SimpleDateFormat(theTimeFormat);

    //???? TODO: ֻ������˲�����ͬ������
    private static HashMap theFastTimeFormatterMap = new HashMap();

    static {
        theFastTimeFormatterMap.put("yyyyMMddHHmmss", new FastDateFormatOfyyyyMMddHHmmss()); //new SimpleDateFormat("yyyyMMddHHmmss") );
        theFastTimeFormatterMap.put("yyyy-MM-dd HH:mm:ss", new FastDateFormatOfyyyy_MM_dd_HH_mm_ss()); //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") );
        theFastTimeFormatterMap.put("yyyy/MM/dd HH:mm:ss", new FastDateFormatOfyyyy__MM__dd_HH_mm_ss()); //new SimpleDateFormat("yyyy/MM/dd HH:mm:ss") );
        theFastTimeFormatterMap.put("yyyyMMdd", new FastDateFormatOfyyyyMMdd()); // new SimpleDateFormat("yyyyMMdd") );
        theFastTimeFormatterMap.put("yyyy-MM-dd", new FastDateFormatOfyyyy_MM_dd()); // new SimpleDateFormat("yyyy-MM-dd") );
        theFastTimeFormatterMap.put("yyyy/MM/dd", new FastDateFormatOfyyyy__MM__dd()); // new SimpleDateFormat("yyyy/MM/dd") );
        theFastTimeFormatterMap.put("HHmmss", new FastDateFormatOfHHmmss()); //new SimpleDateFormat("HHmmss") );
        theFastTimeFormatterMap.put("HH:mm:ss", new FastDateFormatOfHH_mm_ss()); //new SimpleDateFormat("HH:mm:ss") );
        theFastTimeFormatterMap.put("HH", new FastDateFormatOfHH()); //new SimpleDateFormat("HH") );
    }

    /**
     * �Ѻ���ת��Ϊ����
     *
     * @return long
     */
    public static final long millisecondToMinute(long millsecondArg) {
        long v_minute = 0;

        v_minute = millsecondArg / (1000 * 60);
        return v_minute;
    }

    public static final DateFormat getTimeFormatter(String format) {
        //???? TODO: ֱ��ʹ��SimpleDateFormatʱ���������ַ�ʽ�ڶ��߳�����³��ֲ���ȷ��
        //???? TODO: ������SimpleDateFormat�����⡣ ��ˣ����ǲ������Լ���Fastʵ��
        if (useFastDateFormatter) {
            DateFormat sdf = (DateFormat) theFastTimeFormatterMap.get(format);
            if (sdf != null)
                return sdf;
        }

        return new SimpleDateFormat(format);
    }


    ///////////////////////////////////////////////////////////////////

    public static SimpleDateFormat newTimeFormatter(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * ������ʽΪ"1:00:23 AM GMT+08:00"�����ء�0100��
     */
    public static String toHHmmFromHHmmssaz(String time) {
        try {
            SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss a z", Locale.ENGLISH);
            Calendar cal = TimeUtil.toRawCalendar(time, tf);
            String time2 = TimeUtil.toString(cal, "HHmm");
            return time2;
        } catch (Exception e) {
            Log.e(TAG, "toHHmmFromHHmmssaz failure", e);
            return "";
        }
    }

    public static String toHHmmFromHHmmssazForDojoTimePicker(String time) {
        //TODO: "dojo.widget.DropdownTimePicker"��232����bug�����ĺ���Ҫת����
        //return toHHmmFromHHmmssaz(time);
        return time;
    }

    /**
     * �����������ݼ����ڸ�ʽ�жϸ������Ƿ�Ϸ�
     *
     * @param content
     * @param format
     * @return �Ϸ�����true, �Ƿ�����false
     */
    public static boolean isRightDate(String content, String format) {
        boolean ret = false;

        if (!StringUtil.empty(content) && !StringUtil.empty(format)) {
            try {
                toRawCalendar(content, format);
                ret = true;
            } catch (Exception e) {
            }
        }
        return ret;
    }

    /**
     * �ж�ʱ���Ƿ���ȡ�
     * ����ϵͳ�е�ʱ�䲻��Ҫ�ܸߵľ��ȣ���������ṩ���ض��ıȽϷ�����
     */
    public static boolean equals(Calendar c1, Calendar c2) {
        if (c1 == c2)
            return true;
        if ((null == c1) || (null == c2))
            return false;

        final long t1 = c1.getTime().getTime() / 1000;
        final long t2 = c2.getTime().getTime() / 1000;
        return (t1 == t2);
        /*
        return ((c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
            && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
            && (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
            && (c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY))
            && (c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE))
            && (c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND)));
        */
    }

    /**
     * ����Calendar����
     */
    public static Calendar clone(Calendar cal) {
        Calendar c = Calendar.getInstance();
        c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
        return c;
    }

    /**
     * �ж�����������Ƿ�Ϸ�
     *
     * @param time
     * @return
     */
    public static boolean isValidateTime(long time) {
        boolean validate = false;
        Calendar cal = TimeUtil.toCalendar(time, "yyyyMMddHHmmss");
        long timeL = TimeUtil.toNumber(cal, "yyyyMMddHHmmss");
        if (time == timeL) {
            validate = true;
        }
        return validate;
    }

    ///////////////////////////////////////////////////////////////////

    public static String toString(Calendar cal, DateFormat formator) {
        if (null == cal)
            return "";
        assert (formator != null);
        return formator.format(cal.getTime());
    }

    public static String toString(Date date, DateFormat formator) {
        if (null == date)
            return "";
        assert (formator != null);
        return formator.format(date);
    }

    public static String toString(Calendar cal, String format) {
        if (null == cal)
            return "";
        //GeneralAssertion.assertion(!StringUtil.empty(format));
        return toString(cal, getTimeFormatter(format));
    }

    public static String toString(Date date, String format) {
        if (null == date)
            return "";
        //GeneralAssertion.assertion(!StringUtil.empty(format));
        return toString(date, getTimeFormatter(format));
    }

    public static String toString(String format) {
        return toString(Calendar.getInstance(), format);
    }

    public static String toString(String time, String fromFormat, String toFormat) {
        //???? TODO ���ܲ���
        return toString(toCalendar(time, fromFormat), toFormat);
    }

    /**
     * ���ʱ����󣬷��ء����ַ���
     */
    public static String toStringEmpty(String time, String fromFormat, String toFormat) {
        Calendar cal = null;
        try {
            cal = toRawCalendar(time, getTimeFormatter(fromFormat));
        } catch (Exception ex) {
        }
        //???? TODO ���ܲ���
        return toString(cal, toFormat);
    }

    public static String toString(Calendar cal) {
        return toString(cal, theTimeFormator);
    }

    public static String toString(Date date) {
        return toString(date, theTimeFormator);
    }

    public static String toString(long millSeconds) {
        Date date = new Date(millSeconds);
        return toString(date);
        /*
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        return toString( cal );
        */
    }

    public static String toString(long millSeconds, String format) {
        Date date = new Date(millSeconds);
        return toString(date, format);
        /*
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        return toString( cal, format );
        */
    }

    ///////////////////////////////////////////////////////////////////

    /**
     * ��ʱ�����ת��Ϊ����
     *
     * @param format ��ʽ������"yyyyMMddHH"
     */
    public static long toNumber(Calendar cal, String format) {
        String time = toString(cal, format);
        return Long.parseLong(time);
    }

    public static long toNumber(long millSeconds, String format) {
        Date date = new Date(millSeconds);
        return toLong(date, format);
    }

    public static long toNumber(String strTime, String timeFormat, String numberFormat) {
        //???? TODO: Ч�ʲ���
        return toNumber(toCalendar(strTime, timeFormat), numberFormat);
    }

    public static Long toLong(Calendar time) {
        try {
            return new Long(time.getTime().getTime());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    public static Long toLong(Date time) {
        try {
            return new Long(time.getTime());
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return null;
    }

    //TODO????��Date��ת��ΪyyyyMMddhhmmss
    public static long toLong(Date time, String format) {
        String str = toString(time, otherTimeFormat);
        return NumberUtil.toLong(str);
    }

    /**
     * ���ص�ǰ����ǰһ���µ�����
     *
     * @return
     */
    public static Long toLongOfPrevMonth() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -1);
        Long prevMonth = toLong(now);
        return prevMonth;
    }

    public static long toNumberOfyyyyMMdd(Calendar cal) {
        return (cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public static long toNumberOfyyyyMMdd() {
        return toNumberOfyyyyMMdd(Calendar.getInstance());
    }

    public static long toNumberOfyyyyMM(Calendar cal) {
        return (cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1);
    }

    public static long toNumberOfyyyyMM() {
        return toNumberOfyyyyMM(Calendar.getInstance());
    }

    //public static long toLongOfyyyyMM( Calendar cal )
    //{
    //    return (cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1);
    //}

    /**
     * ���ص�ǰʱ���yyyyMM��ʽ
     */
    public static long toLongOfyyyyMM() {
        //return toLongOfyyyyMM( Calendar.getInstance() );
        return toNumberOfyyyyMM(Calendar.getInstance());
    }

    public static int toIntOfyyyyMM(Calendar cal) {
        return (cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1);
    }

    public static int toIntOfyyyyMM() {
        return toIntOfyyyyMM(Calendar.getInstance());
    }

    /**
     * ���ص�ǰʱ���yyyyMMddHHmmss��ʽ
     */
    public static long toLongOfyyyyMMddHHmmss() {
        return toLongOfyyyyMMddHHmmss(Calendar.getInstance());
    }

    public static long toLongOfyyyyMMddHHmmss(Calendar cal) {
        return toNumber(cal, "yyyyMMddHHmmss");
    }

    public static long toLongOfyyyyMMdd() {
        return toNumberOfyyyyMMdd();
    }

    public static String toStringOfdd() {
        return toString(Calendar.getInstance(), "dd");
    }

    ///////////////////////////////////////////////////////////////////

    /**
     * ���ַ���ת��Ϊʱ�����
     */

    public static Calendar toRawCalendar(String time, DateFormat formator)
            throws Exception {
        Calendar cal = Calendar.getInstance();
        Date d = formator.parse(time);
        cal.setTime(d);
        cal.set(Calendar.MILLISECOND, 0);

        // ��֤ÿ�����ֵĸ�ʽ�Ϸ��������ĸ�ʽ�������У����ڡ�19741301����ת��Ϊ��19750101���������׳�����
        String newTime = toString(cal, formator);
        if (!StringUtil.equals(time, newTime)) {
            throw new Exception("Each time unit should located in its range. oldTime = " + time + ", newTime = " + newTime);
        }

        return cal;
    }

    public static Calendar toCalendar(String time, DateFormat formator, boolean showError) {
        if (showError) {
            assert (!StringUtil.empty(time));
            assert (formator != null);
        }

        try {
            return toRawCalendar(time, formator);
        } catch (Exception ex) {
            if (showError)
                Log.e(TAG, "", ex);
            return Calendar.getInstance();
        }
    }

    public static Calendar toRawCalendar(String time, String format)
            throws Exception {
        //return toRawCalendar( time, new SimpleDateFormat(format) );
        return toRawCalendar(time, getTimeFormatter(format));
    }

    public static Calendar toCalendar(String time, String format) {
        assert (!StringUtil.empty(format));
        return toCalendar(time, getTimeFormatter(format), true);
    }

    public static Calendar toRawCalendar(long time, String format)
            throws Exception {
        return toRawCalendar(String.valueOf(time), getTimeFormatter(format));
    }

    public static Calendar toCalendar(long time, String format) {
        assert (!StringUtil.empty(format));
        return toCalendar(String.valueOf(time), getTimeFormatter(format), true);
    }

    public static Calendar toRawCalendar(String time)
            throws Exception {
        return toRawCalendar(time, theTimeFormator);
    }

    public static Calendar toCalendar(String time) {
        return toCalendar(time, theTimeFormator, true);
    }

    public static Calendar toCalendarSilent(String time) {
        return toCalendar(time, theTimeFormator, false);
    }

    /**
     * ����λ�����в�����Χ֮�ڵ�ʱ�䡣
     * ͬʱ�����������Ա�֤ʱ���������ϲ������壬����monthΪ6��dayΪ31ʱ��������dayΪ30��
     *
     * @param year   2000~2010
     * @param month  1~12
     * @param day    1~31
     * @param hour   0~23
     * @param minute 0~59
     * @param second 0~59
     * @return
     */
    public static Calendar toBoundaryCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, NumberUtil.adjustRange(year, 2000, 2010));
        cal.set(Calendar.MONTH, NumberUtil.adjustRange(month, 1, 12) - 1);
        cal.set(Calendar.DAY_OF_MONTH, NumberUtil.adjustRange(day, 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
        cal.set(Calendar.HOUR_OF_DAY, NumberUtil.adjustRange(hour, 0, 23));
        cal.set(Calendar.MINUTE, NumberUtil.adjustRange(minute, 0, 59));
        cal.set(Calendar.SECOND, NumberUtil.adjustRange(second, 0, 59));
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

//    /**
//     * @param delim
//     * @return
//     */
//    public static Date toRawStartRegularBoundaryDate(String time, String delim) throws Exception {
//        try {
//            return toRawRegularBoundaryDate(time, delim, 2000, 1, 1, 0, 0, 0);
//        } catch (Exception e) {
//            throw new Exception("toRawRegularStartDate, failure, exception = " + e);
//        }
//    }

//    public static Date toRawEndRegularBoundaryDate(String time, String delim) throws Exception {
//        try {
//            return toRawRegularBoundaryDate(time, delim, 2010, 12, 31, 23, 59, 59);
//        } catch (Exception e) {
//            throw new Exception("toRawRegularStartDate, failure, exception = " + e);
//        }
//    }

//    public static Date toRawRegularBoundaryDate(
//            String time, String delim,
//            int year, int month, int day, int hour, int minute, int second)
//            throws Exception {
//        String[] set = StringUtil.split(time, delim);
//        if (set.length <= 0)
//            throw new Exception("time format illegal, time = " + time + ", delim = " + delim);
//
//        if (set.length >= 1)
//            year = NumberUtil.toRawInt(set[0]);
//        if (set.length >= 2)
//            month = NumberUtil.toRawInt(set[1]);
//        if (set.length >= 3)
//            day = NumberUtil.toRawInt(set[2]);
//
//        Calendar cal = toBoundaryCalendar(year, month, day, hour, minute, second);
//        return cal.getTime();
//    }

    ///////////////////////////////////////////////////////////////////

    public static Calendar minTime(Calendar time1, Calendar time2) {
        //???? TODO: �ж�time2�Ƿ�Ϊnull
        if ((null == time1) || (time1.after(time2)))
            return time2;
        return time1;
    }

    public static Date minTime(Date time1, Date time2) {
        if ((null == time1) && (null == time2))
            return null;
        if ((null == time1) && (null != time2))
            return time2;
        if ((null != time1) && (null == time2))
            return time1;
        if (time1.after(time2))
            return time2;
        return time1;
    }

    public static Calendar maxTime(Calendar time1, Calendar time2) {
        //???? TODO: �ж�time2�Ƿ�Ϊnull
        if ((null == time1) || (time1.before(time2)))
            return time2;
        return time1;
    }

    public static Date maxTime(Date time1, Date time2) {
        if ((null == time1) && (null == time2))
            return null;
        if ((null == time1) && (null != time2))
            return time2;
        if ((null != time1) && (null == time2))
            return time1;
        if (time1.before(time2))
            return time2;
        return time1;
    }

    public static boolean notAfter(Calendar time1, Calendar time2) {
        if ((null != time1) && (null == time2))
            return false;
        if ((null == time1) && (null != time2))
            return false;
        if ((null != time1) && (null != time2) && (time1.after(time2)))
            return false;
        return true;
    }

    public static boolean notAfter(Date time1, Date time2) {
        if ((null != time1) && (null == time2))
            return false;
        if ((null == time1) && (null != time2))
            return false;
        if ((null != time1) && (null != time2) && (time1.after(time2)))
            return false;
        return true;
    }

    public static boolean equalOrBetween(Calendar time, Calendar start, Calendar end) {
        return (notAfter(start, time) && notAfter(time, end));
    }

    public static boolean equalOrBetween(Date time, Date start, Date end) {
        return (notAfter(start, time) && notAfter(time, end));
    }

    public static List toLongListOfyyyyMMdd(Calendar start, Calendar end) {
        List list = new ArrayList();
        Calendar cal = clone(start);
        while (notAfter(cal, end)) {
            long runTime = TimeUtil.toNumberOfyyyyMMdd(cal);
            list.add(new Long(runTime));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }

    public static List toLongListOfyyyyMMdd(Long startOfyyyyMMdd, Long endOfyyyyMMdd) {
        Calendar start = TimeUtil.toCalendar(startOfyyyyMMdd.longValue(), "yyyyMMdd");
        Calendar end = TimeUtil.toCalendar(endOfyyyyMMdd.longValue(), "yyyyMMdd");
        List list = new ArrayList();
        Calendar cal = clone(start);
        while (notAfter(cal, end)) {
            long runTime = TimeUtil.toNumberOfyyyyMMdd(cal);
            list.add(new Long(runTime));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }

    public static Date toDate(String year, String month, String day, String hour, String minute, String second) {
        String time = year +
                StringUtil.add02Date(month) +
                StringUtil.add02Date(day) +
                StringUtil.add02Date(hour) +
                StringUtil.add02Date(minute) +
                StringUtil.add02Date(second);
        return toCalendar(time, "yyyyMMddHHmmss").getTime();
    }

    public static long toTimeIfNotEmpty(String year, String month, String day, String timeSuffix) {
        long startTime = 0;
        if (!StringUtil.empty(year) && !StringUtil.empty(month) && !StringUtil.empty(day)) {
            startTime = NumberUtil.toLong(year + StringUtil.add02Date(month) + StringUtil.add02Date(day) + timeSuffix);
        }
        return startTime;
    }

    ///////////////////////////////////////////////////////////////////

    public static String toTimespanString(long millisecond) {
        ////StringBuffer sb = new StringBuffer("[ ");
        //StringBuffer sb = new StringBuffer();
        //
        //// hour:minute:second.millis
        //sb.append( StringUtil.format( 2, 5, true, '0', String.valueOf((millisecond/(1000*60*60)))) );
        //sb.append( ":" );
        //sb.append( StringUtil.format( 2, 2, true, '0', String.valueOf((millisecond%(1000*60*60))/(1000*60)) ) );
        //sb.append( ":" );
        //sb.append( StringUtil.format( 2, 2, true, '0', String.valueOf(((millisecond%(1000*60*60))%(1000*60))/1000)) );
        //sb.append( "." );
        //sb.append( StringUtil.format( 3, 3, true, '0', String.valueOf(((millisecond%(1000*60*60))%(1000*60))%1000)) );
        //
        //// second
        //sb.append( "(" );
        //sb.append( millisecond/1000 );
        //sb.append( "." );
        //sb.append( StringUtil.format(3,20,true,'0',String.valueOf(millisecond%1000)) );
        //sb.append( "s)" );
        //
        ////sb.append( " ]" );
        //return sb.toString();
        return toTimespanString(millisecond, true, true);
    }

    public static String toTimespanString(long millisecond, boolean showall, boolean padding) {
        //StringBuffer sb = new StringBuffer("[ ");
        StringBuffer sb = new StringBuffer();

        // hour:minute:second.millis
        long hour = (millisecond / (1000 * 60 * 60));
        long minute = (millisecond % (1000 * 60 * 60)) / (1000 * 60);
        long second = ((millisecond % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
        long millis = ((millisecond % (1000 * 60 * 60)) % (1000 * 60)) % 1000;
        if ((hour > 0) || ((hour <= 0) && showall)) {   // ����ֵ������û����ֵ����ָ����ʾ
            if (padding)
                sb.append(StringUtil.format(2, 5, true, '0', String.valueOf(hour)));
            else
                sb.append(hour);
            sb.append(":");
        }
        if ((minute > 0) || ((minute <= 0) && showall)) {   // ����ֵ������û����ֵ����ָ����ʾ
            if (padding)
                sb.append(StringUtil.format(2, 2, true, '0', String.valueOf(minute)));
            else
                sb.append(minute);
            sb.append(":");
        }
        if (padding)
            sb.append(StringUtil.format(2, 2, true, '0', String.valueOf(second)));
        else
            sb.append(second);
        sb.append(".");
        if (padding)
            sb.append(StringUtil.format(3, 3, true, '0', String.valueOf(millis)));
        else
            sb.append(millis);

        // second
        long ss = millisecond / 1000;
        long ms = millisecond % 1000;
        sb.append("(");
        sb.append(ss);
        sb.append(".");
        if (padding)
            sb.append(StringUtil.format(3, 20, true, '0', String.valueOf(ms)));
        else
            sb.append(ms);
        sb.append("s)");

        //sb.append( " ]" );
        return sb.toString();
    }

    public static void toLastDayOfMonth(Calendar cal) {
        if (cal != null) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
    }

    public static Date getDate(Calendar cal) {
        Calendar adjustedCal = new GregorianCalendar();
        adjustedCal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        adjustedCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        adjustedCal.set(Calendar.DATE, cal.get(Calendar.DATE));
        adjustedCal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        adjustedCal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        adjustedCal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        adjustedCal.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));

        return adjustedCal.getTime();
    }

    public static Date getDate(TimeZone tz) {
        Calendar cal = new GregorianCalendar(tz);

        return getDate(cal);
    }

    public static Date getDate(Date date, TimeZone tz) {
        Calendar cal = new GregorianCalendar(tz);
        cal.setTime(date);

        return getDate(cal);
    }

//    public static String getDescription(long milliseconds) {
//        String s = "";
//
//        int x = 0;
//
//        if (milliseconds % WEEK == 0) {
//            x = (int) (milliseconds / WEEK);
//
//            s = x + ResourceUtil.getResourceString("msg.time.week");
//        } else if (milliseconds % DAY == 0) {
//            x = (int) (milliseconds / DAY);
//
//            s = x + ResourceUtil.getResourceString("msg.time.day");
//        } else if (milliseconds % HOUR == 0) {
//            x = (int) (milliseconds / HOUR);
//
//            s = x + ResourceUtil.getResourceString("msg.time.hour");
//        } else if (milliseconds % MINUTE == 0) {
//            x = (int) (milliseconds / MINUTE);
//
//            s = x + ResourceUtil.getResourceString("msg.time.minute");
//        }
//
//        if (x > 1) {
//            s += "s";
//        }
//
//        return s;
//    }
}


// format : yyyyMMddHHmmss
class FastDateFormatOfyyyyMMddHHmmss
        extends DateFormat {
    protected final String pattern = "yyyyMMddHHmmss";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(date.getYear() + 1900);
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMonth() + 1)));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getDate())));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getHours())));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMinutes())));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getSeconds())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(14);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(Integer.parseInt(text.substring(0, 4)) - 1900);
        d.setMonth(Integer.parseInt(text.substring(4, 6)) - 1);
        d.setDate(Integer.parseInt(text.substring(6, 8)));
        d.setHours(Integer.parseInt(text.substring(8, 10)));
        d.setMinutes(Integer.parseInt(text.substring(10, 12)));
        d.setSeconds(Integer.parseInt(text.substring(12, 14)));
        pos.setIndex(14);
        return d;
    }
}


// format : yyyy-MM-dd HH:mm:ss
class FastDateFormatOfyyyy_MM_dd_HH_mm_ss
        extends DateFormat {
    protected final String pattern = "yyyy-MM-dd HH:mm:ss";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(date.getYear() + 1900);
        toAppendTo.append("-");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMonth() + 1)));
        toAppendTo.append("-");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getDate())));
        toAppendTo.append(" ");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getHours())));
        toAppendTo.append(":");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMinutes())));
        toAppendTo.append(":");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getSeconds())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(19);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(Integer.parseInt(text.substring(0, 4)) - 1900);
        d.setMonth(Integer.parseInt(text.substring(5, 7)) - 1);
        d.setDate(Integer.parseInt(text.substring(8, 10)));
        d.setHours(Integer.parseInt(text.substring(11, 13)));
        d.setMinutes(Integer.parseInt(text.substring(14, 16)));
        d.setSeconds(Integer.parseInt(text.substring(17, 19)));
        pos.setIndex(19);
        return d;
    }
}


// format : yyyy/MM/dd HH:mm:ss
class FastDateFormatOfyyyy__MM__dd_HH_mm_ss
        extends DateFormat {
    protected final String pattern = "yyyy/MM/dd HH:mm:ss";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(date.getYear() + 1900);
        toAppendTo.append("/");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMonth() + 1)));
        toAppendTo.append("/");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getDate())));
        toAppendTo.append(" ");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getHours())));
        toAppendTo.append(":");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMinutes())));
        toAppendTo.append(":");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getSeconds())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(19);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(Integer.parseInt(text.substring(0, 4)) - 1900);
        d.setMonth(Integer.parseInt(text.substring(5, 7)) - 1);
        d.setDate(Integer.parseInt(text.substring(8, 10)));
        d.setHours(Integer.parseInt(text.substring(11, 13)));
        d.setMinutes(Integer.parseInt(text.substring(14, 16)));
        d.setSeconds(Integer.parseInt(text.substring(17, 19)));
        pos.setIndex(19);
        return d;
    }
}


// format : yyyyMMdd
class FastDateFormatOfyyyyMMdd
        extends DateFormat {
    protected final String pattern = "yyyyMMdd";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(date.getYear() + 1900);
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMonth() + 1)));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getDate())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(8);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(Integer.parseInt(text.substring(0, 4)) - 1900);
        d.setMonth(Integer.parseInt(text.substring(4, 6)) - 1);
        d.setDate(Integer.parseInt(text.substring(6, 8)));
        d.setHours(0);
        d.setMinutes(0);
        d.setSeconds(0);
        pos.setIndex(8);
        return d;
    }
}


// format : yyyy-MM-dd
class FastDateFormatOfyyyy_MM_dd
        extends DateFormat {
    protected final String pattern = "yyyy-MM-dd";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(date.getYear() + 1900);
        toAppendTo.append("-");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMonth() + 1)));
        toAppendTo.append("-");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getDate())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(10);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(Integer.parseInt(text.substring(0, 4)) - 1900);
        d.setMonth(Integer.parseInt(text.substring(5, 7)) - 1);
        d.setDate(Integer.parseInt(text.substring(8, 10)));
        d.setHours(0);
        d.setMinutes(0);
        d.setSeconds(0);
        pos.setIndex(10);
        return d;
    }
}


// format : yyyy/MM/dd
class FastDateFormatOfyyyy__MM__dd
        extends DateFormat {
    protected final String pattern = "yyyy/MM/dd";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(date.getYear() + 1900);
        toAppendTo.append("/");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMonth() + 1)));
        toAppendTo.append("/");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getDate())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(10);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(Integer.parseInt(text.substring(0, 4)) - 1900);
        d.setMonth(Integer.parseInt(text.substring(5, 7)) - 1);
        d.setDate(Integer.parseInt(text.substring(8, 10)));
        d.setHours(0);
        d.setMinutes(0);
        d.setSeconds(0);
        pos.setIndex(10);
        return d;
    }
}


// format : HHmmss
class FastDateFormatOfHHmmss
        extends DateFormat {
    protected final String pattern = "HHmmss";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getHours())));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMinutes())));
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getSeconds())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(6);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(0);
        d.setMonth(0);
        d.setDate(0);
        d.setHours(Integer.parseInt(text.substring(0, 2)));
        d.setMinutes(Integer.parseInt(text.substring(2, 4)));
        d.setSeconds(Integer.parseInt(text.substring(4, 6)));
        pos.setIndex(6);
        return d;
    }
}


// format : HH:mm:ss
class FastDateFormatOfHH_mm_ss
        extends DateFormat {
    protected final String pattern = "HH:mm:ss";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getHours())));
        toAppendTo.append(":");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getMinutes())));
        toAppendTo.append(":");
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getSeconds())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(8);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(0);
        d.setMonth(0);
        d.setDate(0);
        d.setHours(Integer.parseInt(text.substring(0, 2)));
        d.setMinutes(Integer.parseInt(text.substring(3, 5)));
        d.setSeconds(Integer.parseInt(text.substring(6, 8)));
        pos.setIndex(8);
        return d;
    }
}


// format : HH
class FastDateFormatOfHH
        extends DateFormat {
    protected final String pattern = "HH";

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(StringUtil.leftPadding(2, '0', String.valueOf(date.getHours())));
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(2);
        return toAppendTo;
    }

    public Date parse(String text, ParsePosition pos) {
        if (StringUtil.zero(text))
            return null;
        if (text.length() != pattern.length())
            return null;
        Date d = new Date();
        d.setYear(0);
        d.setMonth(0);
        d.setDate(0);
        d.setHours(Integer.parseInt(text.substring(0, 2)));
        d.setMinutes(0);
        d.setSeconds(0);
        pos.setIndex(2);
        return d;
    }


}
