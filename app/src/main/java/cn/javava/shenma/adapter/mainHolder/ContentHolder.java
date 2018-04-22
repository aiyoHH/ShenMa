package cn.javava.shenma.adapter.mainHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.javava.shenma.R;
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
    @BindView(R.id.item_main_order)
    TextView mTvOrder;
    @BindView(R.id.is_can_select)
    RelativeLayout bcIcon;

    public ContentHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    public void setData(final Context context, final Room room) {
        //mTvDes.setText(room.roomName+" "+room.balance+"币/次");
        mTvOrder.setText(room.number + "号房间");
        mTvDes.setText(room.balance + "币/次");
        ImageLoader.load(context, room.roomIcon, mIvIcon);
        if (!room.isData) {
            bcIcon.setBackgroundResource(R.drawable.adapter_item_bg_icon);
        }else{
            bcIcon.setBackgroundResource(R.drawable.good_bg);
        }
    }
}
