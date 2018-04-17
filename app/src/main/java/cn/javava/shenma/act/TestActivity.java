package cn.javava.shenma.act;


import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.interf.IJsApi;
import cn.javava.shenma.interf.OnLoadingListener;

/**
 * Created by aiyoRui on 2018/1/10.
 */

public class TestActivity extends BaseActivity implements OnLoadingListener {

    @BindView(R.id.webView)
    WebView mWebView;


    private static final String loginUrl="https://open.weixin.qq.com/connect/qrconnect?appid=wx7b14057ca00d4350&redirect_uri=http%3A%2F%2Fauthc.javava.cn%2FscanQRCodeComplete&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";

    private static final String webViewUrl="http://wawayun.javava.cn/demo.html";

    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }




    @Override
    protected void initEventAndData() {

        mWebView.setWebChromeClient(new WebChromeClient());//重写一下。有的时候可能会出现问题
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);  //这句话必须保留。。不解释
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }



//        mWebView.setWebChromeClient(new WebChromeClient());
//          mWebView.setWebChromeClient(new WebChromeClient());
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setUseWideViewPort(true); // 关键点
//        webSettings.setAllowFileAccess(true); // 允许访问文件
//        webSettings.setSupportZoom(true); // 支持缩放
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.addJavascriptInterface(new IJsApi(this), "JsUtils");

        mWebView.loadUrl(webViewUrl);


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
