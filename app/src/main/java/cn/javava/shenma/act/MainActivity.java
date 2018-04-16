package cn.javava.shenma.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
import cn.javava.shenma.fragment.QRCodeFragment;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnPositionClickListener;
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

    private final  static int WHEEL_TIME=1000*60;
    private final  static int KEY_TIME=1000*10;
    private int[] images={R.mipmap.demo,R.mipmap.demo7,R.mipmap.ic_room1,
            R.mipmap.ic_room2,R.mipmap.ic_room3,R.mipmap.ic_room4,R.mipmap.ic_room5,
            R.mipmap.ic_room6};

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<BannerBean.DataBean> mBannerList;
    List<Room> mRoomList;
    MainAdapter mAdapter;
    ScanLoginFragment loginFragment;
    QRCodeFragment qrCodeFragment;
    TimeCounter timer;
    SwitchTask task;
    int pager;
    int currentClickPosition;


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mBannerList=new ArrayList<>();
        mRoomList=new ArrayList<>();
//        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514876319180.png");
//        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514632576278.png");
//        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514037798443.jpg");

        JZVideoPlayer.setMediaInterface(new CustomMediaPlayerAssertFolder());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
        mAdapter = new MainAdapter(this, mRoomList,mBannerList,mRecyclerView,this);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(15));
        mRecyclerView.setAdapter(mAdapter);


        Session.deviceId=SystemUtil.getDeviceId(this);
        obtainBanner();
        pullInfo();


       //仅仅试用即构娃娃机，娃娃云请关闭
        getToken();

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

            Log.e("lzh2017","..Main...dispatchKeyEvent..=退出触发="+event.getKeyCode());
            if(KeyEvent.KEYCODE_BACK==event.getKeyCode()){

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
            }else if(MotionEvent.ACTION_UP==event.getAction()){

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

    private void obtainBanner(){
        Subscriber subscriber= new Subscriber<BannerBean>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BannerBean response) {
                if(response.getStatus().equals(Key.SUCCESS)){
                    mBannerList.addAll(response.getData());
                    mAdapter.notifyItemChanged(0);
                }

            }
        };

        addSubscrebe(subscriber);
        HttpHelper.getInstance().obtainBanners(subscriber);
    }

    private void pullInfo(){
        Log.e("lzh2018","设置房间列表S");
        Subscriber subscriber=new Subscriber<RoomsBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("lzh2018","设置房间列表E="+e.getMessage());
            }

            @Override
            public void onNext(RoomsBean roomsBean) {
                Log.e("lzh2018","设置房间列表A");
                if("success".equals(roomsBean.getStatus())){
//                    RoomsBean.DataBean dataBean = roomsBean.getData();
//                    for (RoomsBean.DataBean.RoomListBean roomListBean : dataBean.getRoom_list()) {
//                        Room room = new Room();
//                        room.roomIcon = R.mipmap.ic_room1;
//                        room.roomID=roomListBean.getRoom_id();
//                        room.roomName="房间名"+roomListBean.getRoom_name();
//
//                        for (RoomsBean.DataBean.RoomListBean.StreamInfoBean streamInfoBean : roomListBean.getStream_info()) {
//                            room.streamList.add(streamInfoBean.getStream_id());
//                        }
//                        mRoomList.add(room);
//                    }
                    Log.e("lzh2018","设置房间列表B");
                    for (int  i= 0; i < images.length; i++) {
                        Room room = new Room();
                        room.number=i+1;
                        room.roomIcon =images[i];
                        room.roomID="WWJ_ZEGO_3275f295eab4";
                        room.roomName= room.number+"号房间";
                        room.streamList.add("WWJ_ZEGO_STREAM_3275f295eab4_2");
                        room.streamList.add("WWJ_ZEGO_STREAM_3275f295eab4");
                        mRoomList.add(room);
                    }

                    mAdapter.notifyDataSetChanged();
                    }else{
                    for (int  i= 0; i < images.length; i++) {
                        Room room = new Room();
                        room.number=i+1;
                        room.roomIcon =images[i];
                        room.roomID="WWJ_ZEGO_3275f295eab4";
                        room.roomName= room.number+"号房间";
                        room.streamList.add("WWJ_ZEGO_STREAM_3275f295eab4_2");
                        room.streamList.add("WWJ_ZEGO_STREAM_3275f295eab4");
                        mRoomList.add(room);
                    }

                    mAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"房间列表为空.....",Toast.LENGTH_LONG).show();
                    }
                }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().obtainRoomList(subscriber);

    }

    private void getToken(){
      Subscriber<TokenBean> subscriber=new Subscriber<TokenBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TokenBean roomO) {
                if (roomO.getCode() == 0) {
                    Session.accessToken=roomO.getAccess_token();
                }
            }
        };
        addSubscrebe(subscriber);
        HttpHelper.getInstance().getAccessToken(subscriber);
    }

    @Override
    public void onDisMiss() {
        if(Session.login){
            //设置头像 nickname
            mAdapter.notifyItemChanged(2,"notify");

                if(timer==null){
                    timer=new TimeCounter(WHEEL_TIME,1000);
                }else{
                    timer.cancel();
                }
                timer.start();

                if(task==null)task=new SwitchTask();
        }
        loginFragment=null;
    }


    @Override
    public void onClick(int position) {
        PlayActivity.actionStart(this,mRoomList.get(position));
        if(!Session.login)return;
        currentClickPosition=position;
        //查询下余额够不够单钱房间进入条件
//         if(Session.point<10){
//             startActivityForResult(new Intent(MainActivity.this,ShopActivity.class),Key.ACTION_PLAY);
//         }else{
//             PlayActivity.actionStart(this,mRoomList.get(position));
//         }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("lzh2017","onActivityResult============");
        if(requestCode==Key.ACTION_PLAY){
            PlayActivity.actionStart(this,mRoomList.get(currentClickPosition));
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

    private void exitUser(){
        HttpHelper.getInstance().exitUser(new Subscriber<NoneDataBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NoneDataBean noneDataBean) {
                if("success".equals(noneDataBean.getStatus())){
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
