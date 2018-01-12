package cn.javava.shenma.motordrv;

public class clsErrorConst {

	/**
	 * 没有错误
	 */
	public static final int MDB_ERR_NO_ERR= 0;

	/**
	 * 无效的电机索引
	 */
	public static final int MDB_ERR_OVER_INDEX=1;
	//MDB_ERR_REPEAT_INIT,
	//MDB_ERR_ROUND_DETECT_IO_ERR_L,
	//MDB_ERR_ROUND_DETECT_IO_ERR_H,
	/**
	 * 电机断路
	 */
	public static final int MDB_ERR_MOTOR_DRIVER_BREAK=4;
	/**
	 * 电机短路或者电流过大
	 */
	public static final int	MDB_ERR_MOTOR_DRIVER_SHORT=5;
	/**
	 * 电机转动超时
	 */
	public static final int	ERR_MOTOR_ROUND_OVER_TIME=6;

	/**
	 * 电路板返回数据超时
	 */
	public static final int	MDB_ERR_TIME_OVER=47;

	/**
	 *  电路板返回数据起始标志错误
	 */
	public static final int	MDB_ERR_START_FLG=50;

	/**
	 *  电路板返回数据长度过长
	 */
	public static final int	MDB_ERR_DATA_OVER=51;
	/**
	 * 数据帧校验错误
	 */
	public static final int	MDB_ERR_CRC=46;
	/**
	 * 接收的数据帧命令错误
	 */
	public static final int	MDB_ERR_CMD_ERROR=49;

	/**
	 * 返回的数据帧地址错误
	 */
	public static final int	MDB_ERR_ADDR=52;

	/**
	 * 返回的帧序号错误
	 */
	public static final int	MDB_ERR_FID=53;

	/**
	 * 返回的版本号错误
	 */
	public static final int	MDB_ERR_VER=54;

	/**
	 * 没有检测到商品跌落
	 */
	public static final int	MDB_ERR_GOODS_NOT_DROP=77;
	/**
	 * 电路板返回数据错误次数过多
	 */
	public static final int	MDB_ERR_RET_ERROR=0xfa;
	/**
	 * 未知错误
	 */
	public static final int	MDB_ERR_UNKOWEN=0xfb;
	/**
	 * 串口打开失败
	 */
	public static final int	MDB_ERR_CANT_OPEN=0xfc;
//
//
	/**
	 * 串口关闭失败
	 */
	public static final int MDB_ERR_CANT_CLOSE=0xfd;
//			MDB_ERR_NO_SLOT_IS_OK           =0xfe,
//			MDB_ERR_NO_BUTTON_DOWN

}
