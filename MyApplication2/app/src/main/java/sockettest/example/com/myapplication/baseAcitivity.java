package sockettest.example.com.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

public class baseAcitivity extends AutoLayoutActivity {
    private static final String TAG = "MainActivity";
    private Data application;
    private baseAcitivity oContext;

    //保持唤醒
    PowerManager powerManager = null;
    PowerManager.WakeLock mWakeLock = null;


    //双击退出计时
    private long exitTime;

    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }
    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用myApplication的销毁单个Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis()-exitTime>2000&&event.getRepeatCount()==0){
                Toast toast = Toast.makeText(this, "再点击一次返回", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                exitTime=System.currentTimeMillis();
                Log.i("---", "jin ru if zhong");
            }else {
                removeALLActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_acitivity);

        //保持唤醒
        powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "tag");

        //双击返回 添加当前acivity进列表
        if (application == null) {
            // 得到Application对象
            application = (Data) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法


    }
}
