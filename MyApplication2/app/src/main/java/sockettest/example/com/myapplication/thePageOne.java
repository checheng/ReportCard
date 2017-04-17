package sockettest.example.com.myapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import function_class.GridViewAdapter;
import function_class.read_LowerComputer;
import function_class.write_LowerComputer;

public class thePageOne extends baseAcitivity {
    private IBackService iBackService;

    private GridView mGridView;
    private GridViewAdapter mFormGridViewAdapter;
    private GridView mGridView2;
    private GridViewAdapter mFormGridViewAdapter2;

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
                message = message.toUpperCase();
//                ToastUtil.toast(getApplicationContext(),message);
                try {
                    if (message.substring(4, 6).equals("2C")) {
                        //90~111
                        form90_111 = mRead_lowerComputer.retrunContent(message);
                        //稳压指令 带正负值
                        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < form90_111.length; i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("content", form90_111[i]);
                            //      Log.w("eee",String.valueOf(mapList));
                            mapList.add(map);
                        }
                        mFormGridViewAdapter2.setStrings(mapList);
                        mGridView2.setAdapter(mFormGridViewAdapter2);
                        mFormGridViewAdapter2.notifyDataSetChanged();
                        mGridView2.invalidate();
                    } else if (message.substring(4, 6).equals("3E")) {
                        //160~190
                        form160_190 = mRead_lowerComputer.retrunContent(message);
                        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < form160_190.length; i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("content", form160_190[i]);
                            mapList.add(map);
                        }
                        mFormGridViewAdapter.setStrings(mapList);
                        mGridView.setAdapter(mFormGridViewAdapter);
                        mFormGridViewAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv.setText(message);
            }
        }
    }

    private MessageBackReciver mReciver;

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_page_one);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mResultText = (TextView) findViewById(R.id.textView23);
        mReciver = new MessageBackReciver(mResultText);
        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
        init();
    }

    private String[] form160_190 = {
            "\\", "A相", "B相", "C相", "N相",
            "系统电压", "0", "0", "0", "\\",
            "负载电流", "0", "0", "0", "0",
            "补偿电流", "0", "0", "0", "0",
            "系统电流", "0", "0", "0", "0",
            "有功功率", "0", "0", "0", "0",
            "无功功率", "0", "0", "0", "0",
            "功率因数", "0", "0", "0", "0",
            "不平衡度U", "不平衡度I", "输出功率", "系统频率", "\\",
            "0", "0", "0", "0", "\\",
    };

    private String[] form90_111 = {
            "\\","单元一", "单元二","故障信息","0",
            "上电压",  "0", "0", "相序信息", "0",
            "下电压","0", "0","输入状态", "0",
            "总电压",  "0", "0","输出状态", "0",
            "温度", "0", "0","充电状态", "0",
            "稳压指令","0", "0", "运行状态", "0",
            "\\","输出状态","故障信息","重启时间","0",
            "TSC","0", "0", "周期点数","0",
            "\\","\\","\\", "模块温度","0",
    };
    public void init(){
        theStarButton = (Button)findViewById(R.id.star);
        theStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    theStarButton.setEnabled(false);
                    if (!TheStarButtonTag) TheStarButtonTag = true;
                    Thread.sleep(200);
//                    boolean isSend = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0003","0001","02","0001"));
                    theStarButton.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        theStopButton = (Button) findViewById(R.id.stop);
        theStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    theStopButton.setEnabled(false);
                    if (!TheStopButtonTag) TheStopButtonTag = true;
                    Thread.sleep(200);
                    theStopButton.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //form1
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<form160_190.length;i++){
            Map<String,Object> map =new  HashMap<String,Object>();
            map.put("content",form160_190[i]);
            mapList.add(map);
        }
        mFormGridViewAdapter = new GridViewAdapter(thePageOne.this,mapList);

        mGridView = (GridView)findViewById(R.id.the_grid_View);
        mGridView.setAdapter(mFormGridViewAdapter);

        //form2
        List<Map<String,Object>> mapList2 = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<form90_111.length;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("content",form90_111[i]);
            mapList2.add(map);
        }
        mFormGridViewAdapter2 = new GridViewAdapter(thePageOne.this,mapList2);
        mGridView2 = (GridView)findViewById(R.id.the_grid_View2);
        mGridView2.setAdapter(mFormGridViewAdapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Intent intent = new Intent();
        intent.setAction("ITOP.MOBILE.SIMPLE.SERVICE.SENSORSERVICE");
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }


    //循环发送请求指令
    private boolean Cycling_Instructions = true;
    private read_LowerComputer mRead_lowerComputer = new read_LowerComputer();

    @Override
    protected void onResume() {
        Cycling_Instructions = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                    while (Cycling_Instructions) {
                        try {
                            if (TheStarButtonTag){
                                Thread.sleep(1000);
                                boolean isSend = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0003","0001","02","0001"));
                                Thread.sleep(1000);
                                TheStarButtonTag=false;
                            }
                            if (TheStopButtonTag){
                                Thread.sleep(1000);
                                boolean isSend = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0003", "0001", "02", "0000"));
                                Thread.sleep(1000);
                                TheStopButtonTag = false;
                           }
                            if ((!TheStarButtonTag) && (!TheStopButtonTag)) {
                                Thread.sleep(1500);
                                boolean isSend = iBackService.sendMessage(mRead_lowerComputer.theAskCode(90));
                            }
                            if ((!TheStarButtonTag) && (!TheStopButtonTag)) {
                                Thread.sleep(1500);
                                boolean isSend2 = iBackService.sendMessage(mRead_lowerComputer.theAskCode(160));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

        }).start();
        super.onResume();
    }


    @Override
    protected void onPause() {
        //取消循环发送请求
        Cycling_Instructions = false;
        super.onPause();
    }


    private Button theStarButton,theStopButton;
    private boolean TheStarButtonTag=false,TheStopButtonTag=false;
    private write_LowerComputer mWrite_lowerComputer = new write_LowerComputer();
    //滑动切换
    private float x1 = 0, x2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        if (Data.permission) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                x2 = event.getX();
                if (x1 - x2 > 50) {
                    unbindService(serviceConnection);
                    mLocalBroadcastManager.unregisterReceiver(mReciver);
                    Intent intent = new Intent();
                    intent.setClass(this, thePageTwo.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.fromright, R.animator.toleft);//*跳转动画*
                    //finish();
                }
            }
        }
        return super.onTouchEvent(event);
    }


}
