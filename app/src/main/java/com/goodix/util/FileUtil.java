package com.goodix.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

public class FileUtil {

	public final static String STOREROOTPATH = "engtest";
	public final static String STOREREGPATH = "reg";
	public final static String STORESAMPLEPATH = "sample";
	public final static String STORESAMPLEFAKEPATH = "sample_fake";
	private static final String TAG = "FARFRR";
	private static final String HEADSTR = "Time,Rawdata,DisplayData,HeartBeatRate,Status,Led\n";
	private static int sMaxCaputre = 100;
	private static int sMaxEnroll = 40;

	public static void setMaxCaptureCount(int count) {
		sMaxCaputre = count;
	}

	public static int getMaxCaptureCount() {
		return sMaxCaputre;
	}

	public static void setMaxEnrollCount(int count) {
		sMaxEnroll = count;
	}

	public static int getMaxEnrollCount() {
		return sMaxEnroll;
	}

	public static boolean isCaptureed(String filePath, int choose) {
		int Count = 0;
		/*
		 * File dir = new File(Environment.getExternalStorageDirectory()
		 * .getAbsolutePath() + File.separator + STOREROOTPATH + File.separator
		 * + STORESAMPLEPATH + File.separator + filePath);
		 */
		File dir = null;
		if (choose == 1) {
			dir = new File(TestConfig.SAMPLE_FAKE_PATH + filePath);
		} else {
			dir = new File(TestConfig.SAMPLE_PATH + filePath);
		}
		if (!dir.exists()) {
			return false;
		}
		String[] filePaths = null;
		if (dir.isDirectory()) {
			File files[] = dir.listFiles();
			filePaths = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				filePaths[i] = files[i].getAbsolutePath();
				if (isPicture(filePaths[i])) {
					Count++;
				}
			}
		}
		if (Count >= sMaxCaputre) {
			return true;
		}
		return false;
	}

	public static boolean isEnrolled(String filepath) {
		int Count = 0;
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ STOREROOTPATH
				+ File.separator + STOREREGPATH + File.separator + filepath);
		if (!dir.exists()) {
			return false;
		}
		String[] filePaths = null;
		if (dir.isDirectory()) {
			File files[] = dir.listFiles();
			filePaths = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				filePaths[i] = files[i].getAbsolutePath();
				if (isFeatureData(filePaths[i])) {
					Count++;
				}
			}
		}
		Log.d(TAG, "Count = " + Count + "; sMaxEnroll = " + sMaxEnroll);
		if (Count == sMaxEnroll) {
			return true;
		}
		return false;
	}

	public static int getBmpNum(String absFilepath) {
		int Count = 0;
		File dir = new File(absFilepath);
		if (!dir.exists()) {
			Log.d(TAG, "-------------------------------------------");
			return 0;
		}
		String[] filePaths = null;
		if (dir.isDirectory()) {
			File files[] = dir.listFiles();
			Log.d(TAG, "files.length = " + files.length);
			filePaths = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				filePaths[i] = files[i].getAbsolutePath();
				if (isFeatureData(filePaths[i])) {
					Count++;
				}
			}
		}
		return Count;
	}

	public static boolean isPicture(String filePath) {
		if (null == filePath) {
			return false;
		}
		int position = filePath.lastIndexOf(".");
		if (position < 0) {
			return false;
		}
		String subString = null;
		subString = filePath.substring(position).toLowerCase();
		if (subString != null) {
			if (subString.equals(".bmp"))
				return true;
		}
		return false;
	}

	public static boolean isFeatureData(String filePath) {
		if (null == filePath) {
			return false;
		}
		int position = filePath.lastIndexOf(".");
		if (position < 0) {
			return false;
		}
		String subString = null;
		subString = filePath.substring(position).toLowerCase();
		if (subString != null) {
			if (subString.equals(".bmp"))
				return true;
		}
		return false;
	}

	/*
	 * public static int getStatus(String filepath, int mode) { int count = 0;
	 * if (filepath == null) { return FoldErrcode.FAILURE; } String[] segments =
	 * filepath.split("/"); count = getFingerFolderCount(segments[0]); if (count
	 * > 3) { return FoldErrcode.ERR_EXCEED_LIMIT; } if (count == 3) { if (mode
	 * == 0) { if (isCreated(filepath, segments[1])) { if
	 * (isCaptureed(filepath)) { return FoldErrcode.ERR_FOLD_FULL; } else {
	 * return FoldErrcode.SUCCESS; } } else { return
	 * FoldErrcode.ERR_EXCEED_LIMIT; } } else { if (isCreated(filepath,
	 * segments[1])) { if (isEnrolled(filepath)) { return
	 * FoldErrcode.ERR_FOLD_FULL; } else { return FoldErrcode.SUCCESS; } } else
	 * { return FoldErrcode.ERR_EXCEED_LIMIT; } } } else { return
	 * FoldErrcode.SUCCESS; } }
	 */
	public static boolean isCreated(String filepath, String folder) {
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ STOREROOTPATH
				+ File.separator + filepath);
		if (!dir.exists()) {
			return false;
		}
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() && files[i].getName().equals(folder)) {
					return true;
				}
			}
		}
		return false;
	}

	public static int getPepleCount() {
		int count = 0;
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + STOREROOTPATH);
		if (!dir.exists()) {
			return count;
		}
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()
						&& files[i].getName().startsWith("N")) {
					count++;
				}
			}
		}
		return count;
	}

	public static int getFingerFolderCount(String people) {
		int count = 0;
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ STOREROOTPATH
				+ File.separator + people);
		if (!dir.exists()) {
			return count;
		}
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()
						&& (files[i].getName().startsWith("L") || files[i]
								.getName().startsWith("R"))) {
					count++;
				}
			}
		}
		return count;
	}

	public static byte[] loadGrayBitmap(File file) {
		// XYF1207:get the chip size
		int SENSOR_COL = TestConfig.getBitmapCol();
		int SENSOR_ROW = TestConfig.getBitmapRow();
		Log.d(TAG, "SENSOR_COL = " + SENSOR_COL + "; SENSOR_ROW = "
				+ SENSOR_ROW);
		byte[] bitmapData = new byte[SENSOR_COL * SENSOR_ROW];
		int colume_t = (SENSOR_COL + 3) & 0xFC;
		int row_t = (SENSOR_ROW + 3) & 0xFC;
		int blank = colume_t - SENSOR_COL > 0 ? (colume_t - SENSOR_COL) : 0;
		byte[] data = new byte[colume_t * row_t + 1078];
		FileInputStream input = null;
		byte[] dataWithNoHeader = { 0 };
		try {
			input = new FileInputStream(file);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[256];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
			dataWithNoHeader = subByteArray(data, 1078, data.length - 1078);
		} catch (FileNotFoundException ex1) {
			ex1.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		for (int i = 0; i < SENSOR_ROW; i++) {
			for (int j = 0; j < (SENSOR_COL + blank); j++) {
				if (j < SENSOR_COL) {
					bitmapData[(SENSOR_ROW - i - 1) * SENSOR_COL + j] = dataWithNoHeader[i
							* (SENSOR_COL + blank) + j];
				}
			}
		}
		return bitmapData;
	}

	public static byte[] loadGscData(File file) {

		Log.d(TAG, "loadGscData file = " + file.getAbsolutePath());
		BufferedReader br = null;
		// 1+10+1+5+1
		byte[] retBytes = new byte[18 * 4];
		byte[] tmpBytes = null;

		try {
			br = new BufferedReader(new FileReader(file));
			// get: 1 + 10 + 1 + 5 + "-1" + 1 + "-2" + 1
			String gscAllDataStr = br.readLine();
			//Log.d(TAG, "@@@@@----gscAllDataStr = " + gscAllDataStr);
			// get: 1 + 10 + 1 + 5 + "-1" + 1 + "-2"
			String gscSubAllDataStr = gscAllDataStr.substring(0,
					gscAllDataStr.lastIndexOf(','));
			// get: 1 + 10 + 1 + 5 + "-1" + 1
			String gscSubSubAllDataStr = gscSubAllDataStr.substring(0,
					gscSubAllDataStr.lastIndexOf(','));
			
			String[] gscDataStrs = gscSubSubAllDataStr.split(",");  //19
			String[] transGscDataStrs = new String[18];  //18
			
			for (int i = 0; i < gscDataStrs.length; i++) {  //19
				if(i == 17) { 
					//skip
				}else if( i == 18) {
					transGscDataStrs[17] = gscDataStrs[18];
					//Log.d(TAG, "@@@@@@@@@@@@@@  transGscDataStrs[" + (i-1) + "] = " +  transGscDataStrs[17]);
				}else {
					transGscDataStrs[i] = gscDataStrs[i];
					//Log.d(TAG, "@@@@@@@@@@@@@@  transGscDataStrs[" + i + "] = " +  transGscDataStrs[i]);
				}
			}

			for (int i = 0; i < transGscDataStrs.length; i++) { // 18
				tmpBytes = gscIntToBytes(Integer.parseInt(transGscDataStrs[i]));
				retBytes[i * 4 + 0] = tmpBytes[0];
				retBytes[i * 4 + 1] = tmpBytes[1];
				retBytes[i * 4 + 2] = tmpBytes[2];
				retBytes[i * 4 + 3] = tmpBytes[3];
			}

		} catch (Exception ex1) {
			ex1.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retBytes;
	}

	private static byte[] subByteArray(byte[] src, int beginPosition, int length) {
		if (src == null || beginPosition < 0 || src.length < length)
			return null;
		byte[] des = new byte[length];
		for (int i = 0; i < length; i++)
			des[i] = src[beginPosition + i];
		return des;
	}

    public static byte[] bmpMirrorChange(byte[] pData, int row, int colume) {
        int oldRowIndex = 0;
        int oldColIndex = 0;
        int newRowIndex = 0;
        int newColIndex = 0;
        byte[] tmp = new byte[row * colume];

        for (int i = 0; i < row * colume; i++) {
            oldRowIndex = i / colume;
            oldColIndex = i % colume;
            newRowIndex = row - 1 - oldRowIndex;
            newColIndex = oldColIndex;
            tmp[newRowIndex * colume + newColIndex] = pData[i];
        }
        return tmp;
    }
    
	public static int saveGrayBitmap(String FilePath, byte[] pData, int length,
			int row, int colume) {
		/* Gray bitmap file header. */
		byte grayBitmapHeader[] = new byte[1078];
		{
			int colume_t = (colume + 3) & 0xFC;
			int row_t = (row + 3) & 0xFC;
			grayBitmapHeader[0] = 0x42;
			grayBitmapHeader[1] = 0x4d;
			grayBitmapHeader[2] = 0x36;
			grayBitmapHeader[3] = 0x28;
			grayBitmapHeader[4] = 0x00;
			grayBitmapHeader[5] = 0x00;
			grayBitmapHeader[6] = 0x00;
			grayBitmapHeader[7] = 0x00;
			grayBitmapHeader[8] = 0x00;
			grayBitmapHeader[9] = 0x00;
			grayBitmapHeader[10] = 0x36;
			grayBitmapHeader[11] = 0x04;
			grayBitmapHeader[12] = 0x00;
			grayBitmapHeader[13] = 0x00;
			grayBitmapHeader[14] = 0x28;
			grayBitmapHeader[15] = 0x00;
			grayBitmapHeader[16] = 0x00;
			grayBitmapHeader[17] = 0x00;
			grayBitmapHeader[18] = (byte) (colume & 0xFF);
			grayBitmapHeader[19] = (byte) (colume >> 8 & 0xFF);
			grayBitmapHeader[20] = (byte) (colume >> 16 & 0xFF);
			grayBitmapHeader[21] = (byte) (colume >> 24 & 0xFF);
			grayBitmapHeader[22] = (byte) (row & 0xFF);
			grayBitmapHeader[23] = (byte) (row >> 8 & 0xFF);
			grayBitmapHeader[24] = (byte) (row >> 16 & 0xFF);
			grayBitmapHeader[25] = (byte) (row >> 24 & 0xFF);
			grayBitmapHeader[26] = 0x01;
			grayBitmapHeader[27] = 0x00;
			grayBitmapHeader[28] = 0x08;
			grayBitmapHeader[29] = 0x00;
			for (int i = 0; i < 256; i++) {
				grayBitmapHeader[54 + i * 4] = (byte) i;
				grayBitmapHeader[54 + i * 4 + 1] = (byte) i;
				grayBitmapHeader[54 + i * 4 + 2] = (byte) i;
			}
			FileOutputStream fileOutStream;
			byte[] pad = new byte[4];
			try {
				fileOutStream = new FileOutputStream(new File(FilePath));
				fileOutStream.write(grayBitmapHeader);
				// byte[] data = addBMP_RGB_888(pData, row, colume);
				// fileOutStream.write(data);
				for (int i = 0; i < row; i++) {
					fileOutStream.write(pData, (row - i - 1) * colume, colume);
					if (colume_t > colume)
						fileOutStream.write(pad, 0, colume_t - colume);
				}
				fileOutStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static int saveGscDataToTxt(String FilePath, byte[] pData, int length) {
		if (null == pData) {
			return -1;
		}
		FileWriter fwFileWriter;
		try {
			fwFileWriter = new FileWriter(new File(FilePath));
			int[] data = fourByte2Int(pData);
			Log.d(TAG, "data.length = " + data.length);
			long b2 = System.currentTimeMillis();
			StringBuffer sBuffer = new StringBuffer();

			/*
			 * for (int i = 0; i < data.length; i++) { sBuffer.append(data[i]);
			 * if (i != (data.length - 1)) { sBuffer.append(','); } }
			 */
			for (int i = 0; i < data.length; i++) {
				if (i != (data.length - 1)) {
					sBuffer.append(data[i]);
					sBuffer.append(',');
				} else if (i == (data.length - 1)) {
					sBuffer.append("-1,");
					sBuffer.append(data[i]);
					sBuffer.append(",-2,");
				}
			}
			// default gsc result
			sBuffer.append("-1");
			sBuffer.append('\n');
			String res = sBuffer.toString();
			fwFileWriter.write(res);
			long e2 = System.currentTimeMillis();
			//Log.d(TAG, "e2 - b2 = " + (e2 - b2));
			fwFileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// add0418
	public static int saveHbdDataToTxt(String FilePath, String pData, int length) {
		if (null == pData) {
			return -1;
		}
		FileWriter fwFileWriter;
		try {
			File hbdFile = new File(FilePath);
			if (!hbdFile.exists()) {
				hbdFile.createNewFile();
				fwFileWriter = new FileWriter(hbdFile, true);
				fwFileWriter.write(HEADSTR);
			} else {
				fwFileWriter = new FileWriter(hbdFile, true);
			}
			fwFileWriter.write(pData);
			fwFileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// add1211
	public static int saveRawdataToCsv(String FilePath, byte[] pData,
			int length, int row, int colume) {
		{
			Log.d(TAG, "pdata.length = " + length + "; row*col = " + row
					* colume);
			if (null == pData) {
				return -1;
			}
			FileWriter fwFileWriter;
			try {
				fwFileWriter = new FileWriter(new File(FilePath));
				int[] data = twoByte2Int(pData);
				Log.d(TAG, "data.length = " + data.length);
				long b2 = System.currentTimeMillis();
				StringBuffer sBuffer = new StringBuffer();
				for (int i = 0; i < row; i++) {
					for (int j = 0; j < colume; j++) {
						sBuffer.append(data[i * colume + j]);
						sBuffer.append(',');
					}
					sBuffer.append('\n');
				}
				String res = sBuffer.toString();
				fwFileWriter.write(res);
				long e2 = System.currentTimeMillis();
				//Log.d(TAG, "e2 - b2 = " + (e2 - b2));
				fwFileWriter.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	private static byte[] addBMP_RGB_888(byte[] b, int w, int h) {
		int len = b.length;
		System.out.println(b.length);
		byte[] buffer = new byte[w * h * 3];
		int offset = 0;
		for (int i = len - 1; i >= 0; i -= w) {
			// DIB文件格式最后一行为第一行，每行按从左到右顺序
			int end = i, start = i - w + 1;
			for (int j = start; j <= end; j++) {
				buffer[offset] = (byte) (b[j] >> 0);
				buffer[offset + 1] = (byte) (b[j] >> 8);
				buffer[offset + 2] = (byte) (b[j] >> 16);
				offset += 3;
			}
		}
		return buffer;
	}

	public static int[] fourByte2Int(byte[] res) {
		if (res.length % 4 != 0)
			return null;// invalid int byte[]
		int[] t = new int[res.length / 4];
		for (int i = 0; i < t.length; i++) {
			t[i] = (res[i * 4 + 0] & 0xff) | ((res[i * 4 + 1] << 8) & 0xff00)
					| ((res[i * 4 + 2] << 24) >>> 8) | (res[i * 4 + 3] << 24);
		}
		return t;
	}

	public static int[] twoByte2Int(byte[] res) {
		if (res.length % 2 != 0)
			return null;// invalid int byte[]
		int[] t = new int[res.length / 2];
		for (int i = 0; i < t.length; i++) {
			t[i] = (res[i * 2 + 0] & 0xff) | ((res[i * 2 + 1] << 8) & 0xff00);
		}
		return t;
	}

	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[3 - i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}

	public static byte[] gscIntToBytes(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0x000000ff);
		b[1] = (byte) ((n & 0x0000ff00) >> 8);
		b[2] = (byte) ((n & 0x00ff0000) >> 16);
		b[3] = (byte) ((n & 0xff000000) >> 24);
		return b;
	}

	// add0418
	public static String getHbdSaveFormatData(byte[] mTemp) {
		byte[] buffer = new byte[40]; // 0~19:rawdata; 20~39:displaydata
		byte[] ledBuf = new byte[4];
		int[] data = new int[10]; // 0~4: rawdata; 5~9:displaydata
		System.arraycopy(mTemp, 0, buffer, 0, 20);
		System.arraycopy(mTemp, 24, buffer, 20, 20);
		data = fourByte2Int(buffer);
		int HeartBeatRate = mTemp[20];
		int HBDStatus = mTemp[21];
		//int BeatIndex = mTemp[44];
		System.arraycopy(mTemp, 48, ledBuf, 0, 4);
        int Led = fourByte2Int(ledBuf)[0];
		String currentTimeStr = getShortCurrentTimeString();
		Log.d(TAG, "currentTimeStr = " + currentTimeStr);
		String string = currentTimeStr + "," + data[0] + "," + data[5] + ","
				+ HeartBeatRate + "," + HBDStatus + "," + Led + "\n"
				+ currentTimeStr + "," + data[1] + "," + data[6] + ","
				+ HeartBeatRate + "," + HBDStatus + "," + Led + "\n"
				+ currentTimeStr + "," + data[2] + "," + data[7] + ","
				+ HeartBeatRate + "," + HBDStatus + "," + Led + "\n"
				+ currentTimeStr + "," + data[3] + "," + data[8] + ","
				+ HeartBeatRate + "," + HBDStatus + "," + Led + "\n"
				+ currentTimeStr + "," + data[4] + "," + data[9] + ","
				+ HeartBeatRate + "," + HBDStatus + "," + Led + "\n";
		Log.d("HBDTEST", "string = \n" + string);
		
		return string;
	}

	// add0420
	public static String getLongCurrentTimeString() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String yearSTR = String.format("%04d", year);
		int month = (cal.get(Calendar.MONTH)) + 1;
		String monthSTR = String.format("%02d", month);
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		String day_of_monthSTR = String.format("%02d", day_of_month);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String hourSTR = String.format("%02d", hour);
		int minute = cal.get(Calendar.MINUTE);
		String minuteSTR = String.format("%02d", minute);
		int second = cal.get(Calendar.SECOND);
		String secondSTR = String.format("%02d", second);
		return yearSTR + "" + monthSTR + "" + day_of_monthSTR + "" + hourSTR
				+ "" + minuteSTR + "" + secondSTR + "";
	}

	// add0420
	public static String getShortCurrentTimeString() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String hourSTR = String.format("%02d", hour);
		int minute = cal.get(Calendar.MINUTE);
		String minuteSTR = String.format("%02d", minute);
		int second = cal.get(Calendar.SECOND);
		String secondSTR = String.format("%02d", second);
		int mSecond = cal.get(Calendar.MILLISECOND);
		String mSecondSTR = String.format("%03d", mSecond);
		return hourSTR + "" + minuteSTR + "" + secondSTR + "" + mSecondSTR + "";
	}

	/********* add0505 for farfrr ******/
	public static String[] showAllFiles(String dir) {
		// Log.d(TAG, "show all files in dir = " + dir);
		File mFile = new File(dir);
		// add0420
		if (!mFile.exists()) {
			return null;
		}
		File[] fs = mFile.listFiles();
		if (fs.length == 0) {
			return null;
		}
		String[] folder = new String[fs.length];
		for (int i = 0; i < fs.length; i++) {
			Log.e(TAG,
					"folder[" + i + "] absolutepath = "
							+ fs[i].getAbsolutePath());
			String fileName = fs[i].getName();
			folder[i] = fileName.substring(fileName.lastIndexOf("\\") + 1);
			// Log.d(TAG, "folder[i] " + folder[i]);
		}
		return folder;
	}

	// add0505
	public static String getConfigTestTime(String dir) {
		File file = new File(TestConfig.HBD_PATH + dir + "/" + dir + ".config");
		Log.d(TAG, "config = " + file.getAbsolutePath());
		if (!file.exists()) {
			return null;
		}
		LineNumberReader lnReader = null;
		try {
			lnReader = new LineNumberReader(new FileReader(file));
			String lineStr = null;
			int line = -1;
			String[] splitStrs = null;
			while ((lineStr = lnReader.readLine()) != null) { // Time:60
				Log.d(TAG, "line = " + lnReader.getLineNumber()); // from 1
																	// begin
				splitStrs = lineStr.split(":");
				Log.d(TAG, "splitStrs[0] = " + splitStrs[0]);
				Log.d(TAG, "splitStrs[1] = " + splitStrs[1]);
				if (splitStrs[0].equals("Time")) {
					return splitStrs[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (lnReader != null) {
					lnReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String paraseFileName(String fileName) {
		StringBuilder filepath = new StringBuilder();
		String[] segment = fileName.split("_");
		filepath.append(segment[0]).append("/").append(segment[1]);
		return filepath.toString();
	}

	public static char[] oneByte2Char(byte[] res) { // 1 to 1
		char[] t = new char[res.length];
		for (int i = 0; i < t.length; i++) {
			t[i] = (char) res[i];
		}
		return t;
	}


	public static ResultData parserObjInfo(byte[] obj) {
		ResultData resultData = new ResultData();
		byte[] objInt = new byte[resultData.intDataLenth() * 4]; // (27+32+3)*4
		byte[] objChar = new byte[resultData.versionLenth() * 1]; // 16*1
		for (int i = 0; i < objInt.length; i++) {
			objInt[i] = obj[i];
		}
		for (int i = 0; i < objChar.length; i++) {
			objChar[i] = obj[i + objInt.length];
		}
		int[] intData = fourByte2Int(objInt);
		char[] charData = oneByte2Char(objChar);
	    for (int i = 0; i < resultData.intDataLenth(); i++) { // 27+32+3=62
			resultData.setInt(i, intData[i]);
		}
		for (int i = 0; i < resultData.versionLenth(); i++) { // 16
			resultData.setChar(i, charData[i]);
		}
		return resultData;
	};

	public static int saveMpTestData(String file, String data) {
		FileWriter fwFileWriter;
		try {
			File f = new File(file);
			if (!f.exists() || f.isFile()) {
				f.createNewFile();
			}
			fwFileWriter = new FileWriter(f, true);
			fwFileWriter.write(data);
			fwFileWriter.flush();
			fwFileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int[] parseGscFpRet(int score) {
	    Log.d(TAG, "multi_score = " + score);
		int[] rets = new int[2];
		if (score > 0) {
			rets[0] = score & 0xFF;
			rets[1] = (score & 0xFF00) >> 8;
		} else {
			rets[0] = -((-score) & 0xFF);
			rets[1] = (((-score) & 0xFF00) >> 8);
		}
		return rets;
	}

	public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File f : files) {
				deleteFile(f); // delete all files
				f.delete(); // delete dir
			}
		}
	}
	
    public static String getSystemPropertyAsString(String propertyName) {
        String value = null;

        try {
            Class<?> systemPropertiesClazz = Class
                    .forName("android.os.SystemProperties");
            Method method = systemPropertiesClazz
                    .getMethod("get", String.class);
            value = (String) method.invoke(null, propertyName);

        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
        }

        return value;
    }
}
