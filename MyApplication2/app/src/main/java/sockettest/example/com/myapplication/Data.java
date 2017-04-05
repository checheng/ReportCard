package sockettest.example.com.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Data extends Application {
    public static String HOST;
    public static int PORT;
    /**
     * 心跳报文标识
     **/
    public static boolean T = false, run_state = false, Onclick_guzhangfuwei = true;
    public static int MM = 0;
    public static SharedPreferences spp;

    public static boolean permission = false;

    private List<Activity> oList;//用于存放所有启动的Activity的集合

    @Override
    public void onCreate() {
        super.onCreate();
        oList = new ArrayList<Activity>();
        spp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

    /**
     * 将两个数值的字符串合并为一个int型
     **/
    public static int thenumber(String Str) {
        String A = Str.substring(0, 2);
        String B = Str.substring(2, 4);
        int result, a, b;
        a = Integer.parseInt(A, 16);
        b = Integer.parseInt(B, 16);
        result = a * 256 + b;
        return result;
    }

    /**
     * 将一个输入框内数值转成两位16进制字符串形式
     **/
    public static String theString(int i) {

        String a, b;
        a = Integer.toHexString(i / 256);//相对高位
        if (a.length() == 1) {
            a = "0" + a;
        }

        b = Integer.toHexString(i % 256);//相对低位
        if (b.length() == 1) {
            b = "0" + b;
        }


        StringBuffer result = new StringBuffer();
        result = result.append(a).append(b);
//        Log.w("结果",result);“XXXX”

        return result.toString();
    }


    /**
     * 添加主机位至字符串前
     **/

    public static String thehostnumber(String str) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(spp.getString("hostnumber", ""));
        try {
            if (stringBuilder.length() == 2) {
                stringBuilder.append(str);
            } else if (stringBuilder.length() == 1) {
                stringBuilder.append("0").append(str);
            } else {
                stringBuilder.append("01");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    public static String therunstate(boolean run_state) {
        String result;
        if (run_state) {
            result = "运行";
        } else {
            result = "停止";
        }
        return result;
    }

    /**
     * 整10倍数字符串除法
     **/
    public static String division(String s, int n) {
        StringBuilder ss = new StringBuilder();
        switch (n) {
            case 10:
                if (s.length() == 1) {
                    ss.append("0.").append(s);
                } else {
                    ss.append(s);
                    ss.insert(s.length() - 1, ".");
                }
                break;
            case 100:
                if (s.length() == 1) {
                    ss.append("0.0").append(s);
                } else if (s.length() == 2) {
                    ss.append("0.").append(s);
                } else {
                    ss.append(s);
                    ss.insert(s.length() - 2, ".");
                }
                break;
            case 1000:
                if (s.length() == 1) {
                    ss.append("0.00").append(s);
                } else if (s.length() == 2) {
                    ss.append("0.0").append(s);
                } else if (s.length() == 3) {
                    ss.append("0.").append(s);
                } else {
                    ss.append(s);
                    ss.insert(s.length() - 3, ".");
                }
                break;
            default:
                ss.append("0");
                break;
        }
        return ss.toString();
    }

    /**
     * 带小数点的字符串数字转为int（*10 *100 *1000）
     * */
    public  static int pointStringToInt(String string){
        int i=0;
        try {
            int high,low;
            String integer_part = string.substring(0, string.indexOf("."));
            if (integer_part.equals("")) integer_part = "0";
            String decimal_part = string.substring(string.indexOf(".") + 1);
            int times = 10;
            if (decimal_part.length() > 0) {
                for (int m = 0; m > decimal_part.length();m++){
                    times*=10;
                }
            }else times = 0;
            if (decimal_part.equals("")) decimal_part = "0";
            high = Integer.parseInt(integer_part)*times;
            low = Integer.parseInt(decimal_part);
            i=high+low;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 将两位16进制字符串转为带正负号的值
     * */
    public int plus_inus(String string) {
        int value = Integer.parseInt(string, 16);
        Log.w("int", String.valueOf(value));
        if (value / 32768 > 0) {
            value = value % 32768 - 32768;
        }
        return value;
    }
}