package cn.javava.shenma.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import cn.javava.shenma.bean.BannerBean;
import cn.javava.shenma.bean.ConfigBean;
import cn.javava.shenma.bean.NoneDataBean;
import cn.javava.shenma.bean.RoomsBean;
import cn.javava.shenma.bean.PayResultBean;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.bean.TokenBean;
import cn.javava.shenma.bean.UserInfoBean;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.utils.SystemUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by aiyoRui on 2018/01/04.
 */

public class HttpHelper implements ApiInterface {

    private HttpApis httpApis;

    private static volatile HttpHelper httpHelper = null;

    public HttpHelper() {
        initOkHttp();
    }

    public static HttpHelper getInstance() {
        if (httpHelper == null) {
            synchronized (HttpHelper.class) {
                if (httpHelper == null) {
                    httpHelper = new HttpHelper();
                }
            }
        }
        return httpHelper;
    }

    private void initOkHttp() {
        File cacheFile = new File(Key.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(cacheInterceptor)
                .cache(cache)
                .build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(Key.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        httpApis = mRetrofit.create(HttpApis.class);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    @Override
    public void apiTest(Subscriber<RoomO> s, String appId) {
        String url="https://liveroom3671502238-api.zego.im/demo/roomlist?appid="+appId;
        toSubscribe(httpApis.apiTest(url), s);
    }

    public void getAccessToken(Subscriber<TokenBean> s){
        String url="http://api.javava.cn/oauth/token";

        toSubscribe(httpApis.gainToken(url,"client_credentials","a","b"), s);
    }

    @Override
    public void obtainBanners(Subscriber<BannerBean> subscriber) {
        toSubscribe(httpApis.obtainBannerList(Session.deviceId,Session.openid,Session.memberid,Session.token),subscriber);
    }



    @Override
    public void obtainUserInfo(Subscriber<UserInfoBean> subscriber, String deviceId) {
        toSubscribe(httpApis.obtainUserInfo(deviceId),subscriber);
    }



    @Override
    public void obtainRoomList(Subscriber<RoomsBean> subscriber) {
        toSubscribe(httpApis.obtainRoomList(Session.deviceId),subscriber);
    }


    @Override
    public void obtainQRCodePay(Subscriber<PayResultBean> subscriber,String userId,int money) {
        toSubscribe(httpApis.obtainQRCodePay("c",userId,money),subscriber);
    }


    @Override
    public void gainConfig(Subscriber<ConfigBean> subscriber, String accessToken, String sessionId,int confirm) {
        String url="http://api.javava.cn/games";
        toSubscribe(httpApis.gainConfig(url,accessToken,sessionId,confirm),subscriber);
    }

    @Override
    public void exitUser(Subscriber<NoneDataBean> subscriber) {
        toSubscribe(httpApis.exitUser(Session.deviceId,Session.openid,Session.memberid,Session.token),subscriber);
    }

    @Override
    public void feeDeduction(Subscriber<NoneDataBean> subscriber, int fee) {
        toSubscribe(httpApis.feeDeduction(Session.deviceId,fee,Session.openid,Session.memberid,Session.token),subscriber);
    }


    Interceptor logInterceptor = new HttpLoggingInterceptor(
            new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("http", "message-->>" + message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY);

    Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!TextUtils.isEmpty(Session.userId))
                request = request.newBuilder().addHeader("userId", Session.userId).build();

            return chain.proceed(request);
        }
    };

    Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if (!SystemUtil.isNetworkConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            int tryCount = 0;
            Response response = chain.proceed(request);

            while (!response.isSuccessful() && tryCount < 3) {
                tryCount++;
                // retry the request
                response = chain.proceed(request);
            }

            if (SystemUtil.isNetworkConnected()) {
                int maxAge = 60 * 10;
                // 有网络时, 不缓存, 最大保存时长为10分钟
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                // 无网络时，设置超时为1天
                int maxStale = 60 * 60 * 24;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }

            return response;
        }
    };



}
