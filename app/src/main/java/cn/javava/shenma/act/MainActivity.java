package cn.javava.shenma.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.MainAdapter;
import cn.javava.shenma.app.ZegoApiManager;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.BannerBean;
import cn.javava.shenma.bean.NoneDataBean;
import cn.javava.shenma.bean.RoomsBean;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.bean.TokenBean;
import cn.javava.shenma.bean.UserInfoBean;
import cn.javava.shenma.fragment.QRCodeFragment;
import cn.javava.shenma.fragment.RechargeFragment;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnPositionClickListener;
import cn.javava.shenma.utils.CMDCenter;
import cn.javava.shenma.utils.MotorDrvUtil;
import cn.javava.shenma.utils.SystemUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.CustomMediaPlayerAssertFolder;
import cn.javava.shenma.view.SpacesItemDecoration;
import cn.jzvd.JZVideoPlayer;
import okhttp3.Response;
import rx.Subscriber;


/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/5
 * Todo {TODO}.
 */
public class MainActivity extends BaseActivity implements ScanLoginFragment.onDismissListener, OnPositionClickListener {

    private final static int WHEEL_TIME = 1000 * 60;
    private final static int KEY_TIME = 1000 * 10;
    private int[] images = {R.mipmap.demo, R.mipmap.demo7, R.mipmap.ic_room1,
            R.mipmap.ic_room2, R.mipmap.ic_room3, R.mipmap.ic_room4, R.mipmap.ic_room5,
            R.mipmap.ic_room6};

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<BannerBean.DataBean> mBannerList;
    List<Room> mRoomList;
    MainAdapter mAdapter;
    ScanLoginFragment loginFragment;
    TimeCounter timer;
    SwitchTask task;
    int currentClickPosition;
    String videoUrl="";

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mBannerList = new ArrayList<>();
        mRoomList = new ArrayList<>();

        Uri uri = Uri.parse("android.resource://cn.javava.shenma/"+R.raw.local_video);
        videoUrl=uri.toString();
//        JZVideoPlayer.setMediaInterface(new CustomMediaPlayerAssertFolder());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
        mAdapter = new MainAdapter(this, mRoomList, mBannerList,videoUrl,mRecyclerView, this);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(15));
        mRecyclerView.setAdapter(mAdapter);


        Session.deviceId = SystemUtil.getDeviceId(this);

        obtainBanner();
        pullInfo();

        //仅仅试用即构娃娃机，娃娃云请关闭
        getToken();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Session.login) {
            if (timer == null) {
                timer = new TimeCounter(WHEEL_TIME, 1000);
            } else {
                timer.cancel();
            }
            timer.start();

            if (task == null) task = new SwitchTask();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
//        JZVideoPlayer.releaseAllVideos();
        if (task != null) task.stop();
        if (timer != null) timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (!Session.login && loginFragment == null) {
            loginFragment = ScanLoginFragment.getInstance("none");
            loginFragment.setCancelable(false);
            loginFragment.show(getFragmentManager(), "GameResultDialog");
            loginFragment.addOnDdismissListener(this);

        } else if (Session.login) {

            if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("退出提示")
                        .setMessage("确定退出当前登录用户")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exitUser();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            } else if (MotionEvent.ACTION_UP == event.getAction()) {

                if (task != null) task.start();
            } else {
                if (timer != null) timer.cancel();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void obtainBanner() {
        Subscriber subscriber = new Subscriber<BannerBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BannerBean response) {
                if (response.getStatus().equals(Key.SUCCESS)) {
                    mBannerList.clear();
                    mBannerList.addAll(response.getData());
//                    videoUrl="http://v.mifile.cn/b2c-mimall-media/53fc775dd6b29ecd8df3e2ea35129766.mp4";
                    mAdapter.notifyItemChanged(0);
//                    mAdapter.notifyItemChanged(1);
                    Session.bannerList=mBannerList;
                }

            }
        };

        addSubscrebe(subscriber);
        HttpHelper.getInstance().obtainBanners(subscriber);
    }

    private void pullInfo() {

        Subscriber subscriber = new Subscriber<RoomsBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RoomsBean roomsBean) {

                if ("success".equals(roomsBean.getStatus())) {
                    Log.e("lzh2018", "设置房间列表S");
                    mRoomList.clear();
                    List<RoomsBean.DataBean> data = roomsBean.getData();
                    for (int i = 0; i < data.size(); i++) {

                        RoomsBean.DataBean dataBean = data.get(i);
                        Room room = new Room();
                        room.roomIcon = images[i];
                        room.roomID = "WWJ_ZEGO_3275f295eab4";
                        room.roomName = dataBean.getTitle();
                        room.balance = dataBean.getBalance();
                        room.number = dataBean.getGoods_id();

                        room.streamList.add("WWJ_ZEGO_STREAM_3275f295eab4_2");
                        room.streamList.add("WWJ_ZEGO_STREAM_3275f295eab4");
                        mRoomList.add(room);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().obtainRoomList(subscriber);

    }

    private void getToken() {
        Subscriber<TokenBean> subscriber = new Subscriber<TokenBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TokenBean roomO) {
                if (roomO.getCode() == 0) {
                    Session.accessToken = roomO.getAccess_token();
                }
            }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().getAccessToken(subscriber);
    }

    @Override
    public void onDisMiss() {
        if (Session.login) {
            //设置头像 nickname
            mAdapter.notifyItemChanged(2, "notify");

            obtainBanner();
            pullInfo();

            if (timer == null) {
                timer = new TimeCounter(WHEEL_TIME, 1000);
            } else {
                timer.cancel();
            }
            timer.start();

            if (task == null) task = new SwitchTask();
        }
        loginFragment = null;
    }


    @Override
    public void onClick(int position) {
        Room room = mRoomList.get(position);
        if(Session.balance<room.balance){
            RechargeFragment rechargeFragment = RechargeFragment.getInstance("");
            rechargeFragment.show(getFragmentManager(),"");
        }else{
            PlayActivity.actionStart(this, mRoomList.get(position));
            if (!Session.login) return;
            currentClickPosition = position;
         }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("lzh2017", "onActivityResult============");
        if (requestCode == Key.ACTION_PLAY) {
            PlayActivity.actionStart(this, mRoomList.get(currentClickPosition));
        }
    }

    public class TimeCounter extends CountDownTimer {

        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            mAdapter.setTimer(millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {

            exitUser();

        }
    }

    private class SwitchTask implements Runnable {


        @Override
        public void run() {

            Log.e("lzh2017", "计时重置点.......");
            if (timer != null) timer.start();
        }

        public void start() {
            stop();
            UIUtils.postDelayed(this, KEY_TIME);

        }

        public void stop() {
            UIUtils.removeCallbacks(this);
        }
    }

    private void exitUser() {
        HttpHelper.getInstance().exitUser(new Subscriber<NoneDataBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Session.openid = null;
                Session.nickname = null;
                Session.headimgurl = null;
                Session.memberid = 0;
                Session.login = false;
                if (loginFragment != null) {
                    loginFragment.dismiss();
                    loginFragment = null;
                }
                mAdapter.notifyItemChanged(2, "notify");


            }

            @Override
            public void onNext(NoneDataBean noneDataBean) {
                if ("success".equals(noneDataBean.getStatus())) {
                    Session.openid = null;
                    Session.nickname = null;
                    Session.headimgurl = null;
                    Session.memberid = 0;
                    Session.login = false;
                    if (loginFragment != null) {
                        loginFragment.dismiss();
                        loginFragment = null;
                    }
                    mAdapter.notifyItemChanged(2, "notify");
                }
            }
        });
    }







}
