package cn.javava.shenma.act;

import android.support.v4.view.ViewPager;
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
import cn.javava.shenma.utils.ScreenUtil;
import cn.javava.shenma.utils.UIUtils;
import cn.javava.shenma.view.FocusLayout;

public class MainActivity extends BaseActivity {

    private final  static int WHEEL_TIME=5000;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.points_behind)
    LinearLayout llContainer;

    List<String> mList;
    MainAdapter mAdapter;
    SwitchTask task;
    FocusLayout mFocusLayout;


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        mList=new ArrayList<>();
        mList.add("https://app-cdn.siy8.com/6320/images-1514038402338.png");
        mList.add("https://app-cdn.siy8.com/6320/images-1514876319180.png");
        mList.add("https://app-cdn.siy8.com/6320/images-1514632576278.png");
        mList.add("https://app-cdn.siy8.com/6320/images-1514037798443.jpg");

        pullInfo();

        mViewPager.setAdapter(new BannerAdapter(this,mList));

        for (int i = 0; i <mList.size(); i++) {
            Log.e("lzh2017","add point="+i);
            ImageView point = new ImageView(this);
            point.setImageResource(i == 0 ? R.drawable.shape_point_black : R.drawable.shape_point_white);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) params.leftMargin = ScreenUtil.dip2px(10);
            llContainer.addView(point, params);
        }


        if(task == null&&mList.size()>0){
            task = new SwitchTask();
            task.start();
        }

        mAdapter = new MainAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mFocusLayout = new FocusLayout(this);
        bindListener();
        addContentView(mFocusLayout,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));//添加焦点层

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    private void pullInfo(){

    }

    private class SwitchTask implements Runnable {

        @Override
        public void run() {
            int item = mViewPager.getCurrentItem();
            mViewPager.setCurrentItem(++item);
            UIUtils.postDelayed(this, WHEEL_TIME);
        }

        public void start() {
            stop();
            UIUtils.postDelayed(this, WHEEL_TIME);
        }

        public void stop() {
            UIUtils.removeCallbacks(this);
        }
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
