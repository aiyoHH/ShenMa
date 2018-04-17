package cn.javava.shenma.http;


import java.util.List;

import cn.javava.shenma.bean.BannerBean;

/**
 * Created by Li'O on 2017/7/5.
 * Description {TODO}.
 */

public class Session {
    public static boolean login;
    public static String openid;
    public static String userId;
    public static String nickname;
    public static String headimgurl;
    public static String token ;
    public static String deviceId ;
    public static int memberid;
    public static int point;
    public static int balance;

    //仅仅试用既够娃娃机
    public static String accessToken ;

    public static int isZhua ;

    public static List<BannerBean.DataBean> bannerList;

}

