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
        ButterKnife.bind(this);
//        UIUtils.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//            }
//        },30*1000);
    }

    @OnClick(R.id.play1)void play1(){
        SoundPoolUtil.getInstance().soundLaunch();
    }
    @OnClick(R.id.play2)void play2(){
        SoundPoolUtil.getInstance().soundSuccess();
    }
    @OnClick(R.id.play3)void play3(){
        SoundPoolUtil.getInstance().soundFailure();
    }
    @OnClick(R.id.play4)void play4(){
        SoundPoolUtil.getInstance().soundCatch();
    }
    @OnClick(R.id.play5)void play5(){
        SoundPoolUtil.getInstance().soundBGM();
    }

    @OnClick(R.id.play6)void play6(){
        SoundPoolUtil.getInstance().endBGM();
    }

    @OnClick(R.id.play7)void play7(){
        SoundPoolUtil.getInstance().soundLeiSure();
    }

    @OnClick(R.id.play8)void play8(){
        SoundPoolUtil.getInstance().soundLogining();
    }
    @OnClick(R.id.play9)void play9(){
        SoundPoolUtil.getInstance().soundNotSufficentFound();
    }



}
