package cn.javava.shenma.motordrv.para;

public class clsTransforPara  extends clsPara
{
	/**
	 * 货道编号
	 */
	private int SlotId;
	/**
	 * 货道类型
	 */
	private int SlotType;
	/**
	 * 出货过程中是否检查掉货检测传感器结果
	 */
	private boolean UserInputSensor;

	/**
	 *  出货状态
	 */
	private int MotorState;


	/**
	 * 获取货道编号
	 * @return 货道编号
	 */
	public int getSlotId() {
		return SlotId;
	}
	/**
	 * 设置货道编号
	 * @param slotId 货道编号
	 */
	public void setSlotId(int slotId) {
		SlotId = slotId;
	}
	/**
	 * 获取货道类型
	 * @return 货道类型
	 */
	public int getSlotType() {
		return SlotType;
	}
	/**
	 * 设置货道类型
	 * @param slotType 货道类型，可以取clsconst.SLOT_TYPE_MOTOR ,clsconst.SLOT_TYPE_LOCK,clsconst.SLOT_TYPE_NONE
	 */
	public void setSlotType(int slotType) {
		SlotType = slotType;
	}
	/**
	 * 获取是否启用掉货检测
	 * @return 是否启用掉货检测
	 */
	public boolean isUserInputSensor() {
		return UserInputSensor;
	}
	/**
	 * 设置是否启用掉货检测
	 * @param userInputSensor 是否使用掉货检测
	 */
	public void setUserInputSensor(boolean userInputSensor) {
		UserInputSensor = userInputSensor;
	}
	/**
	 * 获取出货状态
	 * @return 出货状态
	 */
	public int getMotorState() {
		return MotorState;
	}
	/**
	 * 设置出货状态
	 * @param motorState 出货状态
	 */
	public void setMotorState(int motorState) {
		MotorState = motorState;
	}

}
