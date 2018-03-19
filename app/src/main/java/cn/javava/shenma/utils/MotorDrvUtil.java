package cn.javava.shenma.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;

import cn.javava.shenma.motordrv.YtMainBoard;
import cn.javava.shenma.motordrv.clsConst;
import cn.javava.shenma.motordrv.clsErrorConst;
import cn.javava.shenma.motordrv.clsToolBox;
import cn.javava.shenma.motordrv.para.clsTransforPara;
import cn.javava.shenma.motordrv.para.clsTransforPoll;

/**
 * Created by aiyoRui on 2018/3/19.
 */

public class MotorDrvUtil {
    public static void openMotor(Context context, int slotid){
        String comid="/dev/ttyS3";

        int ret= YtMainBoard.getInstance().EF_OpenDev(comid, 9600);

        if(ret== clsErrorConst.MDB_ERR_NO_ERR)
        {

            Toast.makeText(context, "打开成功",  Toast.LENGTH_SHORT).show();
            pushRight(context,slotid);
        }
        else
        {

            Toast.makeText(context, "打开失败",  Toast.LENGTH_SHORT).show();
        }
    }
    private static int err_count;
    private static  void pushRight(final Context context, int id){
        err_count=0;
        final clsTransforPara para = new clsTransforPara();
        int addr = clsToolBox.ParseInt("00");
        int slotid = clsToolBox.ParseInt(String.valueOf(id));
        para.setAddr(addr);
        para.setSlotId(slotid % clsConst.MAX_SLOT_COUNT_PER_BRD);
        para.setSlotType(clsConst.SLOT_TYPE_LOCK);

        int ret = YtMainBoard.getInstance().EF_Transfor(para);
        if (ret == 0) {

            UIUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    clsTransforPoll paraPoll = new clsTransforPoll();
                    paraPoll.setAddr(para.getAddr());
                    int ret = YtMainBoard.getInstance().EF_TransforPoll(paraPoll);
                    if (ret == 0) {
                        //显示电流
                        if (paraPoll.getMotorState() == clsConst.STATE_MOTOR_MOTOR_RUNNING_END) {

                            if (paraPoll.getMotorFault() == 0) {
                                Toast.makeText(context, String.format("  %d号货道，出货成功\r\n", para.getSlotId()),  Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, String.format("  %d号货道，出货失败,故障码%s", para.getSlotId()),  Toast.LENGTH_SHORT).show();
                                }
                            UIUtils.getMainHandler().removeCallbacks(this);
                        }
                    } else {
                        Toast.makeText(context, String.format("  出货Poll指令发送失败\n"),  Toast.LENGTH_SHORT).show();
                        if (err_count++ > 3) {
                            UIUtils.getMainHandler().removeCallbacks(this);
                        }
                    }
                }
            }, 200);
        } else {
            Toast.makeText(context, String.format(" 出货指令发送失败\n"),  Toast.LENGTH_SHORT).show();
        }
    }
}
