package cn.javava.shenma.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.javava.shenma.R;

/**
 * Copyright © 2017 Zego. All rights reserved.
 */

public class LogoutDialog extends DialogFragment {

    private TextView mBtnContinue;
    private int rspSeq;

    private OnGameResultCallback mGameResultCallback;


    public interface OnGameResultCallback {
        void onGiveUpPlaying();

        void onContinueToPlay();
    }

    public void setGameResultCallback(OnGameResultCallback callback) {
        mGameResultCallback = callback;
    }

    //
    public void setContinueText(String text) {
        if (mBtnContinue != null) {
            mBtnContinue.setText(text);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentStyle);
    }



    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.0f;
        attributes.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.width = 600; // 宽度
        attributes.height = 820; // 高度
//        attributes.alpha = 0.7f; // 透明度
        window.setAttributes(attributes);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_dialog_layout, container);
        mBtnContinue = view.findViewById(R.id.btn_continue);
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

}

