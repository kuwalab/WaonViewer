package net.kuwalab.android.waonviewer;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.kuwalab.android.util.HexUtil;

import android.util.Log;

public class WaonHistory implements Comparable<WaonHistory> {
    /**
     * 連番
     */
    public int renban;

    /**
     * 日付
     */
    public String date;
    /**
     * 時刻
     */
    public String time;
    /**
     * 使用金額
     */
    public String useMoney;
    /**
     * チャージ金額
     */
    public String chargeMoney;
    /**
     * 残額
     */
    public String restMoney;
    /**
     * 種類
     */
    public String typeName;
    /**
     * ポイント
     */
    public String point;

    public static final Map<Integer, String> TYPE_MAP;

    private static final int TYPE_USE = 0x04;
    private static final int TYPE_CHARGE1 = 0x0c;
    private static final int TYPE_CHARGE2 = 0x10;
    private static final int TYPE_POINT_MOVE_AT = 0x7C;
    private static final int TYPE_POINT_MOVE_TO = 0x18;
    private static final String TYPE_USE_NAME = "使用";
    private static final String TYPE_CHARGE_NAME = "チャージ";
    private static final String TYPE_POINT_MOVE_AT_NAME = "ポイント移動";
    private static final String TYPE_POINT_MOVE_TO_NAME = "ポイント追加";

    static {
        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(TYPE_USE, TYPE_USE_NAME);
        typeMap.put(TYPE_CHARGE1, TYPE_CHARGE_NAME);
        typeMap.put(TYPE_CHARGE2, TYPE_CHARGE_NAME);
        typeMap.put(TYPE_POINT_MOVE_AT, TYPE_POINT_MOVE_AT_NAME);
        typeMap.put(TYPE_POINT_MOVE_TO, TYPE_POINT_MOVE_TO_NAME);

        TYPE_MAP = Collections.unmodifiableMap(typeMap);
    }

    public WaonHistory(int renban, byte[] historyData) {
        this.renban = renban;

        StringBuilder sb = new StringBuilder();
        int year = HexUtil.subInt(historyData[2], 0x0000f8, 3);
        sb.append((year + 2005)).append("年");

        int month1 = HexUtil.subInt(historyData[2], 0x000007, -1);
        int month2 = HexUtil.subInt(historyData[3], 0x000080, 7);
        String month = String.valueOf(month1 + month2);
        month = ("00" + month).substring(month.length());
        sb.append(month).append("月");

        int date = HexUtil.subInt(historyData[3], 0x00007c, 2);
        String strDate = ("00" + date).substring(String.valueOf(date).length());
        sb.append(strDate + "日");
        this.date = sb.toString();

        int hour1 = HexUtil.subInt(historyData[3], 0x000003, -3);
        int hour2 = HexUtil.subInt(historyData[4], 0x0000e0, 5);
        String hour = String.valueOf(hour1 + hour2);
        hour = ("00" + hour).substring(hour.length());
        sb.setLength(0);
        sb.append(hour).append(":");

        int minute1 = HexUtil.subInt(historyData[4], 0x0000001f, -1);
        int minute2 = HexUtil.subInt(historyData[5], 0x00000080, 7);
        String minute = String.valueOf(minute1 + minute2);
        minute = ("00" + minute).substring(minute.length());
        sb.append(minute);
        time = sb.toString();

        int zandaka1 = HexUtil.subInt(historyData[5], 0x0000007f, -11);
        int zandaka2 = HexUtil.subInt(historyData[6], 0x000000ff, -3);
        int zandaka3 = HexUtil.subInt(historyData[7], 0x000000e0, 5);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        restMoney = decimalFormat.format(zandaka1 + zandaka2 + zandaka3);

        int use1 = HexUtil.subInt(historyData[7], 0x0000001f, -13);
        int use2 = HexUtil.subInt(historyData[8], 0x000000ff, -5);
        int use3 = HexUtil.subInt(historyData[9], 0x000000f8, 3);
        useMoney = decimalFormat.format(use1 + use2 + use3);

        int add1 = HexUtil.subInt(historyData[9], 0x0000007, -14);
        int add2 = HexUtil.subInt(historyData[10], 0x000000ff, -6);
        int add3 = HexUtil.subInt(historyData[11], 0x000000fc, 2);
        chargeMoney = decimalFormat.format(add1 + add2 + add3);

        int type = HexUtil.toInt(historyData[1]);
        typeName = TYPE_MAP.get(type);
        if (type == TYPE_POINT_MOVE_AT || type == TYPE_POINT_MOVE_TO) {
            point = chargeMoney;
            chargeMoney = "0";
        } else {
            point = "nothing";
        }

        Log.i("mode", "type:" + type + ":" + typeName + "," + useMoney + ","
                + chargeMoney);
    }

    @Override
    public int compareTo(WaonHistory another) {
        return -(renban - another.renban);
    }

    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();

        map.put("date", date);
        map.put("time", time);
        map.put("chargeMoney", chargeMoney);
        map.put("useMoney", useMoney);
        map.put("restMoney", restMoney);
        map.put("typeName", typeName);
        map.put("point", point);

        return map;
    }
}
