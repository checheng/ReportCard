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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;

import function_class.crc16;

import static function_class.crc16.crc16;
import static sockettest.example.com.myapplication.Data.run_state;
import static sockettest.example.com.myapplication.Data.spp;
import static sockettest.example.com.myapplication.Data.theString;
import static sockettest.example.com.myapplication.Data.thehostnumber;
import static sockettest.example.com.myapplication.Data.thenumber;
import static sockettest.example.com.myapplication.Data.therunstate;

public class fourth extends AutoLayoutActivity {
	private static final String TAG = "MainActivity";
	private Data application;
	private fourth oContext;

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
	private EditText zlud,cfqr,cfkr,jykp,jyki,dykp,dyki,dlkp,cftd,dyrf,gydc,gyac,qyac,gwtp;
	private TextView runstate;

	private  boolean resultCrc16;

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
				//Log.w("字符串",message);
				resultCrc16 = crc16.verify(message);
				int obtianlength = Integer.parseInt(message.substring(4, 6), 16);
				if (obtianlength == 32) {
					Log.w("CRC", String.valueOf(resultCrc16));
					if (resultCrc16) {
//              校验主机位与功能码
						int length = ((message.length() - 10) / 2);/**主机2 功能2 长度2 CRC4**/
						if (message.substring(0, 2).equals(spp.getString("hostnumber","")) && message.substring(2, 4).equals("03") && length == obtianlength) {
							Log.w("校验主机", "校验主机和功能码");
							String s = message;
							try {
								zlud.setText(String.valueOf(thenumber(s.substring(14, 18))));
								dykp.setText(String.valueOf(thenumber(s.substring(18, 22))));
								dyki.setText(String.valueOf(thenumber(s.substring(22, 26))));
								dlkp.setText(String.valueOf(thenumber(s.substring(26, 30))));
								cfqr.setText(String.valueOf(thenumber(s.substring(30, 34))));
								cfkr.setText(String.valueOf(thenumber(s.substring(34, 38))));
								cftd.setText(String.valueOf(thenumber(s.substring(38, 42))));
								jykp.setText(String.valueOf(thenumber(s.substring(42, 46))));
								jyki.setText(String.valueOf(thenumber(s.substring(46, 50))));
								dyrf.setText(String.valueOf(thenumber(s.substring(50, 54))));
								gydc.setText(String.valueOf(thenumber(s.substring(54, 58))));
								gyac.setText(String.valueOf(thenumber(s.substring(58, 62))));
								qyac.setText(String.valueOf(thenumber(s.substring(62, 66))));
								gwtp.setText(String.valueOf(thenumber(s.substring(66, 70))));
							} catch (Exception e) {e.printStackTrace();}
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
	private Button canshuhuichuan,canshuxiugai;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fourthpage);
		if (application == null) {
			// 得到Application对象
			application = (Data) getApplication();
		}
		oContext = this;// 把当前的上下文对象赋值给BaseActivity
		addActivity();// 调用添加方法

		init();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		mResultText = (TextView) findViewById(R.id.hide);
		mReciver = new MessageBackReciver(mResultText);
		mServiceIntent = new Intent(this, BackService.class);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
		mIntentFilter.addAction(BackService.MESSAGE_ACTION);
		canshuhuichuan=(Button)findViewById(R.id.canshuhuichuan);

	}

	public void init(){
		runstate = (TextView)findViewById(R.id.runstate);
		zlud=(EditText)findViewById(R.id.zlUd);
		cfqr=(EditText)findViewById(R.id.cfQr);
		cfkr=(EditText)findViewById(R.id.cfKr);
		jykp=(EditText)findViewById(R.id.jyKp);
		jyki=(EditText)findViewById(R.id.jyKi);
		dykp=(EditText)findViewById(R.id.dyKp);
		dyki=(EditText)findViewById(R.id.dyKi);
		dlkp=(EditText)findViewById(R.id.dlKp);
		cftd=(EditText)findViewById(R.id.cfTd);
		dyrf=(EditText)findViewById(R.id.dyRf);
		gydc=(EditText)findViewById(R.id.gyDC);
		gyac=(EditText)findViewById(R.id.gyAC);
		qyac=(EditText)findViewById(R.id.qyAC);
		gwtp=(EditText)findViewById(R.id.gwTp);
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
		new Handler().postDelayed(new Runnable(){

			public void run() {
				String res ="03 00 1e 00 10";
				res=thehostnumber(res);
				try {
					res = crc16.crc16(res);
					boolean isSend = iBackService.sendMessage(res);//Send Content by socket
					Toast.makeText(fourth.this, isSend ? "参数回传指令已发送" : "参数回传指令未发送", Toast.LENGTH_SHORT).show();
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
	        if(event.getAction() == MotionEvent.ACTION_DOWN) {
	            x1 = event.getX();
	        }  
	        if(event.getAction() == MotionEvent.ACTION_UP) {
	            x2 = event.getX();
				if(x2 - x1 > 50) {
					unbindService(serviceConnection);
					mLocalBroadcastManager.unregisterReceiver(mReciver);
					Intent intent = new Intent();
					intent.setClass(fourth.this,thrid.class);
					startActivity(intent);
					overridePendingTransition(R.animator.fromleft,R.animator.toright);
					//finish();
	            }else  if(x1 - x2 > 50) {
					unbindService(serviceConnection);
					mLocalBroadcastManager.unregisterReceiver(mReciver);
					Intent intent = new Intent();
					intent.setClass(fourth.this, fifth.class);
					startActivity(intent);
					overridePendingTransition(R.animator.fromright,R.animator.toleft);
					//finish();
				}
	        }  
	        return super.onTouchEvent(event);  
	    }  

	public void canshuxiugai(View view){
		try {
			StringBuilder str = new StringBuilder();
			String xiugai = "10001E001020";
			xiugai = thehostnumber(xiugai);
			str.append(xiugai);
			/**
			 * use_module1
			 * use_module2
			 * 默认值0001 0000
			 * **/
			str.append("00010000");
			if (zlud.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(zlud.getText().toString())));}
			if (dykp.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(dykp.getText().toString())));}
			if (dyki.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(dyki.getText().toString())));}
			if (dlkp.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(dlkp.getText().toString())));}
			if (cfqr.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(cfqr.getText().toString())));}
			if (cfkr.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(cfkr.getText().toString())));}
			if (cftd.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(cftd.getText().toString())));}
			if (jykp.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(jykp.getText().toString())));}
			if (jyki.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(jyki.getText().toString())));}
			if (dyrf.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(dyrf.getText().toString())));}
			if (gydc.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(gydc.getText().toString())));}
			if (gyac.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(gyac.getText().toString())));}
			if (qyac.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(qyac.getText().toString())));}
			if (gwtp.length() == 0) {str.append("0000");
			} else {str.append(theString(Integer.parseInt(gwtp.getText().toString())));}

			String result = crc16(str.toString());
	//		Log.w("数据", String.valueOf(result));
			boolean isSend = iBackService.sendMessage(result);
			Toast.makeText(this, isSend ? "参数修改指令已发送" : "参数修改指令未发送", Toast.LENGTH_SHORT).show();

		}catch (Exception e0){e0.printStackTrace();}
	}
	public  void canshuhuichuan(View view){
		clear();
		String res ="03 00 1e 00 10";
		res=thehostnumber(res);
		try {
			res = crc16.crc16(res);
			boolean isSend = iBackService.sendMessage(res);//Send Content by socket
			Toast.makeText(this, isSend ? "参数回传指令已发送" : "参数回传指令未发送", Toast.LENGTH_SHORT).show();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public void clear(){
		zlud.setText("");
		cfqr.setText("");
		cfkr.setText("");
		jykp.setText("");
		jyki.setText("");
		dykp.setText("");
		dyki.setText("");
		dlkp.setText("");
		cftd.setText("");
		dyrf.setText("");
		gydc.setText("");
		gyac.setText("");
		qyac.setText("");
		gwtp.setText("");
	}
}