package net.kuwalab.android.util;

/**
 * バイナリデータを扱うためのユーティリティ
 *
 * @author kuwalab
 */
public class HexUtil {
    /**
     * byte配列を16進数文字列に変換する
     *
     * @param bytes 変換する配列
     * @return 変換後の文字列
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            sb.append(toHexString(bytes[i]));
        }

        return sb.toString();
    }

    /**
     * byte配列を16進数文字列配列に変換する
     *
     * @param bytes 変換する配列
     * @return 変換後の文字列配列
     */
    public static String[] toHexStringArray(byte[] bytes) {
        String[] result = new String[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            result[i] = toHexString(bytes[i]);
        }

        return result;
    }

    /**
     * byte変数を16進数文字列に変換する
     *
     * @param oneByte 変換する変数
     * @return 変換後の文字列
     */
    public static String toHexString(byte oneByte) {
        return String.format("%02x", oneByte);
    }

    /**
     * byte配列を連続したバイナリとみなし、数値に変換する
     *
     * @param bytes 変換する配列
     * @return 変換後の数値
     */
    public static int toInt(byte[] bytes) {
        int result = 0;
        int base = 1;

        for (int i = bytes.length - 1; i >= 0; i--) {
            result = result + (toInt(bytes[i]) * base);
            base = base * 256;
        }

        return result;
    }

    /**
     * byte変数を符号なし整数とみなしint型に変換する
     *
     * @param b 変換する変数
     * @return 変換後の整数
     */
    public static int toInt(byte b) {
        int result = b;

        if (result < 0) {
            result = result + 256;
        }

        return result;
    }

    public static int subInt(byte b, int mask, int shift) {
        int result = b & mask;
        if (shift == 0) {
            return result;
        }

        if (shift > 0) {
            return result >>> shift;
        } else {
            shift = -shift;
            return result << shift;
        }
    }

}
