package cn.javava.shenma.motordrv.para;

/**
 * 测试下位机握手时，使用的参数对象
 * @author Administrator
 *
 */
public class clsHandShakePara extends clsPara
{


	/**
	 * 获取驱动板版本
	 * @return 驱动板版本
	 */
	public int getVer() {
		return ver;
	}

	/**
	 * 设置驱动板版本
	 * @param ver 驱动板版本
	 */
	public void setVer(int ver) {
		this.ver = ver;
	}


	/**
	 * 获取驱动板电机层数
	 * @return 驱动板电机层数
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * 设置驱动板电机层数
	 * @param rowCount 驱动板电机层数
	 */
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * 获取驱动板电机行数
	 * @return 驱动板电机行数
	 */
	public int getColCount() {
		return colCount;
	}

	/**
	 * 设置驱动板电机行数
	 * @param colCount 驱动板电机行数
	 */
	public void setColCount(int colCount) {
		this.colCount = colCount;
	}



	private int ver;


	private int rowCount;
	private int colCount;



}
