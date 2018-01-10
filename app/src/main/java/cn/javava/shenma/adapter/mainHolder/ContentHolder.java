package cn.javava.shenma.adapter.mainHolder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
import cn.javava.shenma.act.PlayActivity;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.utils.ImageLoader;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class ContentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_main_content_icon)
    ImageView mIvIcon;
    @BindView(R.id.item_main_content_des)
    TextView mTvDes;
    View itemView;

    public ContentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.itemView=itemView;
    }

    public void setData(final Context context, final Room room){
        mTvDes.setText(room.roomName);
        ImageLoader.load(context,room.roomIcon,mIvIcon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.actionStart((Activity) context,room);
            }
        });
    }
}
