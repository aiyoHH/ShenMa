package cn.javava.shenma.motordrv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android_serialport_api.SerialPort;
import cn.javava.shenma.motordrv.para.clsDoorSensorPara;
import cn.javava.shenma.motordrv.para.clsHandShakePara;
import cn.javava.shenma.motordrv.para.clsInputSensorPara;
import cn.javava.shenma.motordrv.para.clsPcPollPara;
import cn.javava.shenma.motordrv.para.clsSetRelayPara;
import cn.javava.shenma.motordrv.para.clsTransforPara;
import cn.javava.shenma.motordrv.para.clsTransforPoll;


/**
 * 实现了所有和下位机通信的函数
 *
 * @author Administrator
 *出货过程：<br>
 *1、发送出货指令，调用EF_Transfor函数，等待200ms<br>
 *2、发送出货查询指令，调用EF_PcPoll函数，等待200ms，再次调用EF_PcPoll函数，直到通过参数类返回的电机状态值等于10<br>
 *3、整个过程出货完成<br>
 */
public class YtMainBoard
{

	@SuppressWarnings("unused")
	private static final int  MAX_SLOT_COUNT= 60;

	private static final byte STRATSIGN0= 'W';

	private static final byte STRATSIGN1= 'X';
	private static final byte VER= (byte)0x66;



	private static short fid=0;


	private static short crc16_update(short crc, byte a)
	{
		int i;
		crc = (short)(crc ^a);
		for (i = 0; i < 8; ++i)
		{
			if ((crc & 1)!=0)
			{
				crc = (short)((short)((crc >> 1)) ^ (short)0xA001);
			}
			else
			{
				crc = (short)(crc >> 1);
			}
		}
		return crc;
	}


	private static short Slot_GetChk(byte[] bufData, int buflen)
	{
		int i=0;
		byte crc=0;
		for(i=0;i<buflen;i++)
		{
			crc+=bufData[i];
		}
		return crc;
	}


	private FileOutputStream mOutputStream;

	private FileInputStream mInputStream;

	private SerialPort sp;

	/**
	 * 打开通信串口
	 * @param comid 串口名称
	 * @param baudrate 波特率
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_OpenDev(String comid,int baudrate)
	{
		/*打开串口*/

		boolean isopen=false;
		try
		{
			sp=new SerialPort(new File(comid),9600,0);

			mOutputStream=(FileOutputStream) sp.getOutputStream();
			mInputStream=(FileInputStream) sp.getInputStream();

			isopen=true;
		}
		catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(isopen)
		{
			return clsErrorConst.MDB_ERR_NO_ERR;
		}
		else
		{
			return clsErrorConst. MDB_ERR_CANT_OPEN;
		}
	}


	/**
	 * 关闭已经打开的通信串口
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_CloseDev()
	{
		boolean isclose=false;
		try
		{
			if(sp!=null)
			{
				sp.close();
				isclose=true;
				mOutputStream=null;
				mInputStream=null;
			}

		} catch (SecurityException e)
		{
			e.printStackTrace();
		}

		if(isclose)
		{
			return clsErrorConst.MDB_ERR_NO_ERR;
		}
		else
		{
			return clsErrorConst.MDB_ERR_CANT_CLOSE;
		}
	}

	clsFrame current_frame=null;

	private int SendFrame(byte cmd,byte[] data,short len, byte addr)
	{

		current_frame=new clsFrame();

		byte[] buf=new byte[4096];
		int i=0;
		short chk;
		buf[i++]=STRATSIGN0;
		buf[i++]=STRATSIGN1;
		buf[i++]=addr;
		buf[i++]=VER;
		fid++;
		buf[i++]=(byte)(fid&0x00ff);
		buf[i++]=(byte)((fid>>8)&0x00ff);

		buf[i++]=cmd;

		buf[i++]=(byte)(len);

		if(len!=0)
		{
			System.arraycopy(data, 0, buf, i, len);
			i+=len;
		}
		chk= Slot_GetChk(buf,i);

		buf[i++]=(byte)(chk);

		current_frame.setData(buf);
		current_frame.setLen((short)i);
		current_frame.setAddr(addr);
		current_frame.setCmd(cmd);
		current_frame.setFid(fid);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		SendData(current_frame);
		return clsErrorConst.MDB_ERR_NO_ERR;
	}



	private void SendData(clsFrame frame2) {

		if(frame2!=null)
		{
			try {
				mInputStream.skip(mInputStream.available());
				mOutputStream.write(frame2.getData(), 0, frame2.getLen());

				try {
					Thread.sleep(frame2.getLen());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(clsToolBox.getTimeLongString(new Date())+" SEND:");
				clsToolBox.printX(frame2.getData(),frame2.getLen());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}



	private static final int FIX_LENGTH =9;
	private static final int INDEX_FRAME_DATA_LEN=7;
	private static final int INDEX_FRAME_DATA=8;
	private static final int INDEX_FRAME_CMD=6;
	private static final int INDEX_FRAME_FID=4;
	private static final int INDEX_FRAME_VER=3;
	private static final int INDEX_FRAME_ADDR=2;


	private void delayTime(int max_time,int need_len,FileInputStream is) throws IOException
	{
		int over_time=0;

		while(is.available()<need_len)
		{
			//System.out.println("len="+is.available());
			try {
				Thread.sleep(50);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			over_time+=50;
			if(max_time<over_time)
			{
				System.out.println("overtime");
				break;
			}
		}
	}
//
//	private int ReceFrame(clsFrame r_data)
//	{
//		byte[] b=new byte[2048];
//
//		int err=clsErrorConst.MDB_ERR_NO_ERR;
//		int rlen=0;
//		int _max_wait_time=100;
//
//		try
//		{
//			while(r_data.getTtl()>0)
//			{
//				r_data.subTtl();
//				rlen=0;
//				err=clsErrorConst.MDB_ERR_NO_ERR;
//
//				_max_wait_time=r_data.getWaitTime();
//
//				delayTime(_max_wait_time,FIX_LENGTH,mInputStream);
//				if(mInputStream.available()<FIX_LENGTH)
//				{
//
//				}
//
//				if(err==clsErrorConst.MDB_ERR_NO_ERR)
//				{
//					rlen=mInputStream.read(b, 0, FIX_LENGTH);
//
//					if((b[0]!=STRATSIGN0)||(b[1]!=STRATSIGN1))
//					{
//						err=clsErrorConst.MDB_ERR_START_FLG;
//					}
//					else
//					{
//						int len=clsToolBox.little_byte2short(b, INDEX_FRAME_DATA_LEN)&0x0000ffff;
//						//clsToolBox.printX(b,rlen);
//						//System.out.println(len);
//						/*等待若干时间*/
//
//						if(len>=1500)
//						{
//							err=clsErrorConst.MDB_ERR_DATA_OVER;
//						}
//						else
//						{
//							long end_tick=System.currentTimeMillis()+2000;
//							int need_len=len+FIX_LENGTH-rlen;
//							while(need_len>0)
//							{
//								delayTime(need_len<100?100:need_len,need_len>500?500:need_len,mInputStream);
//								if(mInputStream.available()>0)
//								{
//									rlen+=mInputStream.read(b,rlen,need_len);
//								}
//								else
//								{
//									if(end_tick<System.currentTimeMillis())
//									{
//										System.out.println(end_tick-System.currentTimeMillis());
//										err=clsErrorConst.MDB_ERR_TIME_OVER;
//										break;
//									}
//								}
//								need_len=len+FIX_LENGTH-rlen;
//							}
//						}
//
//						if(err==clsErrorConst.MDB_ERR_NO_ERR)
//						{
//							if(Slot_GetChk(b,rlen)!=0)
//							{
//								err= clsErrorConst.MDB_ERR_CRC;
//							}
//						}
//					}
//				}
//
//				if(b[INDEX_FRAME_CMD]==clsCmdConst.CMD_PC_MES_ERR)
//				{
//					err= clsErrorConst.MDB_ERR_RET_ERROR;
//				}
//
//				if(err!=clsErrorConst.MDB_ERR_NO_ERR)
//				{
//					//mInputStream.skip(mInputStream.available());
//
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//
//						e.printStackTrace();
//					}
//
//					System.out.println("Resend:"+err);
//					SendData(current_frame);
//
//					continue;
//				}
//				else
//				{
//					break;
//				}
//
//			}
//
//			//if(r_data.getTtl()<=0)
//			//{
//			//	err=clsErrorConst.MDB_ERR_TIME_OVER;
//			//}
//
//			if(err==clsErrorConst.MDB_ERR_NO_ERR)
//			{
//				r_data.setLen(rlen);
//				r_data.setData(b);
//			}
//
//		}
//		catch (IOException e)
//		{
//			err=clsErrorConst.MDB_ERR_UNKOWEN;
//			e.printStackTrace();
//
//		}
//
//		return err;
//	}
//


	private int ReceFrame(clsFrame r_data)
	{
		byte[] b=new byte[2048];

		int err=clsErrorConst.MDB_ERR_NO_ERR;
		int rlen=0;
		int _max_wait_time=100;

		try
		{
			while(r_data.getTtl()>0)
			{
				r_data.subTtl();
				rlen=0;
				err=clsErrorConst.MDB_ERR_NO_ERR;

				_max_wait_time=r_data.getWaitTime();

				delayTime(_max_wait_time,FIX_LENGTH,mInputStream);
				int current_len=mInputStream.available();
				if(current_len==0)
				{
					err=clsErrorConst.MDB_ERR_TIME_OVER;
				}

				if(err==clsErrorConst.MDB_ERR_NO_ERR)
				{
					rlen=mInputStream.read(b, 0, FIX_LENGTH);

					if((b[0]!=STRATSIGN0)||(b[1]!=STRATSIGN1))
					{
						err=clsErrorConst.MDB_ERR_START_FLG;
					}
					else if(b[INDEX_FRAME_ADDR]!=current_frame.getAddr())
					{
						err=clsErrorConst.MDB_ERR_ADDR;
					}
					else if(b[INDEX_FRAME_VER]!=VER)
					{
						err=clsErrorConst.MDB_ERR_VER;
					}
					else if(clsToolBox.little_byte2short(b, INDEX_FRAME_FID)!=(short)current_frame.getFid())
					{
						err=clsErrorConst.MDB_ERR_VER;
					}
					else
					{
						int len=b[INDEX_FRAME_DATA_LEN]&0x000000ff;
						//clsToolBox.printX(b,rlen);
						//System.out.println(len);
						/*等待若干时间*/

						if(len>=256)
						{
							err=clsErrorConst.MDB_ERR_DATA_OVER;
						}
						else
						{
							long end_tick=System.currentTimeMillis()+2000;
							int need_len=len+FIX_LENGTH-rlen;
							while(need_len>0)
							{
								delayTime(need_len<50?50:need_len,need_len>500?500:need_len,mInputStream);
								if(mInputStream.available()>0)
								{
									rlen+=mInputStream.read(b,rlen,need_len);
								}
								else
								{
									if(end_tick<System.currentTimeMillis())
									{
										//System.out.println(end_tick-System.currentTimeMillis());
										err=clsErrorConst.MDB_ERR_TIME_OVER;
										break;
									}
								}
								need_len=len+FIX_LENGTH-rlen;
							}
						}

						if(err==clsErrorConst.MDB_ERR_NO_ERR)
						{
							if(r_data.getCmd()!=b[INDEX_FRAME_CMD])
							{
								err= clsErrorConst.MDB_ERR_CMD_ERROR;
							}
						}

						if(err==clsErrorConst.MDB_ERR_NO_ERR)
						{
//							System.out.println("rlen="+rlen);
//							System.out.println("chk="+Slot_GetChk(b,rlen-1)+",b[rlen]="+b[rlen]);
//							clsToolBox.printX(b,rlen);
							if((byte)Slot_GetChk(b,rlen-1)!=b[rlen-1])
							{
								err= clsErrorConst.MDB_ERR_CRC;
							}
						}
					}

					if(b[INDEX_FRAME_CMD]==clsConst.CMD_PC_MES_ERR)
					{
						err= clsErrorConst.MDB_ERR_RET_ERROR;
					}
				}


				if(err!=clsErrorConst.MDB_ERR_NO_ERR)
				{
					//mInputStream.skip(mInputStream.available());

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}

					System.out.println("Resend:"+err);
					SendData(current_frame);

					continue;
				}
				else
				{
					break;
				}

			}

			//if(r_data.getTtl()<=0)
			//{
			//	err=clsErrorConst.MDB_ERR_TIME_OVER;
			//}

			if(err==clsErrorConst.MDB_ERR_NO_ERR)
			{
				r_data.setLen((byte)rlen);
				r_data.setData(b);
				System.out.println(clsToolBox.getTimeLongString(new Date())+" RECE:");
				clsToolBox.printX(r_data.getData(),r_data.getLen());
			}

		}
		catch (IOException e)
		{
			err=clsErrorConst.MDB_ERR_UNKOWEN;
			e.printStackTrace();

		}

		return err;
	}




	private static YtMainBoard instance=null;

	public static YtMainBoard getInstance() {

		if(instance==null)
		{
			instance=new YtMainBoard();


		}
		return instance;
	}


	/**
	 * CMD_PC_LNK=0x01<br>
	 * 上位机与下位机握手
	 * @param para 用于返回参数的类
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_HandShake(clsHandShakePara para)
	{

		int e;

		byte cmd=(byte)clsConst.CMD_PC_LNK;
		byte[] s_data=new byte[20];
		int i=0;
		s_data[i++]=(byte)0;
		SendFrame(cmd, s_data, (short)i,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			int j=INDEX_FRAME_DATA;
			j++;

			para.setVer(data[j++]);
			para.setRowCount(data[j++]);
			para.setColCount(data[j++]);
		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}

		return e;
	}

	/**
	 * CMD_SLOT_DRIVER_POLL=0x02;
	 * 出货指令，通知驱动板出货
	 * @param para 出货参数类
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_Transfor(clsTransforPara para)
	{
		int e;
		byte cmd=(byte)clsConst.CMD_SLOT_DRIVER_TRANSFOR;
		byte[] s_data=new byte[20];
		int i=0;
		s_data[i++]=(byte)para.getSlotId();
		s_data[i++]=(byte)para.getSlotType();
		s_data[i++]=(byte)(para.isUserInputSensor()?1:0);

		SendFrame(cmd, s_data, (short)i,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			int j=INDEX_FRAME_DATA;
			para.setMotorState(data[j++]);
		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}
		return e;
	}

	/**
	 * CMD_SET_RELAY=0x15
	 * 打开或者关闭继电器
	 * @param para 继电器设置类,通过该类设置继电器的参数
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_SetRelay(clsSetRelayPara para)
	{
		int e;
		byte cmd=(byte)clsConst.CMD_SET_RELAY;
		byte[] s_data=new byte[20];
		int i=0;
		s_data[i++]=(byte)para.getRelayId();
		s_data[i++]=(byte)para.getState();

		SendFrame(cmd, s_data, (short)i,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			int j=INDEX_FRAME_DATA;

		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}
		return e;
	}

	/**
	 * 获取掉货检测信号
	 * @param para 掉货检测信号参数类，通过该参数获取传感器信号值
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_TestInputSensor(clsInputSensorPara para)
	{
		int e;
		byte cmd=(byte)clsConst.CMD_PC_TEST_INPUT_SENSOR;
		int i=0;
		SendFrame(cmd, null, (short)i,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			int j=INDEX_FRAME_DATA;
			para.setInputSensorVal(data[j++]);
		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}
		return e;
	}
	/**
	 * CMD_SLOT_DRIVER_POLL=0x02;
	 * 出货过程，查询出货状态
	 * @param para 出货查询参数
	 * @return error code,0表示没有错误，非0表示有错误，错误代码请参见  {@link clsErrorConst}
	 */
	public int EF_TransforPoll(clsTransforPoll para)
	{
		int e;
		byte cmd=(byte)clsConst.CMD_SLOT_DRIVER_POLL;
		SendFrame(cmd, null, (short)0,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			int j=INDEX_FRAME_DATA;
			para.setMotorState(data[j++]);

			para.setMotorFault(data[j++]);
			para.setMotorCurrent(clsToolBox.big_byte2short(data, j));
			j+=2;

		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}
		return e;
	}


	/**
	 * CMD_POLL=0X01<br>
	 * 查询驱动板状态（暂时没有使用）
	 * @param para
	 * @return
	 */
	private int EF_PcPoll(clsPcPollPara para)
	{
		int e;
		int i=0;

		byte cmd=(byte)clsConst.CMD_PC_POLL;
		SendFrame(cmd, null, (short)0,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			i=INDEX_FRAME_DATA;

		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}

		return e;
	}


	public int EF_TestDoorSensor(clsDoorSensorPara para) {
		int e;
		byte cmd=(byte)clsConst.CMD_GET_DOOR_SW_STATE;
		int i=0;
		byte[] s=clsToolBox.intToByte(para.getIndex());

		i=s.length;
		SendFrame(cmd, s, (short)i,(byte)para.getAddr());

		clsFrame r_data=new clsFrame();
		r_data.setCmd(cmd);
		r_data.setTtl(5);
		e=ReceFrame(r_data);

		if(e==clsErrorConst.MDB_ERR_NO_ERR)
		{
			byte[] data=r_data.getData();
			int j=INDEX_FRAME_DATA;
			para.setSensorVal(data[j++]);
		}
		else if(e==clsErrorConst.MDB_ERR_TIME_OVER)
		{

		}
		else  if(e==clsErrorConst.MDB_ERR_CRC)
		{

		}
		else
		{

		}
		return e;
	}
}
