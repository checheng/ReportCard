package function_class;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/2.
 */

public class show {

    private static Toast mToast = null;
    public static void showToast(Context context, String text, int duration) {
        cancelToast();
        mToast = Toast.makeText(context, text, duration);
        mToast.show();
    }

    public static void showToastShort(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    //
    public static void showRquestShort(Context context, String text, boolean isSend) {
        showToast(context, isSend ? (text + "指令已发送") : (text + "指令未发送"), Toast.LENGTH_SHORT);
    }

    public static void showToastShort(Context context, int resId) {
        showToastShort(context, context.getString(resId));
    }

    public static void showToastLong(Context context, String text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showToastLong(Context context, int resId) {
        showToastLong(context, context.getString(resId));
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
