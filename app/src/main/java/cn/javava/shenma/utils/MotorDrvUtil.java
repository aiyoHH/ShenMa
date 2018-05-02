package cn.javava.shenma.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import cn.javava.shenma.bean.NoneDataBean;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.motordrv.YtMainBoard;
import cn.javava.shenma.motordrv.clsConst;
import cn.javava.shenma.motordrv.clsErrorConst;
import cn.javava.shenma.motordrv.clsToolBox;
import cn.javava.shenma.motordrv.para.clsTransforPara;
import cn.javava.shenma.motordrv.para.clsTransforPoll;
import rx.Subscriber;

/**
 * Created by aiyoRui on 2018/3/19.
 */

public class MotorDrvUtil {
    public static void openMotor(Context context, int slotid,int goodId) {
        String comid = "/dev/ttyS3";

        int ret = YtMainBoard.getInstance().EF_OpenDev(comid, 9600);

        if (ret == clsErrorConst.MDB_ERR_NO_ERR) {
            pushRight(context, (slotid-1));
            registerGood(String.valueOf(goodId));
        } else {

            Toast.makeText(context, "打开失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static int err_count;

    private static void pushRight(final Context context, final int id) {
        err_count = 0;
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
                                Toast.makeText(context, String.format("  %d号货道，出货成功\r\n", para.getSlotId()), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, String.format("  %d号货道，出货失败,故障码", para.getSlotId()), Toast.LENGTH_SHORT).show();
                            }
                            UIUtils.getMainHandler().removeCallbacks(this);
                        }
                    } else {
                        Toast.makeText(context, String.format("  出货Poll指令发送失败\n"), Toast.LENGTH_SHORT).show();
                        if (err_count++ > 3) {
                            UIUtils.getMainHandler().removeCallbacks(this);
                        }
                    }
                }
            }, 200);
        } else {
            Toast.makeText(context, String.format(" 出货指令发送失败\n"), Toast.LENGTH_SHORT).show();
        }
    }

    public static void pushAllCase(final Context context) {
        String comid = "/dev/ttyS3";

        int ret = YtMainBoard.getInstance().EF_OpenDev(comid, 9600);
        if (ret == clsErrorConst.MDB_ERR_NO_ERR) {

            new CountDownTimer(60 * 1000, 5000) {
                int current;

                @Override
                public void onTick(long millisUntilFinished) {
                    pushRight(context, current++);
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }


    public static void registerGood(String id) {
        HttpHelper.getInstance().registerGood(new Subscriber<NoneDataBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NoneDataBean noneDataBean) {

            }
        }, id);

    }


}
