package cn.javava.shenma.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import cn.javava.shenma.R;
import cn.javava.shenma.app.App;
import cn.javava.shenma.http.HttpHelper;

/**
 * Created by aiyoRui on 2018/4/27.
 */

public class SoundPoolUtil {

    private static volatile SoundPoolUtil soundPoolUtil = null;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private int SOUND_CATCH;
    private int SOUND_CATCH_SUCCESS;
    private int SOUND_HINT_CONFIRM_CATCH;
    private int SOUND_LEISURE;
    private int SOUND_LOGIN;
    private int SOUND_LOGIN_ING;
    private int SOUND_LOGIN_SUCCESS;
    private int SOUND_LOGIN_FAILURE;
    private int SOUND_LAUNCHER;
    private int SOUND_INSERT_COIN_BGM;
    private int SOUND_NOT_SUFFICENT_FOUND;



    private  SoundPoolUtil() {
        initSoundPool();
    }

    public static SoundPoolUtil getInstance() {
        if (soundPoolUtil == null) {
            synchronized (SoundPoolUtil.class) {
                if (soundPoolUtil == null) {
                    soundPoolUtil = new SoundPoolUtil();
                }
            }
        }
        return soundPoolUtil;
    }

    private void initSoundPool(){
        Context context = App.getInstance().getContext();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        SOUND_LAUNCHER = soundPool.load(context, R.raw.launch, 1);
        SOUND_LOGIN_ING = soundPool.load(context, R.raw.logining, 1);
        SOUND_LOGIN_SUCCESS = soundPool.load(context, R.raw.login_success, 1);
        SOUND_LOGIN_FAILURE = soundPool.load(context, R.raw.login_failure, 1);
        SOUND_LEISURE = soundPool.load(context, R.raw.leisure_30, 1);
        SOUND_LOGIN = soundPool.load(context, R.raw.login, 1);
        SOUND_NOT_SUFFICENT_FOUND=soundPool.load(context, R.raw.not_sufficient_found, 1);

        mediaPlayer= MediaPlayer.create(context, R.raw.insert_coin_bgm);
    }


    public void soundLeiSure(){
        soundPool.play(SOUND_LEISURE, 0.8f, 0.8f, 4, 4, 1.0f);
    }

    public void endLeiSure(){
        soundPool.stop(SOUND_LEISURE);
    }

    public void soundLogin(){
        soundPool.play(SOUND_LOGIN, 0.8f, 0.8f, 4, 4, 1.0f);
    }

    public void soundLaunch(){
        soundPool.play(SOUND_LAUNCHER, 0.8f, 0.8f, 1, 0, 1.0f);
    }

    public void soundLogining(){
        soundPool.play(SOUND_LOGIN_ING, 0.8f, 0.8f, 1, 0, 1.0f);
    }

    public void soundNotSufficentFound(){
        soundPool.play(SOUND_NOT_SUFFICENT_FOUND, 0.8f, 0.8f, 1, 0, 1.0f);
    }

    public void soundFailure(){
        soundPool.play(SOUND_LOGIN_FAILURE, 0.8f, 0.8f, 1, 0, 1.0f);
    }


    public void soundSuccess(){
        soundPool.play(SOUND_LOGIN_SUCCESS, 0.8f, 0.8f, 1, 0, 1.0f);
    }

    public void soundBGM(){
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void endBGM(){
        mediaPlayer.stop();
        mediaPlayer.prepareAsync();
    }

    public void soundCatch(){
        soundPool.play(SOUND_CATCH, 0.8f, 0.8f, 1, 0, 1.0f);
    }




}
