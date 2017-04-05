package function_class;

/**
 * Created by Administrator on 2016/11/28.
 */

public class crc16 {

    /**
     * 添加校验crc16字符串至末尾
     */
    public static String crc16(String adata) {
        if (adata.length() < 4) {
            adata = "0000";
        }
        adata = adata.replace(" ", "");
        String ad = adata.substring(2, 4);
        int[] w = new int[adata.length() / 2];
        for (int i = 0; i < adata.length(); i = i + 2) {
            ad = adata.substring(i, i + 2);
            w[i / 2] = Integer.parseInt(ad, 16);
        }
        int[] data = w;
        int[] stem = new int[data.length + 2];
        int a, b, c;
        a = 0xFFFF;
        b = 0xA001;
        for (int i = 0; i < data.length; i++) {
            a ^= data[i];
            for (int j = 0; j < 8; j++) {
                c = (int) (a & 0x01);
                a >>= 1;
                if (c == 1) {
                    a ^= b;
                }
                System.arraycopy(data, 0, stem, 0, data.length);
                stem[stem.length - 2] = (int) (a & 0xFF);
                stem[stem.length - 1] = (int) (a >> 8);
            }
        }
        int[] z = stem;
        StringBuffer s = new StringBuffer();
        for (int j = 0; j < z.length; j++) {
            s.append(String.format("%02x", z[j]));
        }
//		System.out.print(s);
        return String.valueOf(s);
    }

    /**
     * 验证字符串并获得CRC校验字符串
     */
    public static String thecrc16(String adata) {
        if (adata.length() < 4) {
            adata = "0000";
        }

        String ad = adata.substring(2, 4);
        int[] w = new int[adata.length() / 2];
        for (int i = 0; i < adata.length(); i = i + 2) {
            ad = adata.substring(i, i + 2);
            w[i / 2] = Integer.parseInt(ad, 16);
        }
        int[] data = w;
        int[] stem = new int[data.length + 2];
        int a, b, c;
        a = 0xFFFF;
        b = 0xA001;
        for (int i = 0; i < data.length; i++) {
            a ^= data[i];
            for (int j = 0; j < 8; j++) {
                c = (int) (a & 0x01);
                a >>= 1;
                if (c == 1) {
                    a ^= b;
                }
                System.arraycopy(data, 0, stem, 0, data.length);
                stem[stem.length - 2] = (int) (a & 0xFF);
                stem[stem.length - 1] = (int) (a >> 8);
            }
        }
        int[] z = stem;
        StringBuffer s = new StringBuffer();
        for (int j = 0; j < z.length; j++) {
            s.append(String.format("%02x", z[j]));
        }
        String crc = String.valueOf(s).substring(adata.length());
        return crc;
    }

    /**
     * 收到数据中的crc检验位
     */
    public static String obtainCRC(String str) {
        String crc;
        if (str.length() >= 4) {
            crc = str.substring(str.length() - 4);
        } else crc = null;
        return crc;
    }

    /**
     * 收到数据中用于验证的验证字符串
     */
    public static String obtainString(String str) {
        String string;
        if (str.length() >= 4) {
            string = str.substring(0, str.length() - 4);
        } else {
            string = null;
        }
        return string;
    }

    /**
     * 验证字符串与其校验位是否匹配
     */
    public static boolean verify(String str) {
        str = str.replace(" ", "");
        boolean ff = false;
        if (str.length() > 4) {
            String obtaintString = obtainString(str);
            //  Log.w("获得校验用字符串",obtaintString);
            String obtainCRC = obtainCRC(str);
            //  Log.w("获得校验位",obtainCRC);
            String thecrc16 = thecrc16(obtaintString);
            thecrc16 = thecrc16.toUpperCase();
            // Log.w("验证CRC",thecrc16);
            ff = obtainCRC.equals(thecrc16);
            // Log.w("校验结果为",String.valueOf(ff));
        }
        return ff;
    }

}
