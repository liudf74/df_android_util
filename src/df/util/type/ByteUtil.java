package df.util.type;

import java.io.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ldf
 * Date: 2007-11-5
 * Time: 11:25:19
 */
public class ByteUtil {

    private static String HexCode[] = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
    };

    private static String BcdCode[] = {
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "41", "42", "43", "44", "45", "46"
    };

    /**
     * 字节复制
     *
     * @param source
     * @param dest
     * @param sourceBegin
     * @param sourceEnd
     * @param destBegin
     */
    public static void bytesCopy(byte[] source,
                                 byte[] dest,
                                 int sourceBegin,
                                 int sourceEnd,
                                 int destBegin) {
        int i, j = 0;
        for (i = sourceBegin; i <= sourceEnd; i++) {
            dest[destBegin + j] = source[i];
            j++;
        }
    }

    /**
     * 2字节变为整数
     *
     * @param bytesValue
     * @return
     */
    public static int bytes2ToInt(byte[] bytesValue) {
        return (int) ((0x000000ff & (int) bytesValue[0]) << 8) |
                (0x000000ff & (int) bytesValue[1]);
    }

    /**
     * 4字节变为整数
     *
     * @param bytesValue
     * @return
     */
    public static int bytes4ToInt(byte[] bytesValue) {
        return (int) ((0x000000ff & (int) bytesValue[0]) << 24) |
                ((0x000000ff & (int) bytesValue[1]) << 16) |
                ((0x000000ff & (int) bytesValue[2]) << 8) |
                (0x000000ff & (int) bytesValue[3]);
    }

    /**
     * 整数转换为4字节
     *
     * @param intValue
     * @return
     */
    public static byte[] intToBytes4(int intValue) {
        byte[] mybytes = new byte[4];
        mybytes[3] = (byte) (0x000000ff & (int) intValue);
        mybytes[2] = (byte) ((0x0000ff00 & (int) intValue) >> 8);
        mybytes[1] = (byte) ((0x00ff0000 & (int) intValue) >> 16);
        mybytes[0] = (byte) ((0xff000000 & (int) intValue) >> 24);
        return mybytes;
    }

    /**
     * 整数转换为2字节
     *
     * @param intValue
     * @return
     */
    public static byte[] intToBytes2(int intValue) {
        byte[] mybytes = new byte[2];
        mybytes[1] = (byte) (0x000000ff & (int) intValue);
        mybytes[0] = (byte) ((0x0000ff00 & (int) intValue) >> 8);
        return mybytes;
    }


    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    public static String byteToHexString(byte[] b) {
        StringBuffer tmp_sb = new StringBuffer();
        int n, d1, d2;
        for (int xi = 0; xi <= b.length - 1; xi++) {
            n = b[xi];
            if (n < 0) {
                n = 256 + n;
            }
            d1 = n / 16;
            d2 = n % 16;
            tmp_sb.append(HexCode[d1]);
            tmp_sb.append(HexCode[d2]);
        }
        return tmp_sb.toString();
    }

    public static String byteToBcdString(byte[] b) {
        StringBuffer tmp_sb = new StringBuffer();
        int n, d1, d2;
        for (int xi = 0; xi <= b.length - 1; xi++) {
            n = b[xi];
            if (n < 0) {
                n = 256 + n;
            }
            d1 = n / 16;
            d2 = n % 16;
            tmp_sb.append(BcdCode[d1]);
            tmp_sb.append(BcdCode[d2]);
        }
        return tmp_sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (!StringUtil.empty(hexString)) {
            int totalLength = hexString.length();
            int halfLength = totalLength / 2;
            ByteArrayOutputStream bais = new ByteArrayOutputStream(halfLength);
            for (int i = 0; i < halfLength; i++) {
                int b1 = toByte(hexString.charAt(i * 2));
                int b2 = toByte(hexString.charAt(i * 2 + 1));
                bais.write((0xFF) & ((b1 << 4) | b2));
            }
            if (halfLength * 2 != totalLength) {
                int b1 = toByte(hexString.charAt(totalLength - 1));
                int b2 = 0;
                bais.write((0xFF) & ((b1 << 4) | b2));
            }
            return bais.toByteArray();
        }
        return new byte[0];
    }

    private static int toByte(char ch) {
        int b;
        if ((ch >= '0') && (ch <= '9')) {
            b = (ch - '0');
        } else if ((ch >= 'a') && (ch <= 'z')) {
            b = (ch - 'a') + 0xA;
        } else if ((ch >= 'A') && (ch <= 'Z')) {
            b = (ch - 'A') + 0xA;
        } else {
            b = (0);
        }
        return b;
    }

    public static byte[] joinByteArray(byte[] bytes1, byte[] bytes2) {
        int length1 = bytes1.length;
        int length2 = bytes2.length;
        byte[] result = new byte[length1 + length2];
        System.arraycopy(bytes1, 0, result, 0, length1);
        System.arraycopy(bytes2, 0, result, length1, length2);
        return result;
    }
}
