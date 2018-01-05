package cn.javava.shenma.act;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import butterknife.BindView;
import cn.javava.shenma.R;
import cn.javava.shenma.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


}
