package sockettest.example.com.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;

import function_class.crc16;

import static function_class.crc16.crc16;
import static sockettest.example.com.myapplication.Data.MM;
import static sockettest.example.com.myapplication.Data.run_state;
import static sockettest.example.com.myapplication.Data.spp;
import static sockettest.example.com.myapplication.Data.theString;
import static sockettest.example.com.myapplication.Data.thehostnumber;
import static sockettest.example.com.myapplication.Data.thenumber;
import static sockettest.example.com.myapplication.Data.therunstate;

/**
 * Created by Administrator on 2016/12/6.
 */

public class fifth extends AutoLayoutActivity {
    private static final String TAG = "MainActivity";
    private Data application;
    private fifth oContext;

    private long exitTime;

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    private IBackService iBackService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            iBackService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            iBackService = IBackService.Stub.asInterface(service);
        }
    };
    GestureDetector detector;
    private TextView mResultText;
    private Intent mServiceIntent;

    private  boolean resultCrc16;
    private EditText tongxundizhi2input,xiugaidizhi2input;
    private TextView runstate;

    class MessageBackReciver extends BroadcastReceiver {
        private WeakReference<TextView> textView;

        public MessageBackReciver(TextView tv) {
            textView = new WeakReference<TextView>(tv);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TextView tv = textView.get();
            if (action.equals(BackService.HEART_BEAT_ACTION)) {
                if (null != tv) {
                    tv.setText("Get a heart heat");
                }
            } else {
                String message = intent.getStringExtra("message");
                message = message.replace(" ", "");
                resultCrc16 = crc16.verify(message);
                int obtianlength = Integer.parseInt(message.substring(4, 6), 16);
                if (obtianlength == 6  ) {
                    Log.w("CRC", String.valueOf(resultCrc16));
                    if (resultCrc16) {
                        try {
                            int length = ((message.length() - 10) / 2);/**主机2 功能2 长度2 CRC4**/
                            Log.w("字符串主机",message.substring(0, 2));
                            Log.w("系统主机位",spp.getString("hostnumber",""));
                            if (message.substring(0, 2).equals(spp.getString("hostnumber","")) && (message.substring(2, 4).equals("03")) && length == obtianlength) {
                                tongxundizhi2input.setText(String.valueOf(thenumber(message.substring(10, 14))));
                                xiugaidizhi2input.setText(tongxundizhi2input.getText());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv.setText(message);
                }
            }
        }
    }

    private MessageBackReciver mReciver;

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;

    public void init(){
        runstate = (TextView)findViewById(R.id.runstate);
        tongxundizhi2input =(EditText)findViewById(R.id.tongxundizhi2input);
        xiugaidizhi2input =(EditText)findViewById(R.id.xiugaidizhi2input);
    }

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
        setContentView(R.layout.fifthpage);
        if (application == null) {
            // 得到Application对象
            application = (Data) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mResultText = (TextView) findViewById(R.id.hide);
        mReciver = new MessageBackReciver(mResultText);
        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Intent intent = new Intent();
        intent.setAction("ITOP.MOBILE.SIMPLE.SERVICE.SENSORSERVICE");
        // stopService(intent);
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
        MM= 3;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();

        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();

       /*     if (x2 - x1 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(fifth.this, fourth.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromright, R.animator.toleft);

                //finish();
            }*/
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runstate.setText(therunstate(run_state));
        new Handler().postDelayed(new Runnable(){
            public void run() {

                String res ="03 00 04 00 03";
                res = thehostnumber(res);
                try {
                    res = crc16.crc16(res);
                    boolean isSend = iBackService.sendMessage(res);//Send Content by socket
                    Toast.makeText(fifth.this, isSend ? "参数回传指令已发送" : "参数回传指令未发送", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }

        }, 500);
    }

    public  void canshuxiugai(View view){
        try{
            StringBuilder str = new StringBuilder();
            String xiugai;
            xiugai = thehostnumber("100004000306");
            str.append(xiugai);
            str.append("0000");/**修改地址1**/
            /**修改地址2**/
            if (xiugaidizhi2input.length() == 0) {
                Toast.makeText(this, "修改地址为空", Toast.LENGTH_SHORT).show();
            } else {
                str.append(theString(Integer.parseInt(xiugaidizhi2input.getText().toString())));
                str.append("0000");/**备用3**/
                String result = crc16(str.toString());
                boolean isSend = iBackService.sendMessage(result);
                Toast.makeText(this, isSend ? "参数修改指令已发送" : "参数修改指令未发送", Toast.LENGTH_SHORT).show();
                if (isSend){
                    if (xiugaidizhi2input.getText().length()==2){
                    spp.edit().putString("hostnumber",xiugaidizhi2input.getText().toString()).commit();
                    }else if (xiugaidizhi2input.getText().length()==1){
                        spp.edit().putString("hostnumber","0"+xiugaidizhi2input.getText().toString()).commit();
                    }else {
                        Toast.makeText(getApplicationContext(),"请输入两位有效主机地址",Toast.LENGTH_SHORT);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void canshuhuichuan(View view){
        clear();
        String res ="03 00 04 00 03";
        res = thehostnumber(res);
        try {
            res = crc16.crc16(res);
            boolean isSend = iBackService.sendMessage(res);//Send Content by socket
            Toast.makeText(this, isSend ? "参数回传指令已发送" : "参数回传指令未发送", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        tongxundizhi2input.setText("");
    }
}
