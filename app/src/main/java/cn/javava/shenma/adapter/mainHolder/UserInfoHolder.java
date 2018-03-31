package cn.javava.shenma.adapter.mainHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class UserInfoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_main_logo_title)
    ImageView logoTitle;
    @BindView(R.id.item_main_use_info)
    RelativeLayout userInfo;
    @BindView(R.id.item_main_avatar)
    ImageView avatar;
    @BindView(R.id.item_main_nick)
    TextView nick;
    @BindView(R.id.item_main_get_count)
    TextView getCount;
    @BindView(R.id.item_main_gold_count)
    TextView goldCount;

    public UserInfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setData(Context context){

        if(Session.login){
            logoTitle.setVisibility(View.GONE);
            userInfo.setVisibility(View.VISIBLE);
            nick.setText(Session.nickname);
            goldCount.setText("余额:"+Session.balance+"币");
            ImageLoader.loadCircular(context,Session.headimgurl,avatar);
        }else{
            logoTitle.setVisibility(View.VISIBLE);
            userInfo.setVisibility(View.GONE);
        }

    }
}
