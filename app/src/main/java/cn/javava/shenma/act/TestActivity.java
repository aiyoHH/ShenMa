package cn.javava.shenma.act;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import cn.javava.shenma.R;
import cn.javava.shenma.base.BaseActivity;

/**
 * Created by aiyoRui on 2018/1/10.
 */

public class TestActivity extends BaseActivity {
    @Override
    protected int initLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEventAndData() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Log.e("lzh2017","dpi x=="+dm.xdpi);
        Log.e("lzh2017","dpi y=="+dm.ydpi);
        Log.e("lzh2017","dpi=="+dm.densityDpi);

        Log.e("lzh2017","widthPixels=="+dm.widthPixels);
        Log.e("lzh2017","heightPixels=="+dm.heightPixels);

        Log.e("lzh2017","dpi dest=="+dm.density);


        float width=dm.widthPixels*dm.density;
        float height=dm.heightPixels*dm.density;

        Log.e("lzh2017","width="+width+"/n height="+height);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        Log.e("lzh2017","plexh="+dm.toString()+"/n"+"Second method:"+"Y="+screenWidth+";X="+screenHeight);

    }
}
