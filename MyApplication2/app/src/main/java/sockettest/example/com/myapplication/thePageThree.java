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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import function_class.GridViewAdapter;
import function_class.GridViewDataObject;
import function_class.read_LowerComputer;
import function_class.write_LowerComputer;

import static sockettest.example.com.myapplication.Data.theString;

public class thePageThree extends AutoLayoutActivity {
    private GridView mGridView, mGridView2;
    private GridViewAdapter mFormGridViewAdapter, mFormGridViewAdapter2;

    private Button echo, modify;
    private write_LowerComputer mWrite_lowerComputer = new write_LowerComputer();
    private read_LowerComputer mRead_lowerComputer = new read_LowerComputer();

    //不在UI上显示的值 偏移地址8（content7_13[1]）的 top_mode，偏移地址13（content7_13[6]）的 udc_cap_series_2 需要特别注意
    private String[] content7_13 = new String[7], content25_29 = new String[5];
    private String  com_hn14String;

    private Spinner comp_mode, ct_location;
    private Button use_pwm8_module1, use_pwm8_module2, com_hn0, com_hn1, com_hn2, com_hn14;
    private boolean use_pwm8_module1Tag=false, use_pwm8_module2Tag=false, com_hn0Tag=false, com_hn1Tag=false, com_hn2Tag=false, com_hn14Tag=false;
    private EditText restart_minute, restart_enable, ct_ratio,udc_cap_series_2;

    private Button xitongfuwei,canshufuwei,guzhangfuwei,guhuacanshu,buchanglvfuwei;

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
                try {
                    if (message.substring(4, 6).equals("46")) {
                        Log.w("jinru","");
                        GridViewDataObject[] ss = mRead_lowerComputer.retrunContentGridViewDataObject(message);

                        for (int i = 0; i < ss.length; i++) {
                            if (i < 7) {
                                content7_13[i] = ss[i].getValue();
                            } else if (i > 6 && i < 18) {
                                form14_24[i-7].setValue(ss[i].getValue());
                            } else if (i > 17 && i < 23) {
                                content25_29[i - 18] = ss[i].getValue();
                            } else if (i > 22&&i<34) {
                                form30_40[i-23].setValue(ss[i].getValue());
                            }else if (i==34){
                                com_hn14String = ss[i].getValue();
                            }
                        }
                        List<GridViewDataObject> mapList = new ArrayList<GridViewDataObject>();
                        for (int i = 0; i < form14_24.length; i++) {
                            GridViewDataObject map = form14_24[i];
                            mapList.add(map);
                        }
                        List<GridViewDataObject> mapList2 = new ArrayList<GridViewDataObject>();
                        for (int i = 0; i < form30_40.length; i++) {
                            GridViewDataObject map = form30_40[i];
                            mapList2.add(map);
                        }
                        mFormGridViewAdapter.setGridViewDataObject(mapList);
                        mGridView.setAdapter(mFormGridViewAdapter);
                        mFormGridViewAdapter.notifyDataSetChanged();
                        mFormGridViewAdapter2.setGridViewDataObject(mapList2);
                        mGridView2.setAdapter(mFormGridViewAdapter2);
                        mFormGridViewAdapter2.notifyDataSetChanged();
                        for (String sss:content7_13) Log.w("12345",sss);
                        for (String sss:content25_29) Log.w("12345",sss);
                        if (content7_13[0].equals("1")){
                            comp_mode.setSelection(0);
                        }else comp_mode.setSelection(1);
                        restart_minute.setText(content7_13[2]);
                        restart_enable.setText(content7_13[3]);
                        if (content7_13[4].equals("1")) {
                            use_pwm8_module1Tag = false;
                            use_pwm8_module1.performClick();
                        }else {
                            use_pwm8_module1Tag = true;
                            use_pwm8_module1.performClick();
                        }
                        if (content7_13[5].equals("1")) {
                            use_pwm8_module2Tag = false;
                            use_pwm8_module2.performClick();
                        }else {
                            use_pwm8_module2Tag = true;
                            use_pwm8_module2.performClick();
                        }
                        udc_cap_series_2.setText(content7_13[6]);
                        if (content25_29[0].equals("0")){
                            ct_location.setSelection(0);
                        }else ct_location.setSelection(1);
                        ct_ratio.setText(content25_29[1]);
                        if (content25_29[2].equals("1024")) {
                            com_hn0Tag = false;
                            com_hn0 .performClick();
                        }else {
                            com_hn0Tag = true;
                            com_hn0 .performClick();
                        }
                        if (content25_29[3].equals("1024")) {
                            com_hn1Tag = false;
                            com_hn1 .performClick();
                        }else {
                            com_hn1Tag = true;
                            com_hn1 .performClick();
                        }
                        if (content25_29[4].equals("1024")) {
                            com_hn2Tag = false;
                            com_hn2 .performClick();
                        }else {
                            com_hn2Tag = true;
                            com_hn2 .performClick();
                        }
                        if (com_hn14String.equals("1024")) {
                            com_hn14Tag = false;
                            com_hn14 .performClick();
                        }else {
                            com_hn14Tag = true;
                            com_hn14 .performClick();
                        }
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
        setContentView(R.layout.activity_the_page_three);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mResultText = (TextView) findViewById(R.id.thecenter);//***********
        mReciver = new MessageBackReciver(mResultText);
        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
        init();
    }

    private GridViewDataObject [] form14_24 = {
            new GridViewDataObject("参考电压","0"), new GridViewDataObject("直流给定","0"),
            new GridViewDataObject("直流比例","0"), new GridViewDataObject("直流积分","0"),
            new GridViewDataObject("重复比例","0"), new GridViewDataObject("重复积分","0"),
            new GridViewDataObject("均压比利","0"), new GridViewDataObject("均压积分","0"),
            new GridViewDataObject("超前周期","0"), new GridViewDataObject("电流倍数","0"),
            new GridViewDataObject("电流比例","0"),
    };

    private GridViewDataObject [] form30_40 = {
            new GridViewDataObject("3次补偿","0"), new GridViewDataObject("4次补偿","0"), new GridViewDataObject("5次补偿","0"),
            new GridViewDataObject("6次补偿", "0"), new GridViewDataObject("7次补偿","0"),new GridViewDataObject("8次补偿","0"),
            new GridViewDataObject("9次补偿","0"),new GridViewDataObject("10次补偿","0"),new GridViewDataObject("11次补偿","0"),
            new GridViewDataObject( "12次补偿","0"), new GridViewDataObject("13次补偿","0"),
    };

    public void init() {
        //绑定控件
        comp_mode = (Spinner) findViewById(R.id.comp_mode);
        ct_location = (Spinner) findViewById(R.id.ct_location);
        restart_minute = (EditText) findViewById(R.id.restart_minute);
        restart_enable = (EditText) findViewById(R.id.restart_enable);
        ct_ratio = (EditText) findViewById(R.id.ct_ratio);
        udc_cap_series_2 = (EditText) findViewById(R.id.udc_cap_series_2);
        use_pwm8_module1 = (Button)findViewById(R.id.use_pwm8_module1);
        use_pwm8_module1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (use_pwm8_module1Tag){
                    use_pwm8_module1Tag=false;
                    use_pwm8_module1.setTextColor(0xff000000);
                    use_pwm8_module1.setBackgroundColor(0xffffffff);
                }else{
                    use_pwm8_module1Tag=true;
                    use_pwm8_module1.setTextColor(0xffffffff);
                    use_pwm8_module1.setBackgroundColor(0xff000000);
                }
            }
        });
        use_pwm8_module2 = (Button) findViewById(R.id.use_pwm8_module2);
        use_pwm8_module2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (use_pwm8_module2Tag){
                    use_pwm8_module2Tag=false;
                    use_pwm8_module2.setTextColor(0xff000000);
                    use_pwm8_module2.setBackgroundColor(0xffffffff);
                }else{
                    use_pwm8_module2Tag=true;
                    use_pwm8_module2.setTextColor(0xffffffff);
                    use_pwm8_module2.setBackgroundColor(0xff000000);
                }
            }
        });
        com_hn0 = (Button) findViewById(R.id.com_hn0);
        com_hn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (com_hn0Tag){
                    com_hn0Tag=false;
                    com_hn0.setTextColor(0xff000000);
                    com_hn0.setBackgroundColor(0xffffff);
                }else{
                    com_hn0Tag=true;
                    com_hn0.setTextColor(0xffffffff);
                    com_hn0.setBackgroundColor(0xff000000);
                }

            }
        });
        com_hn1 = (Button) findViewById(R.id.com_hn1);
        com_hn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (com_hn1Tag){
                    com_hn1Tag=false;
                    com_hn1.setTextColor(0xff000000);
                    com_hn1.setBackgroundColor(0xffffffff);
                }else{
                    com_hn1Tag=true;
                    com_hn1.setTextColor(0xffffffff);
                    com_hn1.setBackgroundColor(0xff000000);
                }
            }
        });
        com_hn2 = (Button) findViewById(R.id.com_hn2);
        com_hn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (com_hn2Tag){
                    com_hn2Tag=false;
                    com_hn2.setTextColor(0xff000000);
                    com_hn2.setBackgroundColor(0xffffffff);
                }else{
                    com_hn2Tag=true;
                    com_hn2.setTextColor(0xffffffff);
                    com_hn2.setBackgroundColor(0xff000000);
                }
            }
        });
        com_hn14 = (Button) findViewById(R.id.com_hn14);
        com_hn14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (com_hn14Tag){
                    com_hn14Tag=false;
                    com_hn14.setTextColor(0xff000000);
                    com_hn14.setBackgroundColor(0xffffffff);
                }else{
                    com_hn14Tag=true;
                    com_hn14.setTextColor(0xffffffff);
                    com_hn14.setBackgroundColor(0xff000000);
                }
            }
        });

        //参数回传
        echo = (Button) findViewById(R.id.echo);
        echo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    echo.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend = iBackService.sendMessage(mRead_lowerComputer.theAskCode(7));
                    Thread.sleep(800);
                    echo.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //参数修改
        modify = (Button) findViewById(R.id.modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modify.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0007", "0023", "46", ContentOfModify(form14_24,form30_40,content7_13,content25_29,com_hn14String)));
                    Thread.sleep(800);
                 /*   boolean isSend2 = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0001", "0001", "02", "3366"));
                     Thread.sleep(200);*/
                    modify.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //其他修改参数
        xitongfuwei = (Button) findViewById(R.id.xitongfuwei);
        xitongfuwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    xitongfuwei.setEnabled(false);
                    Thread.sleep(200);
                    boolean isSend2 = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0001", "0001", "02", "0011"));
                    Thread.sleep(200);
                    xitongfuwei.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        canshufuwei = (Button) findViewById(R.id.canshufuwei);
        canshufuwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    canshufuwei.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend2 = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0001", "0001", "02", "0012"));
                     Thread.sleep(200);
                    canshufuwei.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        guzhangfuwei = (Button) findViewById(R.id.guzhangfuwei);
        guzhangfuwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guzhangfuwei.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend2 = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0001", "0001", "02", "0022"));
                     Thread.sleep(200);
                    guzhangfuwei.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        guhuacanshu = (Button) findViewById(R.id.guhuacanshu);
        guhuacanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    guhuacanshu.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend2 = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0001", "0001", "02", "0033"));
                     Thread.sleep(200);
                    guhuacanshu.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buchanglvfuwei = (Button) findViewById(R.id.buchanglvfuwei);
        buchanglvfuwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    buchanglvfuwei.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend2 = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("0001", "0001", "02", "3366"));
                     Thread.sleep(200);
                    buchanglvfuwei.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        List<GridViewDataObject> mapList = new ArrayList<GridViewDataObject>();
        for (int i = 0; i < form14_24.length; i++) {
            GridViewDataObject map = form14_24[i];
            mapList.add(map);
        }
        mFormGridViewAdapter = new GridViewAdapter(this, mapList, "1234");
        mGridView = (GridView) findViewById(R.id.the_gridView);
        mGridView.setAdapter(mFormGridViewAdapter);

        List<GridViewDataObject> mapList2 = new ArrayList<GridViewDataObject>();
        for (int i = 0; i < form30_40.length; i++) {
            GridViewDataObject map = form30_40[i];
            mapList2.add(map);
        }
        mFormGridViewAdapter2 = new GridViewAdapter(this, mapList2, "123");
        mGridView2 = (GridView) findViewById(R.id.the_gridView2);
        mGridView2.setAdapter(mFormGridViewAdapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
        bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    modify.setEnabled(false);
                     Thread.sleep(200);
                    boolean isSend = iBackService.sendMessage(mRead_lowerComputer.theAskCode(7));
                    Thread.sleep(800);
                    modify.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
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
                intent.setClass(this, thePageTwo.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromleft, R.animator.toright);
                //finish();
            } else if (x1 - x2 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(this, thePageFour.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromright, R.animator.toleft);
                //finish();
            }
        }
        return super.onTouchEvent(event);

    }
    public String ContentOfModify(GridViewDataObject[] form14to24, GridViewDataObject[] form30to40,String[] content7o13, String[] content25to29,String com) {
        String s = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            content7o13[6] = theString(Integer.parseInt(udc_cap_series_2.getText().toString(),16));
            content7_13[2]=   restart_minute.getText().toString();
            content7_13[3]=   restart_enable.getText().toString();
            for (int i = 0; i < content7o13.length; i++) {
                switch (i) {
                    case 0:
                        if (comp_mode.getSelectedItemPosition() == 0) {
                            stringBuilder.append("0001");
                        } else stringBuilder.append("0002");
                        break;
                    case 4:
                        if (use_pwm8_module1Tag) {
                            stringBuilder.append("0001");
                        } else stringBuilder.append("0000");
                        break;
                    case 5:
                        if (use_pwm8_module2Tag) {
                            stringBuilder.append("0001");
                        } else stringBuilder.append("0000");
                        break;
                    default:
                        stringBuilder.append(theString(Integer.parseInt(content7o13[i])));
                        break;
                }
            }
            for (int i= 0;i<form14to24.length;i++){
                stringBuilder.append(theString(Integer.parseInt(form14to24[i].getValue())));
            }
            content25to29[1]=ct_ratio.getText().toString();
            for (int i= 0;i<content25to29.length;i++){
                switch (i) {
                    case 0:
                        if (ct_location.getSelectedItemPosition() == 0) {
                            stringBuilder.append("0000");
                        } else stringBuilder.append("0001");
                        break;
                    case 2:
                        if (com_hn0Tag) {
                            stringBuilder.append("0400");
                        } else stringBuilder.append("0000");
                        break;
                    case 3:
                        if (com_hn1Tag) {
                            stringBuilder.append("0400");
                        } else stringBuilder.append("0000");
                        break;
                    case 4:
                        if (com_hn2Tag) {
                            stringBuilder.append("0400");
                        } else stringBuilder.append("0000");
                        break;
                    default:
                       break;
                }
            }
            for (int i= 0;i<form30to40.length;i++){
                stringBuilder.append(theString(Integer.parseInt(form30to40[i].getValue())));
            }
            if (com_hn14Tag){
                stringBuilder.append("0400");
            } else stringBuilder.append("0000");
            s=stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }


}
