package sockettest.example.com.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AutoLayoutActivity {

	///11111111

	float x1 = 0;
	float x2 = 0;
	float y1 = 0;
	float y2 = 0;
	private long exitTime;
	//    private Data app;
	private Data application;
	private MainActivity oContext;

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
	private EditText mEditText,username,password;
    private CheckBox mCheckBox;
    private SharedPreferences sp;
	private Intent mServiceIntent;
    private String neirong,usernameValue,passwordVlue;
    private int jiange;
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
				tv.setText(message);
			}
		};
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (application == null) {
			// 得到Application对象
			application = (Data) getApplication();
		}
		oContext = this;// 把当前的上下文对象赋值给BaseActivity
		addActivity();// 调用添加方法

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		mResultText = (TextView) findViewById(R.id.resule_text);
		mEditText = (EditText) findViewById(R.id.content_edit);
		final EditText nr = (EditText) findViewById(R.id.nr);

		mReciver = new MessageBackReciver(mResultText);

		mServiceIntent = new Intent(this, BackService.class);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
		mIntentFilter.addAction(BackService.MESSAGE_ACTION);

		username = (EditText) findViewById(R.id.the_username);
		password = (EditText) findViewById(R.id.the_password);
		mCheckBox = (CheckBox) findViewById(R.id.rememberusername);

		final EditText ip1 = (EditText) findViewById(R.id.ip1);
		final EditText port1 = (EditText) findViewById(R.id.port1);


		SharedPreferences sharedPreferences = getSharedPreferences("test", Activity.MODE_PRIVATE);
		ip1.setText(sharedPreferences.getString("ip1", ""));
		port1.setText(sharedPreferences.getString("port1", ""));
		mEditText.setText(sharedPreferences.getString("sd1", ""));

		if (sharedPreferences.getString("zd1", "").equals("")) {
		} else {
			nr.setText(sharedPreferences.getString("zd1", ""));
		}

        sp = this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
		Button enterto = (Button) findViewById(R.id.enterto);
		enterto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				usernameValue = username.getText().toString();
				passwordVlue = password.getText().toString();
				SharedPreferences.Editor editor = sp.edit();

				if ((usernameValue.equals("admin") && passwordVlue.equals("123456"))||(usernameValue.equals("admin") && passwordVlue.equals("111111"))) {
					if (usernameValue.equals("admin")&&passwordVlue.equals("111111")) {
						Data.permission = true;
					}
					Toast.makeText(MainActivity.this, "成功登陆", Toast.LENGTH_SHORT).show();
					Data.HOST = "200.100.1.100";//    200.100.1.100    10.10.100.254
					Data.PORT = 8899;

					if (sp.getString("hostnumber", "").length() == 0) {
						editor.putString("hostnumber", "01");
						editor.commit();
					}
					if (mCheckBox.isChecked()) {
						editor.putString("USERNAME", usernameValue);
						editor.putString("PASSWORD", passwordVlue);
						editor.putBoolean("CHECKBOX", true);
						editor.commit();
//						Log.w("记录", sp.getString("USERNAME","")+"\n"+ sp.getString("PASSWORD","")+"\n"+sp.getBoolean("CHECKBOX", false));
					} else if (!mCheckBox.isChecked()) {
						editor.putString("USERNAME", "");
						editor.putString("PASSWORD", "");
						editor.putBoolean("CHECKBOX", false);
						editor.commit();
//						Log.w("清空", sp.getString("USERNAME","")+"\n"+ sp.getString("PASSWORD","")+"\n"+sp.getBoolean("CHECKBOX", false));
					}
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, thePageOne.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(MainActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
				}
			}
		});

//		Log.w("验证", sp.getString("USERNAME","")+"\n"+ sp.getString("PASSWORD","")+"\n"+sp.getBoolean("CHECKBOX", false));
        if (sp.getBoolean("CHECKBOX", false)) {
            mCheckBox.setChecked(true);
            username.setText(sp.getString("USERNAME",""));
            password.setText(sp.getString("PASSWORD",""));
        }

        /*Button qd = (Button) findViewById(R.id.qd);
		qd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jiange = 1000;
				neirong = nr.getText().toString();
				handler.postDelayed(runnable, jiange); //ÿ��1sִ��
			}
		});


		Button tz = (Button) findViewById(R.id.tz);
		tz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				handler.removeCallbacks(runnable);

			}
		});



		Button button = (Button) findViewById(R.id.bd);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//		 app = (Data) getApplication();
				Data.HOST = ip1.getText().toString();
				Data.PORT = Integer.parseInt(port1.getText().toString());

				mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
				bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
			}
		});

		Button the_enter = (Button) findViewById(R.id.the_enter);
		the_enter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				unbindService(serviceConnection);
				mLocalBroadcastManager.unregisterReceiver(mReciver);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, frist.class);
				startActivity(intent);

			}
		});*/
	}



	@Override
	protected void onStart() {
		super.onStart();

		//	mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		//bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//	unbindService(serviceConnection);
		//mLocalBroadcastManager.unregisterReceiver(mReciver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				if (System.currentTimeMillis() - exitTime > 2000) {
					Toast toast = Toast.makeText(this, "再点击一次返回", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER,0,0);
					toast.show();
					exitTime = System.currentTimeMillis();
				} else {
					removeALLActivity();
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onKeyDown(keyCode, event);
	}

	/*public void onClick(View view) {
		switch (view.getId()) {

			case R.id.send:
				if (mEditText.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "发送内容不能为空", Toast.LENGTH_SHORT).show();
				} else {
					String cmd = mEditText.getText().toString();
					*//*  String[] cmds = cmd.split(" ");
			 byte[] aaa = new byte[cmds.length];
		        int i = 0;
		        for (String b : cmds) {
		            if (b.equals("FF")) {aaa[i++] = -1; } else {aaa[i++] = Byte.parseByte(b, 16); }
		        }
				String res = new String(aaa);*//*
					String res = cmd;
					try {
						boolean isSend = iBackService.sendMessage(res);//Send Content by socket
						Toast.makeText(this, isSend ? "success" : "fail", Toast.LENGTH_SHORT).show();
						ImageView bbb = (ImageView) findViewById(R.id.bbb);
						if (isSend) {
							bbb.setImageResource(R.drawable.run);
						} else {
							bbb.setImageResource(R.drawable.stop);
						}


					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
				break;

			default:
				break;
		}
	}*/

/*	final Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				handler.postDelayed(this, jiange);
				if (neirong.equals("")) {

					Toast.makeText(getApplicationContext(), "发送内容不能为空", Toast.LENGTH_SHORT).show();

				} else {
					String cmd = neirong;

					String[] cmds = cmd.split(" ");
					byte[] aaa = new byte[cmds.length];
					int i = 0;
					for (String b : cmds) {
						if (b.equals("FF")) {
							aaa[i++] = -1;
						} else {
							aaa[i++] = Byte.parseByte(b, 16);
						}
					}
					String res = new String(aaa);

					try {
						boolean isSend = iBackService.sendMessage(res);//Send Content by socket

						ImageView bbb = (ImageView) findViewById(R.id.bbb);
						if (isSend) {
							bbb.setImageResource(R.drawable.run);
						} else {
							bbb.setImageResource(R.drawable.stop);
						}


					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};*/

}