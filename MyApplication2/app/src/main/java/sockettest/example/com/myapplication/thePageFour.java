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
import static sockettest.example.com.myapplication.Data.thenumber;

public class thePageFour extends AutoLayoutActivity {
    private GridView mGridView;
    private GridViewAdapter mFormGridViewAdapter;

    private EditText in_parallel_num,run_module_num,master_moudel,rtu_addr,ip_addr,gw_addr,msk_addr,mac_addr;
    private String[] netInfo=new String[4],zzInfo=new String[4];
    private String zzposition;
    private Spinner mSpinner;

    private Button echo,modify;
    private write_LowerComputer mWrite_lowerComputer = new write_LowerComputer();
    private read_LowerComputer mRead_lowerComputer = new read_LowerComputer();

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
                //接收到数据之后判断CRC 长度
                message = message.replace(" ", "");
                message = message.toUpperCase();
                try {
                    if (message.substring(4, 6).equals("52")) {
                        //表格
                        form42_62 = mRead_lowerComputer.retrunContentGridViewDataObject(message);
                        List<GridViewDataObject> mapList = new ArrayList<GridViewDataObject>();
                        for (int i = 0; i < form42_62.length; i++) {
                            GridViewDataObject map = form42_62[i];
                            mapList.add(map);
                        }
                        mFormGridViewAdapter.setGridViewDataObject(mapList);
                        mGridView.setAdapter(mFormGridViewAdapter);
                        mFormGridViewAdapter.notifyDataSetChanged();
                        //下拉框
                        Log.w("zzposion",mRead_lowerComputer.getZzposition());
                        switch (mRead_lowerComputer.getZzposition()) {
                            case "0000":
                                mSpinner.setSelection(0);
                                break;
                            case "0001":
                                mSpinner.setSelection(1);
                                break;
                            case "0010":
                                mSpinner.setSelection(2);
                                break;
                        }
                        //其他
                        zzInfo = mRead_lowerComputer.getZzInfo();
                        netInfo = mRead_lowerComputer.getNetInfo();
                        in_parallel_num.setText(zzInfo[0]);
                        run_module_num.setText(zzInfo[1]);
                        master_moudel.setText(zzInfo[2]);
                        rtu_addr.setText(zzInfo[3]);
                        ip_addr.setText(thenumber(netInfo[0])+"."+thenumber(netInfo[1])+"."+thenumber(netInfo[2])+"."+thenumber(netInfo[3]));
                        gw_addr.setText(thenumber(netInfo[4])+"."+thenumber(netInfo[5])+"."+thenumber(netInfo[6])+"."+thenumber(netInfo[7]));
                        msk_addr.setText(thenumber(netInfo[8])+"."+thenumber(netInfo[9])+"."+thenumber(netInfo[10])+"."+thenumber(netInfo[11]));
                        mac_addr.setText(netInfo[12]+netInfo[13]+netInfo[14]);
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
        setContentView(R.layout.activity_the_page_four);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        mResultText = (TextView) findViewById(R.id.thecenter);//***********
        mReciver = new MessageBackReciver(mResultText);
        mServiceIntent = new Intent(this, BackService.class);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
        init();
    }

    private GridViewDataObject[] form42_62 = {
            new GridViewDataObject("目标\n因数", "0"),
            new GridViewDataObject("TSC下\n额定容量", "0"),
            new GridViewDataObject("TSC下\n支路数量", "0"),
            new GridViewDataObject("TSC下\n过压门限", "0"),
            new GridViewDataObject("TSC下\n过压延时", "0"),
            new GridViewDataObject("TSC下\n欠压门限", "0"),
            new GridViewDataObject("TSC下\n欠压延时", "0"),
            new GridViewDataObject("TSC下\n投切间隔", "0"),
            new GridViewDataObject("直流\n过压", "0"),
            new GridViewDataObject("过压\n延时", "0"),
            new GridViewDataObject("直流\n欠压", "0"),
            new GridViewDataObject("欠压\n延时", "0"),
            new GridViewDataObject("交流\n过压", "0"),
            new GridViewDataObject("交流\n欠压", "0"),
            new GridViewDataObject("输出\n过流", "0"),
            new GridViewDataObject("模块\n过温°", "0"),
            new GridViewDataObject("额定\n容量", "0"),
            new GridViewDataObject("平衡\n容量%", "0"),
            new GridViewDataObject("无功\n容量%", "0"),
            new GridViewDataObject("谐波\n容量%", "0"),
            new GridViewDataObject("装置\nCT比", "0"),
    };
  /*  private EditText in_parallel_num,run_module_num,master_moudel,rtu_addr,ip_addr,gw_addr,msk_addr,mac_addr;
    private String[] netInfo=new String[4],zzInfo=new String[4];*/
    public void init() {
        // azct.getSelectedItemPosition() == 0) {
        mSpinner = (Spinner)findViewById(R.id.Spinner);
        //绑定控件
        in_parallel_num = (EditText) findViewById(R.id.in_parallel_num);
        run_module_num = (EditText) findViewById(R.id.run_module_num);
        master_moudel = (EditText) findViewById(R.id.master_moudel);
        rtu_addr = (EditText) findViewById(R.id.rtu_addr);
        ip_addr = (EditText) findViewById(R.id.ip_addr);
        gw_addr = (EditText) findViewById(R.id.gw_addr);
        msk_addr = (EditText) findViewById(R.id.msk_addr);
        mac_addr = (EditText) findViewById(R.id.mac_addr);


        List<GridViewDataObject> mapList = new ArrayList<GridViewDataObject>();
        for (int i = 0; i < form42_62.length; i++) {
            GridViewDataObject map = form42_62[i];
            mapList.add(map);
        }
        mFormGridViewAdapter = new GridViewAdapter(thePageFour.this, mapList,"123");
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(mFormGridViewAdapter);

        //参数回传
        echo = (Button)findViewById(R.id.echo);
        echo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    echo.setEnabled(false);
                    Thread.sleep(200);
                    boolean isSend = iBackService.sendMessage(mRead_lowerComputer.theAskCode(42));
                    Thread.sleep(800);
                    echo.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //参数修改
        modify  = (Button)findViewById(R.id.modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modify.setEnabled(false);
                    Thread.sleep(200);
                    boolean isSend = iBackService.sendMessage(mWrite_lowerComputer.theWriteCode("002A", "0029", "52", ContentOfModify(form42_62,zzposition,zzInfo,netInfo)));
                    Thread.sleep(800);
                    modify.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                    boolean isSend = iBackService.sendMessage(mRead_lowerComputer.theAskCode(42));
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

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            if(x2 - x1 > 50) {
                unbindService(serviceConnection);
                mLocalBroadcastManager.unregisterReceiver(mReciver);
                Intent intent = new Intent();
                intent.setClass(this,thePageThree.class);
                startActivity(intent);
                overridePendingTransition(R.animator.fromleft,R.animator.toright);//跳转动画
                //finish();
            }
        }
        return super.onTouchEvent(event);
    }
    //将本界面需记录的内容转换成十六进制字符串功能函数
    public String ContentOfModify(GridViewDataObject[] form42, String zzposition,String[] zzInfo, String[] netInfo) {
        String s = "";
        try {
            StringBuilder stringBuilder = new StringBuilder();
            //目标容量0
            for (int i = 0;i<form42.length;i++){
                stringBuilder.append(theString(Integer.parseInt(form42[i].getValue())));
            }
            switch (mSpinner.getSelectedItemPosition()) {
                case 0:
                    stringBuilder.append("0000");
                    break;
                case 1:
                    stringBuilder.append("0001");
                    break;
                case 2:
                    stringBuilder.append("0010");
                    break;
            }
            for (int i = 0; i < zzInfo.length; i++) {
                stringBuilder.append(zzInfo[i]);
            }
            for (int i = 0; i < netInfo.length; i++) {
                    stringBuilder.append(netInfo[i]);
            }
            s=stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
