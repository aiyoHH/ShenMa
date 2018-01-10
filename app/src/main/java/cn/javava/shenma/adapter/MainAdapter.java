package cn.javava.shenma.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import cn.javava.shenma.R;
import cn.javava.shenma.adapter.mainHolder.BannerHolder;
import cn.javava.shenma.adapter.mainHolder.ContentHolder;
import cn.javava.shenma.adapter.mainHolder.VideoHolder;
import cn.javava.shenma.bean.Room;
import cn.javava.shenma.interf.Key;

/**
 * Created by aiyoRui on 2018/1/5.
 */

public class MainAdapter extends RecyclerView.Adapter {

    private final int additional=2;

    Context mContext;
    LayoutInflater mInflater;
    List<Room> mList;
    List<String> bannerList;

    public MainAdapter(Context context, List<Room> list,List<String> bannerList, RecyclerView recyclerView) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.bannerList=bannerList;

        GridLayoutManager layoutManager=new GridLayoutManager(context,2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position>=additional?2:1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==Key.Type.banner){
            return new BannerHolder(mInflater.inflate(R.layout.item_main_banner,parent,false));
        }else if(viewType==Key.Type.video){
            return new VideoHolder(mInflater.inflate(R.layout.item_main_video,parent,false));
        }else{
            return new ContentHolder(mInflater.inflate(R.layout.item_main_content,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VideoHolder){
            ((VideoHolder) holder).setData(mContext);
        }else if(holder instanceof  BannerHolder){
            ((BannerHolder) holder).setData(mContext,bannerList);
        }else if(holder instanceof ContentHolder){

        }

    }

    @Override
    public int getItemCount() {
        return additional+20;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                return Key.Type.banner;
            case 1:
                return Key.Type.video;
                default:
                    return Key.Type.content;
        }
    }


}
