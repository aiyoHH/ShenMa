package cn.javava.shenma.act;


import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.VideoView;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.fragment.RechargeFragment;
import cn.javava.shenma.interf.OnLoadingListener;

/**
 * Created by aiyoRui on 2018/1/10.
 */

public class Test2Activity extends BaseActivity implements OnLoadingListener {

    @BindView(R.id.videoView)
    VideoView myVideoView;



    String videoUrl="http://v.mifile.cn/b2c-mimall-media/53fc775dd6b29ecd8df3e2ea35129766.mp4";

    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }




    @Override
    protected void initEventAndData() {


//        myVideoView.setVideoPath(videoUrl);
//        myVideoView.start();
//        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setVolume(0f,0f);
//                mp.start();
//                mp.setLooping(true);
//
//            }
//        });
//
//        myVideoView
//                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        myVideoView.setVideoPath(videoUrl);
//                        myVideoView.start();
//
//                    }
//                });


        RechargeFragment rechargeFragment = RechargeFragment.getInstance("none");
        rechargeFragment.addOnDdismissListener(new RechargeFragment.onDismissListener() {
            @Override
            public void onDisMiss() {

            }
        });

        rechargeFragment.setCancelable(false);
        rechargeFragment.show(getFragmentManager(),"");


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onFinish(String accessToken) {

    }



   /* @OnClick(R.id.push_good)*/
//    void pushTest() {
//
//        //ScanLoginFragment scanLoginFragment = ScanLoginFragment.getInstance("meiyou");
//       // scanLoginFragment.setCancelable(false);
//       // scanLoginFragment.show(getFragmentManager(), "GameResultDialog");
//
//
////        openEdv();
//
//        MotorDrvUtil.openMotor(this,4);
//
//    }




//    private void openEdv(){
//        String comid="/dev/ttyS3";
//
//        int ret=YtMainBoard.getInstance().EF_OpenDev(comid, 9600);
//
//        if(ret== clsErrorConst.MDB_ERR_NO_ERR)
//        {
//            //label_tips.setText(comid+"打开成功");
//            txt_data.append(String.format("  打开成功\r\n"));
//
//            Toast.makeText(getApplicationContext(), "打开成功",  Toast.LENGTH_SHORT).show();
//
//            pushRight();
//        }
//        else
//        {
//            //label_tips.setText(comid+"打开失败");
//            txt_data.append(String.format("  打开失败\r\n"));
//            Toast.makeText(getApplicationContext(), "打开失败",  Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void pushRight(){
//        final clsTransforPara para = new clsTransforPara();
//        int addr = clsToolBox.ParseInt("00");
//        int slotid = clsToolBox.ParseInt("4");
//        para.setAddr(addr);
//        para.setSlotId(slotid % clsConst.MAX_SLOT_COUNT_PER_BRD);
//        para.setSlotType(clsConst.SLOT_TYPE_LOCK);
//
//        int ret = YtMainBoard.getInstance().EF_Transfor(para);
//        txt_data.append(clsToolBox.getTimeLongString(new Date()) + "  ret=" + ret + "\r\n");
//        if (ret == 0) {
//
//            UIUtils.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    clsTransforPoll paraPoll = new clsTransforPoll();
//                    paraPoll.setAddr(para.getAddr());
//                    int ret = YtMainBoard.getInstance().EF_TransforPoll(paraPoll);
//                    if (ret == 0) {
//                        //显示电流
//                        if (paraPoll.getMotorState() == clsConst.STATE_MOTOR_MOTOR_RUNNING_END) {
//
//                            if (paraPoll.getMotorFault() == 0) {
//                                txt_data.append(String.format("  %d号货道，出货成功\r\n", para.getSlotId()));
//                            } else {
//                                txt_data.append(String.format("  %d号货道，出货失败,故障码%s\r\n", para.getSlotId(), paraPoll.getMotorFault()));
//                            }
//                            UIUtils.getMainHandler().removeCallbacks(this);
//                        }
//                    } else {
//                        txt_data.append(String.format("  出货Poll指令发送失败\r\n"));
//                        if (err_count++ > 3) {
//
//                            UIUtils.getMainHandler().removeCallbacks(this);
//                        }
//                    }
//                }
//            }, 200);
//        } else {
//            txt_data.append(String.format("  出货指令发送失败\r\n"));
//        }
//    }
//
//    @Override
//    public void onFinish(String accessToken) {
//
//    }
}
