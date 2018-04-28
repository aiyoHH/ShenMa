package cn.javava.shenma.act;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.MainAdapter;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.BannerBean;
import cn.javava.shenma.bean.NoneDataBean;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.bean.RoomsBean;
import cn.javava.shenma.fragment.LogoutDialog;
import cn.javava.shenma.fragment.RechargeFragment;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnPositionClickListener;
import cn.javava.shenma.utils.SoundPoolUtil;
import cn.javava.shenma.utils.SystemUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.SpacesItemDecoration;
import rx.Subscriber;


/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/5addSubscrebe
 * Todo {TODO}.
 */
public class MainActivity extends BaseActivity implements ScanLoginFragment.onDismissListener, OnPositionClickListener {

    private final static int WHEEL_TIME = 1000 * 60;
    private final static int KEY_TIME = 1000 * 10;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<BannerBean.DataBean> mBannerList;
    List<Room> mRoomList;
    MainAdapter mAdapter;
    ScanLoginFragment loginFragment;
    TimeCounter timer;
    SwitchTask task;
    int currentClickPosition;
    String videoUrl = "";

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mBannerList = new ArrayList<>();
        mRoomList = new ArrayList<>();
        Session.deviceId = SystemUtil.getDeviceId(this);
        Uri uri = Uri.parse("android.resource://cn.javava.shenma/" + R.raw.local_video);
        videoUrl = uri.toString();
        mAdapter = new MainAdapter(this, mRoomList, mBannerList, videoUrl, mRecyclerView, this);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(15));
        mRecyclerView.setAdapter(mAdapter);
        obtainBanner();
        pullInfo();

        SoundPoolUtil.getInstance().soundLeiSure();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (Session.login) {
            mAdapter.notifyItemChanged(2, "notify");
            if (timer == null) {
                timer = new TimeCounter(WHEEL_TIME, 1000);
            } else {
                timer.cancel();
            }
            if (task == null)
                task = new SwitchTask();
            timer.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (task != null)
            task.stop();
        if (timer != null)
            timer.cancel();
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (timer != null)
            timer.start();
        if (!Session.login && loginFragment == null) {
            loginFragment = ScanLoginFragment.getInstance("none");
            loginFragment.setCancelable(false);
            loginFragment.show(getFragmentManager(), "GameResultDialog");
            SoundPoolUtil.getInstance().soundLogin();
            loginFragment.addOnDdismissListener(this);
        } else if (Session.login) {
            if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                final LogoutDialog logoutDialog = new LogoutDialog();
                logoutDialog.setCancelable(false);
                logoutDialog.setGameResultCallback(new LogoutDialog.OnGameResultCallback() {
                    @Override
                    public void onGiveUpPlaying() {
                        logoutDialog.dismiss();
                    }

                    @Override
                    public void onContinueToPlay() {
                        exitUser();
                        logoutDialog.dismiss();
                    }
                });
                logoutDialog.show(getFragmentManager(), "LogoutDialog");
                new CountDownTimer(6000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        logoutDialog.setContinueText(getString(R.string.continue_to_play, (millisUntilFinished / 1000) + ""));
                    }

                    @Override
                    public void onFinish() {
                        logoutDialog.dismiss();
                    }
                }.start();
            } else if (MotionEvent.ACTION_UP == event.getAction()) {

                if (task != null)
                    task.start();
            } else {
                if (timer != null)
                    timer.cancel();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
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
                    mAdapter.notifyItemChanged(0);
                    Session.bannerList = mBannerList;
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
                if (Key.SUCCESS.equals(roomsBean.getStatus())) {
                    mRoomList.clear();
                    List<RoomsBean.DataBean> data = roomsBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        RoomsBean.DataBean dataBean = data.get(i);
                        Room room = new Room();
                        room.roomID = "WWJ_ZEGO_3275f295eab4";
                        room.roomIcon = dataBean.getThumb();
                        room.roomName = dataBean.getTitle();
                        room.room_id = dataBean.getRoom_id();
                        room.balance = dataBean.getBalance();
                        room.number = dataBean.getGoods_id();
                        room.isData = "yes".equals(dataBean.getIsdata());
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

    private void goodCount(){
        Subscriber subscriber=new Subscriber<NoneDataBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NoneDataBean noneDataBean) {
                if (Key.SUCCESS.equals(noneDataBean.getStatus())) {
                    Session.goodCount=noneDataBean.getData().getCount();
                    mAdapter.notifyItemChanged(2);

                }
            }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().goodCount(subscriber);
    }


    @Override
    public void onDisMiss() {
        if (Session.login) {
            //设置头像 nickname
            mAdapter.notifyItemChanged(2, "notify");
            //登录成功后,在刷新数据
            obtainBanner();
            pullInfo();

            if (timer == null) {
                timer = new TimeCounter(WHEEL_TIME, 1000);
            } else {
                timer.cancel();
            }
            timer.start();

            if (task == null)
                task = new SwitchTask();
        }
        loginFragment = null;
    }


    @Override
    public void onClick(int position, boolean isResponse) {
        if (!isResponse)
            return;
        Room room = mRoomList.get(position);
        if (Session.balance < room.balance) {
            RechargeFragment rechargeFragment = RechargeFragment.getInstance("");
            rechargeFragment.show(getFragmentManager(), "");
            SoundPoolUtil.getInstance().soundNotSufficentFound();
        } else {
            Intent intent = new Intent(this, PlayActivity.class);
            Room selectRoom = mRoomList.get(position);
            intent.putExtra("selectRoom", selectRoom);
            startActivityForResult(intent, 0x81);
            if (!Session.login)
                return;
            currentClickPosition = position;
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

            if (timer != null)
                timer.start();
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
                Session.goodCount = 0;
                Session.login = false;
                if (loginFragment != null) {
                    loginFragment.dismiss();
                    loginFragment = null;
                }
                mAdapter.notifyItemChanged(2, "notify");


            }

            @Override
            public void onNext(NoneDataBean noneDataBean) {
                if (Key.SUCCESS.equals(noneDataBean.getStatus())) {
                    Session.openid = null;
                    Session.nickname = null;
                    Session.headimgurl = null;
                    Session.memberid = 0;
                    Session.goodCount = 0;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            obtainBanner();
            pullInfo();
            goodCount();
        }
    }
}
