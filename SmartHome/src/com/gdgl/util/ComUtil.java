package com.gdgl.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.gdgl.mydata.getFromSharedPreferences;

import android.R.string;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class ComUtil {
	private final static String TAG = "ComUtil";
	// Ӧ�ó����Ŀ¼
	private static String rootPath = Environment.getExternalStorageDirectory()
			.toString() + "/gdgl"+getFromSharedPreferences.getGatewayMAC();
	public static String picturePath = rootPath+"/PictureShot";
	public static String fileName;
	public static FileOutputStream fileOutputStream ;

	public byte[] intToByte(int i) {
		byte[] abyte0 = new byte[4];
		abyte0[0] = (byte) (0xff & i);
		abyte0[1] = (byte) ((0xff00 & i) >> 8);
		abyte0[2] = (byte) ((0xff0000 & i) >> 16);
		abyte0[3] = (byte) ((0xff000000 & i) >> 24);
		return abyte0;
	}

	public int bytesToInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 24) & 0xFF000000);
		return addr;
	}

	public String getByteToString(byte[] b) {
		String dstName = null;
		try {
			dstName = new String(b, "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dstName.trim();
	}

	public byte[] getStringToByte(String s) {
		try {
			return s.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
 	 }

	public int findHeader(byte[] head) {
		if (119 == head[0])
			;// 0x77
		else
			return -1;
		if (-120 == head[1])
			;// 0x88
		else
			return -1;
		if (85 == head[2])
			;// 0x55
		else
			return -1;
		switch (head[3]) {
		case -86:// 0xaa -- one channel
			return 0; // find head
		}
		return -1;
	}

	public int findPackHead(DataInputStream in) {
		try {
			int i = 0;
			_find: while (i != 4) {
				if (in.readByte() == 119) {
					i = 1;// 0x77
				} else {
					System.out.println("ee");
					continue _find;
				}
				if (in.readByte() == -120) {
					i = 2;// 0x88
				} else {
					System.out.println("ee");
					continue _find;
				}
				if (in.readByte() == 85) {
					i = 3;// 0x55
				} else {
					System.out.println("ee");
					continue _find;
				}
				if (in.readByte() == -86) {
					i = 4;// 0xaa
				} else {
					System.out.println("ee");
					continue _find;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("find head error!");
			e.printStackTrace();
		}
		return 0;
	}

	/***
	 * �����л��ķ�ʽ�Ѷ���ת��Ϊ�ֽ����� ��2�����⣺ 1 ����ĳ�Ա˳�����������ǰ����ڴ�Ĵ�С����� 2 �������л�������ַ������β��������
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = new byte[128];
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}

	/***
	 * ������Ӧ�ó����Ŀ¼
	 */
	public static void mkdirH264() {
		String path=picturePath;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			Log.i(TAG, "make directory:"+path);
		}
	}

	public static void creatVideoFile(String fileName) {
		File file = new File(picturePath + "/" + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage() + "createNewFile error ! path = "
						+ rootPath + "/" + fileName);
			}
		}
	}

	/***
	 * ��������д���ļ�
	 * @deprecated
	 * @param inputStream
	 * @param filePath
	 */
	public static void writeToFileFromByte(byte buffer[], String filePath) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filePath,
					true);
			fileOutputStream.write(buffer);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG,
					"writeToFileFromByte : FileNotFoundException"
							+ e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, "writeToFileFromByte : IOException" + e.getMessage());
		}
	}

	public static String setViedoFileNameBySysTime(int channel) {
		String str = "通道ͨ" + String.valueOf(channel) + "_";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date curDate = new Date(System.currentTimeMillis());
		str = str + formatter.format(curDate) + ".PNG";

		fileName = str;
		return str;
	}
	 /** 
     * 保存图片到sdcard中 
     * @param pBitmap 
     */  
    public static boolean savePic(Bitmap pBitmap,String strName)  
    {  
      FileOutputStream fos=null;  
      try {  
        fos=new FileOutputStream(strName);  
        if(null!=fos)  
        {  
            pBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);  
            fos.flush();  
            fos.close();  
            return true;  
        }  
          
    } catch (FileNotFoundException e) {  
        e.printStackTrace();  
    }catch (IOException e) {  
        e.printStackTrace();  
    }  
      return false;  
    }   
	public static String getFilePath() {
		return rootPath + "/" + fileName;
	}
}
