package cn.javava.shenma.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.javava.shenma.utils.ImageLoader;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/5
 * Todo {TODO}.
 */

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
