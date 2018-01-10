package cn.javava.shenma.act;

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
import cn.javava.shenma.http.HttpHelper;
import cn.javava.shenma.utils.ScreenUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.FocusLayout;
import cn.javava.shenma.view.SpacesItemDecoration;
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

    FocusLayout mFocusLayout;
    int[] faces={R.mipmap.ic_room1,R.mipmap.ic_room2,R.mipmap.ic_room3,R.mipmap.ic_room4,R.mipmap.ic_room5,R.mipmap.ic_room6};


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mBannerList=new ArrayList<>();
        mRoomList=new ArrayList<>();
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514038402338.png");
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514876319180.png");
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514632576278.png");
        mBannerList.add("https://app-cdn.siy8.com/6320/images-1514037798443.jpg");

        pullInfo();

        mAdapter = new MainAdapter(this, mRoomList,mBannerList,mRecyclerView);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
