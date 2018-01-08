package cn.javava.shenma.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import cn.javava.shenma.R;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/8
 * Todo {TODO}.
 */

public class PlayActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("lzh2017","KeyEvent event="+event);
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
        }


        return super.onKeyDown(keyCode, event);
    }
}
