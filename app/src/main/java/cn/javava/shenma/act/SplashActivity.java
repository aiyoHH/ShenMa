package cn.javava.shenma.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.javava.shenma.R;
import cn.javava.shenma.utils.SoundPoolUtil;
import cn.javava.shenma.utils.UIUtils;


/**
 * Created by aiyoRui on 2018/4/25.
 */

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        SoundPoolUtil.getInstance().soundLaunch();

        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },30*1000);
    }
}
