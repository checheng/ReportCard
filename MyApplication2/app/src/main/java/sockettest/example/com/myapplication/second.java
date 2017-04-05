package sockettest.example.com.myapplication;

import android.app.Activity;
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
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class second extends Activity {
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
				tv.setText(message);
			}
		};
	}

	private MessageBackReciver mReciver;

	private IntentFilter mIntentFilter;

	private LocalBroadcastManager mLocalBroadcastManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondpage);

		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

		mResultText = (TextView) findViewById(R.id.hide);
		mReciver = new MessageBackReciver(mResultText);
		mServiceIntent = new Intent(this, BackService.class);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
		mIntentFilter.addAction(BackService.MESSAGE_ACTION);


	}




	@Override
	protected void onStart() {
		super.onStart();
		//Data.T=true;

		final Intent intent = new Intent();
		intent.setAction("ITOP.MOBILE.SIMPLE.SERVICE.SENSORSERVICE");
      // stopService(intent);
		mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
		bindService(mServiceIntent, serviceConnection, BIND_AUTO_CREATE);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Data.T=false;
	//	unbindService(serviceConnection);
	//	mLocalBroadcastManager.unregisterReceiver(mReciver);
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
					intent.setClass(second.this, thrid.class);
					startActivity(intent);
					overridePendingTransition(R.animator.fromright,R.animator.toleft);
					//finish();
	            } else if(x2 - x1 > 50) {
					unbindService(serviceConnection);
					mLocalBroadcastManager.unregisterReceiver(mReciver);
					Intent intent = new Intent();
					intent.setClass(second.this, frist.class);
					startActivity(intent);
					overridePendingTransition(R.animator.fromleft,R.animator.toright);

					//finish();
	            }
	        }  
	        return super.onTouchEvent(event);  
	    }  
	      
}