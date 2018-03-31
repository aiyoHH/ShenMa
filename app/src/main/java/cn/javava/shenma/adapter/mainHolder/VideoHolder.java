package cn.javava.shenma.adapter.mainHolder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.http.Session;
import cn.javava.shenma.utils.ImageLoader;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class VideoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_main_video)
    JZVideoPlayerStandard videoPlayer;


    public VideoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setData(Context context){
        //videoPlayer.setVisibility(View.VISIBLE);

        LinkedHashMap map = new LinkedHashMap();
        try {
            map.put(JZVideoPlayer.URL_KEY_DEFAULT, context.getAssets().openFd("local_video.mp4"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object[] dataSourceObjects = new Object[2];
        dataSourceObjects[0] = map;
        dataSourceObjects[1] = this;
        videoPlayer.setUp(dataSourceObjects, 0, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

        //ImageLoader.load(context, "https://app-cdn.siy8.com/6320/images-1514876319180.png",videoPlayer.thumbImageView);

        videoPlayer.startButton.performClick();
        videoPlayer.setJzUserAction(new JZUserAction() {
            @Override
            public void onEvent(int type, Object url, int screen, Object... objects) {
                if(JZUserAction.ON_AUTO_COMPLETE==type){
                    videoPlayer.startVideo();
                    videoPlayer.replayTextView.setText("");
                }
            }
        });



    }
}
