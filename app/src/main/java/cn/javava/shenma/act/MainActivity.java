package cn.javava.shenma.act;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.MainAdapter;
import cn.javava.shenma.app.ZegoApiManager;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.CustomMediaPlayerAssertFolder;
import cn.javava.shenma.view.SpacesItemDecoration;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import rx.Subscriber;


/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/5
 * Todo {TODO}.
 */
public class MainActivity extends BaseActivity implements ScanLoginFragment.onDismissListener {

    private final  static int WHEEL_TIME=1000*60;
    private final  static int KEY_TIME=1000*10;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

//    TextView mTvTimer;

    List<String> mBannerList;
    List<Room> mRoomList;
    MainAdapter mAdapter;
    ScanLoginFragment loginFragment;
    TimeCounter timer;
    SwitchTask task;
    int pager;


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mBannerList=new ArrayList<>();
        mRoomList=new ArrayList<>();
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514876319180.png");
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514632576278.png");
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514037798443.jpg");

        JZVideoPlayer.setMediaInterface(new CustomMediaPlayerAssertFolder());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
        mAdapter = new MainAdapter(this, mRoomList,mBannerList,mRecyclerView);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(15));
        mRecyclerView.setAdapter(mAdapter);
        pullInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Session.login){
            if(timer==null){
                timer=new TimeCounter(WHEEL_TIME,1000);
            }else{
                timer.cancel();
            }
            timer.start();

            if(task==null)task=new SwitchTask();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
//        JZVideoPlayer.releaseAllVideos();
        if(task!=null)task.stop();
        if(timer!=null)timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JZVideoPlayer.releaseAllVideos();
    }




    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("lzh2017","..Main...dispatchKeyEvent..login="+Session.login);
        if(!Session.login&&loginFragment==null){
            loginFragment = ScanLoginFragment.getInstance("none");
            loginFragment.setCancelable(false);
            loginFragment.show(getFragmentManager(), "GameResultDialog");
            loginFragment.addOnDdismissListener(this);
        }else if(Session.login){
            if(MotionEvent.ACTION_UP==event.getAction()){
                Log.e("lzh2017","..Main...dispatchKeyEvent..=按钮计时触发=");
                if(task!=null)task.start();
            }else{
                if(timer!=null)timer.cancel();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void pullInfo(){

        Subscriber<RoomO> subscriber=new Subscriber<RoomO>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RoomO roomO) {
                if (roomO.getCode() == 0) {
                    for (RoomO.DataBean.RoomListBean roomListBean : roomO.getData().getRoom_list()) {
                        if (roomListBean.getStream_info() == null || roomListBean.getStream_info().size() == 0) continue;
                        Room room = new Room();
                        room.roomIcon = R.mipmap.ic_room1;
                        room.roomID = roomListBean.getRoom_id();
                        room.roomName = roomListBean.getRoom_name();
                        for (RoomO.DataBean.RoomListBean.StreamInfoBean streamInfoBean : roomListBean.getStream_info()) {
                            room.streamList.add(streamInfoBean.getStream_id());
                        }
                        mRoomList.add(room);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().apiTest(subscriber, String.valueOf(ZegoApiManager.getInstance().getAppID()));

//        Subscriber subscriber=new Subscriber<LiveRoomsBean>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(LiveRoomsBean liveRoomsBean) {
//
//                for (LiveRoomsBean.ContentBean contentBean : liveRoomsBean.getContent()) {
//
//                }
//
//                    for (RoomO.DataBean.RoomListBean roomListBean : roomO.getData().getRoom_list()) {
//                        if (roomListBean.getStream_info() == null || roomListBean.getStream_info().size() == 0) continue;
//                        Room room = new Room();
//                        room.roomIcon = R.mipmap.ic_room1;
//                        room.roomID = roomListBean.getRoom_id();
//                        room.roomName = roomListBean.getRoom_name();
//                        for (RoomO.DataBean.RoomListBean.StreamInfoBean streamInfoBean : roomListBean.getStream_info()) {
//                            room.streamList.add(streamInfoBean.getStream_id());
//                        }
//                        mRoomList.add(room);
//                    }
//                    mAdapter.notifyDataSetChanged();
//
//            }
//        };
//        addSubscrebe(subscriber);
//        HttpHelper.getInstance().obtainLiveRoomList(subscriber,pager);
    }

    @Override
    public void onDisMiss() {
        if(Session.login){
            //设置头像 nickname
            mAdapter.notifyItemChanged(1,"notify");

                if(timer==null){
                    timer=new TimeCounter(WHEEL_TIME,1000);
                }else{
                    timer.cancel();
                }
                timer.start();

                if(task==null)task=new SwitchTask();

        }
    }


    public class TimeCounter extends CountDownTimer{

        public TimeCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("lzh2017","退出倒计时:"+millisUntilFinished/1000);
               mAdapter.setTimer(millisUntilFinished/1000);
        }

        @Override
        public void onFinish() {
            Session.openid=null;
            Session.nickname=null;
            Session.headimgurl=null;
            Session.unionid=null;
            Session.login=false;
            if(loginFragment!=null){
                loginFragment.dismiss();
                loginFragment=null;
            }
            mAdapter.notifyItemChanged(1,"notify");
        }
    }

    private class SwitchTask implements Runnable {


        @Override
        public void run() {

            Log.e("lzh2017","计时重置点.......");
            if(timer!=null)timer.start();
        }

        public void start() {
            stop();
            UIUtils.postDelayed(this, KEY_TIME);

        }

        public void stop() {
            UIUtils.removeCallbacks(this);
        }
    }


}
