package cn.javava.shenma.motordrv.para;

public class clsSetRelayPara extends clsPara
{

	/**
	 * 继电器编号
	 */
	private int RelayId;
	/**
	 * 继电器状态
	 */
	private int state;
	/**
	 * 获取继电器编号
	 * @return 继电器编号
	 */
	public int getRelayId() {
		return RelayId;
	}
	/**
	 * 设置继电器编号
	 * @param relayId 继电器编号
	 */
	public void setRelayId(int relayId) {
		RelayId = relayId;
	}
	/**
	 * 获取继电器状态
	 * @return 继电器状态
	 */
	public int getState() {
		return state;
	}
	/**
	 * 设置继电器状态
	 * @param state 继电器状态
	 */
	public void setState(int state) {
		this.state = state;
	}


}
