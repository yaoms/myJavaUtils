package conch.yaoms.util;

import java.io.File;
import java.io.RandomAccessFile;

import android.os.Environment;
import android.util.Log;

public class LogUtil {
	/**
	 * 写文件到sd卡上
	 * 
	 * @param context
	 */
	public static void writeFileToSD(String context) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			Log.d("TestFile", "SD card is not avaiable/writeable right now.");
			return;
		}
		try {
			String pathName = "/sdcard/";
			String fileName = "log.txt";
			File path = new File(pathName);
			File file = new File(pathName + fileName);
			if (!path.exists()) {
				Log.d("TestFile", "Create the path:" + pathName);
				path.mkdir();
			}
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + fileName);
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(file.length());
			raf.write(context.getBytes());
			raf.writeBytes("\n");
			raf.close();

			/*
			 * String pathName = "/sdcard/"; String fileName = "log.txt"; File
			 * path = new File(pathName); File file = new File(pathName +
			 * fileName); if (!path.exists()) { Log.d("TestFile",
			 * "Create the path:" + pathName); path.mkdir(); } if
			 * (!file.exists()) { Log.d("TestFile", "Create the file:" +
			 * fileName); file.createNewFile(); } FileOutputStream stream = new
			 * FileOutputStream(file); String s = context; byte[] buf =
			 * s.getBytes(); stream.write(buf); stream.close();
			 */

		} catch (Exception e) {
			Log.e("TestFile", "Error on writeFilToSD.");
		}
	}
}
