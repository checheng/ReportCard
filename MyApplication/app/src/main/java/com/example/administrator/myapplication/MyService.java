package com.example.administrator.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import function_class.Byte2String;
import function_class.Crc16;
import udp.NetworkState;
import udp.SocketConnectListener;
import udp.UdpSocketClientManage;

import static udp.NetworkState.NETWORK_STATE_CONNECT_SUCCEED;


public class MyService extends Service {

    UdpSocketClientManage mUdpSocketClientManage = null;
    private String mstrDataString="";

    public MyService() {

    }


/*    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 接受到消息之后，对UI控件进行修改
                    Toast.makeText(MyService.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1: // 接受到消息之后，对UI控件进行修改
//                    修改控件
                    Toast.makeText(MyService.this, mstrDataString, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };*/

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        String strLoString = mUdpSocketClientManage.getLocalIpAddress();
        if (strLoString != null) {
            //手机端的连接路由之后获得本地IP地址
        }
        mUdpSocketClientManage = new UdpSocketClientManage();
        mUdpSocketClientManage.RegSocketConnectListener(new SocketConnectListener() {
            @Override
            public void OnConnectStatusCallBack(NetworkState networkState) {
                if (networkState == NETWORK_STATE_CONNECT_SUCCEED) {
                    EventBus.getDefault().post(new MessageEvent(1, "连接成功！"));
                }

          /*      switch (networkState) {
                    case NETWORK_STATE_CONNECT_SUCCEED:
                        mHandler.sendEmptyMessage(0);
                        break;
                    default:
                        break;
                }*/
            }
            @Override
            public void OnReceiverCallBack(int nLength, byte[] data) {
                /**收到内容**/
                mstrDataString = Byte2String.bytesToHexStringwithlength(data,nLength);
                if (!mstrDataString.equals("")) {
                    boolean f = Crc16.verify(mstrDataString);
                    /**校验crc16及长度帧头**/
                    if (f && mstrDataString.length() == 36 && mstrDataString.substring(0, 4).equals("0102")) {
                        Log.w("提示", "校验成功");
                        //对数据进行处理
                        try {
                            String[] sss = new String[6];
                            StringBuffer ss = new StringBuffer();
                            double aa;
                            //日发电量
                            ss.replace(0,ss.length(),String.valueOf(Integer.parseInt(mstrDataString.substring(4, 8),16)));
                            sss[0] = ss.toString();
                            //总发电量
                            ss= ss.replace(0,ss.length(),String.valueOf(Integer.parseInt(mstrDataString.substring(8, 16), 16)));
                            sss[1] = ss.toString();
                            //当前功率
                            ss = ss.replace(0,ss.length(),String.valueOf(Integer.parseInt(mstrDataString.substring(16, 20), 16))).insert(ss.length() - 1, ".");
                            sss[2] = ss.toString();
                            //co2减排
                            ss = ss.replace(0,ss.length(),String.valueOf(Integer.parseInt(mstrDataString.substring(20, 24), 16))).insert(ss.length() - 1, ".");
                            sss[3] = ss.toString();
                            //总辐照度
                            ss= ss.replace(0,ss.length(),String.valueOf(Integer.parseInt(mstrDataString.substring(24, 28), 16)));
                            sss[4] = ss.toString();
                            //环境温度
                            ss = ss.replace(0,ss.length(),String.valueOf(Integer.parseInt(mstrDataString.substring(28, 32), 16))).insert(ss.length() - 1, ".");
                            sss[5] = ss.toString();
                            EventBus.getDefault().post(new DataPass(sss));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(new MessageEvent(2, mstrDataString));
                        Log.w("收到内容", mstrDataString);
                    }

                }
//                mHandler.sendEmptyMessage(1);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    mUdpSocketClientManage.Connect();
                    mUdpSocketClientManage.setNetworkParameter("59.172.153.70",7070);//59.172.153.70  //192.168.2.253
                    byte [] send_content = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
                    mUdpSocketClientManage.send(send_content,send_content.length);
                    Log.w("发送请求数据","已发送");
                    Thread.sleep(3000);
                    while (true){
                        mUdpSocketClientManage.send(send_content,send_content.length);
                        Log.w("发送请求数据","已发送");
                        Thread.sleep(30000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
     }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void receivce(MessageEvent event) {
//        Log.w("service",event.getThe_content());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
