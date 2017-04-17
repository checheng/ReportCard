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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;

import function_class.crc16;

import static function_class.crc16.crc16;
import static sockettest.example.com.myapplication.Data.permission;
import static sockettest.example.com.myapplication.Data.run_state;
import static sockettest.example.com.myapplication.Data.spp;
import static sockettest.example.com.myapplication.Data.theString;
import static sockettest.example.com.myapplication.Data.thehostnumber;
import static sockettest.example.com.myapplication.Data.thenumber;
import static sockettest.example.com.myapplication.Data.therunstate;


/**
 * Created by Administrator on 2016/12/2.
 */

public class thrid extends AutoLayoutActivity {
    private static final String TAG = "MainActivity";
    private Data application;
    private thrid oContext;
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

    private long exitTime;

    private EditText bbct, cqtm, zzva, bqva, bqra, xxen, yxnm, bphzb, jbzb, xbzb, ysre;
    private TextView TextView3, TextView4, TextView5, TextView6, TextView7, TextView8;
    private Spinner azct, yxmd;
    private static int posazct, posyxmd;
    private static boolean wugongclick = false, bupinghengclick = false, xieboclick = false;
    private Button wugong, bupingheng, xiebo;
    private TextView runstate;

    private boolean resultCrc16;

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
//                Log.w("字符串", message);
                resultCrc16 = crc16.verify(message);
                int obtianlength = Integer.parseInt(message.substring(4, 6), 16);
                if (obtianlength == 26) {
                    Log.w("CRC", String.valueOf(resultCrc16));
                    if (resultCrc16) {
                        int length = ((message.length() - 10) / 2);/**主机2 功能2 长度2 CRC4**/
                        if (message.substring(0, 2).equals(spp.getString("hostnumber", "")) && message.substring(2, 4).equals("03") && (length == obtianlength)) {
                            String s = message;
                            try {
                                if (thenumber(s.substring(6, 10)) == 0) {
                                    azct.setSelection(0);
                                } else if (thenumber(s.substring(6, 10)) == 1) {
                                    azct.setSelection(1);
                                }

                                if (s.substring(10, 14).equals("2000")) {
                                    yxmd.setSelection(0);
                                } else if (s.substring(10, 14).equals("8000")) {
                                    yxmd.setSelection(2);
                                } else if (s.substring(10, 11).equals("1")) {
                                    yxmd.setSelection(1);
                                    if (s.substring(11, 12).equals("1")) {
                                        wugong.setText("开");
                                        wugongclick = true;
                                    }
                                    if (s.substring(12, 13).equals("1")) {
                                        bupingheng.setText("开");
                                        bupinghengclick = true;
                                    }
                                    if (s.substring(13, 14).equals("1")) {
                                        xiebo.setText("开");
                                        xieboclick = true;
                                    }
                                }

                                cqtm.setText(String.valueOf(thenumber(s.substring(14, 18))));
                                bbct.setText(String.valueOf(thenumber(s.substring(18, 22))));
                                yxnm.setText(String.valueOf(thenumber(s.substring(22, 26))));
                                bqva.setText(String.valueOf(thenumber(s.substring(26, 30))));
                                bqra.setText(String.valueOf(thenumber(s.substring(30, 34))));
                                zzva.setText(String.valueOf(thenumber(s.substring(34, 38))));
                                jbzb.setText(String.valueOf(thenumber(s.substring(38, 42))));
                                bphzb.setText(String.valueOf(thenumber(s.substring(42, 46))));
                                xbzb.setText(String.valueOf(thenumber(s.substring(46, 50))));
                                xxen.setText(String.valueOf(thenumber(s.substring(50, 54))));
                                ysre.setText(String.valueOf(thenumber(s.substring(54, 58))));

                            } catch (Exception e) {
                                e.printStackTrace();
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000 && event.getRepeatCount() == 0) {
                Toast toast = Toast.makeText(this, "再点击一次返回", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                exitTime = System.currentTimeMillis();
                Log.i("---", "jin ru if zhong");
            } else {
                removeALLActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thridpage);
        if (application == null) {
            // 得到Application对象
            application = (Data) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法

        init();
        //判断是否是管理员用户
        if (!permission) {
            TextView3.setVisibility(View.INVISIBLE);
            TextView4.setVisibility(View.INVISIBLE);
            TextView5.setVisibility(View.INVISIBLE);
            TextView6.setVisibility(View.INVISIBLE);
            TextView7.setVisibility(View.INVISIBLE);
            TextView8.setVisibility(View.INVISIBLE);
            zzva.setVisibility(View.INVISIBLE);
            bqva.setVisibility(View.INVISIBLE);
            bqra.setVisibility(View.INVISIBLE);
            xxen.setVisibility(View.INVISIBLE);
            yxnm.setVisibility(View.INVISIBLE);
            ysre.setVisibility(View.INVISIBLE);
        }
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mResultText = (TextView) findViewById(R.id.hide);
        mReciver = new MessageBackReciver(mResultText);
        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
    }

    public void init() {
        TextView3 = (TextView) findViewById(R.id.textView3);
        TextView4 = (TextView) findViewById(R.id.textView4);
        TextView5 = (TextView) findViewById(R.id.textView5);
        TextView6 = (TextView) findViewById(R.id.textView6);
        TextView7 = (TextView) findViewById(R.id.textView7);
        TextView8 = (TextView) findViewById(R.id.textView8);

        runstate = (TextView) findViewById(R.id.runstate);
        azct = (Spinner) findViewById(R.id.zlUd);
        bbct = (EditText) findViewById(R.id.bbCT);
        yxmd = (Spinner) findViewById(R.id.yxMD);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1);
        //  yxmd.setAdapter(adapter);
        yxmd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    try {
                        wugong.setEnabled(true);
                        wugong.setBackgroundResource(R.drawable.the_shape2);
                        wugong.setTextColor(0xffffffff);
                        wugongclick = false;
                        bupingheng.setEnabled(true);
                        bupingheng.setBackgroundResource(R.drawable.the_shape2);
                        bupingheng.setTextColor(0xffffffff);
                        bupinghengclick = false;
                        xiebo.setEnabled(true);
                        xiebo.setBackgroundResource(R.drawable.the_shape2);
                        xiebo.setTextColor(0xffffffff);
                        xieboclick = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        wugong.setBackgroundResource(R.drawable.the_shape2);
                        wugong.setTextColor(0xffffffff);
                        wugong.setBackgroundColor(0xff696969);
                        wugong.setText("关");
                        wugong.setEnabled(false);
                        bupingheng.setBackgroundResource(R.drawable.the_shape2);
                        bupingheng.setTextColor(0xffffffff);
                        bupingheng.setBackgroundColor(0xff696969);
                        bupingheng.setText("关");
                        bupingheng.setEnabled(false);
                        xiebo.setBackgroundResource(R.drawable.the_shape2);
                        xiebo.setTextColor(0xffffffff);
                        xiebo.setBackgroundColor(0xff696969);
                        xiebo.setText("关");
                        xiebo.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cqtm = (EditText) findViewById(R.id.cfQr);
        zzva = (EditText) findViewById(R.id.cfKr);
        bqva = (EditText) findViewById(R.id.jyKp);
        bqra = (EditText) findViewById(R.id.jyKi);
        xxen = (EditText) findViewById(R.id.dyKp);
        yxnm = (EditText) findViewById(R.id.yxNM);
        bphzb = (EditText) findViewById(R.id.bphzb);
        jbzb = (EditText) findViewById(R.id.jbzb);
        xbzb = (EditText) findViewById(R.id.xbzb);
        ysre = (EditText) findViewById(R.id.ysRE);
        wugong = (Button) findViewById(R.id.button_wugong);
        wugong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wugongclick) {
                    wugong.setText("开");
                    wugong.setTextColor(0xff000000);
                    wugong.setBackgroundColor(0xffffffff);
                    wugongclick = true;
                } else if (wugongclick) {
                    wugong.setText("关");
                    wugong.setTextColor(0xffffffff);
                    wugong.setBackgroundResource(R.drawable.the_shape2);
                    wugongclick = false;
                }
            }
        });
        bupingheng = (Button) findViewById(R.id.button_bupingheng);
        bupingheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bupinghengclick) {
                    bupingheng.setText("开");
                    bupinghengclick = true;
                    bupingheng.setTextColor(0xff000000);
                    bupingheng.setBackgroundColor(0xffffffff);
                } else if (bupinghengclick) {
                    bupingheng.setText("关");
                    bupinghengclick = false;
                    bupingheng.setTextColor(0xffffffff);
                    bupingheng.setBackgroundResource(R.drawable.the_shape2);
                }
            }
        });
        xiebo = (Button) findViewById(R.id.button_xiebo);
        xiebo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!xieboclick) {
                    xiebo.setText("开");
                    xieboclick = true;
                    xiebo.setTextColor(0xff000000);
                    xiebo.setBackgroundColor(0xffffffff);
                } else if (xieboclick) {
                    xiebo.setText("关");
                    xieboclick = false;
                    xiebo.setTextColor(0xffffffff);
                    xiebo.setBackgroundResource(R.drawable.the_shape2);
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
        runstate.setText(therunstate(run_state));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                String res = "03 00 0A 00 0D";
                res = thehostnumber(res);
                try {
                    res = crc16(res);
                    boolean isSend = iBackService.sendMessage(res);//Send Content by socket
                    Toast.makeText(thrid.this, isSend ? "参数回传指令已发送" : "参数回传指令未发送", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unbindService(serviceConnection);
        //mLocalBroadcastManager.unregisterReceiver(mReciver);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            if (x1 - x2 > 50) {
           /*     if (permission) {
                    unbindService(serviceConnection);
                    mLocalBroadcastManager.unregisterReceiver(mReciver);
                    Intent intent = new Intent();
                    intent.setClass(thrid.this, fourth.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.fromright, R.animator.toleft);
                    //finish();
                }*/
            } else if (x2 - x1 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(thrid.this, frist.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromleft, R.animator.toright);
                //finish();
            }
        }
        return super.onTouchEvent(event);
    }

    public void canshuxiugai(View view) {
        try {
            String xiugai = "10000A000D1A";
            xiugai = thehostnumber(xiugai);
            StringBuilder ss = new StringBuilder();
            ss.append(xiugai);
            // azct.setSelection(1);//0负载侧 1系统侧
            // yxmd.setSelection(0);//0调试模式 1补偿模式 2 定标模式
            if (azct.getSelectedItemPosition() == 0) {
                ss.append("0000");
            } else if (azct.getSelectedItemPosition() == 1) {
                ss.append("0001");
            }

            if (yxmd.getSelectedItemPosition() == 0) {
                ss.append("2000");
            } else if (yxmd.getSelectedItemPosition() == 2) {
                ss.append("8000");
            } else if (yxmd.getSelectedItemPosition() == 1) {
                int i1 = 4096;
                if (wugongclick) {
                    i1 += 256;
                }
                if (bupinghengclick) {
                    i1 += 16;
                }
                if (xieboclick) {
                    i1 += 1;
                }
                ss.append(theString(i1));
            }
            if (cqtm.length() == 0) {
                ss.append("0000");
            } else {
                ss.append(theString(Integer.parseInt(cqtm.getText().toString())));
            }
            if (bbct.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(bbct.getText().toString())));
            if (yxnm.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(yxnm.getText().toString())));
            if (bqva.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(bqva.getText().toString())));
            if (bqra.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(bqra.getText().toString())));
            if (zzva.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(zzva.getText().toString())));
            if (jbzb.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(jbzb.getText().toString())));
            if (bphzb.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(bphzb.getText().toString())));
            if (xbzb.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(xbzb.getText().toString())));
            if (xxen.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(xxen.getText().toString())));
            if (ysre.length() == 0) {
                ss.append("0000");
            } else ss.append(theString(Integer.parseInt(ysre.getText().toString())));

            String result = crc16(ss.toString());
//            Log.w("数据", String.valueOf(result));
            boolean isSend = iBackService.sendMessage(result);
            Toast.makeText(this, isSend ? "参数修改指令已发送" : "参数修改指令未发送", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void canshuhuichuan(View view) {
        clear();
        String res = "03 00 0A 00 0D";
        res = thehostnumber(res);
        try {
            res = crc16(res);
            boolean isSend = iBackService.sendMessage(res);//Send Content by socket
            Toast.makeText(this, isSend ? "参数回传指令已发送" : "参数回传指令未发送", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void kaiji(View view) {
        String res = "10 00 03 00 01 02 00 01";
        res = thehostnumber(res);
        try {
            res = crc16.crc16(res);
            boolean isSend = iBackService.sendMessage(res);//Send Content by socket
            Toast.makeText(this, isSend ? "开机指令已发送" : "开机指令未发送", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void guanji(View view) {
        String res = "10 00 03 00 01 02 00 00";
        res = thehostnumber(res);
        try {
            res = crc16.crc16(res);
            boolean isSend = iBackService.sendMessage(res);//Send Content by socket
            Toast.makeText(this, isSend ? "关机指令已发送" : "关机指令未发送", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void chongdian(View view) {
        try {
            String res = "10 00 02 00 01 02 00 01";
            res = thehostnumber(res);
            res = crc16.crc16(res);
            boolean isSend = iBackService.sendMessage(res);//Send Content by socket
            Toast.makeText(getApplicationContext(), isSend ? "充电指令已发送" : "充电指令未发送", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quxiaochongdian(View view) {
        try {
            String res1 = "10 00 02 00 01 02 00 00";
            res1 = thehostnumber(res1);
            res1 = crc16.crc16(res1);
            boolean isSend = iBackService.sendMessage(res1);//Send Content by socket
            Toast.makeText(getApplicationContext(), isSend ? "取消充电指令已发送" : "取消充电指令未发送", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guzhangfuweianniu(View view) {
        String res = "10 00 00 00 01 02 00 01";
        res = thehostnumber(res);
        try {
            res = crc16.crc16(res);
            boolean isSend = iBackService.sendMessage(res);//Send Content by socket
            Toast.makeText(getApplicationContext(), isSend ? "复位指令已发送" : "复位指令未发送", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        bbct.setText("");
        cqtm.setText("");
        zzva.setText("");
        bqva.setText("");
        bqra.setText("");
        xxen.setText("");
        yxnm.setText("");
        ysre.setText("");
        bphzb.setText("");
        jbzb.setText("");
        xbzb.setText("");
    }
}
