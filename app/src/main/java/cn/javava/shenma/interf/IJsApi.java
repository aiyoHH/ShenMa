package cn.javava.shenma.interf;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Li'O on 2018/01/23.
 * Description {TODO}.
 */

public class IJsApi {

    OnLoadingListener mLoadingListener;
    public IJsApi( OnLoadingListener listener){

        mLoadingListener=listener;
    }

    @JavascriptInterface
    public void loginSuccess(String jsonObject) throws JSONException {
        Log.e("lzh2017","accessToken="+jsonObject.toString());

        mLoadingListener.onFinish(jsonObject);
    }


}
