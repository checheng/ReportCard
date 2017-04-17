package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends BaseActivity {
    private TextView rifadianliang,zongfadianliang,dangqiangonglv,co2jianpai,zongfuzhaodu,huanjingwendu;

    public void init(){
        rifadianliang = (TextView)findViewById(R.id.date_rifadianliang);
        zongfadianliang = (TextView)findViewById(R.id.date_zongfadianliang);
        dangqiangonglv = (TextView)findViewById(R.id.date_dangqiangonglv);
        co2jianpai = (TextView)findViewById(R.id.date_co2jianpai);
        zongfuzhaodu = (TextView)findViewById(R.id.data_zongfuzhaodu);
        huanjingwendu = (TextView)findViewById(R.id.date_huanjingwendu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopService(new Intent(this,MyService.class));
    }

    public void onEventMaiThread(MessageEvent MessageEvent) {
        if (MessageEvent.getThe_type() == 1){

        }
        if (MessageEvent.getThe_type() == 2) {
            Log.w("收到内容", MessageEvent.getThe_content().toString());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  date_receive(DataPass DataPass){
        String[] the_date;
        the_date = DataPass.getThe_date();
//        Log.w("数组",the_date[0]+","+the_date[1]+","+the_date[2]+","+the_date[3]+","+the_date[4]+","+the_date[5]);
        rifadianliang.setText(the_date[0]+" kWh");
        zongfadianliang.setText(the_date[1]+" kWh");
        dangqiangonglv.setText(the_date[2]+" kW");
        co2jianpai.setText(the_date[3]+" T");
        zongfuzhaodu.setText(the_date[4]+" W/㎡");
        huanjingwendu.setText(the_date[5]+" ℃");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public  void the_receive(String event){
        Log.w("activity",event);
    }
}
