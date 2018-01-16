package cn.javava.shenma.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import cn.javava.shenma.R;
import cn.javava.shenma.interf.Key;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/15
 * Todo {TODO}.
 */

public class ScanLoginFragment extends DialogFragment {

    private static final String loginUrl="https://open.weixin.qq.com/connect/qrconnect?appid=wx7b14057ca00d4350&redirect_uri=http%3A%2F%2Fauthc.javava.cn%2FscanQRCodeComplete&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";

    WebView webView;

    public static ScanLoginFragment getInstance(String msg){
        ScanLoginFragment secondFragment= new ScanLoginFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Key.DATA,msg);
        secondFragment.setArguments(bundle);
        return secondFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_scan_login,container);

         webView=view.findViewById(R.id.webview_login);

//        webView.setJavascriptInterface(new IJsApi(this,this));
        webView.clearCache(true);

        webView.loadUrl(loginUrl);

        return view;
    }
}
