package cn.javava.shenma.motordrv.para;

public class clsDoorSensorPara extends clsPara
{
	/**
	 * 门开关检测值
	 */
	private int  SensorVal;


	/**
	 * 门编号，可以取0~59
	 */
	private int index;

	public int getSensorVal() {
		return SensorVal;
	}

	public void setSensorVal(int sensorVal) {
		SensorVal = sensorVal;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}




}
