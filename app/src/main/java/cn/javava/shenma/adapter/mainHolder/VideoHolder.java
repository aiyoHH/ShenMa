package cn.javava.shenma.adapter.mainHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.utils.ImageLoader;
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
        videoPlayer.setVisibility(View.VISIBLE);
        videoPlayer.setUp("http://jzvd.nathen.cn/25a8d119cfa94b49a7a4117257d8ebd7/f733e65a22394abeab963908f3c336db-5287d2089db37e62345123a1be272f8b.mp4", JZVideoPlayer.SCREEN_WINDOW_LIST,"");
        ImageLoader.load(context, "https://app-cdn.siy8.com/6320/images-1514038402338.png",videoPlayer.thumbImageView);



    }
}
