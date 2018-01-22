package cn.javava.shenma.act;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.BannerAdapter;
import cn.javava.shenma.adapter.MainAdapter;
import cn.javava.shenma.app.App;
import cn.javava.shenma.app.ZegoApiManager;
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.fragment.ScanLoginFragment;
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.utils.ScreenUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.CustomMediaPlayerAssertFolder;
import cn.javava.shenma.view.FocusLayout;
import cn.javava.shenma.view.SpacesItemDecoration;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZVideoPlayer;
import rx.Subscriber;


/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/5
 * Todo {TODO}.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<String> mBannerList;
    List<Room> mRoomList;
    MainAdapter mAdapter;

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
    protected void onStop() {
        super.onStop();
        JZVideoPlayer.releaseAllVideos();
    }



    ScanLoginFragment loginFragment;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(!Session.login&&loginFragment==null){
//            loginFragment = ScanLoginFragment.getInstance("none");
//            loginFragment.setCancelable(false);
//            loginFragment.show(getFragmentManager(), "GameResultDialog");
//        }
        return super.onKeyDown(keyCode, event);
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
    }


}
