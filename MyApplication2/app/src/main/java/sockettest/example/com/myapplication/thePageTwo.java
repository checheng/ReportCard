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
import android.widget.GridView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import function_class.GridViewAdapter;
import function_class.read_LowerComputer;

public class thePageTwo extends baseAcitivity {

    private GridView mGridView;
    private GridViewAdapter mFormGridViewAdapter;



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
                    if (message.substring(4, 6).equals("60")) {
                        //112~159
                        form112_159= mRead_lowerComputer.retrunContent(message);
                        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < form112_159.length; i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("content", form112_159[i]);
                            //      Log.w("eee",String.valueOf(mapList));
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

        ;
    }

    private MessageBackReciver mReciver;

    private IntentFilter mIntentFilter;

    private LocalBroadcastManager mLocalBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_page_two);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mResultText = (TextView) findViewById(R.id.textView22);//***********
        mReciver = new MessageBackReciver(mResultText);
        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
        init();
    }
    private String[] form112_159 = {
            " ", "A相", "B相", "C相",
            "总畸变率I", "0", "0", "0",
            "2次畸变", "0", "0", "0",
            "3次畸变", "0", "0", "0",
            "4次畸变", "0", "0", "0",
            "5次畸变", "0", "0", "0",
            "6次畸变", "0", "0", "0",
            "7次畸变", "0", "0", "0",
            "8次畸变", "0", "0", "0",
            "9次畸变", "0", "0", "0",
            "10次畸变", "0", "0", "0",
            "11次畸变", "0", "0", "0",
            "12次畸变", "0", "0", "0",
            "13次畸变", "0", "0", "0",
            "14次畸变", "0", "0", "0",
            "15次畸变", "0", "0", "0",
            "总畸变率U", "0", "0", "0",
    };



    public void  init(){



        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<form112_159.length;i++){
            Map<String,Object> map =new HashMap<String,Object>();
            map.put("content",form112_159[i]);
            mapList.add(map);
        }
        mFormGridViewAdapter = new GridViewAdapter(this,mapList);
        mGridView = (GridView)findViewById(R.id.the_grid_View1);
        mGridView.setAdapter( mFormGridViewAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                        Thread.sleep(1000);
                        boolean isSend = iBackService.sendMessage(mRead_lowerComputer.theAskCode(112));
                        Thread.sleep(500);
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




    //滑动切换
    private float x1 = 0, x2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            if (x2 - x1 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(this, thePageOne.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromleft, R.animator.toright);
                //finish();
            } else if (x1 - x2 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(this, thePageThree.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromright, R.animator.toleft);
                //finish();
            }
        }
        return super.onTouchEvent(event);

    }
}
