package cn.javava.shenma.http;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by aiyoRui on 2018/1/4.
 */

public interface ApiInterface {

    void apiTest(Subscriber<ResponseBody> s);

}
