package cn.javava.shenma.adapter.mainHolder;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.utils.ImageLoader;
import cn.javava.shenma.view.CustomVideoView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class VideoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_main_video)
    CustomVideoView myVideoView;


    public VideoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setData(Context context, final String videoUrl){
        //videoPlayer.setVisibility(View.VISIBLE);

//
//        Log.e("lzh2017","video url=="+url);
//        LinkedHashMap map = new LinkedHashMap();
//
////            map.put(JZVideoPlayer.URL_KEY_DEFAULT, context.getAssets().openFd("local_video.mp4"));
//            map.put(JZVideoPlayer.URL_KEY_DEFAULT, url);
//
//        Object[] dataSourceObjects = new Object[2];
//        dataSourceObjects[0] = map;
//        dataSourceObjects[1] = this;
////        videoPlayer.setUp(dataSourceObjects, 0, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
//
//        videoPlayer.setUp(url,JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"");
//        //ImageLoader.load(context, "https://app-cdn.siy8.com/6320/images-1514876319180.png",videoPlayer.thumbImageView);
//
//
//
//        videoPlayer.setJzUserAction(new JZUserAction() {
//            @Override
//            public void onEvent(int type, Object url, int screen, Object... objects) {
//
//                Log.e("lzh2018","onEvent=="+type);
//                if(JZUserAction.ON_AUTO_COMPLETE==type){
//                    videoPlayer.startVideo();
//                    videoPlayer.replayTextView.setText("");
//                }
//            }
//        });
//
//        videoPlayer.startButton.performClick();


     if(TextUtils.isEmpty(videoUrl)){return;}
        myVideoView.setVideoPath(videoUrl);
        myVideoView.start();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f,0f);
                mp.start();
                mp.setLooping(true);

            }
        });

        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        myVideoView.setVideoPath(videoUrl);
                        myVideoView.start();

                    }
                });


    }
}
