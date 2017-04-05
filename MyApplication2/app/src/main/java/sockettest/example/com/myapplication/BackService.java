package sockettest.example.com.myapplication;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;

import function_class.crc16;
import function_class.hex_string;

import static sockettest.example.com.myapplication.Data.T;
import static sockettest.example.com.myapplication.Data.spp;
import static sockettest.example.com.myapplication.Data.thehostnumber;

public class BackService extends Service {
	private static final String TAG = "BackService";
	private static final long HEART_BEAT_RATE = 3 * 1000;
	private byte[] mBytes;
	private String headbeat1 = "03 00 50 00 0E";
	private String headbeat2 = "03 00 5E 00 14";
	private String dizhicanshuhuichuanbeat = "03 00 04 00 03";
	private OutputStream os = null;

	private Data app;

	public static final String MESSAGE_ACTION = "org.feng.message_ACTION";
	public static final String HEART_BEAT_ACTION = "org.feng.heart_beat_ACTION";

	private ReadThread mReadThread;

	private LocalBroadcastManager mLocalBroadcastManager;

	private WeakReference<Socket> mSocket;

	// For heart Beat
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable = new Runnable() {

		@Override
		public void run() {
			if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
				boolean isSuccess = sendMsg("");
				if (!isSuccess) {
					mHandler.removeCallbacks(heartBeatRunnable);
					mReadThread.release();
					releaseLastSocket(mSocket);
					new InitSocketThread().start();
				}
			}
			mHandler.postDelayed(this, HEART_BEAT_RATE);
		}
	};

	private long sendTime = 0L;
	private IBackService.Stub iBackService = new IBackService.Stub() {
		public boolean sendMessage(String message) throws RemoteException {
			return sendMsg(message);
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return iBackService;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		new InitSocketThread().start();
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);


		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (this) {
					headbeat1 = headbeat1.replace(" ", "");
					headbeat1 = thehostnumber(headbeat1);
					String ss1 = crc16.crc16(headbeat1);
					headbeat2 = headbeat2.replace(" ", "");
					headbeat2 = thehostnumber(headbeat2);
					String ss2 = crc16.crc16(headbeat2);
					long before, after, before1, after1;
					String thehostnumber = spp.getString("hostnumber", "");
					while (T) {
						before = System.currentTimeMillis();
						if (!thehostnumber.equals(spp.getString("hostnumber", ""))) {
							thehostnumber = spp.getString("hostnumber", "");
						}
						try {
//						Log.w("ss",ss);
							boolean isSend = iBackService.sendMessage(ss1);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						after = System.currentTimeMillis() - before;
						try {
							Thread.sleep(500 - after);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						before1 = System.currentTimeMillis();
						try {
//						Log.w("ss",ss);
							boolean isSend = iBackService.sendMessage(ss2);//Send Content by socket
//						if (isSend){Log.w("通过校验",ss);}
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						after1 = System.currentTimeMillis() - before1;
						try {
							Thread.sleep(500 - after1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();

	}


	public boolean sendMsg(String msg) {
		if (null == mSocket || null == mSocket.get()) {
			return false;
		}
		Socket soc = mSocket.get();
		try {
			if (!soc.isClosed() && !soc.isOutputShutdown()) {
				os = soc.getOutputStream();
				String message = msg + "";
				//	StringtoIntHex(message);
				message = message.replace(" ", "");
				message = message.toUpperCase();
				mBytes = hex_string.hexStringToByte(message);
				os.write(mBytes);
				os.flush();
				sendTime = System.currentTimeMillis();
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	private void initSocket() {//��ʼ��Socket
		try {
			app = (Data) getApplication();
			Socket so = new Socket(Data.HOST, Data.PORT);
			Log.w("host", Data.HOST);
			Log.w("port", String.valueOf(Data.PORT));
			mSocket = new WeakReference<Socket>(so);
			mReadThread = new ReadThread(so);
			mReadThread.start();
			mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void releaseLastSocket(WeakReference<Socket> mSocket) {
		try {
			if (null != mSocket) {
				Socket sk = mSocket.get();
				if (!sk.isClosed()) {
					sk.close();
				}
				sk = null;
				mSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class InitSocketThread extends Thread {
		@Override
		public void run() {
			super.run();
			initSocket();
		}
	}

	// Thread to read content from Socket
	class ReadThread extends Thread {
		private WeakReference<Socket> mWeakSocket;
		private boolean isStart = true;

		public ReadThread(Socket socket) {
			mWeakSocket = new WeakReference<Socket>(socket);
		}

		public void release() {
			isStart = false;
			releaseLastSocket(mWeakSocket);
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			super.run();
			Socket socket = mWeakSocket.get();
			if (null != socket) {
				try {
					InputStream is = socket.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int length = 0;
					while (!socket.isClosed() && !socket.isInputShutdown() && isStart && ((length = is.read(buffer)) != -1)) {
						if (length > 0) {

							/**
							 * 获得接收到的内容*
							 * */
							//获得将16进制数组转成16进制字符串的String
							String message = new String(hex_string.bytesToHexStringwithlength(buffer, length)).trim();
							Log.w("message",message);
							if (message.equals("ok")) {
								Intent intent = new Intent(HEART_BEAT_ACTION);
								mLocalBroadcastManager.sendBroadcast(intent);
							} else {
								Intent intent = new Intent(MESSAGE_ACTION);
								intent.putExtra("message", message);
								mLocalBroadcastManager.sendBroadcast(intent);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
