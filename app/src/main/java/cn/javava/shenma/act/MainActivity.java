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
import cn.javava.shenma.base.BaseActivity;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.utils.ScreenUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.FocusLayout;
import cn.javava.shenma.view.SpacesItemDecoration;
import cn.jzvd.JZVideoPlayer;


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

//        mFocusLayout = new FocusLayout(this);
//        bindListener();
//        addContentView(mFocusLayout,
//                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));//添加焦点层




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

    }



    private void bindListener() {
        //获取根元素
        View mContainerView = this.getWindow().getDecorView();//.findViewById(android.R.id.content);
        //得到整个view树的viewTreeObserver
        ViewTreeObserver viewTreeObserver = mContainerView.getViewTreeObserver();
        //给观察者设置焦点变化监听
        viewTreeObserver.addOnGlobalFocusChangeListener(mFocusLayout);
    }

}
