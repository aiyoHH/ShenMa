package cn.javava.shenma.adapter.mainHolder;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.adapter.BannerAdapter;
import cn.javava.shenma.utils.ImageLoader;
import cn.javava.shenma.utils.ScreenUtil;
import cn.javava.shenma.utils.UIUtils;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/10
 * Todo {TODO}.
 */

public class BannerHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_main_banner_viewPager)
    ViewPager mViewPager;
    @BindView(R.id.item_main_banner_points)
    LinearLayout llContainer;
    SwitchTask task;
    private final  static int WHEEL_TIME=5000;

    public BannerHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setData(Context context,List<String> list){

        mViewPager.setAdapter(new BannerAdapter(context,list));
        llContainer.removeAllViews();
        for (int i = 0; i <list.size(); i++) {
            ImageView point = new ImageView(context);
            point.setImageResource(i == 0 ? R.drawable.shape_point_black : R.drawable.shape_point_white);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) params.leftMargin = ScreenUtil.dip2px(10);
            llContainer.addView(point, params);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int count = llContainer.getChildCount();
                position = position % count;
                for (int i = 0; i < count; i++) {
                    ImageView iv = (ImageView) llContainer.getChildAt(i);
                    iv.setImageResource(i == position ? R.drawable.shape_point_black : R.drawable.shape_point_white);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if(task == null&&list.size()>0){
            task = new SwitchTask();
            task.start();
        }

    }

    public class BannerAdapter extends PagerAdapter {
        Context context;
        List<String> list;

        public BannerAdapter(Context context,List<String> list) {
            this.context=context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String uri=list.get(position%list.size());
            ImageView imageView=new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader.loadTop(context,uri,imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
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
}
