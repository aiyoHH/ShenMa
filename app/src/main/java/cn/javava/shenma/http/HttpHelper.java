package cn.javava.shenma.http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import cn.javava.shenma.bean.RoomO;
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
        toSubscribe(httpApis.apiTest(appId), s);
    }

    @Override
    public void getEntrptedConfig(Subscriber<ResponseBody> subscriber,String url) {
        toSubscribe(httpApis.getEntrptedConfig(url),subscriber);
    }

    @Override
    public void obtainUserList(Subscriber<ResponseBody> subscriber) {
        toSubscribe(httpApis.obtainUserList(),subscriber);
    }

    @Override
    public void obtainUserInfo(Subscriber<ResponseBody> subscriber, String userId) {
        toSubscribe(httpApis.obtainUserInfo(userId),subscriber);
    }

    @Override
    public void obtainUserMe(Subscriber<ResponseBody> subscriber) {
        toSubscribe(httpApis.obtainUserMe(),subscriber);
    }

    @Override
    public void obtainLiveList(Subscriber<ResponseBody> subscriber) {
        toSubscribe(httpApis.obtainLiveList(),subscriber);
    }

    @Override
    public void obtainLiveRoomList(Subscriber<ResponseBody> subscriber) {
        toSubscribe(httpApis.obtainLiveRoomList(),subscriber);
    }

    @Override
    public void obtainQRCodePay(Subscriber<ResponseBody> subscriber) {
        toSubscribe(httpApis.obtainQRCodePay(),subscriber);
    }


    @Override
    public void checkResultPay(Subscriber<ResponseBody> subscriber,String tradeNo) {
        toSubscribe(httpApis.checkResultPay(tradeNo),subscriber);
    }

    @Override
    public void scanQRCodeLogin(Subscriber<ResponseBody> subscriber) {
        toSubscribe(httpApis.scanQRCodeLogin(),subscriber);
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
