package df.util.type;


import java.io.UnsupportedEncodingException;

public class DumpPackage {
    private static char getHexStr(byte val) {
        if ((val >= 0) && (val <= 9)) {
            return (char) ('0' + val);
        } else if ((val >= 0xA) && (val <= 0xF)) {
            return (char) ('A' + val - 0xA);
        } else {
            return '*';
        }
    }

    public static String dumpPackage(byte[] src, int offset, int len) {
        if (null == src)
            return "";
        int srcLength = src.length;
        if (0 == srcLength)
            return "";

        StringBuffer sb = new StringBuffer();
        StringBuffer show = null;
        int loop;

        int cur_val;
        byte high_b, low_b;
        char show_val;
        String place_info = null;

        int maxLength;
        if (srcLength < offset + len) {
            maxLength = srcLength - len;
            if (maxLength <= 0) {
                return "";
            }
        } else {
            maxLength = len;
        }
        for (loop = 0; loop < maxLength; loop++) {
            if (loop % 8 == 0) {
                if (loop == 0) {
                    sb.append("0x0000 : ");
                    show = new StringBuffer();
                } else {
                    sb.append("  ");
                }
            }
            if ((loop % 16 == 0) && (loop > 0)) {
                place_info = Integer.toHexString(loop);
                sb.append("  " + show.toString() + "\n0x");
                for (int i = place_info.length(); i < 4; i++) {
                    sb.append('0');
                }
                sb.append(place_info + " : ");
                show = null;
                show = new StringBuffer();
            }
            sb.append(' ');
            cur_val = src[offset + loop];
            low_b = (byte) (cur_val & 0xF);
            high_b = (byte) ((cur_val >> 4) & 0xF);
            sb.append(getHexStr(high_b));
            sb.append(getHexStr(low_b));

            if ((cur_val >= 0x20) &&
                    (cur_val < 0x80)) {
                show_val = (char) (' ' + (cur_val - 0x20));
            } else {
                show_val = '.';
            }
            show.append(show_val);
        }
        if (show.length() > 0) {
            int left = len % 16;
            if (left > 0) {
                for (loop = left; loop < 16; loop++) {
                    if (loop % 8 == 0) {
                        sb.append("  ");
                    }
                    sb.append("   ");
                }
                sb.append("    " + show.toString() + "\n");
            }
            show = null;
        } else {
            sb.append('\n');
        }
        return new String(sb);
    }

    public static String dumpPackage(byte[] src, int len) {
        return dumpPackage(src, 0, len);
    }

    public static String dumpPackage(byte[] src) {
        if (null == src)
            return "";
        return dumpPackage(src, src.length);
    }

    public static String dumpPackage(String str, String enc) {
        byte[] b;
        try {
            b = str.getBytes(enc);
        } catch (UnsupportedEncodingException e) {
            return "UnsupportedEncodingException = " + e.getMessage();
        }
        return dumpPackage(b);
    }

    public static String dumpPackage(String str) {
        byte[] b;
        try {
            b = str.getBytes(EncodingUtil.getSystemEncoding());
        } catch (UnsupportedEncodingException e) {
            return "UnsupportedEncodingException = " + e.getMessage();
        }
        return dumpPackage(b);
    }

}
