package cn.javava.shenma.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.constants.ZegoConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.bean.Room;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/8
 * Todo {TODO}.
 */

public class PlayActivity extends AppCompatActivity{


    @BindView(R.id.play_cancel)
    ImageButton mBtnCancel;
    @BindView(R.id.play_confirm)
    ImageButton mBtnConfirm;

    private Room mRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
//        Intent intent = getIntent();
//
//        if (intent != null) {
//            mRoom = (Room) intent.getSerializableExtra("room");
//
//            setTitle(mRoom.roomName);
//            setContentView(R.layout.activity_play);
//
//            ButterKnife.bind(this);
//
////            initStreamList();
////            initViews();
////            startPlay();
//
//        } else {
//            Toast.makeText(this, "房间信息初始化错误, 请重新开始", Toast.LENGTH_LONG).show();
//            finish();
//        }


        // 从加速服务器拉流
        ZegoLiveRoom.setConfig(ZegoConstants.Config.PREFER_PLAY_ULTRA_SOURCE + "=1");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("lzh2017","KeyCode="+keyCode);
//        Log.e("lzh2017","KeyEvent event="+event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Toast.makeText(this,"左........",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Toast.makeText(this,"........右",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Toast.makeText(this,"...$$上$$...",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Toast.makeText(this,"...#下#...",Toast.LENGTH_LONG).show();
                break;
            case KeyEvent.KEYCODE_BUTTON_C:
                Toast.makeText(this,"...确定...",Toast.LENGTH_LONG).show();
                mBtnConfirm.performClick();
                break;
            case KeyEvent.KEYCODE_BACK:
                //弹窗是否退出
                mBtnCancel.performClick();
               startActivity(new Intent(PlayActivity.this,ShopActivity.class));
                return true;
        }


        return super.onKeyDown(keyCode, event);
    }
}
