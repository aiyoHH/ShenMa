package cn.javava.shenma.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.javava.shenma.bean.BannerBean;
import cn.javava.shenma.utils.ImageLoader;
import cn.javava.shenma.utils.UIUtils;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/5
 * Todo {TODO}.
 */

public class BannerAdapter extends PagerAdapter {
    Context context;
    List<BannerBean.DataBean> list;


    public BannerAdapter(Context context,List<BannerBean.DataBean> list) {
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
        String uri=list.get(position%list.size()).getThumb();

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
