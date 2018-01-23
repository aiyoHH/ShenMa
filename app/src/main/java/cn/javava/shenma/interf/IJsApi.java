package cn.javava.shenma.interf;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by Li'O on 2018/01/23.
 * Description {TODO}.
 */

public class IJsApi {
    Context mContext;
    OnLoadingListener mLoadingListener;
    public IJsApi(Context context, OnLoadingListener listener){
        mContext=context;
        mLoadingListener=listener;
    }


    @JavascriptInterface
    public void loginSuccess(String accessToken) {
        Log.e("lzh2017","accessToken="+accessToken);
        mLoadingListener.onFinish(accessToken);
    }

}
