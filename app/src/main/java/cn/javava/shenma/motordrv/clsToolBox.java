package cn.javava.shenma.motordrv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class clsToolBox {

	static short s(byte[] b)
	{
		return (short)(((b[1] & 0x000000FF) << 8) | (0x000000FF & b[0]));
	}

	static short little_byte2short(byte[] b,int startindex)
	{
		return (short)(((b[startindex+1] & 0x000000FF) << 8) | (0x000000FF & b[startindex]));
	}

	static short big_byte2short(byte[] b)
	{
		return (short)(((b[0] & 0x000000FF) << 8) | (0x000000FF & b[1]));

	}

	static short big_byte2short(byte[] b,int startindex)
	{
		return (short)(((b[startindex] & 0x000000FF) << 8) | (0x000000FF & b[startindex+1]));

	}

	/**
	 * 基于位移的int转化成byte[]
	 * @param number 将要转换的数字
	 * @return 返回一个字节数组
	 */
	public static byte[] intToByte(int number) {
		byte[] abyte = new byte[4];
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[0] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[1] = (byte) ((0xff00 & number) >> 8);
		abyte[2] = (byte) ((0xff0000 & number) >> 16);
		abyte[3] = (byte) ((0xff000000 & number) >> 24);
		return abyte;
	}

	/**
	 * 取得时间字符串，形如：20090831150534
	 * @param date 日期
	 * @return 返回形如"yyyyMMddHHmmss"的字符串
	 */
	public static String getTimeLongString(java.util.Date date)
	{
		String TemString=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");
		sdf.setTimeZone(timezone);
		TemString = sdf.format(date);
		return TemString;
	}

	/**
	 * 基于位移的int转化成byte[]
	 * @param number 将要转换的数字
	 * @param abyte 目标数组
	 * @param index 存储起始索引
	 * @return 返回一个int
	 */
	public static int intToByte(int number,byte[] abyte,int index) {
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[index++] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[index++] = (byte) ((0xff00 & number) >> 8);
		abyte[index++] = (byte) ((0xff0000 & number) >> 16);
		abyte[index++] = (byte) ((0xff000000 & number) >> 24);
		return index;
	}


	/**
	 * @param number
	 * @param abyte
	 */
	public static int shortToByte(short number,byte[] abyte,int index) {
		// "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
		abyte[index++] = (byte) (0xff & number);
		// ">>"右移位，若为正数则高位补0，若为负数则高位补1
		abyte[index++] = (byte) ((0xff00 & number) >> 8);

		return index;
	}

	/**
	 *基于位移的 byte[]转化成int
	 * @param  bytes
	 * @return  number
	 */

	public static int little_byte2int(byte[] bytes) {
		int number = bytes[0] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[1] << 8) & 0xFF00);
		number |= ((bytes[2] << 16) & 0xFF0000);
		number |= ((bytes[3] << 24) & 0xFF000000);
		return number;
	}


	/**
	 * 基于位移的 byte[]转化成int
	 * @param bytes
	 * @param index
	 * @return
	 */
	public static int little_byte2int(byte[] bytes,int index) {
		int number = bytes[index+0] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[index+1] << 8) & 0xFF00);
		number |= ((bytes[index+2] << 16) & 0xFF0000);
		number |= ((bytes[index+3] << 24) & 0xFF000000);
		return number;
	}

	public static Calendar ParseDateString(String datestring)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		java.util.Date date;
		Calendar c=Calendar.getInstance();
		try {
			date = sdf.parse(datestring);
			c.setTimeInMillis(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return c;
	}


	/**
	 *基于位移的 byte[]转化成int
	 * @param   bytes
	 * @return int  number
	 */

	public static int big_byte2int(byte[] bytes) {
		int number = bytes[3] & 0xFF;
		// "|="按位或赋值。
		number |= ((bytes[2] << 8) & 0xFF00);
		number |= ((bytes[1] << 16) & 0xFF0000);
		number |= ((bytes[0] << 24) & 0xFF000000);
		return number;
	}

	public static int ParseInt(String str_sid) {
		try {
			return Integer.parseInt(str_sid);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}

	}

	/**
	 * 将一个asc数据流转化成一个字符串
	 * @param b
	 * @param start_index
	 * @return
	 */
	public static String AscByte2StringTerminalByNull(byte[] b,int start_index)
	{
		if(b==null)
		{
			return "";
		}
		int end_index=start_index;
		for(;end_index<b.length;end_index++)
		{
			if(b[end_index]==0)
			{
				break;
			}
		}


		try {
			return new String(b,start_index,end_index-start_index,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 将一个asc数据流转化成一个字符串
	 * @param b
	 * @param start_index
	 * @return
	 */
	public static String AscByte2StringByEndIndex(byte[] b,int start_index,int end_index)
	{
		if(b==null)
		{
			return "";
		}

		try {
			return new String(b,start_index,end_index-start_index,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static void printX(byte x)
	{
		System.out.print(String.format("%02X,",x));
	}

	public static void printX(byte[] b,int len) {
		int i;
		for(i=0;i<len;i++)
		{
			printX(b[i]);
		}
		System.out.println();
	}


	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取外置SD卡路径
	 * @return  应该就一条记录或空
	 */
	public static ArrayList<String> getExtSDCardPath()
	{
		ArrayList<String> lResult = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("external_sd"))
				{
					String [] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory())
					{
						lResult.add(path);
					}
				}
			}
			isr.close();
		} catch (Exception e) {
		}
		return lResult;
	}
}
