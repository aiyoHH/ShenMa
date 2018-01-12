package cn.javava.shenma.motordrv;

/**
 * @author Administrator
 * 售货机
 */
public class clsConst {
	/**
	 * 错误命令代码(有使用)
	 */
	public static final int CMD_PC_MES_ERR              =0xff;
	/**
	 * 握手命令(有使用)
	 */
	public static final int CMD_PC_LNK                  =0X00;
	/**
	 * 查询从板状态命令(暂时没有使用)
	 */
	public static final int CMD_PC_POLL                 =0X01;

	/**
	 * 出货过程，查询出货状态
	 */
	public static final int CMD_SLOT_DRIVER_POLL=0x2;
	/**
	 * 出货指令
	 */
	public static final int CMD_SLOT_DRIVER_TRANSFOR=0x3;
	/**
	 * 获取掉货检测信号
	 */
	public static final int CMD_PC_TEST_INPUT_SENSOR =0x4;
	/**
	 * 打开或者关闭继电器
	 */
	public static final int CMD_SET_RELAY=0x15;
	/**
	 * 获取温度值
	 */
	public static final int CMD_GET_TEMPER=0x16;

	public static final int CMD_GET_DOOR_SW_STATE=0x19;

	public static final int CMD_ONE_SEC_TICK = 0xfc;


	public static final int CMD_OPEN_SERIAL_PORT = 0xff;
	public static final int CMD_START_OK = 0xfe;
	public static final int CMD_CHK_BRD_RET = 0xfd;



	public static final String CONTENT_FILE_DIR = "/source/";
	public static final String VEDIO_FILE_PATH = CONTENT_FILE_DIR+"v1.mov";
	public static final String IMG_FILE_PATH = CONTENT_FILE_DIR+"pic.jpg";
	public static final String CONTENT_FILE_PATH =CONTENT_FILE_DIR+ "t1.txt";

	//电机转动时，查询电机状态使用
	public static final int STATE_MOTOR_MOTOR_STOP=0;
	public static final int STATE_MOTOR_MOTOR_SELECTED=1;
	public static final int STATE_MOTOR_MOTOR_RUNNING_1=2;
	public static final int STATE_MOTOR_MOTOR_RUNNING_2=3;
	public static final int STATE_MOTOR_MOTOR_RUNNING_3=4;
	public static final int STATE_MOTOR_MOTOR_RUNNING_4=5;
	public static final int STATE_MOTOR_MOTOR_RUNNING_END=10;


	/**
	 * 电机
	 */
	public static final int SLOT_TYPE_MOTOR=0;
	/**
	 * 电磁锁
	 */
	public static final int SLOT_TYPE_LOCK=1;
	/**
	 * 没有连接
	 */
	public static final int SLOT_TYPE_NONE=255;

	/**
	 * 驱动板最大电机数量
	 */
	public static int MAX_SLOT_COUNT_PER_BRD=60;
}
