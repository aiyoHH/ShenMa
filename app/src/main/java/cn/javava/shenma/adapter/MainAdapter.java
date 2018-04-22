package cn.javava.shenma.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.javava.shenma.R;
import cn.javava.shenma.adapter.mainHolder.BannerHolder;
import cn.javava.shenma.adapter.mainHolder.ContentHolder;
import cn.javava.shenma.adapter.mainHolder.UserInfoHolder;
import cn.javava.shenma.adapter.mainHolder.VideoHolder;
import cn.javava.shenma.bean.BannerBean;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.interf.Key;
import cn.javava.shenma.interf.OnPositionClickListener;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class MainAdapter extends RecyclerView.Adapter {

    private final int additional = 3;
    private TextView mTvTimer;

    Context mContext;
    LayoutInflater mInflater;
    List<Room> mList;
    List<BannerBean.DataBean> bannerList;
    String mVideoUrl;
    OnPositionClickListener mListener;

    public MainAdapter(Context context, List<Room> list, List<BannerBean.DataBean> bannerList, String url, RecyclerView recyclerView, OnPositionClickListener listener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.bannerList = bannerList;
        this.mListener = listener;
        this.mVideoUrl = url;

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position >= additional ? 1 : 2;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Key.Type.banner) {
            return new BannerHolder(mInflater.inflate(R.layout.item_main_banner, parent, false));
        } else if (viewType == Key.Type.video) {
            return new VideoHolder(mInflater.inflate(R.layout.item_main_video, parent, false));
        } else if (viewType == Key.Type.userInfo) {
            return new UserInfoHolder(mInflater.inflate(R.layout.item_main_userinfo, parent, false));
        } else {
            return new ContentHolder(mInflater.inflate(R.layout.item_main_content, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VideoHolder) {
            ((VideoHolder) holder).setData(mContext, mVideoUrl);
        } else if (holder instanceof UserInfoHolder) {
            ((UserInfoHolder) holder).setData(mContext);
            mTvTimer = holder.itemView.findViewById(R.id.item_main_timer);
        } else if (holder instanceof BannerHolder) {
            if (bannerList.size() > 0)
                ((BannerHolder) holder).setData(mContext, bannerList);
        } else if (holder instanceof ContentHolder) {
            ((ContentHolder) holder).setData(mContext, mList.get(position - additional));
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    //获取焦点时变化
                    if (hasFocus) {
                        ViewCompat.animate(v).scaleX(1.05f).scaleY(1.05f).translationZ(0.5f).start();
                    } else {
                        ViewCompat.animate(v).scaleX(1.0f).scaleY(1.0f).translationZ(0f).start();
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        mListener.onClick(position - additional,mList.get(position - additional).isData);
                }
            });
        }

    }

    public void setTimer(long timer) {
        if (mTvTimer != null)
            mTvTimer.setText("退出倒计时:" + timer);
    }

    @Override
    public int getItemCount() {
        return additional + mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return Key.Type.banner;
            case 1:
                return Key.Type.video;
            case 2:
                return Key.Type.userInfo;
            default:
                return Key.Type.content;
        }
    }


}
