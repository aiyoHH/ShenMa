package cn.javava.shenma.act;

import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.javava.shenma.R;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.interf.IJsApi;
import cn.javava.shenma.interf.OnLoadingListener;
import cn.javava.shenma.motordrv.YtMainBoard;
import cn.javava.shenma.motordrv.clsConst;
import cn.javava.shenma.motordrv.clsErrorConst;
import cn.javava.shenma.motordrv.clsToolBox;
import cn.javava.shenma.motordrv.para.clsTransforPara;
import cn.javava.shenma.motordrv.para.clsTransforPoll;
import cn.javava.shenma.utils.UIUtils;
import wendu.dsbridge.DWebView;

/**
 * Created by aiyoRui on 2018/1/10.
 */

public class TestActivity extends BaseActivity implements OnLoadingListener {

    @BindView(R.id.txt_data)
    TextView txt_data;
    @BindView(R.id.test_webview)
    DWebView webView;

    int err_count=0;
    private static final String loginUrl="https://open.weixin.qq.com/connect/qrconnect?appid=wx7b14057ca00d4350&redirect_uri=http%3A%2F%2Fauthc.javava.cn%2FscanQRCodeComplete&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";


    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEventAndData() {
        txt_data.setMovementMethod(ScrollingMovementMethod.getInstance());

        webView.setJavascriptInterface(new IJsApi(this));
        webView.clearCache(true);
        //webView.setBackgroundColor(0);
        webView.loadUrl(loginUrl);

    }

    @OnClick(R.id.push_good)
    void pushTest() {

        //ScanLoginFragment scanLoginFragment = ScanLoginFragment.getInstance("meiyou");
       // scanLoginFragment.setCancelable(false);
       // scanLoginFragment.show(getFragmentManager(), "GameResultDialog");

        openEdv();

    }


    private void openEdv(){
        String comid="/dev/ttyS1";

        int ret=YtMainBoard.getInstance().EF_OpenDev(comid, 9600);

        if(ret== clsErrorConst.MDB_ERR_NO_ERR)
        {
            //label_tips.setText(comid+"打开成功");
            txt_data.append(String.format("  打开成功\r\n"));

            Toast.makeText(getApplicationContext(), "打开成功",  Toast.LENGTH_SHORT).show();

            pushRight();
        }
        else
        {
            //label_tips.setText(comid+"打开失败");
            txt_data.append(String.format("  打开失败\r\n"));
            Toast.makeText(getApplicationContext(), "打开失败",  Toast.LENGTH_SHORT).show();
        }
    }

    private void pushRight(){
        final clsTransforPara para = new clsTransforPara();
        int addr = clsToolBox.ParseInt("0");
        int slotid = clsToolBox.ParseInt("0");
        para.setAddr(addr);
        para.setSlotId(slotid % clsConst.MAX_SLOT_COUNT_PER_BRD);
        para.setSlotType(clsConst.SLOT_TYPE_LOCK);

        int ret = YtMainBoard.getInstance().EF_Transfor(para);
        txt_data.append(clsToolBox.getTimeLongString(new Date()) + "  ret=" + ret + "\r\n");
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
                                txt_data.append(String.format("  %d号货道，出货成功\r\n", para.getSlotId()));
                            } else {
                                txt_data.append(String.format("  %d号货道，出货失败,故障码%s\r\n", para.getSlotId(), paraPoll.getMotorFault()));
                            }
                            UIUtils.getMainHandler().removeCallbacks(this);
                        }
                    } else {
                        txt_data.append(String.format("  出货Poll指令发送失败\r\n"));
                        if (err_count++ > 3) {

                            UIUtils.getMainHandler().removeCallbacks(this);
                        }
                    }
                }
            }, 200);
        } else {
            txt_data.append(String.format("  出货指令发送失败\r\n"));
        }
    }

    @Override
    public void onFinish(String accessToken) {

    }
}
