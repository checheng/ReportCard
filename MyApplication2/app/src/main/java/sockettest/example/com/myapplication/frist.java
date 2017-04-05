package sockettest.example.com.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import function_class.crc16;

import static function_class.crc16.crc16;
import static sockettest.example.com.myapplication.Data.Onclick_guzhangfuwei;
import static sockettest.example.com.myapplication.Data.T;
import static sockettest.example.com.myapplication.Data.division;
import static sockettest.example.com.myapplication.Data.run_state;
import static sockettest.example.com.myapplication.Data.spp;
import static sockettest.example.com.myapplication.Data.thehostnumber;
import static sockettest.example.com.myapplication.Data.thenumber;

/**
 * Created by Administrator on 2016/11/25.
 */

public class frist extends AutoLayoutActivity {

    private Data application;
    private frist oContext;

    private long exitTime,fristclicktime;
    private static final String TAG = "MainActivity";
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

    private EditText ua,ub,uc,ia,ib,ic,iaa,ibb,icc,inn,IIa,IIb,IIc,A_phase,B_phase,C_phase,frequency,temperature,IGBTtemperature,bupinghengdu,Udc1,Udc2,Udc3;
    private TextView state,receive,power1,power2,power3,power4;
    private Button guzhang,guzhangfuwei;
    private boolean guzhangzhishideng=false;
    private TextView guowen,acgy,acqy,dcgy,i1nhd,apfgl,phaseerr,igbt1errhd,i1glhd,i2nhd,dc1gyhd,dc2gyhd,igbt2errhd,i2glhd,erroreeprom;
    private  PopupWindow mPopupWindow;

    private String chongdian="";
    private Timer timer = null;
    private TimerTask task = null;
    private int im=0;
    GestureDetector detector;
    private TextView mResultText;
    private Intent mServiceIntent;
    private  boolean resultCrc16;
//    private  boolean,chongdianstate=false

    class MessageBackReciver extends BroadcastReceiver {
        private WeakReference<TextView> textView;

        public MessageBackReciver(TextView tv) {
            textView = new WeakReference<TextView>(tv);
        }

        /**充电状态显示**/
        private Handler handler=new Handler(){
            /**重写handleMessage方法*/
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1%4){
                    case 1:
                        power2.setBackgroundColor(Color.WHITE);
                        break;
                    case 2:
                        power3.setBackgroundColor(Color.WHITE);
                        break;
                    case 3:
                        power4.setBackgroundColor(Color.WHITE);
                        break;
                    case 0:
                        power2.setBackgroundColor(Color.BLACK);
                        power3.setBackgroundColor(Color.BLACK);
                        power4.setBackgroundColor(Color.BLACK);
                        break;
                }
                startTime();//执行计时方法
            }
        };
        private void startTime(){
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    im++;
                    Message message = handler.obtainMessage();//获取Message对象
                    message.arg1 = im;//设置Message对象附带的参数
                    handler.sendMessage(message);//向主线程发送消息
                }
            };
            timer.schedule(task, 1000);//执行计时器事件
        }
        private void stopTime(){
            timer.cancel();//注销计时器事件
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
                //校验Crc
                message = message.replace(" ", "");
                receive.setText(message);
                //  Log.w("字符串",message);
                resultCrc16 = crc16.verify(message);
                int obtianlength = Integer.parseInt(message.substring(4, 6), 16);
              //  Log.w("长度",String.valueOf(obtianlength == 40 || obtianlength == 28));
                if (obtianlength == 40 || obtianlength == 28) {
                    Log.w("CRC", String.valueOf(resultCrc16));
                    if (resultCrc16) {
                        int length = ((message.length() - 10) / 2);/**主机2 功能2 长度2 CRC4**/
                        if (message.substring(0, 2).equals(spp.getString("hostnumber","")) && message.substring(2, 4).equals("03") && length == obtianlength) {
                            String s = message;
                            if (s.length() == 90) {
                                /**01031C的应答**/
                                //  Log.w(" ua",String.valueOf(thenumber(s.substring(6,10))));
                                try {
                                    ua.setText(String.valueOf(thenumber(s.substring(6, 10))));
                                    ub.setText(String.valueOf(thenumber(s.substring(10, 14))));
                                    uc.setText(String.valueOf(thenumber(s.substring(14, 18))));
                                    IIa.setText(String.valueOf(thenumber(s.substring(18, 22))));
                                    IIb.setText(String.valueOf(thenumber(s.substring(22, 26))));
                                    IIc.setText(String.valueOf(thenumber(s.substring(26, 30))));
                                    iaa.setText(String.valueOf(thenumber(s.substring(30, 34))));
                                    ibb.setText(String.valueOf(thenumber(s.substring(34, 38))));
                                    icc.setText(String.valueOf(thenumber(s.substring(38, 42))));
                                    inn.setText(String.valueOf(thenumber(s.substring(42, 46))));
                                    ia.setText(String.valueOf(thenumber(s.substring(46, 50))));
                                    ib.setText(String.valueOf(thenumber(s.substring(50, 54))));
                                    ic.setText(String.valueOf(thenumber(s.substring(54, 58))));
                                    DecimalFormat df = new DecimalFormat("#0.000");
                                    A_phase.setText(division(String.valueOf((thenumber(s.substring(58, 62)))),1000));
                                    B_phase.setText(division(String.valueOf((thenumber(s.substring(62, 66)))),1000));
                                    C_phase.setText(division(String.valueOf((thenumber(s.substring(66, 70)))),1000));
                                    frequency.setText(division(String.valueOf(thenumber(s.substring(82, 86))),100));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (s.length() == 66) {
                                /**对应010328**/
                                try {
                                    /**当前状态指示灯**/
                                    int thestate = thenumber((s.substring(6,10)));
                                    Log.w("thestate",String.valueOf(thestate));
                                    if (thestate==0){
                                        run_state=false;
                                        if (state.getText().equals("  运行  ")){state.setText("  停止  ");
                                        }
                                    } else if (thestate == 1) {
                                        run_state=true;
                                        if (state.getText().equals("  停止  ")){state.setText("  运行  ");}
                                    }


                                    /**
                                     * 充电状态
                                     **/
                                /*    int powerstate =thenumber((s.substring(10,14)));
                                    if (powerstate==0){
                                        chongdianstate=false;
                                        if (chongdian.getText().equals("充电")){state.setText("取消充电");
                                        }
                                    } else if (powerstate == 1) {
                                        chongdianstate=true;
                                        if (chongdian.getText().equals("取消充电")){state.setText("充电");}
                                    }
*/
                                    if (!chongdian.equals(s.substring(10, 14))) {
                                        chongdian=s.substring(10,14);

                                        if (chongdian.equals("0003")) {
                                            /**充电中**/
                                            power1.setBackgroundColor(Color.WHITE);
                                            power2.setBackgroundColor(Color.BLACK);
                                            power3.setBackgroundColor(Color.BLACK);
                                            power4.setBackgroundColor(Color.BLACK);
                                            startTime();


                                        } else if (chongdian.toUpperCase().equals("000B")) {
                                            /**充满**/
                                            stopTime();
                                            im = 0;
                                            power1.setBackgroundColor(Color.WHITE);
                                            power2.setBackgroundColor(Color.WHITE);
                                            power3.setBackgroundColor(Color.WHITE);
                                            power4.setBackgroundColor(Color.WHITE);

                                        } else {
                                            /**闲置**/
                                            stopTime();
                                            im = 0;
                                            power1.setBackgroundColor(Color.BLACK);
                                            power2.setBackgroundColor(Color.BLACK);
                                            power3.setBackgroundColor(Color.BLACK);
                                            power4.setBackgroundColor(Color.BLACK);
                                        }
                                    }


                                    /**处理故障指示**/
                                    int m = thenumber(s.substring(14, 18));
                                    if (!(m%32768==0)) {
                                        /**存在故障**/
                                        guzhang.setBackgroundResource(R.drawable.the_wrong);/**指示灯变红**/
                                    }
                                    StringBuilder ss1 = new StringBuilder();
                                    ss1.append(Integer.toString(Integer.parseInt(s.substring(14, 18), 16), 2));
                                    while (!(ss1.length() == 16)) {
                                        ss1.insert(0, "0");
                                    }/**高位补零*/
                                    Log.w("故障位数",ss1.toString());
                                    //   Log.w("故障指示",ss1.toString());
                                    Log.w("故障指示灯",String.valueOf(guzhangzhishideng));
                                    if (guzhangzhishideng)/**故障指示界面已开启**/ {
                                        if (ss1.substring(7, 8).equals("1")) {
                                            guowen.setBackgroundResource(R.drawable.the_wrong);
                                            guowen.setTextColor(0xffffffff);
                                        } else if (ss1.substring(0, 1).equals("0")) {
                                            guowen.setBackgroundResource(R.drawable.the_right);
                                            guowen.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(6, 7).equals("1")) {
                                            acgy.setBackgroundResource(R.drawable.the_wrong);
                                            acgy.setTextColor(0xffffffff);
                                        } else {
                                            acgy.setBackgroundResource(R.drawable.the_right);
                                            acgy.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(5, 6).equals("1")) {
                                            acqy.setBackgroundResource(R.drawable.the_wrong);
                                            acqy.setTextColor(0xffffffff);
                                        } else {
                                            acqy.setBackgroundResource(R.drawable.the_right);
                                            acqy.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(4, 5).equals("1")) {
                                            dcgy.setBackgroundResource(R.drawable.the_wrong);
                                            dcgy.setTextColor(0xffffffff);
                                        } else {
                                            dcgy.setBackgroundResource(R.drawable.the_right);
                                            dcgy.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(3, 4).equals("1")) {
                                            apfgl.setBackgroundResource(R.drawable.the_wrong);
                                            apfgl.setTextColor(0xffffffff);
                                        } else {
                                            apfgl.setBackgroundResource(R.drawable.the_right);
                                            apfgl.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(2, 3).equals("1")) {
                                            phaseerr.setBackgroundResource(R.drawable.the_wrong);
                                            phaseerr.setTextColor(0xffffffff);
                                        } else {
                                            phaseerr.setBackgroundResource(R.drawable.the_right);
                                            phaseerr.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(15, 16).equals("1")) {
                                            igbt1errhd.setBackgroundResource(R.drawable.the_wrong);
                                            igbt1errhd.setTextColor(0xffffffff);
                                        } else {
                                            igbt1errhd.setBackgroundResource(R.drawable.the_right);
                                            igbt1errhd.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(14, 15).equals("1")) {
                                            i1glhd.setBackgroundResource(R.drawable.the_wrong);
                                            i1glhd.setTextColor(0xffffffff);
                                        } else {
                                            i1glhd.setBackgroundResource(R.drawable.the_right);
                                            i1glhd.setTextColor(0xff000000);
                                        }

                                        if (ss1.substring(13, 14).equals("1")) {
                                            dc1gyhd.setBackgroundResource(R.drawable.the_wrong);
                                            dc1gyhd.setTextColor(0xffffffff);
                                        } else {
                                            dc1gyhd.setBackgroundResource(R.drawable.the_right);
                                            dc1gyhd.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(12, 13).equals("1")) {
                                            igbt2errhd.setBackgroundResource(R.drawable.the_wrong);
                                            igbt2errhd.setTextColor(0xffffffff);
                                        } else {
                                            igbt2errhd.setBackgroundResource(R.drawable.the_right);
                                            igbt2errhd.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(11, 12).equals("1")) {
                                            i2glhd.setBackgroundResource(R.drawable.the_wrong);
                                            i2glhd.setTextColor(0xffffffff);
                                        } else {
                                            i2glhd.setBackgroundResource(R.drawable.the_right);
                                            i2glhd.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(10, 11).equals("1")) {
                                            dc2gyhd.setBackgroundResource(R.drawable.the_wrong);
                                            dc2gyhd.setTextColor(0xffffffff);
                                        } else {
                                            dc2gyhd.setBackgroundResource(R.drawable.the_right);
                                            dc2gyhd.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(1, 2).equals("1")) {
                                            erroreeprom.setBackgroundResource(R.drawable.the_wrong);
                                            erroreeprom.setTextColor(0xffffffff);
                                        } else {
                                            erroreeprom.setBackgroundResource(R.drawable.the_right);
                                            erroreeprom.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(9, 10).equals("1")) {
                                            i1nhd.setBackgroundResource(R.drawable.the_wrong);
                                            i1nhd.setTextColor(0xffffffff);
                                        } else {
                                            i1nhd.setBackgroundResource(R.drawable.the_right);
                                            i1nhd.setTextColor(0xff000000);
                                        }
                                        if (ss1.substring(8, 9).equals("1")) {
                                            i2nhd.setBackgroundResource(R.drawable.the_wrong);
                                            i2nhd.setTextColor(0xffffffff);
                                        } else {
                                            i2nhd.setBackgroundResource(R.drawable.the_right);
                                            i2nhd.setTextColor(0xff000000);
                                        }
                                    }
                                    temperature.setText(String.valueOf(thenumber(s.substring(22, 26))) + "°c");
                                    IGBTtemperature.setText(String.valueOf(thenumber(s.substring(26, 30))) + "°c");
                                    Udc1.setText(String.valueOf(thenumber(s.substring(34, 38))));
                                    Udc2.setText(String.valueOf(thenumber(s.substring(38, 42))));
                                    Udc3.setText(String.valueOf(thenumber(s.substring(42, 46))));
                                    if(Integer.valueOf(s.substring(58, 62),16)<100){
                                        String ty1 =division(String.valueOf(thenumber(s.substring(58, 62))),10);
                                    bupinghengdu.setText(ty1);
                                    }else bupinghengdu.setText("100");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
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
        mResultText = (TextView) findViewById(R.id.textView10);
        state = (TextView)findViewById(R.id.state);
        ua = (EditText)findViewById(R.id.ua);
        ub = (EditText)findViewById(R.id.ub);
        uc = (EditText)findViewById(R.id.uc);
        ia= (EditText)findViewById(R.id.ia);
        ib= (EditText)findViewById(R.id.ib);
        ic= (EditText)findViewById(R.id.ic);
        iaa= (EditText)findViewById(R.id.iaa);
        ibb= (EditText)findViewById(R.id.ibb);
        icc= (EditText)findViewById(R.id.icc);
        inn= (EditText)findViewById(R.id.inn);
        IIa= (EditText)findViewById(R.id.IIa);
        IIb= (EditText)findViewById(R.id.IIb);
        IIc= (EditText)findViewById(R.id.IIc);
        A_phase = (EditText)findViewById(R.id.A_phase);
        B_phase = (EditText)findViewById(R.id.B_phase);
        C_phase = (EditText)findViewById(R.id.C_phase);
        frequency = (EditText)findViewById(R.id.frequency);
        temperature = (EditText)findViewById(R.id.temperature);
        IGBTtemperature = (EditText)findViewById(R.id.IGBTtemperature);
        bupinghengdu = (EditText)findViewById(R.id.bupinghengdu);
        Udc1 = (EditText)findViewById(R.id.Udc1);
        Udc2 = (EditText)findViewById(R.id.Udc2);
        Udc3 = (EditText)findViewById(R.id.Udc3);
        guzhang = (Button)findViewById(R.id.guzhang);
        power1 = (TextView)findViewById(R.id.power1);
        power2 = (TextView)findViewById(R.id.power2);
        power3 = (TextView)findViewById(R.id.power3);
        power4 = (TextView)findViewById(R.id.power4);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fristpage);
        /**开机发送故障复位指令**/
        if (Onclick_guzhangfuwei) {
            Onclick_guzhangfuwei=false;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    String res = "10 00 00 00 01 02 00 01";
                    res = thehostnumber(res);
                    try {
                        res = crc16(res);
                        boolean isSend = iBackService.sendMessage(res);//Send Content by socket
                        Toast.makeText(frist.this, isSend ? "故障复位指令已发送" : "故障回传指令未发送", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 200);
        }
        if (application == null) {
            // 得到Application对象
            application = (Data) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        init();
        receive = (TextView)findViewById(R.id.receive1);

        mReciver = new MessageBackReciver(mResultText);

        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);

        guzhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    showPopupWindow();
                    guzhangzhishideng = true;
                    }catch (Exception e){e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final Intent intent = new Intent();
        intent.setAction("ITOP.MOBILE.SIMPLE.SERVICE.SENSORSERVICE");
        //	stopService(intent);
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        T=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        T=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        T=false;
        //unbindService(serviceConnection);
        //mLocalBroadcastManager.unregisterReceiver(mReciver);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            if(x1 - x2 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(frist.this,thrid.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromright,R.animator.toleft);/**跳转动画**/
                //finish();
            }
        }
        return super.onTouchEvent(event);
    }


    /**修改返回键**/





    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(frist.this).inflate(R.layout.guzhangbutton, null);
        mPopupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
       // mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(contentView);
        //设置各个控件的点击响应
        guowen = (TextView)contentView.findViewById(R.id.guowen);
        acgy = (TextView)contentView.findViewById(R.id.ACgy);
        acqy = (TextView)contentView.findViewById(R.id.ACqy);
        dcgy = (TextView)contentView.findViewById(R.id.DCgy);
        i1nhd = (TextView)contentView.findViewById(R.id.I1_N_HD);
        apfgl = (TextView)contentView.findViewById(R.id.APFgl);
        phaseerr = (TextView)contentView.findViewById(R.id.PHASE_ERR);
        igbt1errhd = (TextView)contentView.findViewById(R.id.IGBT1_ERR_HD);
        i1glhd = (TextView)contentView.findViewById(R.id.I1gl_HD);
        i2nhd = (TextView)contentView.findViewById(R.id.I2_N_HD);
        dc1gyhd = (TextView)contentView.findViewById(R.id.DC1gy_HD);
        dc2gyhd = (TextView)contentView.findViewById(R.id.DC2gy_HD);
        igbt2errhd = (TextView)contentView.findViewById(R.id.IGBT2_ERR_HD);
        i2glhd = (TextView)contentView.findViewById(R.id.I2gl_HD);
        erroreeprom = (TextView)contentView.findViewById(R.id.ERROR_eeprom);
        guzhangfuwei = (Button)contentView.findViewById(R.id.guzhangfuwei);


        guzhangfuwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res ="10 00 00 00 01 02 00 01";
                res = thehostnumber(res);
                try {
                    res = crc16.crc16(res);
                    boolean isSend = iBackService.sendMessage(res);//Send Content by socket
                    Toast.makeText(getApplicationContext(), isSend ? "复位指令已发送" : "复位指令未发送", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

               // mPopupWindow.dismiss();
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(frist.this).inflate(R.layout.activity_main, null);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

    }

}
