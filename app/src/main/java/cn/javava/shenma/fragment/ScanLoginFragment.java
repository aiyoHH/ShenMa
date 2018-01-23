package cn.javava.shenma.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.javava.shenma.R;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.IJsApi;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnLoadingListener;
import wendu.dsbridge.DWebView;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/15
 * Todo {TODO}.
 */

public class ScanLoginFragment extends DialogFragment implements OnLoadingListener {

    private static final String loginUrl="https://open.weixin.qq.com/connect/qrconnect?appid=wx7b14057ca00d4350&redirect_uri=http%3A%2F%2Fauthc.javava.cn%2FscanQRCodeComplete&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";

    WebView webView;

    public static ScanLoginFragment getInstance(String msg){
        ScanLoginFragment secondFragment= new ScanLoginFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Key.DATA,msg);
        secondFragment.setArguments(bundle);
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentStyle);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scan_login,container);

        webView=view.findViewById(R.id.webview_login);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(true);
        webView.addJavascriptInterface(new IJsApi(this),"android");
        webView.clearCache(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(0);
        webView.loadUrl(loginUrl);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount=0.0f;
        attributes.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(attributes);
    }

    @Override
    public void onFinish(String accessToken) {
        Session.accessToken=accessToken;
        //联网获取用户信息so on
    }
}
