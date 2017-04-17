package function_class;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/4/11.
 */

public class ReadSDFile {


	public static void readFileByBytes(String filepath) throws IOException {

		try {
			String encoding = "GBK";
			File file = new File(filepath);
			if (file.isFile() && file.exists()) { //判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] toByteArray2(String filepath) throws IOException {

		File sd = Environment.getExternalStorageDirectory();
		boolean can_write = sd.canWrite();
		Log.i("FILEREAD", String.valueOf(can_write));

		File f = new File(Environment.getExternalStorageDirectory(),"1234.doc");
		Log.i("FILE", Environment.getExternalStorageDirectory().getAbsolutePath());
		Log.i("FILEPATH", f.getAbsolutePath());
		if (!f.exists()) {
			throw new FileNotFoundException(filepath);
		}

//		String filename = f.getName();

		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			byte[] filecontent = new byte[1024];
			while (fs.read(filecontent) != -1) {
				// System.out.println("reading");
			}
			for (int i=0;i<5;i++){
				Log.w("内容",String.valueOf(filecontent[i]));
			}
			return filecontent;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



/*		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		int length = fis.available();

		InputStream in = null;
		try {
			System.out.println("以字节为单位读取文件内容，一次读多个字节：");
			// 一次读多个字节
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			ReadFromFile.showAvailableBytes(in);
			// 读入多个字节到字节数组中，byteread为一次读入的字节数
			while ((byteread = in.read(tempbytes)) != -1) {
				System.out.write(tempbytes, 0, byteread);//好方法，第一个参数是数组，第二个参数是开始位置，第三个参数是长度
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}*/

/*	public String readSDFile(String fileName) throws IOException {

		File file = new File(fileName);

		FileInputStream fis = new FileInputStream(file);

		int length = fis.available();

		byte [] buffer = new byte[length];
		fis.read(buffer);

		String res = EncodingUtils.getString(buffer, "UTF-8");

		fis.close();
		return res;
	}*/

	/**
	 * 6.0获取sd卡外部存储方式改变
	 * */
	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE };
	public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
		}

	}
}
