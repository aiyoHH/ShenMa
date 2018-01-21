package cn.javava.shenma.http;

import cn.javava.shenma.bean.RoomO;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by aiyoRui on 2018/1/4.
 */

public interface ApiInterface {

    void apiTest(Subscriber<RoomO> s,String appId);

    void obtainUserList(Subscriber<ResponseBody> subscriber);

    void obtainUserInfo(Subscriber<ResponseBody> subscriber,String userId);

    void obtainUserMe(Subscriber<ResponseBody> subscriber);

    void obtainLiveList(Subscriber<ResponseBody> subscriber);

    void obtainLiveRoomList(Subscriber<ResponseBody> subscriber);

    void obtainQRCodePay(Subscriber<ResponseBody> subscriber);

    void checkResultPay(Subscriber<ResponseBody> subscriber,String tradeNo);

    void scanQRCodeLogin(Subscriber<ResponseBody> subscriber);

}
