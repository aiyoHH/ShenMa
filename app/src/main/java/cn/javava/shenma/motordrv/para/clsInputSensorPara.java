package cn.javava.shenma.motordrv.para;

public class clsInputSensorPara extends clsPara
{
	/**
	 * 传感器检测结果
	 */
	private int  InputSensorVal;

	/**
	 * 获取传感器检测结果，等于0表示传感器没有被触发，等于1表示传感器已经被触发
	 * @return 返回传感器检测结果
	 */
	public int getInputSensorVal() {
		return InputSensorVal;
	}

	/**
	 * 设置传感器检测结果
	 * @param inputSensorVal 传感器检测结果
	 */
	public void setInputSensorVal(int inputSensorVal) {
		InputSensorVal = inputSensorVal;
	}

}
