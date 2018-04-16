package cn.javava.shenma.fragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.javava.shenma.R;
import cn.javava.shenma.interf.OnLoadingListener;

/**
 * Description {des}
 *
 * @author Li'O
 * @date 2018/1/15
 * Todo {TODO}.
 */

public class CatchExitFragment extends DialogFragment  {


    public static CatchExitFragment getInstance(){
        CatchExitFragment secondFragment= new CatchExitFragment();
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentStyle);
    }

    private GameResultDialog.OnGameResultCallback mGameResultCallback;


    public interface OnGameResultCallback {
        void onGiveUpPlaying();

        void onContinueToPlay();
    }

    public void setGameResultCallback(GameResultDialog.OnGameResultCallback callback) {
        mGameResultCallback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_catch_success,container);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if(KeyEvent.KEYCODE_BUTTON_C==keyCode) {
                    if (mGameResultCallback != null) {

                        mGameResultCallback.onContinueToPlay();

                    }
                } else if (KeyEvent.KEYCODE_BACK == keyCode) {
                    if (mGameResultCallback != null) {

                        mGameResultCallback.onGiveUpPlaying();
                    }
                }

                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount=0.0f;
        attributes.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.width = 800; // 宽度
        attributes.height = 1000; // 高度
//        attributes.alpha = 0.7f; // 透明度
        window.setAttributes(attributes);

    }


}
