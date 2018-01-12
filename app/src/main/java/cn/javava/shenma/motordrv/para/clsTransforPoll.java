package cn.javava.shenma.motordrv.para;

public class clsTransforPoll extends clsPara {

	/**
	 * 电机运行状态
	 */
	private int motorState;
	/**
	 * 电机故障码
	 */
	private int motorFault;
	/**
	 * 电机电流
	 */
	private int motorCurrent;

	/**
	 * 获取电机运行状态,当返回结果为STATE_MOTOR_MOTOR_RUNNING_END（10） 时表示电机转动已经结束
	 * @return 返回电机运行状态
	 */
	public int getMotorState() {
		return motorState;
	}
	/**
	 * 设置电机运行状态
	 * @param motorState 电机运行状态
	 */
	public void setMotorState(int motorState) {
		this.motorState = motorState;
	}

	/**
	 * 获取电机故障码
	 * @return 电机故障码
	 */
	public int getMotorFault() {
		return motorFault;
	}
	/**
	 * 设置电机故障码
	 * @param motorFault 电机故障码
	 */
	public void setMotorFault(int motorFault) {
		this.motorFault = motorFault;
	}
	/**
	 * 获取电机电流
	 * @return 电机电流
	 */
	public int getMotorCurrent() {
		return motorCurrent;
	}
	/**
	 * 设置电机电流
	 * @param motorCurrent 电机电流
	 */
	public void setMotorCurrent(int motorCurrent) {
		this.motorCurrent = motorCurrent;
	}

}
